package com.easybili.mappers;

import com.easybili.entities.po.StatisticsInfo;
import com.easybili.entities.query.StatisticsInfoQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StatisticsInfoMapper {
    // 基于主键的操作
    StatisticsInfo selectByPrimaryKey(@Param("statisticsDate") String statisticsDate, @Param("userId") String userId, @Param("dataType") Integer dataType);

    int insert(StatisticsInfo statisticsInfo);

    int updateByPrimaryKey(StatisticsInfo statisticsInfo);

    int deleteByPrimaryKey(@Param("statisticsDate") String statisticsDate, @Param("userId") String userId, @Param("dataType") Integer dataType);

    // 基于 Query 的操作
    List<StatisticsInfo> selectByQuery(@Param("query") StatisticsInfoQuery query);

    int deleteByQuery(@Param("query") StatisticsInfoQuery query);

    int updateByQuery(@Param("query") StatisticsInfoQuery query, @Param("record") StatisticsInfo record);

    List<StatisticsInfo> selectStatFans(@Param("statisticsDate") String statisticsDate);

    List<StatisticsInfo> selectStatComment(@Param("statisticsDate")String statisticsDate);

    List<StatisticsInfo> selectStatOther(@Param("statisticsDate")String statisticsDate, @Param("actionTypeArray") Integer[] actionTypeArray);

    void insertBatch(@Param("statisticsInfoList") List<StatisticsInfo> statisticsInfoList);

    Map<String, Integer> selectTotalCountInfo(@Param("userId") String userId);

    List<StatisticsInfo> selectListTotalInfoByQuery(@Param("query") StatisticsInfoQuery query);

    List<StatisticsInfo> selectUserCountTotalInfoByQuery(@Param("query")StatisticsInfoQuery query);
}