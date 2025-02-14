package com.easybili.service;

import com.easybili.constants.Constants;
import com.easybili.entities.component.EsSearchComponent;
import com.easybili.entities.component.RedisComponent;
import com.easybili.entities.config.AppConfig;
import com.easybili.entities.dto.SysSettingDto;
import com.easybili.entities.enums.ResponseCodeEnum;
import com.easybili.entities.enums.UserActionTypeEnum;
import com.easybili.entities.enums.VideoRecommendTypeEnum;
import com.easybili.entities.exception.BusinessException;
import com.easybili.entities.po.VideoInfo;
import com.easybili.entities.po.VideoInfoFile;
import com.easybili.entities.po.VideoInfoPost;
import com.easybili.entities.query.*;
import com.easybili.entities.vo.PaginationResultVO;
import com.easybili.entities.vo.SimplePage;
import com.easybili.mappers.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.prefs.BackingStoreException;

@Service
@Slf4j
public class VideoInfoServiceImpl {
    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Resource
    private VideoInfoPostMapper videoInfoPostMapper;
    @Resource
    private VideoInfoMapper videoInfoMapper;
    @Resource
    private VideoDanmuMapper videoDanmuMapper;
    @Resource
    private VideoCommentMapper videoCommentMapper;
    @Resource
    private VideoInfoFileMapper videoInfoFileMapper;
    @Resource
    private RedisComponent redisComponent;
    @Resource
    private UserInfoServiceImpl userInfoService;
    @Resource
    private AppConfig appConfig;
    @Resource
    private EsSearchComponent esSearchComponent;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private VideoInfoFilePostMapper videoInfoFilePostMapper;

    public VideoInfo getVideoInfoById(String videoId) {
        return videoInfoMapper.selectById(videoId);
    }

    public List<VideoInfo> getVideoInfoByQuery(VideoInfoQuery query) {
        return videoInfoMapper.selectByQuery(query);
    }

    public List<VideoInfo> getVideoInfoByCategoryId(Integer categoryId) {
        return videoInfoMapper.selectByCategoryId(categoryId);
    }

    public List<VideoInfo> getVideoInfoByCreateTime(LocalDateTime createTime) {
        return videoInfoMapper.selectByCreateTime(createTime);
    }

    public List<VideoInfo> getVideoInfoByLastPlayTime(LocalDateTime lastPlayTime) {
        return videoInfoMapper.selectByLastPlayTime(lastPlayTime);
    }

    public List<VideoInfo> getVideoInfoByRecommendType(Byte recommendType) {
        return videoInfoMapper.selectByRecommendType(recommendType);
    }

    public List<VideoInfo> getVideoInfoByTags(String tags) {
        return videoInfoMapper.selectByTags(tags);
    }

    public List<VideoInfo> getVideoInfoByUserId(String userId) {
        return videoInfoMapper.selectByUserId(userId);
    }

    public boolean addVideoInfo(VideoInfo videoInfo) {
        return videoInfoMapper.insert(videoInfo) > 0;
    }


    public boolean deleteVideoInfo(String videoId) {
        return videoInfoMapper.deleteById(videoId) > 0;
    }

    public List<VideoInfo> findListByParam(VideoInfoQuery query) {
        return videoInfoMapper.selectByQuery(query);
    }

    public PaginationResultVO<VideoInfo> findListByPage(VideoInfoQuery query) {
        if (query == null) {
            throw new IllegalArgumentException("Query object cannot be null");
        }
        if(query.getPageNo() == null){
            query.setPageNo(1);
        }
        if(query.getPageSize() ==null){
            query.setPageSize(10);
        }
        int start = (query.getPageNo() - 1) * query.getPageSize(); // 10 pcs every page
        query.setSimplePage(new SimplePage(start, query.getPageSize()));
        List<VideoInfo> dataList = videoInfoMapper.selectByQuery(query);
        return new PaginationResultVO<>(dataList.size(), query.getPageSize(), query.getPageNo(), dataList);
    }

