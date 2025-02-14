package com.easybili.web.service;


import com.easybili.constants.Constants;
import com.easybili.entities.component.RedisComponent;
import com.easybili.entities.dto.TokenUserInfoDto;
import com.easybili.utils.StringTools;

import com.easybili.web.enums.UserSexEnum;
import com.easybili.web.enums.UserStatusEnum;
import com.easybili.web.mappers.UserInfoMapper;
import com.easybili.web.po.UserInfo;
import com.easybili.web.utils.UserInfoConverter;
import com.easybili.web.vo.UserInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.easybili.web.exception.BusinessException;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class UserInfoServiceImpl {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Resource
    private RedisComponent redisComponent;

    /**
     * query user info
     */
    public UserInfoVO getUserInfo(String userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        if (userInfo == null) {
            return null;
        }

        // 将 UserInfo 转换为 UserInfoVO
        return UserInfoConverter.toUserInfoVO(userInfo);
    }


//    public UserInfoVO getUserInfoByUserName(String userName){
//        UserInfo userInfo = userInfoMapper.selectByUserName(userName);
//        if (userInfo == null) {
//            return null;
//        }
//
//    }

    /**
     * insert user
     */
    public void insertUser(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
    }

    /**
     * update user
     */
    public void updateUser(UserInfo userInfo) {
        userInfoMapper.update(userInfo);
    }

    /**
     * delete user
     */
    public void deleteUser(String userId) {
        userInfoMapper.deleteById(userId);
    }

    /**
     * delete user
     */
    public void register(String email, String userName, String passWord){
        UserInfo userInfo = this.userInfoMapper.selectByEmail(email);
        if(userInfo != null){
            throw new BusinessException("the email already exists");
        }
        UserInfo userInfoByName = this.userInfoMapper.selectByUserName(userName);
        if (userInfoByName != null){
            throw new BusinessException("the name already exists");
        }
        userInfo = new UserInfo();
        String userId = StringTools.getRandomNumber(Constants.LENGTH_ID);
        userInfo.setUserId(userId);
        userInfo.setUserName(userName);
        userInfo.setEmail(email);
        userInfo.setPassword(StringTools.encodeByMd5(passWord));
        userInfo.setJoinTime(LocalDateTime.now());
        userInfo.setStatus(UserStatusEnum.ACTIVE.getStatus());
        userInfo.setSex(UserSexEnum.OTHER.getSex());
        userInfo.setTheme(Constants.THEME_ONE);

        // TODO init the user coins


        this.userInfoMapper.insert(userInfo);
    }

    public TokenUserInfoDto login(String email, String passWord, String ip){
        UserInfo userInfo = this.userInfoMapper.selectByEmail(email);
        if (userInfo == null || !userInfo.getPassword().equals(passWord)){
            throw new BusinessException("Account or Password not correct");
        }
        if (UserStatusEnum.BANNED.getStatus().equals(userInfo.getStatus())){
            throw new BusinessException("This account is banned.");
        }
        UserInfo updateInfo = new UserInfo();
        updateInfo.setUserId(userInfo.getUserId());
        updateInfo.setLastLoginIp(ip);
        updateInfo.setLastLoginTime(LocalDateTime.now());
        this.userInfoMapper.update(updateInfo);

        TokenUserInfoDto tokenUserInfoDto = new TokenUserInfoDto();
        BeanUtils.copyProperties(userInfo, tokenUserInfoDto);
        redisComponent.saveTokenInfo(tokenUserInfoDto);
        return tokenUserInfoDto;
    }

}
