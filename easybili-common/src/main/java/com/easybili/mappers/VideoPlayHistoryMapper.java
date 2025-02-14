package com.easybili.mappers;

import com.easybili.entities.po.VideoPlayHistory;
import com.easybili.entities.query.VideoPlayHistoryQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VideoPlayHistoryMapper {
    List<VideoPlayHistory> selectByQuery(@Param("query") VideoPlayHistoryQuery query);

    VideoPlayHistory selectByPrimaryKey(@Param("userId") String userId,@Param("videoId") String videoId);

    int insert(VideoPlayHistory record);

    int update(@Param("record") VideoPlayHistory record, @Param("userId") String userId,@Param("videoId") String videoId);

    int deleteById(VideoPlayHistory record);

    void deleteByQuery(@Param("query") VideoPlayHistoryQuery query);
}
