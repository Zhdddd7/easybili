package com.easybili.mappers;

import com.easybili.entities.po.UserVideoSeries;
import com.easybili.entities.query.UserVideoSeriesQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserVideoSeriesMapper {

    int insert(UserVideoSeries series);

    int deleteById(Integer seriesId);

    int updateById(UserVideoSeries series);

    UserVideoSeries selectById(Integer seriesId);

    List<UserVideoSeries> selectByQuery(UserVideoSeriesQuery query);

    int updateByQuery(UserVideoSeries record, UserVideoSeriesQuery query);

    int deleteByQuery(UserVideoSeriesQuery query);
}
