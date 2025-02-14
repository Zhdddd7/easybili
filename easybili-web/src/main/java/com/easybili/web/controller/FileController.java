package com.easybili.web.controller;

import com.easybili.constants.Constants;
import com.easybili.entities.component.RedisComponent;
import com.easybili.entities.config.AppConfig;
import com.easybili.entities.dto.SysSettingDto;
import com.easybili.entities.dto.TokenUserInfoDto;
import com.easybili.entities.dto.UploadingFileDto;
import com.easybili.entities.dto.VideoPlayInfoDto;
import com.easybili.entities.enums.ResponseCodeEnum;
import com.easybili.entities.exception.BusinessException;
import com.easybili.entities.po.VideoInfoFile;
import com.easybili.entities.vo.ResponseVO;
import com.easybili.service.VideoInfoFileServiceImpl;
import com.easybili.utils.DateUtils;
import com.easybili.utils.FFmpegUtils;
import com.easybili.utils.StringTools;
import com.easybili.web.annotation.GlobalInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("/api/file")
@Validated
@Slf4j
public class FileController extends BaseController<Object>{
    @Resource
    private AppConfig appConfig;

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private FFmpegUtils fFmpegUtils;

    @Resource
    private VideoInfoFileServiceImpl videoInfoFileService;

    @RequestMapping("/getResource")
    public void getResource(HttpServletResponse response, @NotNull String sourceName) throws IOException {

        if (!StringTools.pathIsOk(sourceName)) {
            throw new BusinessException(ResponseCodeEnum.BUSINESS_CUSTOM_ERROR.getMessage());
        }
        String suffix = StringTools.getSuffix(sourceName);
        response.setContentType("image/" + suffix.replace(".", ""));
        response.setHeader("Cache-Control", "max-age=2592000");
        readFile(response, sourceName);
    }

    protected void readFile(HttpServletResponse response, String filePath) {
        File file = new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER + filePath);
        if (!file.exists()) {
            return;
        }
        try (OutputStream out = response.getOutputStream(); FileInputStream in = new FileInputStream(file)) {
            byte[] byteData = new byte[1024];
            int len = 0;
            while ((len = in.read(byteData)) != -1) {
                out.write(byteData, 0, len);
            }
            out.flush();
        }catch (Exception e) {
            log.error("error FILE IO", e);
        }
    }

// give the task an id, and store the file uploading DTO object into the redis
    @RequestMapping("/preUploadVideo")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<String> preUploadVideo(@NotEmpty String fileName, @NotNull Integer chunks){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        String uploadId = redisComponent.savePreVideoFileInfo(tokenUserInfoDto.getUserId(), fileName, chunks);
        return ResponseVO.getSuccessResponseVO(uploadId);
    }

    @RequestMapping("/uploadVideo")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Object> uploadVideo(@NotNull MultipartFile chunkFile, @NotNull Integer chunkIndex, @NotEmpty String uploadId) throws IOException {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        UploadingFileDto uploadingFileDto = redisComponent.getUploadVideoFile(tokenUserInfoDto.getUserId(), uploadId);
        if (uploadingFileDto == null){
            throw new BusinessException("The file not exists");
        }

        SysSettingDto sysSettingDto = redisComponent.getSysSettingDto();
        if(uploadingFileDto.getFileSize() > sysSettingDto.getVideoSize() * Constants.MB_SIZE){
            throw new BusinessException("The file is over the size limit");
        }
        // check the chunk
        if(chunkIndex - 1 > uploadingFileDto.getChunkIndex() || chunkIndex > uploadingFileDto.getChunks() -1){
            throw new BusinessException(ResponseCodeEnum.BUSINESS_CUSTOM_ERROR.getMessage());
        }

        String folder = appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.TEMP_FOLDER + uploadingFileDto.getFilePath();
        File targetFile = new File(folder + "/" + chunkIndex);
        chunkFile.transferTo(targetFile);
        // update the dto attr
        uploadingFileDto.setChunkIndex(chunkIndex);
        uploadingFileDto.setFileSize(uploadingFileDto.getFileSize() + chunkFile.getSize());
        redisComponent.updateVideoFileInfo(tokenUserInfoDto.getUserId(), uploadingFileDto);

        return ResponseVO.getSuccessResponseVO();
    }

    @RequestMapping("/delUploadVideo")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Object> delUploadVideo(@NotEmpty String uploadId) throws IOException {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        UploadingFileDto uploadingFileDto = redisComponent.getUploadVideoFile(tokenUserInfoDto.getUserId(), uploadId);
        if(uploadingFileDto == null){
            throw new BusinessException("The file not exists");
        }
        redisComponent.delVideoFileInfo(tokenUserInfoDto.getUserId(), uploadId);
        FileUtils.deleteDirectory(new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.TEMP_FOLDER + uploadingFileDto.getFilePath()));
        return ResponseVO.getSuccessResponseVO(uploadId);
    }

    @RequestMapping("/uploadImage")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Object> uploadImage(@NotNull MultipartFile file, @NotNull Boolean createThumbnail) throws IOException {
        String day = DateUtils.formatNow("yyyyMMdd");
        String folder = appConfig.getProjectFolder() + Constants.FILE_FOLDER + Constants.COVER_FOLDER + day;
        File folderFile = new File(folder);
        if(!folderFile.exists()){
            folderFile.mkdirs();
        }
        String fileName = file.getOriginalFilename();
        String fileSuffix = StringTools.getSuffix(fileName);
        String realFileName = StringTools.getRandomString(30) + fileSuffix;
        String filePath = folder + "/" + realFileName;
        file.transferTo(new File(filePath));
        if (createThumbnail != null && createThumbnail){
            fFmpegUtils.createImageThumbnail(filePath);
        }
        return ResponseVO.getSuccessResponseVO(Constants.COVER_FOLDER + day + "/" + realFileName);
    }

    @RequestMapping("/videoResource/{fileId}")
    public void videoResource(HttpServletResponse response, @PathVariable @NotEmpty String fileId){
            VideoInfoFile videoInfoFile = videoInfoFileService.getVideoInfoFileById(fileId);
            String filePath = videoInfoFile.getFilePath();
            readFile(response, filePath + "/" + Constants.M3U8_NAME);
            // TODO update some of the properties
            VideoPlayInfoDto videoPlayInfoDto = new VideoPlayInfoDto();
            videoPlayInfoDto.setVideoId(videoInfoFile.getVideoId());
            videoPlayInfoDto.setFileIndex(videoInfoFile.getFileIndex());

            TokenUserInfoDto tokenUserInfoDto = getTokenInfoFromCookie();
            if(tokenUserInfoDto != null){
                videoPlayInfoDto.setUserId(tokenUserInfoDto.getUserId());
            }
            redisComponent.addVideoPlay(videoPlayInfoDto);
    }

    @RequestMapping("/videoResource/{fileId}/{ts}")
    public void videoResourceTs(HttpServletResponse response, @PathVariable @NotEmpty String fileId, @PathVariable @NotEmpty String ts){
        VideoInfoFile videoInfoFile = videoInfoFileService.getVideoInfoFileById(fileId);
        String filePath = videoInfoFile.getFilePath();
        readFile(response, filePath + "/" + ts);
    }
}
