package com.easybili.admin.controller;

import com.easybili.entities.po.VideoComment;
import com.easybili.entities.query.UserInfoQuery;
import com.easybili.entities.query.VideoCommentQuery;
import com.easybili.entities.vo.PaginationResultVO;
import com.easybili.entities.vo.ResponseVO;
import com.easybili.service.UserInfoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
@Validated
@Slf4j
public class UserController extends BaseController<Object>{
    @Resource
    private UserInfoServiceImpl userInfoService;

    @RequestMapping("/loadUser")
    public ResponseVO<Object> loadUser(UserInfoQuery userInfoQuery) {
        userInfoQuery.setOrderBy("join_time desc");
        return ResponseVO.getSuccessResponseVO(userInfoService.findListByPage(userInfoQuery));
    }

    @RequestMapping("/changeStatus")
    public ResponseVO<Object> changeStatus(String userId, Integer status) {
        userInfoService.changeUserStatus(userId, status);
        return ResponseVO.getSuccessResponseVO(null);
    }

}
