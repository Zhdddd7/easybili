package com.easybili.mappers;

import com.easybili.entities.po.UserAction;
import com.easybili.entities.query.UserActionQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserActionMapper {

    void insert(UserAction userAction);

    void update(UserAction userAction);

    void deleteById(@Param("actionId") Integer actionId);

    UserAction selectById(@Param("actionId") Integer actionId);

    List<UserAction> selectByQuery(@Param("query") UserActionQuery query);

    UserAction selectByVideoIdAndCommentIdAndActionTypeAndUserId(@Param("videoId")String videoId,@Param("commentId") Integer commentId,@Param("actionType") Integer actionType,@Param("userId") String userId);
}
