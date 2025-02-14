package com.easybili.service;

import com.easybili.constants.Constants;
import com.easybili.entities.enums.ResponseCodeEnum;
import com.easybili.entities.exception.BusinessException;
import com.easybili.entities.po.UserFocus;
import com.easybili.entities.po.UserInfo;
import com.easybili.entities.query.UserFocusQuery;
import com.easybili.entities.vo.PaginationResultVO;
import com.easybili.entities.vo.SimplePage;
import com.easybili.mappers.UserFocusMapper;
import com.easybili.mappers.UserInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserFocusServiceImpl {
    @Resource
    private UserFocusMapper userFocusMapper;
    @Resource
    private UserInfoMapper userInfoMapper;

    public int insert(UserFocus userFocus) {
        return userFocusMapper.insert(userFocus);
    }

    public int deleteByPrimaryKey(String userId, String focusUserId) {
        return userFocusMapper.deleteById(userId, focusUserId);
    }

    public int updateByPrimaryKey(UserFocus userFocus) {
        return userFocusMapper.updateById(userFocus);
    }

    public UserFocus selectByPrimaryKey(String userId, String focusUserId) {
        return userFocusMapper.selectById(userId, focusUserId);
    }

    public List<UserFocus> selectByQuery(UserFocusQuery query) {
        return userFocusMapper.selectByQuery(query);
    }

    public int updateByQuery(UserFocus record, UserFocusQuery query) {
        return userFocusMapper.updateByQuery(record, query);
    }

    public int deleteByQuery(UserFocusQuery query) {
        return userFocusMapper.deleteByQuery(query);
    }

    public void focusUser(String userId, String focusUserId) {
        if(userId.equals(focusUserId)){
            throw new BusinessException("Do not follow yourself");
        }
        UserFocus dbInfo = this.userFocusMapper.selectById(userId, focusUserId);
        if(dbInfo != null){
            return ;
        }
        UserInfo userInfo = userInfoMapper.selectById(focusUserId);
        if(userInfo == null){
            throw new BusinessException(ResponseCodeEnum.BUSINESS_CUSTOM_ERROR.getMessage());
        }
        UserFocus focus = new UserFocus();
        focus.setUserId(userId);
        focus.setFocusUserId(focusUserId);
        focus.setFocusTime(LocalDateTime.now());
        this.userFocusMapper.insert(focus);
    }

    public void cancelFocusUser(String userId, String focusUserId) {
        this.userFocusMapper.deleteById(userId, focusUserId);
    }

    public PaginationResultVO<UserFocus> findListByPage(UserFocusQuery query) {
        if (query == null) {
            throw new IllegalArgumentException("Query object cannot be null");
        }
        if(query.getPageNo() == null){
            query.setPageNo(1);
        }
        Integer pageSize = query.getPageSize() == null?15:query.getPageSize();
        Integer start = (query.getPageNo() - 1) * pageSize; // 10 pcs every page
        query.setSimplePage(new SimplePage(start, pageSize));
        List<UserFocus> dataList = userFocusMapper.selectByQuery(query);
        return new PaginationResultVO<>(dataList.size(), pageSize, query.getPageNo(), dataList);
    }
}