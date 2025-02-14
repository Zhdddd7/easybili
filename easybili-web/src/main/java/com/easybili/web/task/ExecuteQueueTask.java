package com.easybili.web.task;

import com.easybili.entities.component.EsSearchComponent;
import com.easybili.entities.component.RedisComponent;
import com.easybili.entities.dto.VideoPlayInfoDto;
import com.easybili.entities.enums.SearchOrderTypeEnum;
import com.easybili.entities.po.VideoInfoFilePost;
import com.easybili.redis.RedisUtils;
import com.easybili.service.VideoInfoPostServiceImpl;
import com.easybili.service.VideoInfoServiceImpl;
import com.easybili.service.VideoPlayHistoryServiceImpl;
import com.easybili.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ExecuteQueueTask {
    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private VideoInfoPostServiceImpl videoInfoPostService;
    @Resource
    private VideoInfoServiceImpl videoInfoService;
    @Resource
    private EsSearchComponent esSearchComponent;
    @Resource
    private VideoPlayHistoryServiceImpl videoPlayHistoryService;

    @PostConstruct
    public void consumeTransferFileQueue() {
        executorService.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) { // 检查线程是否被中断
                VideoInfoFilePost videoInfoFilePost = null;
                try {
                    videoInfoFilePost = redisComponent.getFileFromTransferQueue();
                    if (videoInfoFilePost == null) {
                        Thread.sleep(1500);
                        continue;
                    }
                    videoInfoPostService.transferVideoFile(videoInfoFilePost);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // 恢复中断状态
                    log.info("Transfer file queue task interrupted.");
                } catch (Exception e) {
                    log.error("The transfer failed", e);
                }
            }
        });
    }

    @PostConstruct
    public void consumeVideoPlayQueue() {
        executorService.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) { // 检查线程是否被中断
                try {
                    VideoPlayInfoDto videoPlayInfoDto = redisComponent.getVideoPlayFromQueue();
                    if (videoPlayInfoDto == null) {
                        Thread.sleep(1500);
                        continue;
                    }
                    videoInfoService.addReadCount(videoPlayInfoDto.getVideoId());
                    if (!StringTools.isEmpty(videoPlayInfoDto.getUserId())) {
                        videoPlayHistoryService.saveHistory(videoPlayInfoDto.getUserId(), videoPlayInfoDto.getVideoId(), videoPlayInfoDto.getFileIndex());
                    }
                    redisComponent.recordVideoPlayCount(videoPlayInfoDto.getVideoId());
                    esSearchComponent.updateDocCount(videoPlayInfoDto.getVideoId(), SearchOrderTypeEnum.VIDEO_PLAY.getField(), 1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // 恢复中断状态
                    log.info("Video play queue task interrupted.");
                } catch (Exception e) {
                    log.error("Retrieve video play file Q message failed", e);
                }
            }
        });
    }

    @PreDestroy
    public void shutdownExecutor() {
        log.info("Shutting down executor service...");
        executorService.shutdown(); // 优雅关闭线程池
        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) { // 等待任务完成
                executorService.shutdownNow(); // 强制关闭
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }



}
