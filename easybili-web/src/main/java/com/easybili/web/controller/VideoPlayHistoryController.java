package com.easybili.web.controller;

import com.easybili.entities.dto.TokenUserInfoDto;
import com.easybili.entities.po.VideoPlayHistory;
import com.easybili.entities.query.VideoPlayHistoryQuery;
import com.easybili.entities.vo.ResponseVO;
import com.easybili.service.StatisticsInfoServiceImpl;
import com.easybili.service.VideoPlayHistoryServiceImpl;
import com.easybili.web.annotation.GlobalInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RestController
@RequestMapping("/api/history")
@Slf4j
public class VideoPlayHistoryController extends BaseController<Object>{
    @Resource
    private VideoPlayHistoryServiceImpl videoPlayHistoryService;
    @Resource
    private StatisticsInfoServiceImpl statisticsInfoService;

    @RequestMapping("/loadHistory")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Object> loadHistory(Integer pageNo) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        VideoPlayHistoryQuery videoPlayHistoryQuery = new VideoPlayHistoryQuery();
        videoPlayHistoryQuery.setUserId(tokenUserInfoDto.getUserId());
        videoPlayHistoryQuery.setOrderBy("last_update_time desc");
        videoPlayHistoryQuery.setPageNo(pageNo);
        videoPlayHistoryQuery.setQueryVideoDetail(true);
        return ResponseVO.getSuccessResponseVO(videoPlayHistoryService.findListByPage(videoPlayHistoryQuery));
    }

    @RequestMapping("/cleanHistory")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Object> cleanHistory() {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        VideoPlayHistoryQuery videoPlayHistoryQuery = new VideoPlayHistoryQuery();
        videoPlayHistoryQuery.setUserId(tokenUserInfoDto.getUserId());
        videoPlayHistoryService.deleteByQuery(videoPlayHistoryQuery);
        return ResponseVO.getSuccessResponseVO(null);
    }

    @RequestMapping("/delHistory")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Object> delHistory(@NotEmpty String videoId) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        VideoPlayHistoryQuery videoPlayHistoryQuery = new VideoPlayHistoryQuery();
        videoPlayHistoryQuery.setUserId(tokenUserInfoDto.getUserId());
        videoPlayHistoryQuery.setVideoId(videoId);
        videoPlayHistoryService.deleteByQuery(videoPlayHistoryQuery);
        return ResponseVO.getSuccessResponseVO(null);
    }

    @RequestMapping("/test")
    public ResponseVO<Object> test(){
        statisticsInfoService.statisticsData();
        return ResponseVO.getSuccessResponseVO(null);
    }


}
