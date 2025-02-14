package com.easybili.admin.controller;

import com.easybili.annotation.RecordUserMessage;
import com.easybili.entities.dto.TokenUserInfoDto;
import com.easybili.entities.enums.MessageTypeEnum;
import com.easybili.entities.enums.VideoStatusEnum;
import com.easybili.entities.po.VideoInfoPost;
import com.easybili.entities.query.VideoInfoPostQuery;
import com.easybili.entities.vo.PaginationResultVO;
import com.easybili.entities.vo.ResponseVO;
import com.easybili.service.VideoInfoFilePostServiceImpl;
import com.easybili.service.VideoInfoPostServiceImpl;
import com.easybili.service.VideoInfoServiceImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequestMapping("/videoInfo")
@Validated
public class VideoInfoController extends BaseController<Object>{

    @Resource
    private VideoInfoPostServiceImpl videoInfoPostService;

    @Resource
    private VideoInfoFilePostServiceImpl videoInfoFilePostService;

    @Resource
    private VideoInfoServiceImpl videoInfoService;

    @RequestMapping("/loadVideoList")
    public ResponseVO<Object> loadVideoList(VideoInfoPostQuery videoInfoPostQuery){
        videoInfoPostQuery.setOrderBy("v.last_update desc");
        videoInfoPostQuery.setQueryCountInfo(true);
        videoInfoPostQuery.setQueryUserInfo(true);

        PaginationResultVO<VideoInfoPost> resultVO = videoInfoPostService.findListByPage(videoInfoPostQuery);
        return ResponseVO.getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/auditVideo")
    @RecordUserMessage(messageType = MessageTypeEnum.SYS)
    public ResponseVO<Object> auditVideo(@NotEmpty String videoId, @NotNull Integer status, String reason) throws IOException {
        videoInfoPostService.auditVideo(videoId, status, reason);
        return ResponseVO.getSuccessResponseVO();
    }

    @RequestMapping("/recommendVideo")
    public ResponseVO<Object> recommendVideo(@NotEmpty String videoId) throws IOException {
        videoInfoService.recommendVideo(videoId);
        return ResponseVO.getSuccessResponseVO();
    }

    @RequestMapping("/deleteVideo")
    public ResponseVO<Object> deleteVideo(@NotEmpty String videoId) throws IOException {
        videoInfoService.deleteVideo(videoId, null);
        return ResponseVO.getSuccessResponseVO();
    }




}
