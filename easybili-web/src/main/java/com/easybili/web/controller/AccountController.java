package com.easybili.web.controller;


import com.easybili.constants.Constants;
import com.easybili.entities.component.RedisComponent;

import com.easybili.entities.dto.TokenUserInfoDto;

import com.easybili.entities.dto.UserCountInfoDto;
import com.easybili.entities.exception.BusinessException;
import com.easybili.entities.vo.ResponseVO;
import com.easybili.service.UserInfoServiceImpl;
import com.easybili.utils.StringTools;
import com.wf.captcha.ArithmeticCaptcha;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/account")
@Validated
public class AccountController extends BaseController<String>{
    @Resource
    private UserInfoServiceImpl userInfoService;

    @Resource
    private RedisComponent redisComponent;

//  the method written in sessions
//    @RequestMapping("/checkCode")
//    public ResponseVO<String> checkCode(HttpSession session){
//        ArithmeticCaptcha captcha = new ArithmeticCaptcha(100, 42);
//        String code = captcha.text();
//        session.setAttribute("checkCode", code);
//        String checkCodeBase64 = captcha.toBase64();
//        return ResponseVO.getSuccessResponseVO(checkCodeBase64);
//    }
//
//    @RequestMapping("register")
//    public ResponseVO<Boolean> register(HttpSession session, String checkCode){
//        String myCheckCode = (String) session.getAttribute("checkCode");
//        return ResponseVO.getSuccessResponseVO(myCheckCode.equalsIgnoreCase(checkCode));
//    }
//}
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

    @RequestMapping("/register")
    public ResponseVO<Boolean> register(@NotEmpty @Email @Size(max = 100) String email,
                                        @NotEmpty @Size(max = 30) String nickName,
                                        @NotEmpty @Pattern(regexp = Constants.REGEX_PASSWORD) String registerPassword,
                                        @NotEmpty String checkCodeKey,
                                        @NotEmpty String checkCode
    ) {
        try {
            if (!checkCode.equalsIgnoreCase(redisComponent.getCheckCode(checkCodeKey))) {
//                String notice = "the checkcode is not right, current checkcode is " + redisComponent.getCheckCode(checkCodeKey) + ", but you are providing " + checkCode;
                throw new BusinessException("the checkcode is not correct");
            }
            userInfoService.register(email, nickName, registerPassword);
            return ResponseVO.getSuccessResponseVO();
        } finally {
            redisComponent.cleanCheckCode(checkCodeKey);
        }

    }

    // here we use password instead of passWord, since the frontend is using password
    @RequestMapping("/login")
    public ResponseVO<TokenUserInfoDto> login(HttpServletRequest request, HttpServletResponse response,
                                              @NotEmpty @Email String email,
                                              @NotEmpty String password,
                                              @NotEmpty String checkCodeKey,
                                              @NotEmpty String checkCode){
        try {
            if (!checkCode.equalsIgnoreCase(redisComponent.getCheckCode(checkCodeKey))) {
//                String notice = "the checkcode is not right, current checkcode is " + redisComponent.getCheckCode(checkCodeKey) + ", but you are providing " + checkCode;
                throw new BusinessException("the checkcode is not correct");
            }
            String ip = getIpAddr();
            TokenUserInfoDto tokenUserInfoDto = userInfoService.login(email, password, ip);
            saveToken2Cookie(response, tokenUserInfoDto.getToken());

            return ResponseVO.getSuccessResponseVO(tokenUserInfoDto);
        } finally {
            redisComponent.cleanCheckCode(checkCodeKey);
            Cookie[] cookies = request.getCookies();
            String token = null;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(Constants.TOKEN_HEAD)) {
                    token = cookie.getValue();
                }
            }
            if (!StringTools.isEmpty(token)) {
                redisComponent.cleanToken(token);
            }

        }

    }

    @RequestMapping("/autoLogin")
    public ResponseVO<TokenUserInfoDto> autoLogin(HttpServletResponse response){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        System.out.println("a simple hint");
        System.out.println(tokenUserInfoDto);
        if(tokenUserInfoDto == null){
            return ResponseVO.getSuccessResponseVO();
        }
        if(tokenUserInfoDto.getExpireAt() - System.currentTimeMillis() < Constants.REDIS_KEY_EXPIRES_ONE_DAY){
            redisComponent.saveTokenInfo(tokenUserInfoDto);
            saveToken2Cookie(response, tokenUserInfoDto.getToken());
        }
        return ResponseVO.getSuccessResponseVO(tokenUserInfoDto);
    }

    @RequestMapping("logout")
    public ResponseVO<Object> logout(HttpServletResponse response){
        cleanCookie(response);
        return ResponseVO.getSuccessResponseVO();
    }

    @RequestMapping("/getUserCountInfo")
    public ResponseVO<Object> getUserCountInfo(){
        TokenUserInfoDto tokenUserInfoDto = getTokenInfoFromCookie();
        UserCountInfoDto userCountInfoDto = userInfoService.getUserCountInfo(tokenUserInfoDto.getUserId());
        return ResponseVO.getSuccessResponseVO(userCountInfoDto);
    }




}
