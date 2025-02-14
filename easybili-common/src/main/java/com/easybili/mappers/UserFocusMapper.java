package com.easybili.mappers;

import com.easybili.entities.po.UserFocus;
import com.easybili.entities.query.UserFocusQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserFocusMapper {

    Integer insert(UserFocus userFocus);

    Integer deleteById(@Param("userId") String userId, @Param("focusUserId") String focusUserId);

    Integer updateById(UserFocus userFocus);

    UserFocus selectById(@Param("userId") String userId, @Param("focusUserId") String focusUserId);

    List<UserFocus> selectByQuery(@Param("query")UserFocusQuery query);

    Integer updateByQuery(@Param("record") UserFocus record, @Param("query") UserFocusQuery query);

    Integer deleteByQuery(@Param("query")UserFocusQuery query);

    Integer selectFansCount(@Param("userId") String userId);

    Integer selectFocusCount(@Param("userId") String userId);

}