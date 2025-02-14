package com.easybili.admin.interceptor;

import com.easybili.constants.Constants;
import com.easybili.entities.component.RedisComponent;
import com.easybili.entities.exception.BusinessException;
import com.easybili.utils.StringTools;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AppIntercepter implements HandlerInterceptor {

    private final static String URL_ACCOUNT = "/account";
    private final static String URL_FILE = "/file";

    @Resource
    private RedisComponent redisComponent;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(null == handler){
            return false;
        }
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        // like the checkcode, login should not be intercepted
        if(request.getRequestURI().contains(URL_ACCOUNT)){
            return true;
        }
        String token = request.getHeader(Constants.TOKEN_ADMIN);
        // retrieve figures
        if(request.getRequestURI().contains(URL_FILE)){
            token = getTokenFromCookie(request);
        }
        if(StringTools.isEmpty(token)){
            throw new BusinessException("not login or out of time");
        }
        Object sessionObj = redisComponent.getTokenInfo4Admin(token);
        if(null == sessionObj){
            throw new BusinessException("not login or out of time");
        }
        return true;
    }

    // get token from cookie
    private String getTokenFromCookie(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            return null;
        }
        String token = null;
        for(Cookie cookie: cookies){
            if(cookie.getName().equals(Constants.TOKEN_ADMIN)){
                return cookie.getValue();
            }
        }
        return null;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
