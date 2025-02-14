package com.easybili.service;

import com.easybili.constants.Constants;
import com.easybili.entities.component.RedisComponent;
import com.easybili.entities.enums.StatisticsTypeEnum;
import com.easybili.entities.enums.UserActionTypeEnum;
import com.easybili.entities.po.StatisticsInfo;
import com.easybili.entities.po.VideoInfo;
import com.easybili.entities.query.StatisticsInfoQuery;
import com.easybili.entities.query.UserInfoQuery;
import com.easybili.entities.query.VideoInfoQuery;
import com.easybili.mappers.StatisticsInfoMapper;
import com.easybili.mappers.UserFocusMapper;
import com.easybili.mappers.UserInfoMapper;
import com.easybili.mappers.VideoInfoMapper;
import com.easybili.utils.DateUtils;
import com.easybili.utils.StringTools;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsInfoServiceImpl{

    @Resource
    private StatisticsInfoMapper statisticsInfoMapper;
    @Resource
    private RedisComponent redisComponent;
    @Resource
    private VideoInfoMapper videoInfoMapper;
    @Resource
    private UserFocusMapper userFocusMapper;
    @Resource
    private UserInfoMapper userInfoMapper;

     
    public StatisticsInfo findByPrimaryKey(String statisticsDate, String userId, Integer dataType) {
        return statisticsInfoMapper.selectByPrimaryKey(statisticsDate, userId, dataType);
    }
     
    public int save(StatisticsInfo statisticsInfo) {
        return statisticsInfoMapper.insert(statisticsInfo);
    }

     
    public int update(StatisticsInfo statisticsInfo) {
        return statisticsInfoMapper.updateByPrimaryKey(statisticsInfo);
    }

     
    public int deleteByPrimaryKey(String statisticsDate, String userId, Integer dataType) {
        return statisticsInfoMapper.deleteByPrimaryKey(statisticsDate, userId, dataType);
    }

     
    public List<StatisticsInfo> findByQuery(StatisticsInfoQuery query) {
        return statisticsInfoMapper.selectByQuery(query);
    }

     
    public int deleteByQuery(StatisticsInfoQuery query) {
        return statisticsInfoMapper.deleteByQuery(query);
    }

     
    public int updateByQuery(StatisticsInfoQuery query, StatisticsInfo record) {
        return statisticsInfoMapper.updateByQuery(query, record);
    }

    public void statisticsData(){
        List<StatisticsInfo> statisticsInfoList = new ArrayList<>();
        final String staticDate = DateUtils.getBeforeDayDate(3);

        Map<String, Integer> videoPlayCountMap = redisComponent.getVideoPlayCount(staticDate);
        List<String> playVideoKeys = new ArrayList<>(videoPlayCountMap.keySet());
        playVideoKeys = playVideoKeys.stream().map(item -> item.substring(item.lastIndexOf(":") + 1)).collect(Collectors.toList());
        VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
        videoInfoQuery.setVideoIdArray(playVideoKeys.toArray(new String[playVideoKeys.size()]));
        List<VideoInfo> videoInfoList = videoInfoMapper.selectByQuery(videoInfoQuery);

        Map<String, Integer> videoCountMap = videoInfoList.stream().collect(Collectors.groupingBy(VideoInfo::getUserId, Collectors.summingInt(item ->
                videoPlayCountMap.get(Constants.REDIS_KEY_VIDEO_PLAY_COUNT + staticDate + ":" +  item.getVideoId()))));

        videoCountMap.forEach((k, v) ->
        {
            StatisticsInfo statisticsInfo = new StatisticsInfo();
            statisticsInfo.setUserId(k);
            statisticsInfo.setStatisticsDate(staticDate);
            statisticsInfo.setDataType(StatisticsTypeEnum.PLAY.getType());
            statisticsInfo.setStatisticsCount(v);
            statisticsInfoList.add(statisticsInfo);
        });

        // stats fans count
        List<StatisticsInfo> fansDataList = this.statisticsInfoMapper.selectStatFans(staticDate);
        for(StatisticsInfo statisticsInfo: fansDataList){
            statisticsInfo.setStatisticsDate(staticDate);
            statisticsInfo.setDataType(StatisticsTypeEnum.FANS.getType());
        }
        statisticsInfoList.addAll(fansDataList);

        // stats comments count
        List<StatisticsInfo> commentDataList = this.statisticsInfoMapper.selectStatComment(staticDate);
        for(StatisticsInfo statisticsInfo: commentDataList){
            statisticsInfo.setStatisticsDate(staticDate);
            statisticsInfo.setDataType(StatisticsTypeEnum.COMMENT.getType());
        }
        statisticsInfoList.addAll(commentDataList);

        // Danmu, like, collect, coins
        List<StatisticsInfo> otherDataList = this.statisticsInfoMapper.selectStatOther(staticDate, new Integer[]{
                UserActionTypeEnum.VIDEO_LIKE.getType(),
                UserActionTypeEnum.VIDEO_COIN.getType(),
                UserActionTypeEnum.VIDEO_COLLECT.getType(),
        });
        for(StatisticsInfo statisticsInfo: otherDataList){
            statisticsInfo.setStatisticsDate(staticDate);
            if (UserActionTypeEnum.VIDEO_LIKE.getType().equals(statisticsInfo.getDataType())) {
                statisticsInfo.setDataType(StatisticsTypeEnum.LIKE.getType());
            } else if (UserActionTypeEnum.VIDEO_COLLECT.getType().equals(statisticsInfo.getDataType())) {
                statisticsInfo.setDataType(StatisticsTypeEnum.COLLECTION.getType());
            } else if (UserActionTypeEnum.VIDEO_COIN.getType().equals(statisticsInfo.getDataType())) {
                statisticsInfo.setDataType(StatisticsTypeEnum.COIN.getType());
            }
        }
        statisticsInfoList.addAll(otherDataList);
        this.statisticsInfoMapper.insertBatch(statisticsInfoList);
    }

    public Map<String, Integer> getStatisticInfoActualTime(String userId) {
        Map<String, Integer> result = statisticsInfoMapper.selectTotalCountInfo(userId);
        if(!StringTools.isEmpty(userId)){
            result.put("userId", userFocusMapper.selectFansCount(userId));
        }else{
            result.put("userCount", userInfoMapper.selectCountByQuery(new UserInfoQuery()));
        }
        return result;
    }

    public List<StatisticsInfo> findListTotalInfoByQuery(StatisticsInfoQuery query) {
        return this.statisticsInfoMapper.selectListTotalInfoByQuery(query);
    }

    public List<StatisticsInfo> findUserCountTotalInfoByQuery(StatisticsInfoQuery query) {
        return this.statisticsInfoMapper.selectUserCountTotalInfoByQuery(query);
    }
}