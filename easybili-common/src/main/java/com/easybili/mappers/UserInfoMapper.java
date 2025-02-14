package com.easybili.mappers;

import com.easybili.entities.po.UserInfo;
import com.easybili.entities.query.UserInfoQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserInfoMapper {
    UserInfo selectById(String id);

    UserInfo selectByUserName(String userName);

    UserInfo selectByEmail(String email);

    void insert(UserInfo userInfo);

    void update(@Param("userInfo")UserInfo userInfo, @Param("userId") String userId);

    void deleteById(String id);

    Integer updateCoinCountInfo(@Param("userId") String userId, @Param("changeCount")Integer changeCount);

    List<UserInfo> selectList(@Param("query") UserInfoQuery query);

    Integer selectCountByQuery(@Param("query") UserInfoQuery query);

//    void updateByUserId(UserInfo userInfo, @Param("userId") String userId);
}
