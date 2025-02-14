package com.easybili.mappers;

import com.easybili.entities.dto.CountInfoDto;
import com.easybili.entities.po.VideoInfo;
import com.easybili.entities.query.VideoInfoQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface VideoInfoMapper {
    VideoInfo selectById(String videoId);

    List<VideoInfo> selectByQuery(@Param("query") VideoInfoQuery query);

    List<VideoInfo> selectByCategoryId(Integer categoryId);

    List<VideoInfo> selectByCreateTime(LocalDateTime createTime);

    List<VideoInfo> selectByLastPlayTime(LocalDateTime lastPlayTime);

    List<VideoInfo> selectByRecommendType(Byte recommendType);

    List<VideoInfo> selectByTags(String tags);

    List<VideoInfo> selectByUserId(String userId);

    int insert(VideoInfo videoInfo);

    int update(@Param("videoInfo")VideoInfo videoInfo,@Param("videoId") String videoId);

    int deleteById(String videoId);

    int updateByQuery(@Param("videoInfo")VideoInfo videoInfo, @Param("query")VideoInfoQuery query);

    void insertOrUpdate(@Param("videoInfo")VideoInfo videoInfo);

    void updateCountInfo(@Param("videoId") String videoId, @Param("field") String field, @Param("changeCount") Integer changeCount);

    Integer selectCountByQuery(@Param("query") VideoInfoQuery query);

    CountInfoDto selectSumCountInfo(String userId);
}
