package com.easybili.admin.controller;

import com.easybili.entities.dto.TokenUserInfoDto;
import com.easybili.entities.enums.StatisticsTypeEnum;
import com.easybili.entities.po.StatisticsInfo;
import com.easybili.entities.query.StatisticsInfoQuery;
import com.easybili.entities.query.UserInfoQuery;
import com.easybili.entities.vo.ResponseVO;
import com.easybili.service.StatisticsInfoServiceImpl;
import com.easybili.service.UserInfoServiceImpl;
import com.easybili.utils.DateUtils;

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
@RequestMapping("/index")
@Validated
@Slf4j
public class IndexController extends BaseController<Object>{
    @Resource
    private StatisticsInfoServiceImpl statisticsInfoService;
    @Resource
    private UserInfoServiceImpl userInfoService;

    @RequestMapping("/getActualTimeStatisticsInfo")
    public ResponseVO<Object> getActualTimeStatisticsInfo() {
        String preDate = DateUtils.getBeforeDayDate(1);
        StatisticsInfoQuery query = new StatisticsInfoQuery();
        query.setStatisticsDate(preDate);
        List<StatisticsInfo> preDayData = statisticsInfoService.findListTotalInfoByQuery(query);

        Integer userCount = userInfoService.findCountByQuery(new UserInfoQuery());
        preDayData.forEach(item ->
        {
            if(StatisticsTypeEnum.FANS.getType().equals(item.getDataType())){
                item.setStatisticsCount(userCount);
            }
        });
        Map<Integer, Integer> preDayDataMap = preDayData.stream().collect(Collectors.toMap(StatisticsInfo::getDataType, StatisticsInfo::getStatisticsCount,
                (data1, data2) -> data2));
        Map<String, Integer> totalCountInfo = statisticsInfoService.getStatisticInfoActualTime(null);

        Map<String, Object> result = new HashMap<>();
        result.put("preDayData", preDayDataMap);
        result.put("totalCountInfo", totalCountInfo);
        return ResponseVO.getSuccessResponseVO(result);}

    @RequestMapping("/getWeekStatisticsInfo")
    public ResponseVO<Object> getActualTimeStatisticsInfo(Integer dataType) {
        List<String> dateList = DateUtils.getBeforeDays(7);

        StatisticsInfoQuery query = new StatisticsInfoQuery();
        query.setDataType(dataType);
        query.setStatisticsDateStart(dateList.get(0));
        query.setStatisticsDateEnd(dateList.get(dateList.size() - 1));
        query.setOrderBy("s.statistics_date asc");

        List<StatisticsInfo> statisticsInfoList = null;
        if(!StatisticsTypeEnum.FANS.getType().equals(dataType)){
            statisticsInfoList = statisticsInfoService.findListTotalInfoByQuery(query);
        }
        else{
            statisticsInfoList = statisticsInfoService.findUserCountTotalInfoByQuery(query);
        }

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
