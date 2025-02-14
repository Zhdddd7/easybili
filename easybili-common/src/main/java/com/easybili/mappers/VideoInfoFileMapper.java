package com.easybili.mappers;

import com.easybili.entities.po.VideoInfoFile;
import com.easybili.entities.query.VideoInfoFileQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VideoInfoFileMapper {
    VideoInfoFile selectById(String fileId);

    List<VideoInfoFile> selectByVideoId(String videoId);

    int insert(VideoInfoFile videoInfoFile);

    int update(VideoInfoFile videoInfoFile);

    int deleteById(String fileId);

    void deleteByParam(@Param("query")VideoInfoFileQuery query);

    void insertBatch(@Param("videoInfoFileList") List<VideoInfoFile> videoInfoFileList);

    List<VideoInfoFile> selectByQuery(@Param("query")VideoInfoFileQuery query);
}
