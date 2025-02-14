package com.easybili.service;

import com.easybili.entities.po.VideoInfo;
import com.easybili.entities.po.VideoPlayHistory;
import com.easybili.entities.query.VideoPlayHistoryQuery;
import com.easybili.entities.vo.PaginationResultVO;
import com.easybili.entities.vo.SimplePage;
import com.easybili.mappers.VideoPlayHistoryMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VideoPlayHistoryServiceImpl {

    @Resource
    private VideoPlayHistoryMapper videoPlayHistoryMapper;

    public List<VideoPlayHistory> findListByQuery(VideoPlayHistoryQuery query) {
        return videoPlayHistoryMapper.selectByQuery(query);
    }

    public int save(VideoPlayHistory record) {
        return videoPlayHistoryMapper.insert(record);
    }

    public int update(VideoPlayHistory record, String userId, String videoId) {
        return videoPlayHistoryMapper.update(record, userId, videoId);
    }

    public int delete(VideoPlayHistory record) {
        return videoPlayHistoryMapper.deleteById(record);
    }

    public void saveHistory(String userId, String videoId, Integer fileIndex){
        VideoPlayHistory videoPlayHistory = new VideoPlayHistory();
        videoPlayHistory.setVideoId(videoId);
        videoPlayHistory.setUserId(userId);
        videoPlayHistory.setFileIndex(fileIndex);
        videoPlayHistory.setLastUpdateTime(LocalDateTime.now());
        // insert or update: primary key is videoId & userId
        VideoPlayHistory dbvideoPlayHistory = videoPlayHistoryMapper.selectByPrimaryKey(userId, videoId);
        if(dbvideoPlayHistory != null){
            videoPlayHistoryMapper.update(videoPlayHistory, userId, videoId);
        }else{
            videoPlayHistoryMapper.insert(videoPlayHistory);
        }

    }

    public PaginationResultVO<VideoPlayHistory> findListByPage(VideoPlayHistoryQuery query) {
        if (query == null) {
            throw new IllegalArgumentException("Query object cannot be null");
        }
        if(query.getPageNo() == null){
            query.setPageNo(1);
        }
        if(query.getPageSize() ==null){
            query.setPageSize(10);
        }
        int start = (query.getPageNo() - 1) * query.getPageSize(); // 10 pcs every page
        query.setSimplePage(new SimplePage(start, query.getPageSize()));
        List<VideoPlayHistory> dataList = videoPlayHistoryMapper.selectByQuery(query);
        return new PaginationResultVO<>(dataList.size(), query.getPageSize(), query.getPageNo(), dataList);
    }

    public void deleteByQuery(VideoPlayHistoryQuery query) {
        videoPlayHistoryMapper.deleteByQuery(query);
    }
}
