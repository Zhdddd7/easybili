package com.easybili.mappers;

import com.easybili.entities.po.UserVideoSeriesVideo;
import com.easybili.entities.query.UserVideoSeriesVideoQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserVideoSeriesVideoMapper {

    int insert(UserVideoSeriesVideo seriesVideo);

    int deleteById(Integer seriesId, String videoId);

    int updateById(UserVideoSeriesVideo seriesVideo);

    UserVideoSeriesVideo selectById(Integer seriesId, String videoId);

    List<UserVideoSeriesVideo> selectByQuery(UserVideoSeriesVideoQuery query);

    int updateByQuery(UserVideoSeriesVideo record, UserVideoSeriesVideoQuery query);

    int deleteByQuery(UserVideoSeriesVideoQuery query);
}