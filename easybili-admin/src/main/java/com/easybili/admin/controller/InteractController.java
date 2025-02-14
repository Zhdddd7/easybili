package com.easybili.admin.controller;

import com.easybili.entities.dto.TokenUserInfoDto;
import com.easybili.entities.po.VideoComment;
import com.easybili.entities.po.VideoDanmu;
import com.easybili.entities.query.VideoCommentQuery;
import com.easybili.entities.query.VideoDanmuQuery;
import com.easybili.entities.vo.PaginationResultVO;
import com.easybili.entities.vo.ResponseVO;
import com.easybili.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/interact")
@Validated
@Slf4j
public class InteractController extends BaseController<Object>{
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

    @RequestMapping("/loadComment")
    public ResponseVO<Object> loadComment(Integer pageNo, String videoNameFuzzy) {
        VideoCommentQuery videoCommentQuery = new VideoCommentQuery();
        videoCommentQuery.setOrderBy("comment_id desc");
        videoCommentQuery.setPageNo(pageNo);
        videoCommentQuery.setQueryVideoInfo(true);
        videoCommentQuery.setVideoNameFuzzy(videoNameFuzzy);

        PaginationResultVO<VideoComment> resultVO = videoCommentService.findListByPage(videoCommentQuery);
        return ResponseVO.getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/delComment")
    public ResponseVO<Object> delComment(@NotNull Integer commentId) {
        videoCommentService.deleteComment(commentId, null);
        return ResponseVO.getSuccessResponseVO();
    }

    @RequestMapping("/loadDanmu")
    public ResponseVO<Object> loadDanmu(Integer pageNo, String videoNameFuzzy) {
        VideoDanmuQuery videoDanmuQuery = new VideoDanmuQuery();
        videoDanmuQuery.setOrderBy("danmu_id desc");
        videoDanmuQuery.setPageNo(pageNo);
        videoDanmuQuery.setQueryVideoInfo(true);
        PaginationResultVO<VideoDanmu> resultVO = videoDanmuService.findListByPage(videoDanmuQuery);
        return ResponseVO.getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/delDanmu")
    public ResponseVO<Object> delDanmu(@NotNull Integer danmuId) {
        videoDanmuService.deleteDanmu(null, danmuId);
        return ResponseVO.getSuccessResponseVO();
    }


}
