package com.easybili.service;

import com.easybili.entities.po.UserVideoSeriesVideo;
import com.easybili.entities.query.UserVideoSeriesVideoQuery;
import com.easybili.mappers.UserVideoSeriesVideoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserVideoSeriesVideoServiceImpl {

    @Resource
    private UserVideoSeriesVideoMapper mapper;

    public int insert(UserVideoSeriesVideo seriesVideo) {
        return mapper.insert(seriesVideo);
    }

    public int deleteById(Integer seriesId, String videoId) {
        return mapper.deleteById(seriesId, videoId);
    }

    public int updateById(UserVideoSeriesVideo seriesVideo) {
        return mapper.updateById(seriesVideo);
    }

    public UserVideoSeriesVideo selectById(Integer seriesId, String videoId) {
        return mapper.selectById(seriesId, videoId);
    }

    public List<UserVideoSeriesVideo> selectByQuery(UserVideoSeriesVideoQuery query) {
        return mapper.selectByQuery(query);
    }

    public int updateByQuery(UserVideoSeriesVideo record, UserVideoSeriesVideoQuery query) {
        return mapper.updateByQuery(record, query);
    }

    public int deleteByQuery(UserVideoSeriesVideoQuery query) {
        return mapper.deleteByQuery(query);
    }
}