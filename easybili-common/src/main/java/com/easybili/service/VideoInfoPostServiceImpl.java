package com.easybili.service;

import com.easybili.constants.Constants;
import com.easybili.entities.component.EsSearchComponent;
import com.easybili.entities.component.RedisComponent;
import com.easybili.entities.config.AppConfig;
import com.easybili.entities.dto.SysSettingDto;
import com.easybili.entities.dto.UploadingFileDto;
import com.easybili.entities.enums.ResponseCodeEnum;
import com.easybili.entities.enums.VideoFileTransferResultEnum;
import com.easybili.entities.enums.VideoFileUpdateTypeEnum;
import com.easybili.entities.enums.VideoStatusEnum;
import com.easybili.entities.exception.BusinessException;
import com.easybili.entities.po.VideoInfo;
import com.easybili.entities.po.VideoInfoFile;
import com.easybili.entities.po.VideoInfoFilePost;
import com.easybili.entities.po.VideoInfoPost;
import com.easybili.entities.query.VideoInfoFilePostQuery;
import com.easybili.entities.query.VideoInfoFileQuery;
import com.easybili.entities.query.VideoInfoPostQuery;
import com.easybili.entities.vo.PaginationResultVO;
import com.easybili.entities.vo.SimplePage;
import com.easybili.mappers.*;
import com.easybili.utils.DateUtils;
import com.easybili.utils.FFmpegUtils;
import com.easybili.utils.StringTools;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VideoInfoPostServiceImpl {
    @Resource
    private VideoInfoPostMapper videoInfoPostMapper;
    @Resource
    private VideoInfoFilePostMapper videoInfoFilePostMapper;
    @Resource
    private VideoInfoMapper videoInfoMapper;
    @Resource
    private VideoInfoFileMapper videoInfoFileMapper;
    @Resource
    private RedisComponent redisComponent;
    @Resource
    private AppConfig appConfig;
    @Resource
    private FFmpegUtils fFmpegUtils;
    @Resource
    private EsSearchComponent esSearchComponent;
    @Resource
    private UserInfoMapper userInfoMapper;

    public VideoInfoPost getVideoInfoPostById(String videoId) {
        return videoInfoPostMapper.selectById(videoId);
    }

    public List<VideoInfoPost> getVideoInfoPostsByCategoryId(Integer categoryId) {
        return videoInfoPostMapper.selectByCategoryId(categoryId);
    }

    public List<VideoInfoPost> getVideoInfoPostsByPCategoryId(Integer pCategoryId) {
        return videoInfoPostMapper.selectByPCategoryId(pCategoryId);
    }

    public List<VideoInfoPost> getVideoInfoPostsByUserId(String userId) {
        return videoInfoPostMapper.selectByUserId(userId);
    }

    public boolean addVideoInfoPost(VideoInfoPost videoInfoPost) {
        return videoInfoPostMapper.insert(videoInfoPost) > 0;
    }

    public PaginationResultVO<VideoInfoPost> findListByPage(VideoInfoPostQuery query) {
        if (query == null) {
            throw new IllegalArgumentException("Query object cannot be null");
        }
        if(query.getPageNo() == null){
            query.setPageNo(1);
        }
        Integer start = (query.getPageNo() - 1) * Constants.PAGE_SIZE; // 10 pcs every page
        query.setSimplePage(new SimplePage(start, Constants.PAGE_SIZE));
        List<VideoInfoPost> dataList = videoInfoPostMapper.selectList(query);
        return new PaginationResultVO<>(dataList.size(), Constants.PAGE_SIZE, query.getPageNo(), dataList);
    }

    public boolean deleteVideoInfoPost(String videoId) {
        return videoInfoPostMapper.deleteById(videoId) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveVideo(VideoInfoPost videoInfoPost, List<VideoInfoFilePost> uploadFileList) {
        // upload too many files
        if(uploadFileList.size() > redisComponent.getSysSettingDto().getVideoCount()){
            throw new BusinessException(ResponseCodeEnum.BUSINESS_CUSTOM_ERROR.getMessage());
        }

        if(!StringTools.isEmpty(videoInfoPost.getVideoId())){
            VideoInfoPost videoInfoPostDb = this.videoInfoPostMapper.selectById(videoInfoPost.getVideoId());
            // this video not in the database, which should be stored in the upload phase
            if (videoInfoPostDb == null){
                throw new BusinessException(ResponseCodeEnum.BUSINESS_CUSTOM_ERROR.getMessage());
            }
            // while coding or pending, you can not modify your files
            if (ArrayUtils.contains(new Integer[] {VideoStatusEnum.STATUS0.getStatus(), VideoStatusEnum.STATUS2.getStatus()}, videoInfoPostDb.getStatus())){
                throw new BusinessException(ResponseCodeEnum.BUSINESS_CUSTOM_ERROR.getMessage());
            }
        }

        LocalDateTime now = LocalDateTime.now();
        String videoId = videoInfoPost.getVideoId();
        List<VideoInfoFilePost> deleteFileList = new ArrayList<>();
        List<VideoInfoFilePost> addFileList = uploadFileList;
            // this is a new video
        if(StringTools.isEmpty(videoId)){
            videoId = StringTools.getRandomString(10);
            videoInfoPost.setVideoId(videoId);
            videoInfoPost.setCreateTime(now);
            videoInfoPost.setLastUpdate(now);
            videoInfoPost.setStatus(VideoStatusEnum.STATUS0.getStatus());
            this.videoInfoPostMapper.insert(videoInfoPost);
        }
        else{
            // here we use a strict query where you can only edit your own video
            VideoInfoFilePostQuery fileQuery = new VideoInfoFilePostQuery();
            fileQuery.setVideoId(videoId);
            fileQuery.setUserId(videoInfoPost.getUserId());
            List<VideoInfoFilePost> dbInfoFileList = this.videoInfoFilePostMapper.selectList(fileQuery);
            System.out.println("the dbInfoFileList is: ");
            System.out.println(dbInfoFileList);
            Map<String, VideoInfoFilePost> uploadFileMap = uploadFileList.stream().collect(Collectors.toMap(item -> item.getUploadId(), Function.identity(),
                    (data1, data2) -> data2));

            // exam whether the name is modified
            Boolean updateFileName = false;
            for(VideoInfoFilePost fileInfo: dbInfoFileList){
                VideoInfoFilePost updateFile = uploadFileMap.get(fileInfo.getUploadId());
                if(updateFile == null){
                    deleteFileList.add(fileInfo);
                }
                else if(!updateFile.getFileName().equals(fileInfo.getFileName())){
                    updateFileName = true;
                }
            }
            addFileList = uploadFileList.stream().filter(item -> item.getFileId() == null).collect(Collectors.toList());
            videoInfoPost.setLastUpdate(now);
            Boolean changeVideoInfo = this.changeVideoInfo(videoInfoPost);
            if(!addFileList.isEmpty()){
                // submit new file: needs to be audited
                videoInfoPost.setStatus(VideoStatusEnum.STATUS0.getStatus());
            }
            // modify attr
            else if(changeVideoInfo || updateFileName){
                videoInfoPost.setStatus(VideoStatusEnum.STATUS2.getStatus());
            }
            this.videoInfoPostMapper.updateByVideoId(videoInfoPost, videoInfoPost.getVideoId());
        }


        // The following handle the files
        if(!deleteFileList.isEmpty()){
            List<String> delFileList = deleteFileList.stream().map(item -> item.getFileId()).collect(Collectors.toList());
            this.videoInfoFilePostMapper.deleteBatchByFileId(delFileList, videoInfoPost.getUserId());

            // delete the files in the database
            List<String> delFilePathList = deleteFileList.stream().map(item -> item.getFilePath()).collect(Collectors.toList());
            // add the file to message queue
            redisComponent.addFile2DelQueue(videoId, delFilePathList);
        }
        Integer index = 1;
        for(VideoInfoFilePost videoInfoFile: uploadFileList){
            videoInfoFile.setFileIndex(index++ );
            videoInfoFile.setVideoId(videoId);
            videoInfoFile.setUserId(videoInfoPost.getUserId());

            if(videoInfoFile.getFileId() == null){
                videoInfoFile.setFileId(StringTools.getRandomString((20)));
                videoInfoFile.setUpdateType(VideoFileUpdateTypeEnum.UPDATE.getStatus());
                videoInfoFile.setTransferResult(VideoFileTransferResultEnum.TRANSFER.getStatus());
            }
        }
        this.videoInfoFilePostMapper.insertOrUpdateBatch(uploadFileList);

        if(addFileList != null && !addFileList.isEmpty()){
            for(VideoInfoFilePost file: addFileList){
                file.setUserId(videoInfoPost.getUserId());
                file.setVideoId(videoId);
            }
            redisComponent.addFile2TransferQueue(addFileList);
        }


    }

    private Boolean changeVideoInfo(VideoInfoPost videoInfoPost){
        VideoInfoPost dbInfo = this.videoInfoPostMapper.selectById(videoInfoPost.getVideoId());
        // title, cover, tags, desc
        return  (!videoInfoPost.getVideoName().equals(dbInfo.getVideoName())
               || !videoInfoPost.getVideoCover().equals(dbInfo.getVideoCover())
                || !videoInfoPost.getTags().equals(dbInfo.getTags())
                || !videoInfoPost.getIntroduction().equals(dbInfo.getIntroduction() == null?"":dbInfo.getIntroduction()));
    }

    public void transferVideoFile(VideoInfoFilePost videoInfoFilePost) {
        VideoInfoFilePost updateFilePost = new VideoInfoFilePost();
        try{
            UploadingFileDto fileDto = redisComponent.getUploadVideoFile(videoInfoFilePost.getUserId(), videoInfoFilePost.getUploadId());

            String tempFilePath = appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.TEMP_FOLDER + fileDto.getFilePath();
            File tempFile = new File(tempFilePath);

            String targetFilePath = appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.VIDEO_FOLDER + fileDto.getFilePath();
            File targetFile = new File(targetFilePath);

            // copyDirectory will not create the directory automatically
            if(!targetFile.exists()){
                targetFile.mkdirs();
            }

            FileUtils.copyDirectory(tempFile, targetFile);
            FileUtils.forceDelete(tempFile);
            redisComponent.delVideoFileInfo(videoInfoFilePost.getUserId(), videoInfoFilePost.getUploadId());
            // file combination
            String completeVideo = targetFilePath + Constants.TEMP_VIDEO_NAME;
            this.union(targetFilePath, completeVideo, true);

            // Acquire the duration of the video
            Integer duration = fFmpegUtils.getVideoInfoDuration(completeVideo);
            updateFilePost.setDuration(duration);
            updateFilePost.setFileSize(new File(completeVideo).length());
            updateFilePost.setFilePath(Constants.VIDEO_FOLDER + fileDto.getFilePath());
            updateFilePost.setTransferResult(VideoFileTransferResultEnum.SUCCESS.getStatus());

            this.convertVideo2Ts(completeVideo);

        }catch(Exception e){
            log.error("The file transfer failed", e);
            updateFilePost.setTransferResult(VideoFileTransferResultEnum.FAIL.getStatus());
        }finally {
            videoInfoFilePostMapper.updateByUploadIdAndUserId(updateFilePost, videoInfoFilePost.getUploadId(),videoInfoFilePost.getUserId());

            VideoInfoFilePostQuery filePostQuery = new VideoInfoFilePostQuery();
            filePostQuery.setVideoId(videoInfoFilePost.getVideoId());
            filePostQuery.setTransferResult(VideoFileTransferResultEnum.FAIL.getStatus());
            Integer failCount = videoInfoFilePostMapper.selectCount(filePostQuery);
            if(failCount > 0){
                VideoInfoPost videoUpdate =  new VideoInfoPost();
                videoUpdate.setStatus(VideoStatusEnum.STATUS1.getStatus());
                videoInfoPostMapper.updateByVideoId(videoUpdate, videoInfoFilePost.getVideoId());
                return;
            }
            filePostQuery.setTransferResult(VideoFileTransferResultEnum.TRANSFER.getStatus());
            Integer transferCount = videoInfoFilePostMapper.selectCount(filePostQuery);
            if(transferCount == 0){
                Integer duration = videoInfoFilePostMapper.sumDuration(videoInfoFilePost.getVideoId());
                VideoInfoPost videoUpdate = new VideoInfoPost();
                videoUpdate.setStatus(VideoStatusEnum.STATUS2.getStatus());
                videoUpdate.setDuration(duration);
                videoInfoPostMapper.updateByVideoId(videoUpdate, videoInfoFilePost.getVideoId());
            }
        }
    }

    private void convertVideo2Ts(String completeVideo){
        File videoFile =  new File(completeVideo);
        File tsFolder = videoFile.getParentFile();
        String codec = fFmpegUtils.getVideoCodec(completeVideo);
        if(Constants.VIDEO_CODE_HEVC.equals(codec)){
            String tempFileName = completeVideo + Constants.VIDEO_CODE_FILE_SUFFIX;
            new File(completeVideo).renameTo(new File(tempFileName));
            fFmpegUtils.convertHevc2Mp4(tempFileName, completeVideo);
            new File(tempFileName).delete();
        }
        fFmpegUtils.convertVideo2Ts(tsFolder, completeVideo);
        videoFile.delete();
    }

    // Merge the bin files into a mp4, and del the bin files
    private void union(String dirPath, String toFilePath, Boolean delSource) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            throw new BusinessException("Directory does not exist");
        }
        File[] fileList = dir.listFiles();
        File targetFile = new File(toFilePath);
        try (RandomAccessFile writeFile = new RandomAccessFile(targetFile, "rw")) {
            byte[] b = new byte[1024 * 10];
            for (int i = 0; i < fileList.length; i++) {
                int len = 0;
                // Process each chunk file
                File chunkFile = new File(dirPath + File.separator + i);
                RandomAccessFile readFile = null;
                try {
                    readFile = new RandomAccessFile(chunkFile, "r");
                    while ((len = readFile.read(b)) > 0) {
                        writeFile.write(b, 0, len);
                    }
                } catch (Exception e) {
                    log.error("Error reading chunk file", e);
                    throw new BusinessException("Error reading chunk file");
                } finally {
                    readFile.close();
                }
            }
        } catch (Exception e) {
            throw new BusinessException("Error merging files in directory " + dirPath);
        } finally {
            if (delSource) {
                for (int i = 0; i < fileList.length; i++) {
                    fileList[i].delete();
                }
            }
        }
    }

    public Integer findCountByParam(VideoInfoPostQuery videoInfoPostQuery) {
        return videoInfoPostMapper.findCountByParam(videoInfoPostQuery);
    }

    @Transactional(rollbackFor = Exception.class)
    public void auditVideo(String videoId, Integer status, String reason) throws IOException {
        VideoStatusEnum videoStatusEnum = VideoStatusEnum.getByStatus(status);
        if(videoStatusEnum == null){
            throw new BusinessException(ResponseCodeEnum.BUSINESS_CUSTOM_ERROR.getMessage());
        }
        VideoInfoPost videoInfoPost = new VideoInfoPost();
        videoInfoPost.setStatus(status);

        // this is an optimistic locking
        VideoInfoPostQuery videoInfoPostQuery = new VideoInfoPostQuery();
        videoInfoPostQuery.setStatus(VideoStatusEnum.STATUS2.getStatus());
        videoInfoPostQuery.setVideoId(videoId);
        Integer auditCount = this.videoInfoPostMapper.updateByParam(videoInfoPost, videoInfoPostQuery);
        if(auditCount == 0){
            throw new BusinessException("Audit failed, please try later");
        }

        VideoInfoFilePost videoInfoFilePost = new VideoInfoFilePost();
        videoInfoFilePost.setUpdateType(VideoFileUpdateTypeEnum.NO_UPDATE.getStatus());
        VideoInfoFilePostQuery videoInfoFilePostQuery = new VideoInfoFilePostQuery();
        videoInfoFilePostQuery.setVideoId(videoId);
        this.videoInfoFilePostMapper.updateByParam(videoInfoFilePost, videoInfoFilePostQuery);

        if(VideoStatusEnum.STATUS4 == videoStatusEnum){
            return;
        }

        VideoInfoPost infoPost = this.videoInfoPostMapper.selectById(videoId);
        VideoInfo dbVideoInfo = this.videoInfoMapper.selectById(videoId);
        if(dbVideoInfo == null){
            SysSettingDto sysSettingDto = redisComponent.getSysSettingDto();
            userInfoMapper.updateCoinCountInfo(infoPost.getUserId(), sysSettingDto.getPostVideoCoinCount());

        }
        // update upload info to prim table
        VideoInfo videoInfo = new VideoInfo();
        BeanUtils.copyProperties(infoPost, videoInfo);
        // give default val
        videoInfo.setRecommendType(0);
        videoInfo.setCoinCount(0);
        videoInfo.setPlayCount(0);
        videoInfo.setLikeCount(0);
        videoInfo.setCommentCount(0);
        this.videoInfoMapper.insertOrUpdate(videoInfo);

        // update video info to prim table, delete before add
        VideoInfoFileQuery videoInfoFileQuery = new VideoInfoFileQuery();
        videoInfoFileQuery.setVideoId(videoId);
        this.videoInfoFileMapper.deleteByParam(videoInfoFileQuery);

        VideoInfoFilePostQuery videoInfoFilePostQuery2 = new VideoInfoFilePostQuery();
        videoInfoFilePostQuery2.setVideoId(videoId);
        List<VideoInfoFilePost> videoInfoFilePostList = this.videoInfoFilePostMapper.selectList(videoInfoFilePostQuery2);

        List<VideoInfoFile> videoInfoFileList = videoInfoFilePostList.stream()
                .map(post -> {
                    VideoInfoFile file = new VideoInfoFile();
                    BeanUtils.copyProperties(post, file);
                    return file;
                })
                .collect(Collectors.toList());

        if (videoInfoFileList.isEmpty()) {
            throw new IllegalArgumentException("videoInfoFileList cannot be null or empty");
        }
        this.videoInfoFileMapper.insertBatch(videoInfoFileList);
        // delete temp files
        List<String> filePathList = redisComponent.getDelFileList(videoId);
        if(filePathList != null){
            for(String path: filePathList){
                File file = new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER + path);
                if(file.exists()){
                    FileUtils.deleteDirectory(file);
                }
            }
        }
        redisComponent.cleanDelFileList(videoId);
        // write db first and then write es, can maintain data consistency, since es does not have Transaction
        esSearchComponent.saveDoc(videoInfo);
    }

}
