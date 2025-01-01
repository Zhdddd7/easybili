package com.easybili.web.mappers;

import com.easybili.web.po.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper {
    UserInfo selectById(String id);

    UserInfo selectByUserName(String userName);

    UserInfo selectByEmail(String email);

    void insert(UserInfo userInfo);

    void update(UserInfo userInfo);

    void deleteById(String id);
}
