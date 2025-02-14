package com.easybili.mappers;


import com.easybili.entities.po.VideoInfoPost;
import com.easybili.entities.query.VideoInfoPostQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VideoInfoPostMapper {
    VideoInfoPost selectById(String videoId);

    List<VideoInfoPost> selectByCategoryId(Integer categoryId);

    List<VideoInfoPost> selectByPCategoryId(Integer pCategoryId);

    List<VideoInfoPost> selectByUserId(String userId);

    int insert(VideoInfoPost videoInfoPost);

    int deleteById(String videoId);

    List<VideoInfoPost> selectList(@Param("query")VideoInfoPostQuery query);

    void updateByVideoId(@Param("videoUpdate")VideoInfoPost videoUpdate, @Param("videoId") String videoId);

    Integer sumDuration(String videoId);

    Integer findCountByParam(@Param("query")VideoInfoPostQuery query);

    Integer updateByParam(@Param("videoUpdate")VideoInfoPost videoUpdate, @Param("query")VideoInfoPostQuery query);
}
