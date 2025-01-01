package com.easybili.web.controller;

import com.easybili.web.po.UserInfo;
import com.easybili.web.service.UserInfoServiceImpl;
import com.easybili.web.vo.ResponseVO;
import com.easybili.web.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/user")
public class TestUserInfoController extends BaseController<String> {

    @Autowired
    private UserInfoServiceImpl userInfoService;

    // 查询用户信息
    @GetMapping("/{id}")
    public ResponseVO<UserInfoVO> getUser(@PathVariable("id") String id) {
        UserInfoVO userInfo = userInfoService.getUserInfo(id);
        if (userInfo == null) {
            return ResponseVO.getErrorResponseVO("User not found");
        }
        return ResponseVO.getSuccessResponseVO(userInfo);
    }

    // 创建用户
    @PostMapping
    public ResponseVO<String> createUser(@RequestBody UserInfo userInfo) {
        userInfo.setJoinTime(LocalDateTime.now());
        userInfoService.insertUser(userInfo);
        return ResponseVO.getSuccessResponseVO("User created successfully");
    }

    // 更新用户信息
    @PutMapping
    public ResponseVO<String> updateUser(@RequestBody UserInfo userInfo) {
        userInfoService.updateUser(userInfo);
        return ResponseVO.getSuccessResponseVO("User updated successfully");
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public ResponseVO<String> deleteUser(@PathVariable("id") String id) {
        userInfoService.deleteUser(id);
        return ResponseVO.getSuccessResponseVO("User deleted successfully");
    }
}
