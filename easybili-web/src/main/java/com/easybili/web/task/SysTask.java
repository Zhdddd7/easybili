package com.easybili.web.task;

import com.easybili.service.StatisticsInfoServiceImpl;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SysTask {
    @Resource
    private StatisticsInfoServiceImpl statisticsInfoService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void statisticsData(){
        statisticsInfoService.statisticsData();
    }
}
