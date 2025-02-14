package com.easybili.web.controller;

import com.easybili.entities.po.VideoDanmu;
import com.easybili.entities.po.VideoInfo;
import com.easybili.entities.query.VideoDanmuQuery;
import com.easybili.entities.vo.ResponseVO;
import com.easybili.service.VideoDanmuServiceImpl;
import com.easybili.service.VideoInfoServiceImpl;
import com.easybili.web.annotation.GlobalInterceptor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController("videoDanmuController")
@RequestMapping("/api/danmu")
public class VideoDanmuController extends BaseController{
    @Resource
    private VideoDanmuServiceImpl videoDanmuService;
    @Resource
    private VideoInfoServiceImpl videoInfoService;

    @RequestMapping("/postDanmu")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Object> postDanmu(@NotEmpty String videoId, @NotEmpty String fileId,
                                @NotEmpty @Size(max = 200) String text,
                                @NotNull Integer mode, @NotEmpty String color,
                                @NotNull Integer time) {
        VideoDanmu videoDanmu = new VideoDanmu();
        videoDanmu.setVideoId(videoId);
        videoDanmu.setFileId(fileId);
        videoDanmu.setText(text);
        videoDanmu.setMode(mode);
        videoDanmu.setColor(color);
        videoDanmu.setTime(time);
        videoDanmu.setUserId(getTokenUserInfoDto().getUserId());
        videoDanmu.setPostTime(LocalDateTime.now());
        videoDanmuService.saveVideoDanmu(videoDanmu);
        return ResponseVO.getSuccessResponseVO();
    }

    @RequestMapping("/loadDanmu")
    public ResponseVO<Object> loadDanmu(@NotEmpty String fileId, @NotEmpty String videoId) {
        VideoInfo videoInfo = videoInfoService.getVideoInfoById(videoId);
        // check if this video allows interaction
        if(videoInfo.getInteraction() != null && videoInfo.getInteraction().contains("1")){
            return ResponseVO.getSuccessResponseVO(new ArrayList<>());
        }
        VideoDanmuQuery videoDanmuQuery = new VideoDanmuQuery();
        videoDanmuQuery.setFileId(fileId);
        videoDanmuQuery.setOrderBy("danmu_id asc");

        return ResponseVO.getSuccessResponseVO(videoDanmuService.findListByParam(videoDanmuQuery));
    }

}
