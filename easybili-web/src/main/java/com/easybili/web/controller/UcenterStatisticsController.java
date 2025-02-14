package com.easybili.web.controller;

import com.easybili.entities.dto.TokenUserInfoDto;
import com.easybili.entities.po.StatisticsInfo;
import com.easybili.entities.po.VideoInfo;
import com.easybili.entities.query.StatisticsInfoQuery;
import com.easybili.entities.query.VideoInfoQuery;
import com.easybili.entities.vo.ResponseVO;
import com.easybili.service.StatisticsInfoServiceImpl;
import com.easybili.utils.DateUtils;
import com.easybili.web.annotation.GlobalInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ucenter")
@Validated
@Slf4j
public class UcenterStatisticsController extends BaseController<Object>{
    @Resource
    private StatisticsInfoServiceImpl statisticsInfoService;

    @RequestMapping("/getActualTimeStatisticsInfo")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Object> getActualTimeStatisticsInfo() {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        String preDate = DateUtils.getBeforeDayDate(1);
        StatisticsInfoQuery query = new StatisticsInfoQuery();
        query.setStatisticsDate(preDate);
        query.setUserId(tokenUserInfoDto.getUserId());

        List<StatisticsInfo> preDayData = statisticsInfoService.findByQuery(query);
        Map<Integer, Integer> preDayDataMap = preDayData.stream().collect(Collectors.toMap(StatisticsInfo::getDataType, StatisticsInfo::getStatisticsCount,
                (data1, data2) -> data2));
        Map<String, Integer> totalCountInfo = statisticsInfoService.getStatisticInfoActualTime(tokenUserInfoDto.getUserId());

        Map<String, Object> result = new HashMap<>();
        result.put("preDayData", preDayDataMap);
        result.put("totalCountInfo", totalCountInfo);
        return ResponseVO.getSuccessResponseVO(result);}

    @RequestMapping("/getWeekStatisticsInfo")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Object> getActualTimeStatisticsInfo(Integer dataType) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        List<String> dateList = DateUtils.getBeforeDays(7);

        StatisticsInfoQuery query = new StatisticsInfoQuery();
        query.setDataType(dataType);
        query.setUserId(tokenUserInfoDto.getUserId());
        query.setStatisticsDateStart(dateList.get(0));
        query.setStatisticsDateEnd(dateList.get(dateList.size() - 1));
        query.setOrderBy("statistics_date asc");
        List<StatisticsInfo> statisticsInfoList = statisticsInfoService.findByQuery(query);
        Map<String, StatisticsInfo> dataMap = statisticsInfoList.stream().collect(Collectors.toMap(item ->item.getStatisticsDate(),
                Function.identity(), (data1, data2) -> data2));
        List<StatisticsInfo> resultDataList = new ArrayList<>();
        for(String date :dateList){
            StatisticsInfo dataItem = dataMap.get(date);
            if(dataItem == null){
                dataItem = new StatisticsInfo();
                dataItem.setStatisticsCount(0);
                dataItem.setStatisticsDate(date);
            }
            resultDataList.add(dataItem);
        }
        return ResponseVO.getSuccessResponseVO(resultDataList);
    }


}
