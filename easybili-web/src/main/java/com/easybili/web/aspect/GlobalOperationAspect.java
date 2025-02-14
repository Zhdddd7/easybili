package com.easybili.web.aspect;

import com.easybili.constants.Constants;
import com.easybili.entities.dto.TokenUserInfoDto;
import com.easybili.entities.enums.ResponseCodeEnum;
import com.easybili.entities.exception.BusinessException;
import com.easybili.redis.RedisUtils;
import com.easybili.utils.StringTools;
import com.easybili.web.annotation.GlobalInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class GlobalOperationAspect {
    @Resource
    private RedisUtils<Object> redisUtils;
    @Before("@annotation(com.easybili.web.annotation.GlobalInterceptor)")
    public void interceptorDo(JoinPoint point){
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        GlobalInterceptor interceptor = method.getAnnotation(GlobalInterceptor.class);
        if(interceptor == null){
            return;
        }
        if(interceptor.checkLogin()){
            checkLogin();
        }
    }

    private void checkLogin(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(Constants.TOKEN_HEAD);
        if(StringTools.isEmpty(token)){
            throw new BusinessException(ResponseCodeEnum.NOT_LOGIN.getMessage());
        }
        TokenUserInfoDto tokenUserInfoDto = (TokenUserInfoDto) redisUtils.get(Constants.REDIS_KEY_TOKEN_WEB + token);
        if (tokenUserInfoDto == null){
            throw new BusinessException(ResponseCodeEnum.NOT_LOGIN.getMessage());
        }

    }


}
