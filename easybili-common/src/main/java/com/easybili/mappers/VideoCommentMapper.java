package com.easybili.mappers;

import com.easybili.entities.po.VideoComment;
import com.easybili.entities.query.VideoCommentQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideoCommentMapper<T, P> {

    void insert(VideoComment videoComment);

    void update(VideoComment videoComment);

    void deleteById(@Param("commentId") Integer commentId);

    VideoComment selectById(@Param("commentId") Integer commentId);

    List<VideoComment> selectByQuery(@Param("query") VideoCommentQuery query);

    List<T> selectByQueryWithChildren(@Param("query") P query);

    void updateCountInfo(@Param("commentId") Integer commentId, @Param("field") String field, @Param("changeCount") Integer changeCount,
                         @Param("opposeField")String opposeField, @Param("opposeChangeCount") Integer opposeChangeCount);

    void updateByQuery(@Param("videoComment") VideoComment videoComment,@Param("query") VideoCommentQuery query);

    void updateByCommentId(@Param("videoComment") VideoComment videoComment,@Param("commentId") Integer commentId);

    void deleteByQuery(@Param("query")VideoCommentQuery query);
}
