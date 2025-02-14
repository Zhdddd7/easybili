package com.easybili.mappers;

import com.easybili.entities.po.VideoInfoFilePost;
import com.easybili.entities.query.VideoInfoFilePostQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface VideoInfoFilePostMapper {
    VideoInfoFilePost selectById(String fileId);

    List<VideoInfoFilePost> selectByVideoId(String videoId);

    List<VideoInfoFilePost> selectByUploadIdAndUserId(@Param("uploadId") String uploadId, @Param("userId")String userId);

    int insert(VideoInfoFilePost videoInfoFilePost);

    int update(VideoInfoFilePost videoInfoFilePost);

    int deleteById(String fileId);

    List<VideoInfoFilePost> selectList(VideoInfoFilePostQuery fileQuery);

    void deleteBatchByFileId(@Param("delFileList") List<String> delFileList, @Param("userId") String userId);

    void insertOrUpdateBatch(@Param("uploadFileList") List<VideoInfoFilePost> uploadFileList);

    void updateByUploadIdAndUserId(@Param("updateFilePost")VideoInfoFilePost updateFilePost,@Param("uploadId") String uploadId,@Param("userId") String userId);

    Integer selectCount(VideoInfoFilePostQuery filePostQuery);

    Integer sumDuration(String videoId);

    Integer updateByParam(@Param("updateFilePost")VideoInfoFilePost updateFilePost, @Param("query") VideoInfoFilePostQuery query);

    List<VideoInfoFilePost> selectByQuery(@Param("query")VideoInfoFilePostQuery query);

    void deleteByQuery(@Param("query")VideoInfoFilePostQuery query);
}
