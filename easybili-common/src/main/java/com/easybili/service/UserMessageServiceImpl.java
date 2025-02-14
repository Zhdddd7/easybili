package com.easybili.service;

import com.easybili.entities.dto.UserMessageCountDto;
import com.easybili.entities.dto.UserMessageExtendDto;
import com.easybili.entities.enums.MessageReadTypeEnum;
import com.easybili.entities.enums.MessageTypeEnum;
import com.easybili.entities.po.*;
import com.easybili.entities.query.UserMessageQuery;
import com.easybili.entities.query.VideoCommentQuery;
import com.easybili.entities.vo.PaginationResultVO;
import com.easybili.entities.vo.SimplePage;
import com.easybili.mappers.UserMessageMapper;
import com.easybili.mappers.VideoCommentMapper;
import com.easybili.mappers.VideoInfoMapper;
import com.easybili.mappers.VideoInfoPostMapper;
import com.easybili.utils.DateUtils;
import com.easybili.utils.JsonUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserMessageServiceImpl {
    @Resource
    private UserMessageMapper userMessageMapper;
    @Resource
    private VideoInfoMapper videoInfoMapper;
    @Resource
    private VideoCommentMapper<VideoComment, VideoCommentQuery> videoCommentMapper;
    @Resource
    private VideoInfoPostMapper videoInfoPostMapper;

    public UserMessage findByPrimaryKey(Integer messageId) {
        return userMessageMapper.selectByPrimaryKey(messageId);
    }

    public int save(UserMessage userMessage) {
        return userMessageMapper.insert(userMessage);
    }

    public int update(UserMessage userMessage) {
        return userMessageMapper.updateByPrimaryKey(userMessage);
    }

    public int deleteByPrimaryKey(Integer messageId) {
        return userMessageMapper.deleteByPrimaryKey(messageId);
    }

    public List<UserMessage> findByUserId(String userId) {
        return userMessageMapper.selectByUserId(userId);
    }

    public int deleteByUserId(String userId) {
        return userMessageMapper.deleteByUserId(userId);
    }

    public List<UserMessage> findByQuery(UserMessageQuery query) {
        return userMessageMapper.selectByQuery(query);
    }

    public int deleteByQuery(UserMessageQuery query) {
        return userMessageMapper.deleteByQuery(query);
    }

    public int updateByQuery(UserMessageQuery query, UserMessage record) {
        return userMessageMapper.updateByQuery(query, record);
    }

    @Async
    public void saveUserMessage(String videoId, String sendUserId, MessageTypeEnum messageTypeEnum, String content, Integer replyCommentId){
        VideoInfo videoInfo = videoInfoMapper.selectById(videoId);
        if(videoInfo == null){
            return;
        }
        UserMessageExtendDto extendDto = new UserMessageExtendDto();
        String userId = videoInfo.getUserId();
        extendDto.setMessageContent(content);
        // collect, like: if already send a message, do not repeatedly send message
        if(ArrayUtils.contains(new Integer[]{MessageTypeEnum.LIKE.getType(), MessageTypeEnum.COLLECTION.getType()}, messageTypeEnum.getType())){
            UserMessageQuery userMessageQuery = new UserMessageQuery();
            userMessageQuery.setUserId(userId);
            userMessageQuery.setVideoId(videoId);
            userMessageQuery.setMessageType(messageTypeEnum.getType());
            Integer count = userMessageMapper.selectCount(userMessageQuery);
            // already sent
            if(count > 0){
                return ;
            }
        }
        UserMessage userMessage = new UserMessage();
        userMessage.setUserId(userId);
        userMessage.setVideoId(videoId);
        userMessage.setReadType(MessageReadTypeEnum.NO_READ.getType());
        userMessage.setCreateTime(LocalDateTime.now());
        userMessage.setMessageType(messageTypeEnum.getType());
        userMessage.setSendUserId(sendUserId);
        // considering comments
        if(replyCommentId != null){
            VideoComment comment = videoCommentMapper.selectById(replyCommentId);
            if(comment != null){
                //
                userId = comment.getUserId();
                extendDto.setMessageContentReply(comment.getContent());
            }
        }
        if(userId.equals(sendUserId)){
            return;
        }
        // system info
        if(MessageTypeEnum.SYS == messageTypeEnum){
            VideoInfoPost videoInfoPost = videoInfoPostMapper.selectById(videoId);
            extendDto.setAuditStatus(videoInfoPost.getStatus());
        }

        userMessage.setUserId(userId);
        userMessage.setExtendJson(JsonUtils.convertObj2Json(extendDto));
        this.userMessageMapper.insert(userMessage);

    }

    public Integer findCountByParam(UserMessageQuery userMessageQuery) {
        return userMessageMapper.selectCount(userMessageQuery);
    }

    public List<UserMessageCountDto> getMessageTypeNoReadCount(String userId) {
        return this.userMessageMapper.getMessageTypeNoReadCount(userId);
    }

    public PaginationResultVO<UserMessage> findByPage(@Param("query") UserMessageQuery query) {
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
        List<UserMessage> dataList = userMessageMapper.selectByQuery(query);
        return new PaginationResultVO<>(dataList.size(), query.getPageSize(), query.getPageNo(), dataList);
    }
}
