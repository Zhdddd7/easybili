package com.easybili.service;

import com.easybili.entities.po.VideoInfoFilePost;
import com.easybili.entities.query.VideoInfoFilePostQuery;
import com.easybili.mappers.VideoInfoFilePostMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class VideoInfoFilePostServiceImpl {
    @Resource
    private VideoInfoFilePostMapper videoInfoFilePostMapper;

    public VideoInfoFilePost getVideoInfoFilePostById(String fileId) {
        return videoInfoFilePostMapper.selectById(fileId);
    }

    public List<VideoInfoFilePost> getVideoInfoFilePostsByVideoId(String videoId) {
        return videoInfoFilePostMapper.selectByVideoId(videoId);
    }

    public List<VideoInfoFilePost> getVideoInfoFilePostsByUploadIdAndUserId(String uploadId, String userId) {
        return videoInfoFilePostMapper.selectByUploadIdAndUserId(uploadId, userId);
    }

    public boolean addVideoInfoFilePost(VideoInfoFilePost videoInfoFilePost) {
        return videoInfoFilePostMapper.insert(videoInfoFilePost) > 0;
    }

    public boolean updateVideoInfoFilePost(VideoInfoFilePost videoInfoFilePost) {
        return videoInfoFilePostMapper.update(videoInfoFilePost) > 0;
    }

    public boolean deleteVideoInfoFilePost(String fileId) {
        return videoInfoFilePostMapper.deleteById(fileId) > 0;
    }

    public List<VideoInfoFilePost> findListByQuery(VideoInfoFilePostQuery videoInfoFilePostQuery) {
        return videoInfoFilePostMapper.selectByQuery(videoInfoFilePostQuery);
    }
}
