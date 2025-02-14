package com.easybili.admin.controller;

import com.easybili.constants.Constants;

import com.easybili.entities.component.RedisComponent;
import com.easybili.entities.vo.ResponseVO;
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
        Cookie cookie = new Cookie(Constants.TOKEN_ADMIN, token);

        // also here we can set the val to -1, which means the session mode
        cookie.setMaxAge(-1);
        // here the time unit is second, the expiry time is 1 day
//        cookie.setMaxAge(Constants.REDIS_KEY_EXPIRES_ONE_DAY/1000);
        cookie.setPath("/");
        response.addCookie(cookie);
    }


}
