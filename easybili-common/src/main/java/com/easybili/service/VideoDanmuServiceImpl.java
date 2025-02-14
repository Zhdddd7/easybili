package com.easybili.service;
import com.easybili.entities.component.EsSearchComponent;
import com.easybili.entities.enums.ResponseCodeEnum;
import com.easybili.entities.enums.SearchOrderTypeEnum;
import com.easybili.entities.enums.UserActionTypeEnum;
import com.easybili.entities.exception.BusinessException;
import com.easybili.entities.po.VideoDanmu;
import com.easybili.entities.po.VideoInfo;
import com.easybili.entities.query.VideoDanmuQuery;
import com.easybili.entities.vo.PaginationResultVO;
import com.easybili.entities.vo.SimplePage;
import com.easybili.mappers.VideoDanmuMapper;
import com.easybili.mappers.VideoInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class VideoDanmuServiceImpl {

    @Resource
    private VideoDanmuMapper videoDanmuMapper;
    @Resource
    private VideoInfoMapper videoInfoMapper;
    @Resource
    private EsSearchComponent esSearchComponent;

    public void addDanmu(VideoDanmu videoDanmu) {
        videoDanmuMapper.insert(videoDanmu);
    }

    public void updateDanmu(VideoDanmu videoDanmu) {
        videoDanmuMapper.update(videoDanmu);
    }

    public void deleteDanmuById(Integer danmuId) {
        videoDanmuMapper.deleteById(danmuId);
    }

    public VideoDanmu getDanmuById(Integer danmuId) {
        return videoDanmuMapper.selectById(danmuId);
    }

    public List<VideoDanmu> findListByParam(VideoDanmuQuery query) {
        return videoDanmuMapper.selectByQuery(query);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveVideoDanmu(VideoDanmu videoDanmu) {
        VideoInfo videoInfo = videoInfoMapper.selectById(videoDanmu.getVideoId());
        if(videoInfo == null){
            throw new BusinessException(ResponseCodeEnum.BUSINESS_CUSTOM_ERROR.getMessage());
        }
        // check if this video allows interaction
        if(videoInfo.getInteraction() != null && videoInfo.getInteraction().contains("1")){
            throw new BusinessException("The author shut the interaction");
        }
        this.videoDanmuMapper.insert(videoDanmu);
        this.videoInfoMapper.updateCountInfo(videoDanmu.getVideoId(), UserActionTypeEnum.VIDEO_DANMU.getField(), 1);
        // TODO  update ES data
        esSearchComponent.updateDocCount(videoDanmu.getVideoId(), SearchOrderTypeEnum.VIDEO_DANMU.getField(), 1);


    }

    public PaginationResultVO<VideoDanmu> findListByPage(VideoDanmuQuery query) {
        if (query == null) {
            throw new IllegalArgumentException("Query object cannot be null");
        }
        if(query.getPageNo() == null){
            query.setPageNo(1);
        }
        int pageSize = query.getPageSize() == null?10: query.getPageSize();
        int start = (query.getPageNo() - 1) * pageSize; // 10 pcs every page
        query.setSimplePage(new SimplePage(start, pageSize));
        List<VideoDanmu> dataList = videoDanmuMapper.selectByQuery(query);
        Integer pageTotal = (int) Math.ceil((double) dataList.size() / pageSize);
        return new PaginationResultVO<>(dataList.size(), pageSize, query.getPageNo() , pageTotal, dataList);
    }

    public void deleteDanmu(String userId, Integer danmuId){
        VideoDanmu videoDanmu = videoDanmuMapper.selectById(danmuId);
        if(videoDanmu == null){
            throw new BusinessException(ResponseCodeEnum.BUSINESS_CUSTOM_ERROR.getMessage());
        }
        VideoInfo videoInfo = videoInfoMapper.selectById(videoDanmu.getVideoId());
        if(videoInfo == null){
            throw new BusinessException(ResponseCodeEnum.BUSINESS_CUSTOM_ERROR.getMessage());
        }
        if(userId != null && !videoInfo.getUserId().equals(userId)){
            throw new BusinessException(ResponseCodeEnum.BUSINESS_CUSTOM_ERROR.getMessage());
        }
        videoDanmuMapper.deleteById(danmuId);

    }
}
