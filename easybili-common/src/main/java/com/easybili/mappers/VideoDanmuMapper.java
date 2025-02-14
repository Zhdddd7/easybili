package com.easybili.mappers;

import com.easybili.entities.po.VideoDanmu;
import com.easybili.entities.query.VideoDanmuQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideoDanmuMapper {

    void insert(VideoDanmu videoDanmu);

    void update(VideoDanmu videoDanmu);

    void deleteById(@Param("danmuId") Integer danmuId);

    VideoDanmu selectById(@Param("danmuId") Integer danmuId);

    List<VideoDanmu> selectByQuery(@Param("query") VideoDanmuQuery query);

    void deleteByParam(@Param("query")VideoDanmuQuery query);
}
