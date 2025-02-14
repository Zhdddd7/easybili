package com.easybili.admin.controller;


import com.easybili.entities.config.AppConfig;
import com.easybili.entities.component.RedisComponent;
import com.easybili.entities.exception.BusinessException;
import com.easybili.entities.vo.ResponseVO;
import com.easybili.utils.StringTools;
import com.wf.captcha.ArithmeticCaptcha;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/account")
@Validated
public class AccountController extends BaseController<String>{

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private AppConfig appConfig;

    @RequestMapping("/checkCode")
    public ResponseVO<Map<String, String>> checkCode(HttpSession session) {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(100, 42);
        String code = captcha.text();
        String checkCodeKey = redisComponent.saveCheckCode(code);
        String checkCodeBase64 = captcha.toBase64();

        Map<String, String> result = new HashMap<>();
        result.put("checkCode", checkCodeBase64);
        result.put("checkCodeKey", checkCodeKey);

        return ResponseVO.getSuccessResponseVO(result);
    }


    // here we use password instead of passWord, since the frontend is using password
    @RequestMapping("login")
    public ResponseVO<String> login(HttpServletResponse response,
                                    @NotEmpty String account,
                                    @NotEmpty String password,
                                    @NotEmpty String checkCodeKey,
                                    @NotEmpty String checkCode){

        try {
            if (!checkCode.equalsIgnoreCase(redisComponent.getCheckCode(checkCodeKey))) {
//                String notice = "the checkcode is not right, current checkcode is " + redisComponent.getCheckCode(checkCodeKey) + ", but you are providing " + checkCode;
                throw new BusinessException("The checkcode is not correct");
            }
            System.out.println("the checkcode passed");
            if(!account.equals(appConfig.getAdminAccount()) || !password.equals(StringTools.encodeByMd5(appConfig.getAdminPassword()))){
                throw new BusinessException("The account or the password not correct");
            }
            String token = redisComponent.saveTokenInfo4Admin(account);
            saveToken2Cookie(response, token);
            return ResponseVO.getSuccessResponseVO(account);
        } finally {
            redisComponent.cleanCheckCode(checkCodeKey);
        }

    }





}
