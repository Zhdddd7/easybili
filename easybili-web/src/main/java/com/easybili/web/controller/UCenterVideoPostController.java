package com.easybili.web.controller;

import com.easybili.entities.dto.TokenUserInfoDto;
import com.easybili.entities.enums.ResponseCodeEnum;
import com.easybili.entities.enums.VideoStatusEnum;
import com.easybili.entities.exception.BusinessException;
import com.easybili.entities.po.VideoInfoFilePost;
import com.easybili.entities.po.VideoInfoPost;
import com.easybili.entities.query.VideoInfoFilePostQuery;
import com.easybili.entities.query.VideoInfoPostQuery;
import com.easybili.entities.vo.PaginationResultVO;
import com.easybili.entities.vo.ResponseVO;
import com.easybili.entities.vo.VideoPostEditInfoVO;
import com.easybili.entities.vo.VideoStatusCountInfoVO;
import com.easybili.service.VideoInfoFilePostServiceImpl;
import com.easybili.service.VideoInfoPostServiceImpl;
import com.easybili.service.VideoInfoServiceImpl;
import com.easybili.utils.JsonUtils;
import com.easybili.web.annotation.GlobalInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("/api/ucenter")
@Validated
@Slf4j
public class UCenterVideoPostController extends BaseController<Object>{
    @Resource
    private VideoInfoPostServiceImpl videoInfoPostService;

    @Resource
    private VideoInfoFilePostServiceImpl videoInfoFilePostService;

    @Resource
    private VideoInfoServiceImpl videoInfoService;

    @RequestMapping("/postVideo")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Object> postVideo(String videoId,
                                        @NotEmpty String videoCover,
                                        @NotEmpty @Size(max = 100) String videoName,
                                        @NotNull Integer pCategoryId,
                                        Integer categoryId,
                                        @NotNull Integer postType,
                                        @NotEmpty @Size(max = 300) String tags,
                                        @Size(max = 2000) String introduction,
                                        @Size(max = 3) String interaction,
                                        @NotEmpty String uploadFileList
                                        ) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        List<VideoInfoFilePost> filePostList = JsonUtils.convertJsonArray2List(uploadFileList, VideoInfoFilePost.class);

        VideoInfoPost videoInfo = new VideoInfoPost();
        videoInfo.setVideoId(videoId);
        videoInfo.setVideoName(videoName);
        videoInfo.setVideoCover(videoCover);
        videoInfo.setPCategoryId(pCategoryId);
        videoInfo.setCategoryId(categoryId);
        videoInfo.setPostType(postType);
        videoInfo.setTags(tags);
        videoInfo.setIntroduction(introduction);
        videoInfo.setInteraction(interaction);
        videoInfo.setUserId(tokenUserInfoDto.getUserId());

        videoInfoPostService.saveVideo(videoInfo, filePostList);
        return ResponseVO.getSuccessResponseVO();
    }

    @RequestMapping("/loadVideoList")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Object> loadVideoList(Integer status, Integer pageNo, String videoNameFuzzy){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        VideoInfoPostQuery videoInfoPostQuery = new VideoInfoPostQuery();
        videoInfoPostQuery.setUserId(tokenUserInfoDto.getUserId());
        videoInfoPostQuery.setPageNo(pageNo);
        videoInfoPostQuery.setOrderBy("v.create_time desc");
        if(status != null){
            if(status == -1){
                // exclude the audit passed videos and audit failed videos
                videoInfoPostQuery.setExcludeStatusArray(new Integer[]{VideoStatusEnum.STATUS3.getStatus(), VideoStatusEnum.STATUS4.getStatus()});
            }
            else{
                videoInfoPostQuery.setStatus(status);
            }
        }
        videoInfoPostQuery.setVideoNameFuzzy(videoNameFuzzy);
        videoInfoPostQuery.setQueryCountInfo(true);
        PaginationResultVO<VideoInfoPost> resultVO = videoInfoPostService.findListByPage(videoInfoPostQuery);
        return ResponseVO.getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/getVideoCountInfo")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Object> getVideoCountInfo(){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();

        VideoInfoPostQuery videoInfoPostQuery = new VideoInfoPostQuery();
        videoInfoPostQuery.setUserId(tokenUserInfoDto.getUserId());
        videoInfoPostQuery.setStatus(VideoStatusEnum.STATUS3.getStatus());
        Integer auditPassCount = videoInfoPostService.findCountByParam(videoInfoPostQuery);

        videoInfoPostQuery.setStatus(VideoStatusEnum.STATUS4.getStatus());
        Integer auditFailCount = videoInfoPostService.findCountByParam(videoInfoPostQuery);

        videoInfoPostQuery.setStatus(null);
        videoInfoPostQuery.setExcludeStatusArray(new Integer[]{VideoStatusEnum.STATUS3.getStatus(), VideoStatusEnum.STATUS4.getStatus()});
        Integer inProgress = videoInfoPostService.findCountByParam(videoInfoPostQuery);

        VideoStatusCountInfoVO countInfoVO = new VideoStatusCountInfoVO();
        countInfoVO.setAuditPassCount(auditPassCount);
        countInfoVO.setAuditFailCount(auditFailCount);
        countInfoVO.setInProgress(inProgress);
        return ResponseVO.getSuccessResponseVO(countInfoVO);
    }

    @RequestMapping("/getVideoByVideoId")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Object> getVideoByVideoId(@NotEmpty String videoId){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        VideoInfoPost videoInfoPost = this.videoInfoPostService.getVideoInfoPostById(videoId);
        if(videoInfoPost == null || !videoInfoPost.getUserId().equals(tokenUserInfoDto.getUserId())){
            throw new BusinessException(ResponseCodeEnum.NOT_FOUND.getMessage());
        }
        VideoInfoFilePostQuery videoInfoFilePostQuery = new VideoInfoFilePostQuery();
        videoInfoFilePostQuery.setVideoId(videoId);
        videoInfoFilePostQuery.setOrderBy("file_index asc");
        List<VideoInfoFilePost> videoInfoFilePostList = this.videoInfoFilePostService.findListByQuery(videoInfoFilePostQuery);
        VideoPostEditInfoVO vo = new VideoPostEditInfoVO();
        vo.setVideoInfo(videoInfoPost);
        vo.setVideoInfoFileList(videoInfoFilePostList);
        return ResponseVO.getSuccessResponseVO(vo);
    }

    @RequestMapping("/saveVideoInteraction")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Object> saveVideoInteraction(@NotEmpty String videoId, String interaction){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        videoInfoService.changeInteraction(videoId, tokenUserInfoDto.getUserId(), interaction);
        return ResponseVO.getSuccessResponseVO();
    }

    @RequestMapping("/deleteVideo")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Object> deleteVideo(@NotEmpty String videoId){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        videoInfoService.deleteVideo(videoId, tokenUserInfoDto.getUserId());
        return ResponseVO.getSuccessResponseVO();
    }


}
