package com.easybili.web.controller;

import com.easybili.entities.dto.TokenUserInfoDto;
import com.easybili.entities.po.VideoComment;
import com.easybili.entities.po.VideoDanmu;
import com.easybili.entities.po.VideoInfo;
import com.easybili.entities.query.VideoCommentQuery;
import com.easybili.entities.query.VideoDanmuQuery;
import com.easybili.entities.query.VideoInfoQuery;
import com.easybili.entities.vo.PaginationResultVO;
import com.easybili.entities.vo.ResponseVO;
import com.easybili.service.*;
import com.easybili.web.annotation.GlobalInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/ucenter")
@Validated
@Slf4j
public class UCenterInteractionController extends BaseController<Object>{
    @Resource
    private VideoInfoPostServiceImpl videoInfoPostService;

    @Resource
    private VideoInfoFilePostServiceImpl videoInfoFilePostService;

    @Resource
    private VideoInfoServiceImpl videoInfoService;

    @Resource
    private VideoCommentServiceImpl videoCommentService;
    @Resource
    private VideoDanmuServiceImpl videoDanmuService;

    @RequestMapping("/loadAllVideo")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Object> loadAllVideo() {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
        videoInfoQuery.setUserId(tokenUserInfoDto.getUserId());
        videoInfoQuery.setOrderBy("create_time desc");
        List<VideoInfo> videoInfoList = videoInfoService.findListByParam(videoInfoQuery);
        return ResponseVO.getSuccessResponseVO(videoInfoList);}

    @RequestMapping("/loadComment")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Object> loadComment(Integer pageNo, String videoId) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();

        VideoCommentQuery videoCommentQuery = new VideoCommentQuery();
        videoCommentQuery.setVideoId(videoId);
        videoCommentQuery.setVideoUserId(tokenUserInfoDto.getUserId());
        videoCommentQuery.setOrderBy("comment_id desc");
        videoCommentQuery.setPageNo(pageNo);
        videoCommentQuery.setQueryVideoInfo(true);
        PaginationResultVO<VideoComment> resultVO = videoCommentService.findListByPage(videoCommentQuery);
        return ResponseVO.getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/delComment")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Object> delComment(@NotNull Integer commentId) {
        videoCommentService.deleteComment(commentId, getTokenUserInfoDto().getUserId());
        return ResponseVO.getSuccessResponseVO();
    }

    @RequestMapping("/loadDanmu")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Object> loadDanmu(Integer pageNo, String videoId) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        VideoDanmuQuery danmuQuery = new VideoDanmuQuery();
        danmuQuery.setVideoId(videoId);
        danmuQuery.setPageNo(pageNo);
        danmuQuery.setOrderBy("danmu_id desc");
        danmuQuery.setQueryVideoInfo(true);
        PaginationResultVO<VideoDanmu> resultVO = videoDanmuService.findListByPage(danmuQuery);
        return ResponseVO.getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/delDanmu")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Object> delDanmu(@NotNull Integer danmuId) {
        videoDanmuService.deleteDanmu(getTokenUserInfoDto().getUserId(), danmuId);
        return ResponseVO.getSuccessResponseVO();
    }

}
