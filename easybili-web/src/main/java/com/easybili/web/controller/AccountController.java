package com.easybili.web.controller;

import com.easybili.web.component.RedisComponent;
import com.easybili.constants.Constants;
import com.easybili.web.dto.TokenUserInfoDto;
import com.easybili.web.exception.BusinessException;
import com.easybili.web.service.UserInfoServiceImpl;
import com.easybili.web.vo.ResponseVO;
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

    @RequestMapping("register")
    public ResponseVO<Boolean> register(@NotEmpty @Email @Size(max = 100) String email,
                                        @NotEmpty @Size(max = 30) String userName,
                                        @NotEmpty @Pattern(regexp = Constants.REGEX_PASSWORD) String passWord,
                                        @NotEmpty String checkCodeKey,
                                        @NotEmpty String checkCode
    ) {
        try {
            if (!checkCode.equalsIgnoreCase(redisComponent.getCheckCode(checkCodeKey))) {
//                String notice = "the checkcode is not right, current checkcode is " + redisComponent.getCheckCode(checkCodeKey) + ", but you are providing " + checkCode;
                throw new BusinessException("the checkcode is not correct");
            }
            userInfoService.register(email, userName, passWord);
            return ResponseVO.getSuccessResponseVO();
        } finally {
            redisComponent.cleanCheckCode(checkCodeKey);
        }

    }

    @RequestMapping("login")
    public ResponseVO<TokenUserInfoDto> login(HttpServletResponse response,
                                    @NotEmpty @Email String email,
                                    @NotEmpty String passWord,
                                    @NotEmpty String checkCodeKey,
                                    @NotEmpty String checkCode){
        try {
            if (!checkCode.equalsIgnoreCase(redisComponent.getCheckCode(checkCodeKey))) {
//                String notice = "the checkcode is not right, current checkcode is " + redisComponent.getCheckCode(checkCodeKey) + ", but you are providing " + checkCode;
                throw new BusinessException("the checkcode is not correct");
            }
            String ip = getIpAddr();
            TokenUserInfoDto tokenUserInfoDto = userInfoService.login(email, passWord, ip);
            saveToken2Cookie(response, tokenUserInfoDto.getToken());
            // TODO set fanscount, coins, focuscount

            return ResponseVO.getSuccessResponseVO(tokenUserInfoDto);
        } finally {
            redisComponent.cleanCheckCode(checkCodeKey);
        }

    }

    @RequestMapping("autologin")
    public ResponseVO<TokenUserInfoDto> autoLogin(HttpServletResponse response){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        if(tokenUserInfoDto == null){
            return ResponseVO.getSuccessResponseVO();
        }
        if(tokenUserInfoDto.getExpireAt() - System.currentTimeMillis() < Constants.REDIS_KEY_EXPIRES_ONE_DAY){
            redisComponent.saveTokenInfo(tokenUserInfoDto);
            saveToken2Cookie(response, tokenUserInfoDto.getToken());
        }
        // TODO set fanscount, coins, focuscount
        return ResponseVO.getSuccessResponseVO(tokenUserInfoDto);
    }

//    @RequestMapping("logout")
    // when logout event triggered, clean the cookie



}
