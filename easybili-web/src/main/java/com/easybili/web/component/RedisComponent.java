package com.easybili.web.component;

import com.easybili.constants.Constants;
import com.easybili.redis.RedisUtils;
import jdk.nashorn.internal.parser.Token;
import org.springframework.stereotype.Component;
import com.easybili.web.dto.TokenUserInfoDto;
import javax.annotation.Resource;
import java.util.UUID;

@Component
public class RedisComponent {
    @Resource
    private RedisUtils redisUtils;

    public String saveCheckCode(String code){
        String checkCodeKey = UUID.randomUUID().toString();
        redisUtils.setex(Constants.REDIS_KEY_PREFIX + checkCodeKey, code, Constants.REDIS_KEY_EXPIRES_ONE_MIN * 10);
        return checkCodeKey;
    }

    // retrieve checkcode while registering
    public String getCheckCode(String checkCodeKey){
        return (String) redisUtils.get(Constants.REDIS_KEY_PREFIX + checkCodeKey);
    }

    // delete checkcode to release storage
    public void cleanCheckCode(String checkCodeKey){
        redisUtils.delete(Constants.REDIS_KEY_PREFIX + checkCodeKey);
    }

    public void saveTokenInfo(TokenUserInfoDto tokenUserInfoDto){
        String token = UUID.randomUUID().toString();
        tokenUserInfoDto.setExpireAt(System.currentTimeMillis() + Constants.REDIS_KEY_EXPIRES_ONE_DAY * 7);
        tokenUserInfoDto.setToken(token);
        redisUtils.setex(Constants.REDIS_KEY_PREFIX + token, tokenUserInfoDto, Constants.REDIS_KEY_EXPIRES_ONE_DAY * 7);
    }

    public TokenUserInfoDto getTokenInfo(String token){
        return (TokenUserInfoDto) redisUtils.get(Constants.REDIS_KEY_TOKEN_WEB + token);
    }

}
