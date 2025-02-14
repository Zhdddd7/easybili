package com.easybili.service;

import com.easybili.constants.Constants;
import com.easybili.entities.enums.CommentTopTypeEnum;
import com.easybili.entities.enums.ResponseCodeEnum;
import com.easybili.entities.enums.UserActionTypeEnum;
import com.easybili.entities.exception.BusinessException;
import com.easybili.entities.po.UserInfo;
import com.easybili.entities.po.VideoComment;
import com.easybili.entities.po.VideoInfo;
import com.easybili.entities.po.VideoInfoPost;
import com.easybili.entities.query.VideoCommentQuery;
import com.easybili.entities.vo.PaginationResultVO;
import com.easybili.entities.vo.SimplePage;
import com.easybili.entities.vo.VideoCommentResultVO;
import com.easybili.mappers.UserInfoMapper;
import com.easybili.mappers.VideoCommentMapper;
import com.easybili.mappers.VideoInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;

@Service
public class VideoCommentServiceImpl {
    @Resource
    private VideoCommentMapper videoCommentMapper;
    @Resource
    private VideoInfoMapper videoInfoMapper;
    @Resource
    private UserInfoMapper userInfoMapper;

    public void addComment(VideoComment videoComment) {
        videoCommentMapper.insert(videoComment);
    }

    public void updateComment(VideoComment videoComment) {
        videoCommentMapper.update(videoComment);
    }

    public void deleteCommentById(Integer commentId) {
        videoCommentMapper.deleteById(commentId);
    }

    public VideoComment getCommentById(Integer commentId) {
        return videoCommentMapper.selectById(commentId);
    }

    public List<VideoComment> findCommentByQuery(VideoCommentQuery query) {
        List<VideoComment> dataList = new ArrayList<>();
        if(query.getLoadChildren() != null && query.getLoadChildren()){
            dataList = videoCommentMapper.selectByQueryWithChildren(query);
        }else {
            dataList = videoCommentMapper.selectByQuery(query);
        }
        return dataList;
    }

    public void postComment(VideoComment comment, Integer replyCommentId) {
        VideoInfo videoInfo = videoInfoMapper.selectById(comment.getVideoId());
        if(videoInfo == null){
            throw new BusinessException(ResponseCodeEnum.BUSINESS_CUSTOM_ERROR.getMessage());
        }
        if(videoInfo.getInteraction() != null && videoInfo.getInteraction().contains("0")){
            throw new BusinessException("This up has closed the comment");
        }
        if(replyCommentId != null){
            VideoComment replyComment = getCommentById(replyCommentId);
            if(replyComment == null || !replyComment.getVideoId().equals(comment.getVideoId())){
                throw new BusinessException(ResponseCodeEnum.BUSINESS_CUSTOM_ERROR.getMessage());
            }
            if(replyComment.getPCommentId() == 0){
                comment.setPCommentId(replyComment.getCommentId());
            }
            else{
                comment.setPCommentId(replyComment.getPCommentId());
                comment.setReplyUserId(replyComment.getUserId());
            }
            UserInfo userInfo = userInfoMapper.selectById(replyComment.getUserId());
            comment.setUserName(userInfo.getUserName());
            comment.setReplyAvatar(userInfo.getAvatar());
        }
        else{
            comment.setPCommentId(0);
        }
        comment.setPostTime(LocalDateTime.now());
        comment.setVideoUserId(videoInfo.getUserId());
        this.videoCommentMapper.insert(comment);
        if(comment.getPCommentId() == 0){
            this.videoInfoMapper.updateCountInfo(comment.getVideoId(), UserActionTypeEnum.VIDEO_COMMENT.getField(), 1);
        }
    }

    public PaginationResultVO<VideoComment> findListByPage(VideoCommentQuery query) {
        if (query == null) {
            throw new IllegalArgumentException("Query object cannot be null");
        }
        if(query.getPageNo() == null){
            query.setPageNo(1);
        }
        Integer pageSize = query.getPageSize() == null?10: query.getPageSize();
        Integer start = (query.getPageNo() - 1) * pageSize; // 10 pcs every page
        query.setSimplePage(new SimplePage(start, pageSize));
        List dataList = new ArrayList<>();
        if(query.getLoadChildren() != null && query.getLoadChildren()){
            dataList = videoCommentMapper.selectByQueryWithChildren(query);
        }else {
            dataList = videoCommentMapper.selectByQuery(query);
        }
        Integer pageTotal = (int) Math.ceil((double) dataList.size() / pageSize);
        return new PaginationResultVO<>(dataList.size(), pageSize, query.getPageNo() , pageTotal, dataList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void topComment(Integer commentId, String userId) {
        this.cancelTopComment(commentId, userId);
        VideoComment videoComment = new VideoComment();
        videoComment.setTopType(CommentTopTypeEnum.TOP.getType());
        videoCommentMapper.updateByCommentId(videoComment, commentId);
    }

    public void cancelTopComment(Integer commentId, String userId) {
        VideoComment dbVideoComment = videoCommentMapper.selectById(commentId);
        if(dbVideoComment == null){
            throw new BusinessException(ResponseCodeEnum.BUSINESS_CUSTOM_ERROR.getMessage());
        }
        VideoInfo videoInfo = videoInfoMapper.selectById(dbVideoComment.getVideoId());
        if(videoInfo == null){
            throw new BusinessException(ResponseCodeEnum.BUSINESS_CUSTOM_ERROR.getMessage());
        }
        if(!videoInfo.getUserId().equals(userId)){
            throw new BusinessException(ResponseCodeEnum.BUSINESS_CUSTOM_ERROR.getMessage());
        }
        VideoComment videoComment = new VideoComment();
        videoComment.setTopType(CommentTopTypeEnum.NO_TOP.getType());

        VideoCommentQuery videoCommentQuery = new VideoCommentQuery();
        videoCommentQuery.setVideoId(dbVideoComment.getVideoId());
        videoCommentQuery.setTopType(CommentTopTypeEnum.TOP.getType());
        videoCommentMapper.updateByQuery(videoComment, videoCommentQuery);
    }

    public void deleteComment(Integer commentId, String userId) {
        VideoComment videoComment = videoCommentMapper.selectById(commentId);
        if(videoComment == null){
            throw new BusinessException(ResponseCodeEnum.BUSINESS_CUSTOM_ERROR.getMessage());
        }
        VideoInfo videoInfo = videoInfoMapper.selectById(videoComment.getVideoId());
        if(videoInfo == null){
            throw new BusinessException(ResponseCodeEnum.BUSINESS_CUSTOM_ERROR.getMessage());
        }
        // video creator and comment person can delete this comment
        if(userId != null && !videoInfo.getUserId().equals(userId) && !videoComment.getUserId().equals(userId)){
            throw new BusinessException(ResponseCodeEnum.BUSINESS_CUSTOM_ERROR.getMessage());
        }
        videoCommentMapper.deleteById(commentId);
        // update count
        if(videoComment.getPCommentId() == 0){
            videoInfoMapper.updateCountInfo(videoInfo.getVideoId(), UserActionTypeEnum.VIDEO_COMMENT.getField(), -1);
        // delete secondary comment
            VideoCommentQuery videoCommentQuery = new VideoCommentQuery();
            videoCommentQuery.setPCommentId(commentId);
            videoCommentMapper.deleteByQuery(videoCommentQuery);
        }
    }
}
