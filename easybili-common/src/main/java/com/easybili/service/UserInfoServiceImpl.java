package com.easybili.service;

import com.easybili.constants.Constants;
import com.easybili.entities.component.RedisComponent;
import com.easybili.entities.dto.CountInfoDto;
import com.easybili.entities.dto.SysSettingDto;
import com.easybili.entities.dto.TokenUserInfoDto;
import com.easybili.entities.dto.UserCountInfoDto;
import com.easybili.entities.enums.ResponseCodeEnum;
import com.easybili.entities.enums.UserSexEnum;
import com.easybili.entities.enums.UserStatusEnum;
import com.easybili.entities.enums.VideoRecommendTypeEnum;
import com.easybili.entities.exception.BusinessException;
import com.easybili.entities.po.UserFocus;
import com.easybili.entities.po.UserInfo;
import com.easybili.entities.po.VideoInfo;
import com.easybili.entities.query.UserInfoQuery;
import com.easybili.entities.vo.PaginationResultVO;
import com.easybili.entities.vo.SimplePage;
import com.easybili.entities.vo.UserInfoVO;
import com.easybili.mappers.UserFocusMapper;
import com.easybili.mappers.UserInfoMapper;
import com.easybili.mappers.VideoInfoMapper;
import com.easybili.utils.StringTools;

import com.easybili.utils.UserInfoConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserInfoServiceImpl {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private RedisComponent redisComponent;
    @Resource
    private UserFocusMapper userFocusMapper;
    @Resource
    private VideoInfoMapper videoInfoMapper;

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
    public UserInfo getUserInfoById(String userId) {
    return userInfoMapper.selectById(userId);
}

    /**
     * insert user
     */
    public void insertUser(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
    }

    /**
     * update user
     */
    public void updateByUserId(UserInfo userInfo, String userId) {
        userInfoMapper.update(userInfo, userId);
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

        SysSettingDto sysSettingDto = redisComponent.getSysSettingDto();
        userInfo.setCurrentCoinCount(sysSettingDto.getRegisterCoinCount());
        userInfo.setTotalCoinCount(sysSettingDto.getRegisterCoinCount());
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
        this.userInfoMapper.update(updateInfo, updateInfo.getUserId());

        TokenUserInfoDto tokenUserInfoDto = new TokenUserInfoDto();
        BeanUtils.copyProperties(userInfo, tokenUserInfoDto);
        redisComponent.saveTokenInfo(tokenUserInfoDto);
        return tokenUserInfoDto;
    }

    public UserInfo getUserDetailInfo(String currentUserId, String userId){
        UserInfo userInfo = getUserInfoById(userId);
        if(userInfo == null){
            throw new BusinessException(ResponseCodeEnum.NOT_FOUND.getMessage());
        }
        CountInfoDto countInfoDto = videoInfoMapper.selectSumCountInfo(userId);
        BeanUtils.copyProperties(countInfoDto, userInfo);

        Integer fansCount = userFocusMapper.selectFansCount(userId);
        Integer focusCount = userFocusMapper.selectFocusCount(userId);
        userInfo.setFansCount(fansCount);
        userInfo.setFocusCount(focusCount);

        if(currentUserId == null){
            userInfo.setHaveFocus(false);
        }else{
            UserFocus userFocus = userFocusMapper.selectById(currentUserId, userId);
            userInfo.setHaveFocus(userFocus != null);
        }

        return  userInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUserInfo(UserInfo userInfo, TokenUserInfoDto tokenUserInfoDto) {
        UserInfo dbInfo = this.userInfoMapper.selectById(userInfo.getUserId());
        if(!dbInfo.getUserName().equals(userInfo.getUserName()) && dbInfo.getCurrentCoinCount()< Constants.UPDATE_USER_NAME_COIN){
            throw new BusinessException("You do not have enough coins");
        }
        if(!dbInfo.getUserName().equals(userInfo.getUserName())){
            // set a lock
            Integer count = this.userInfoMapper.updateCoinCountInfo(userInfo.getUserId(), -Constants.UPDATE_USER_NAME_COIN);
            if(count == 0){
                throw new BusinessException("You do not have enough coins");
            }
        }
        this.userInfoMapper.update(userInfo, userInfo.getUserId());

        Boolean updateTokenInfo = false;
        if(!userInfo.getAvatar().equals(tokenUserInfoDto.getAvatar())){
            tokenUserInfoDto.setAvatar(userInfo.getAvatar());
            updateTokenInfo = true;
        }
        if(!userInfo.getUserName().equals(tokenUserInfoDto.getUserName())){
            tokenUserInfoDto.setUserName(userInfo.getUserName());
            updateTokenInfo = true;
        }
        if(updateTokenInfo){
            redisComponent.updateTokenInfo(tokenUserInfoDto);
        }
    }

    public UserCountInfoDto getUserCountInfo(String userId) {
        UserInfo userInfo = getUserInfoById(userId);
        Integer fansCount = userFocusMapper.selectFansCount(userId);
        Integer focusCount = userFocusMapper.selectFocusCount(userId);
        UserCountInfoDto userCountInfoDto = new UserCountInfoDto();
        BeanUtils.copyProperties(userInfo, userCountInfoDto);
        userCountInfoDto.setFansCount(fansCount);
        userCountInfoDto.setFocusCount(focusCount);
        userCountInfoDto.setCurrentCoinCount(userCountInfoDto.getCurrentCoinCount());
        return userCountInfoDto;
    }


    public Integer findCountByQuery(UserInfoQuery userInfoQuery) {
        return this.userInfoMapper.selectCountByQuery(userInfoQuery);
    }


    public Object findListByPage(UserInfoQuery query) {
        if (query == null) {
            throw new IllegalArgumentException("Query object cannot be null");
        }
        if(query.getPageNo() == null){
            query.setPageNo(1);
        }
        if(query.getPageSize() ==null){
            query.setPageSize(10);
        }
        int start = (query.getPageNo() - 1) * query.getPageSize(); // 10 pcs every page
        query.setSimplePage(new SimplePage(start, query.getPageSize()));
        List<UserInfo> dataList = userInfoMapper.selectList(query);
        return new PaginationResultVO<>(dataList.size(), query.getPageSize(), query.getPageNo(), dataList);
    }

    public void changeUserStatus(String userId, Integer status) {
        UserInfo userInfo = new UserInfo();
        userInfo.setStatus(status);
        this.userInfoMapper.update(userInfo, userId);
    }
}
