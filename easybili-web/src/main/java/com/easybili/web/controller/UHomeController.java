package com.easybili.web.controller;

import com.easybili.entities.dto.TokenUserInfoDto;
import com.easybili.entities.enums.UserActionTypeEnum;
import com.easybili.entities.enums.VideoOrderTypeEnum;
import com.easybili.entities.po.*;
import com.easybili.entities.query.UserActionQuery;
import com.easybili.entities.query.UserFocusQuery;
import com.easybili.entities.query.VideoInfoQuery;
import com.easybili.entities.vo.PaginationResultVO;
import com.easybili.entities.vo.ResponseVO;
import com.easybili.entities.vo.UserInfoVO;
import com.easybili.service.UserActionServiceImpl;
import com.easybili.service.UserFocusServiceImpl;
import com.easybili.service.UserInfoServiceImpl;
import com.easybili.service.VideoInfoServiceImpl;
import com.easybili.web.annotation.GlobalInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.*;

@RestController
@RequestMapping("/api/uhome")
@Validated
@Slf4j
public class UHomeController extends BaseController<Object> {
    @Resource
    private UserInfoServiceImpl userInfoService;
    @Resource
    private VideoInfoServiceImpl videoInfoService;
    @Resource
    private UserFocusServiceImpl userFocusService;
    @Resource
    private UserActionServiceImpl userActionService;

    @RequestMapping("/getUserInfo")
    public ResponseVO<Object> getUserInfo(@NotEmpty String userId){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        UserInfo userInfo = userInfoService.getUserDetailInfo(tokenUserInfoDto ==null?null:tokenUserInfoDto.getUserId(), userId);
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(userInfo, userInfoVO);
        return ResponseVO.getSuccessResponseVO(userInfoVO);
    }

    @RequestMapping("/updateUserInfo")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Object> updateUserInfo(@NotEmpty @Size(max = 20)String userName,
                                             @NotEmpty @Size(max = 100)String avatar,
                                             @NotNull Integer sex,
                                             @Size(max = 10)String birthday,
                                             @Size(max = 100)String school,
                                             @Size(max = 80) String personIntroduction,
                                             @Size(max = 300) String noticeInfo
                                             ){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(tokenUserInfoDto.getUserId());
        userInfo.setUserName(userName);
        userInfo.setAvatar(avatar);
        userInfo.setSex(sex);
        userInfo.setBirthday(birthday);
        userInfo.setSchool(school);
        userInfo.setPersonIntroduction(personIntroduction);
        userInfo.setNoticeInfo(noticeInfo);

        userInfoService.updateUserInfo(userInfo, tokenUserInfoDto);

        return ResponseVO.getSuccessResponseVO();
    }

    @RequestMapping("/saveTheme")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Object> saveTheme(@Min(1) @Max (10)@NotNull Integer theme){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        UserInfo userInfo = new UserInfo();
        userInfo.setTheme(theme);
        userInfoService.updateByUserId(userInfo, tokenUserInfoDto.getUserId());
        return ResponseVO.getSuccessResponseVO();
    }

    @RequestMapping("/focus")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Object> focus(@NotEmpty String focusUserId){
        userFocusService.focusUser(getTokenUserInfoDto().getUserId(), focusUserId);
        return ResponseVO.getSuccessResponseVO();
    }

    @RequestMapping("/cancelFocus")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Object> cancelFocus(@NotEmpty String focusUserId){
        userFocusService.cancelFocusUser(getTokenUserInfoDto().getUserId(), focusUserId);
        return ResponseVO.getSuccessResponseVO();
    }

    @RequestMapping("/loadFocusList")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Object> loadFocusList(Integer pageNo){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        UserFocusQuery userFocusQuery = new UserFocusQuery();
        userFocusQuery.setUserId(tokenUserInfoDto.getUserId());
        userFocusQuery.setPageNo(pageNo);
        userFocusQuery.setOrderBy("u.focus_time desc");
        userFocusQuery.setQueryType(0);
        PaginationResultVO<UserFocus> resultVO = userFocusService.findListByPage(userFocusQuery);
        return ResponseVO.getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/loadFansList")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Object> loadFansList(Integer pageNo){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        UserFocusQuery userFocusQuery = new UserFocusQuery();
        userFocusQuery.setFocusUserId(tokenUserInfoDto.getUserId());
        userFocusQuery.setPageNo(pageNo);
        userFocusQuery.setOrderBy("u.focus_time desc");
        userFocusQuery.setQueryType(1);
        PaginationResultVO<UserFocus> resultVO = userFocusService.findListByPage(userFocusQuery);
        return ResponseVO.getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/loadVideoList")
    public ResponseVO<Object> loadVideoList(@NotEmpty String userId, Integer type, Integer pageNo, String videoName, Integer orderType){
        VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
        if(type != null){
            videoInfoQuery.setPageSize(10);
        }
        VideoOrderTypeEnum videoOrderTypeEnum = VideoOrderTypeEnum.getByType(orderType);
        if(videoOrderTypeEnum == null){
            videoOrderTypeEnum = VideoOrderTypeEnum.CREATE_TIME;
        }
        videoInfoQuery.setOrderBy(videoOrderTypeEnum.getField() + " desc");
        videoInfoQuery.setVideoNameFuzzy(videoName);
        videoInfoQuery.setPageNo(pageNo);
        videoInfoQuery.setUserId(userId);
        PaginationResultVO<VideoInfo> resultVO = videoInfoService.findListByPage(videoInfoQuery);
        return ResponseVO.getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/loadUserCollection")
    public ResponseVO<Object> loadUserCollection(@NotEmpty String userId, Integer pageNo){
        UserActionQuery actionQuery = new UserActionQuery();
        actionQuery.setActionType(UserActionTypeEnum.VIDEO_COLLECT.getType());
        actionQuery.setUserId(userId);
        actionQuery.setPageNo(pageNo);
        actionQuery.setOrderBy("action_time desc");
        actionQuery.setQueryVideoInfo(true);
        PaginationResultVO<UserAction> resultVO = userActionService.findListByPage(actionQuery);
        return ResponseVO.getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/series/loadVideoSeriesWithVideo")
    public ResponseVO<Object> loadVideoSeriesWithVideo(){

        return ResponseVO.getSuccessResponseVO();
    }




}
