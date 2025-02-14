package com.easybili.web.controller;

import com.easybili.constants.Constants;
import com.easybili.entities.component.RedisComponent;
import com.easybili.entities.dto.TokenUserInfoDto;
import com.easybili.entities.vo.ResponseVO;
import com.easybili.redis.RedisUtils;



import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.List;

@RestControllerAdvice
public class BaseController<T> {

    @Resource
    private RedisComponent redisComponent;

    /**
     * 返回成功信息
     */
    protected ResponseVO<T> success(T data) {
        return new ResponseVO<>(200, "Success", data);
    }

    /**
     * 返回分页结果
     */
    protected ResponseVO<List<T>> success(List<T> data, long total) {
        ResponseVO<List<T>> response = new ResponseVO<>(200, "Success", data);
        response.setTotal(total);
        return response;
    }

    /**
     * 返回错误信息
     */
    protected ResponseVO<T> error(String message) {
        return new ResponseVO<>(500, message, null);
    }

    /**
     * 全局异常处理
     */
    @ExceptionHandler(Exception.class)
    protected ResponseVO<T> handleException(Exception e) {
        // 可以在这里记录异常日志
        return new ResponseVO<>(500, e.getMessage(), null);
    }

    protected String getIpAddr() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    protected void saveToken2Cookie(HttpServletResponse response, String token){
        Cookie cookie = new Cookie(Constants.TOKEN_HEAD, token);
        // here the time unit is second, the expiry time is 7 days
        cookie.setMaxAge(Constants.REDIS_KEY_EXPIRES_ONE_DAY * 7 /1000);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    protected TokenUserInfoDto getTokenUserInfoDto(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(Constants.TOKEN_HEAD);
        return redisComponent.getTokenInfo(token);
    }

    protected TokenUserInfoDto getTokenInfoFromCookie(){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String token = getTokenFromCookie(request);
        if(token == null){
            return null;
        }
        return redisComponent.getTokenInfo(token);
    }

    private String getTokenFromCookie(HttpServletRequest request){
        Cookie[] cookies =  request.getCookies();
        if(cookies == null){
            return null;
        }
        for (Cookie cookie:cookies){
            if(cookie.getName().equalsIgnoreCase(Constants.TOKEN_HEAD)){
                return cookie.getValue();
            }
        }
        return null;
    }

    protected void cleanCookie(HttpServletResponse response) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(Constants.TOKEN_HEAD)) {
                redisComponent.cleanToken(cookie.getValue());
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
                break;
            }
        }
    }


}
