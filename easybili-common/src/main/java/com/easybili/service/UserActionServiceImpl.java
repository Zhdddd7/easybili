package com.easybili.service;

import com.easybili.entities.component.EsSearchComponent;
import com.easybili.entities.enums.ResponseCodeEnum;
import com.easybili.entities.enums.SearchOrderTypeEnum;
import com.easybili.entities.enums.UserActionTypeEnum;
import com.easybili.entities.exception.BusinessException;
import com.easybili.entities.po.VideoComment;
import com.easybili.entities.po.VideoInfo;
import com.easybili.entities.query.UserActionQuery;
import com.easybili.entities.query.VideoCommentQuery;
import com.easybili.entities.query.VideoInfoQuery;
import com.easybili.entities.vo.PaginationResultVO;
import com.easybili.entities.vo.SimplePage;
import com.easybili.mappers.UserActionMapper;
import com.easybili.mappers.UserInfoMapper;
import com.easybili.mappers.VideoCommentMapper;
import com.easybili.mappers.VideoInfoMapper;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import com.easybili.entities.po.UserAction;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserActionServiceImpl {
    @Resource
    private UserActionMapper userActionMapper;
    @Resource
    private VideoInfoMapper videoInfoMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private VideoCommentMapper<VideoComment, VideoCommentQuery> videoCommentMapper;
    @Resource
    private EsSearchComponent esSearchComponent;

    public void addAction(UserAction userAction) {
        userActionMapper.insert(userAction);
    }

    public void updateAction(UserAction userAction) {
        userActionMapper.update(userAction);
    }

    public void deleteActionById(Integer actionId) {
        userActionMapper.deleteById(actionId);
    }

    public UserAction getActionById(Integer actionId) {
        return userActionMapper.selectById(actionId);
    }

    public List<UserAction> findListByParam(UserActionQuery query) {
        return userActionMapper.selectByQuery(query);
    }

    public PaginationResultVO<UserAction> findListByPage(UserActionQuery query) {
        if (query == null) {
            throw new IllegalArgumentException("Query object cannot be null");
        }
        if(query.getPageNo() == null){
            query.setPageNo(1);
        }
        if(query.getPageSize() ==null){
            query.setPageSize(10);
        }
        int start = (query.getPageNo() - 1) * query.getPageSize(); // 10 pcs every page
        query.setSimplePage(new SimplePage(start, query.getPageSize()));
        List<UserAction> dataList = userActionMapper.selectByQuery(query);
        return new PaginationResultVO<>(dataList.size(), query.getPageSize(), query.getPageNo(), dataList);

    }

    @Transactional(rollbackFor = Exception.class)
    public void saveAction(UserAction bean) {
        VideoInfo videoInfo = videoInfoMapper.selectById(bean.getVideoId());
        if(videoInfo == null){
            throw new BusinessException(ResponseCodeEnum.BUSINESS_CUSTOM_ERROR.getMessage());
        }
        bean.setVideoUserId(videoInfo.getUserId());
        UserActionTypeEnum userActionTypeEnum = UserActionTypeEnum.getByType(bean.getActionType());
        if(userActionTypeEnum == null){
            throw new BusinessException(ResponseCodeEnum.BUSINESS_CUSTOM_ERROR.getMessage());
        }
        // to make sure the user can only like, coin and collect once
        UserAction dbAction = userActionMapper.selectByVideoIdAndCommentIdAndActionTypeAndUserId(bean.getVideoId(), bean.getCommentId(), bean.getActionType(), bean.getUserId());
        bean.setActionTime(LocalDateTime.now());
        switch (userActionTypeEnum){
            case VIDEO_LIKE:
            case VIDEO_COLLECT:
                if(dbAction != null){
                    userActionMapper.deleteById(dbAction.getActionId());
                }else{
                    userActionMapper.insert(bean);
                }
                Integer changeCount  = dbAction==null?1:-1;
                videoInfoMapper.updateCountInfo(bean.getVideoId(), userActionTypeEnum.getField(), changeCount);

                if(userActionTypeEnum == UserActionTypeEnum.VIDEO_COLLECT){
                    esSearchComponent.updateDocCount(videoInfo.getVideoId(), SearchOrderTypeEnum.VIDEO_COLLECT.getField(), changeCount);
                }
                break;
            case VIDEO_COIN:
                if(videoInfo.getUserId().equals(bean.getUserId())){
                    throw new BusinessException("You can not coin for yourself");
                }
                if(dbAction != null){
                    throw new BusinessException("You have already coined for this video");
                }
                // an optimistic lock
                Integer updateCount = userInfoMapper.updateCoinCountInfo(bean.getUserId(), -bean.getActionCount());
                if(updateCount == 0){
                    throw new BusinessException("You do not have enough coins");
                }
                updateCount = userInfoMapper.updateCoinCountInfo(videoInfo.getUserId(), bean.getActionCount());
                userActionMapper.insert(bean);
                videoInfoMapper.updateCountInfo(bean.getVideoId(), userActionTypeEnum.getField(), bean.getActionCount());
                break;
            case COMMENT_LIKE:
            case COMMENT_HATE:
                UserActionTypeEnum opposeTypeEnum = UserActionTypeEnum.COMMENT_LIKE == userActionTypeEnum?UserActionTypeEnum.COMMENT_HATE: UserActionTypeEnum.COMMENT_LIKE;
                UserAction opposeAction = userActionMapper.selectByVideoIdAndCommentIdAndActionTypeAndUserId(bean.getVideoId(), bean.getCommentId(),
                        opposeTypeEnum.getType(), bean.getUserId());
                if(opposeAction != null){
                    userActionMapper.deleteById(opposeAction.getActionId());
                }
                if(dbAction != null){
                    userActionMapper.deleteById(dbAction.getActionId());
                }else{
                    userActionMapper.insert(bean);
                }
                changeCount = dbAction == null?1:-1;
                Integer opposeChangeCount = -changeCount;
                videoCommentMapper.updateCountInfo(bean.getCommentId(), userActionTypeEnum.getField(), changeCount,
                        opposeAction== null?null:opposeTypeEnum.getField(), opposeChangeCount);
        }

    }
}
