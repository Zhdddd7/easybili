package com.easybili.web.controller;

import com.easybili.entities.component.EsSearchComponent;
import com.easybili.entities.component.RedisComponent;
import com.easybili.entities.dto.TokenUserInfoDto;
import com.easybili.entities.enums.ResponseCodeEnum;
import com.easybili.entities.enums.UserActionTypeEnum;
import com.easybili.entities.enums.VideoRecommendTypeEnum;
import com.easybili.entities.exception.BusinessException;
import com.easybili.entities.po.UserAction;
import com.easybili.entities.po.VideoInfo;
import com.easybili.entities.po.VideoInfoFile;
import com.easybili.entities.query.UserActionQuery;
import com.easybili.entities.query.VideoInfoFileQuery;
import com.easybili.entities.query.VideoInfoQuery;
import com.easybili.entities.vo.PaginationResultVO;
import com.easybili.entities.vo.ResponseVO;
import com.easybili.entities.vo.VideoInfoResultVO;
import com.easybili.service.UserActionServiceImpl;
import com.easybili.service.VideoInfoFileServiceImpl;
import com.easybili.service.VideoInfoServiceImpl;
import jdk.nashorn.internal.parser.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/video")
@Validated
@Slf4j
public class VideoController extends BaseController{
    @Resource
    private VideoInfoServiceImpl videoInfoService;
    @Resource
    private VideoInfoFileServiceImpl videoInfoFileService;
    @Resource
    private UserActionServiceImpl userActionService;
    @Resource
    private RedisComponent redisComponent;
    @Resource
    private EsSearchComponent esSearchComponent;

    @RequestMapping("/loadRecommendVideo")
    public ResponseVO<Object> loadRecommendVideo(){
        VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
        videoInfoQuery.setOrderBy("create_time desc");
        videoInfoQuery.setQueryUserInfo(true);
        videoInfoQuery.setRecommendType(VideoRecommendTypeEnum.RECOMMEND.getType());
        List<VideoInfo> recommendVideoList = videoInfoService.findListByParam(videoInfoQuery);
        return ResponseVO.getSuccessResponseVO(recommendVideoList);
    }

    @RequestMapping("/loadVideo")
    public ResponseVO<Object> loadVideo(Integer pCategoryId, Integer categoryId, Integer pageNo){
        VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
        videoInfoQuery.setOrderBy("create_time desc");
        videoInfoQuery.setCategoryId(categoryId);
        videoInfoQuery.setPCategoryId(pCategoryId);
        videoInfoQuery.setPageNo(pageNo);
        videoInfoQuery.setQueryUserInfo(true);
        videoInfoQuery.setRecommendType(VideoRecommendTypeEnum.NO_RECOMMEND.getType());
        PaginationResultVO<VideoInfo> resultVO = videoInfoService.findListByPage(videoInfoQuery);
        return ResponseVO.getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/getVideoInfo")
    public ResponseVO<Object> getVideoInfo(@NotEmpty String videoId){
        VideoInfo videoInfo = videoInfoService.getVideoInfoById(videoId);
        if(videoInfo == null){
            throw new BusinessException(ResponseCodeEnum.NOT_FOUND.getMessage());
        }
        // TODO retrieve user behavior
        // only the login state user can see the video states
        TokenUserInfoDto userInfoDto  = getTokenUserInfoDto();
        List<UserAction> userActionList = new ArrayList<>();
        if(userInfoDto != null){
            UserActionQuery userActionQuery = new UserActionQuery();
            userActionQuery.setVideoId(videoId);
            userActionQuery.setUserId(userInfoDto.getUserId());
            userActionQuery.setActionTypeArray(new Integer[]{UserActionTypeEnum.VIDEO_LIKE.getType(), UserActionTypeEnum.VIDEO_COLLECT.getType(), UserActionTypeEnum.VIDEO_COIN.getType()});
            userActionList = userActionService.findListByParam(userActionQuery);
        }

        VideoInfoResultVO resultVO = new VideoInfoResultVO(videoInfo, userActionList);
        return ResponseVO.getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/loadVideoPList")
    public ResponseVO<Object> loadVideoPList(@NotEmpty String videoId){
        VideoInfoFileQuery videoInfoFileQuery = new VideoInfoFileQuery();
        videoInfoFileQuery.setVideoId(videoId);
        videoInfoFileQuery.setOrderBy("file_index asc");
        List<VideoInfoFile> fileList = videoInfoFileService.findListByParam(videoInfoFileQuery);
        return ResponseVO.getSuccessResponseVO(fileList);
    }

    @RequestMapping("/reportVideoPlayOnline")
    public ResponseVO<Object> reportVideoPlayOnline(@NotEmpty String fileId, @NotEmpty String deviceId){
        return ResponseVO.getSuccessResponseVO(redisComponent.reportVideoPlayOnline(fileId, deviceId));
    }

    @RequestMapping("/search")
    public ResponseVO<Object> search(@NotEmpty String keyword, Integer orderType, Integer pageNo){
        redisComponent.addKeywordCount(keyword);
        PaginationResultVO<VideoInfo> resultVO = esSearchComponent.search(true, keyword, orderType, pageNo, 30);
        return ResponseVO.getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/getSearchKeywordTop")
    public ResponseVO<Object> getSearchKeywordTop(){
        List<String> keywordList = redisComponent.getKeywordTop(10);
        return ResponseVO.getSuccessResponseVO(keywordList);
    }

    @RequestMapping("/loadHotVideoList")
    public ResponseVO<Object> loadHotVideoList(Integer pageNo){
        VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
        videoInfoQuery.setPageNo(pageNo);
        videoInfoQuery.setQueryUserInfo(true);
        videoInfoQuery.setOrderBy("play_count desc");
        videoInfoQuery.setLastPlayHour(24);
        PaginationResultVO<VideoInfo> resultVO = videoInfoService.findListByPage(videoInfoQuery);
        return ResponseVO.getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/getVideoRecommend")
    public ResponseVO<Object> getVideoRecommend(String keyword, String videoId){
        List<VideoInfo> resultVO = esSearchComponent.recommend(keyword, videoId, 1, 30);
        return ResponseVO.getSuccessResponseVO(resultVO);
    }


}
