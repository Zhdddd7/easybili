package com.easybili.mappers;

import com.easybili.entities.dto.UserMessageCountDto;
import com.easybili.entities.po.UserMessage;
import com.easybili.entities.query.UserMessageQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMessageMapper {
    // 基于主键的操作
    UserMessage selectByPrimaryKey(@Param("messageId") Integer messageId);
    int insert(UserMessage userMessage);
    int updateByPrimaryKey(UserMessage userMessage);
    int deleteByPrimaryKey(@Param("messageId") Integer messageId);

    // 基于索引的操作
    List<UserMessage> selectByUserId(@Param("userId") String userId);
    int deleteByUserId(@Param("userId") String userId);

    // 基于 Query 的操作
    List<UserMessage> selectByQuery(@Param("query") UserMessageQuery query);
    int deleteByQuery(@Param("query") UserMessageQuery query);
    int updateByQuery(@Param("query") UserMessageQuery query, @Param("record") UserMessage record);

    Integer selectCount(@Param("query") UserMessageQuery query);

    List<UserMessageCountDto> getMessageTypeNoReadCount(@Param("userId") String userId);
}
