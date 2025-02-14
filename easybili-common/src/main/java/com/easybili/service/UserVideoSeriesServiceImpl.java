package com.easybili.service;

import com.easybili.entities.po.UserVideoSeries;
import com.easybili.entities.query.UserVideoSeriesQuery;
import com.easybili.mappers.UserVideoSeriesMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserVideoSeriesServiceImpl {

    @Resource
    private UserVideoSeriesMapper mapper;

    public int insert(UserVideoSeries series) {
        return mapper.insert(series);
    }

    public int deleteById(Integer seriesId) {
        return mapper.deleteById(seriesId);
    }

    public int updateById(UserVideoSeries series) {
        return mapper.updateById(series);
    }

    public UserVideoSeries selectById(Integer seriesId) {
        return mapper.selectById(seriesId);
    }

    public List<UserVideoSeries> selectByQuery(UserVideoSeriesQuery query) {
        return mapper.selectByQuery(query);
    }

    public int updateByQuery(UserVideoSeries record, UserVideoSeriesQuery query) {
        return mapper.updateByQuery(record, query);
    }

    public int deleteByQuery(UserVideoSeriesQuery query) {
        return mapper.deleteByQuery(query);
    }
}