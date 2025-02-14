package com.easybili.service;

import com.easybili.entities.po.VideoInfoFile;
import com.easybili.entities.query.VideoInfoFileQuery;
import com.easybili.mappers.VideoInfoFileMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class VideoInfoFileServiceImpl {
    @Resource
    private VideoInfoFileMapper videoInfoFileMapper;

    public VideoInfoFile getVideoInfoFileById(String fileId) {
        return videoInfoFileMapper.selectById(fileId);
    }

    public List<VideoInfoFile> getVideoInfoFilesByVideoId(String videoId) {
        return videoInfoFileMapper.selectByVideoId(videoId);
    }

    public boolean addVideoInfoFile(VideoInfoFile videoInfoFile) {
        return videoInfoFileMapper.insert(videoInfoFile) > 0;
    }

    public boolean updateVideoInfoFile(VideoInfoFile videoInfoFile) {
        return videoInfoFileMapper.update(videoInfoFile) > 0;
    }

    public boolean deleteVideoInfoFile(String fileId) {
        return videoInfoFileMapper.deleteById(fileId) > 0;
    }

    public List<VideoInfoFile> findListByParam(VideoInfoFileQuery videoInfoFileQuery) {
        return videoInfoFileMapper.selectByQuery(videoInfoFileQuery);
    }
}