    // this function is used when user want to shut the interaction field
    @Transactional(rollbackFor = Exception.class)
    public void changeInteraction(String videoId, String userId, String interaction){
        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setInteraction(interaction);
        VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
        videoInfoQuery.setVideoId(videoId);
        videoInfoMapper.updateByQuery(videoInfo, videoInfoQuery);

        VideoInfoPost videoInfoPost = new VideoInfoPost();
        videoInfoPost.setInteraction(interaction);
        VideoInfoPostQuery videoInfoPostQuery = new VideoInfoPostQuery();
        videoInfoPostQuery.setVideoId(videoId);
        videoInfoPostQuery.setUserId(userId);
        videoInfoPostMapper.updateByParam(videoInfoPost, videoInfoPostQuery);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteVideo(String videoId, String userId) {
        VideoInfoPost videoInfoPost = this.videoInfoPostMapper.selectById(videoId);
        // video not found/userId == null -> admin/not your video
        if(videoInfoPost == null|| userId!= null &&!userId.equals(videoInfoPost.getUserId())){
            throw new BusinessException(ResponseCodeEnum.BUSINESS_CUSTOM_ERROR.getMessage());
        }

        this.videoInfoMapper.deleteById(videoId);
        this.videoInfoPostMapper.deleteById(videoId);

        SysSettingDto sysSettingDto = redisComponent.getSysSettingDto();
        userInfoMapper.updateCoinCountInfo(videoInfoPost.getUserId(), -sysSettingDto.getPostVideoCoinCount());
        esSearchComponent.delDoc(videoId);
        // an async thread to execute
        executorService.execute(() ->
                {
                 VideoInfoFileQuery videoInfoFileQuery = new VideoInfoFileQuery();
                 videoInfoFileQuery.setVideoId(videoId);

                 // delete files
                 videoInfoFileMapper.deleteByParam(videoInfoFileQuery);
                 VideoInfoFilePostQuery videoInfoFilePostQuery = new VideoInfoFilePostQuery();
                 videoInfoFilePostQuery.setVideoId(videoId);
                 videoInfoFilePostMapper.deleteByQuery(videoInfoFilePostQuery);

                 // delete danmu
                VideoDanmuQuery videoDanmuQuery = new VideoDanmuQuery();
                videoDanmuQuery.setVideoId(videoId);
                videoDanmuMapper.deleteByParam(videoDanmuQuery);
                // delete comment
                VideoCommentQuery videoCommentQuery = new VideoCommentQuery();
                videoCommentQuery.setVideoId(videoId);
                videoCommentMapper.deleteByQuery(videoCommentQuery);

                List<VideoInfoFile> videoInfoFileList = videoInfoFileMapper.selectByQuery(videoInfoFileQuery);
                for(VideoInfoFile item: videoInfoFileList){
                    try{
                        FileUtils.deleteDirectory(new File(appConfig.getProjectFolder() + item.getFilePath()));
                    }catch(IOException e){
                        log.error("delete file error, the path is  {}", item.getFilePath());
                    }
                }
                });


    }

    public void addReadCount(String videoId) {
        this.videoInfoMapper.updateCountInfo(videoId, UserActionTypeEnum.VIDEO_PLAY.getField(), 1);
    }

    public Integer findCountByQuery(VideoInfoQuery videoInfoQuery) {
        return videoInfoMapper.selectCountByQuery(videoInfoQuery);
    }

    public void recommendVideo(String videoId){
        VideoInfo videoInfo = videoInfoMapper.selectById(videoId);
        if(videoInfo == null){
            throw new BusinessException(ResponseCodeEnum.BUSINESS_CUSTOM_ERROR.getMessage());
        }
        Integer recommendType = null;
        if(VideoRecommendTypeEnum.RECOMMEND.getType().equals(videoInfo.getRecommendType())){
            recommendType = VideoRecommendTypeEnum.NO_RECOMMEND.getType();
        }else{
            recommendType = VideoRecommendTypeEnum.RECOMMEND.getType();
        }
        VideoInfo updateInfo = new VideoInfo();
        updateInfo.setRecommendType(recommendType);
        videoInfoMapper.update(updateInfo, videoId);
    }
}
