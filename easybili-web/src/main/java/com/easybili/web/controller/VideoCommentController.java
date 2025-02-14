package com.easybili.web.controller;

import com.easybili.annotation.RecordUserMessage;
import com.easybili.entities.dto.TokenUserInfoDto;
import com.easybili.entities.enums.MessageTypeEnum;
import com.easybili.entities.enums.UserActionTypeEnum;
import com.easybili.entities.po.UserAction;
import com.easybili.entities.po.VideoComment;
import com.easybili.entities.po.VideoInfo;
import com.easybili.entities.query.UserActionQuery;
import com.easybili.entities.query.VideoCommentQuery;
import com.easybili.entities.vo.PaginationResultVO;
import com.easybili.entities.vo.ResponseVO;
import com.easybili.entities.vo.VideoCommentResultVO;
import com.easybili.mappers.VideoInfoMapper;
import com.easybili.service.UserActionServiceImpl;
import com.easybili.service.VideoCommentServiceImpl;
import com.easybili.service.VideoInfoServiceImpl;
import com.easybili.web.annotation.GlobalInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comment")
@Validated
@Slf4j
public class VideoCommentController extends BaseController<Object>{
    @Resource
    private VideoCommentServiceImpl videoCommentService;
    @Resource
    private UserActionServiceImpl userActionService;
    @Resource
    private VideoInfoServiceImpl videoInfoService;
    @Resource
    private VideoInfoMapper videoInfoMapper;

    @RequestMapping("/postComment")
    @GlobalInterceptor(checkLogin = true)
    @RecordUserMessage(messageType = MessageTypeEnum.COMMENT)
    public ResponseVO<Object> postComment(@NotEmpty String videoId,
                                                @NotEmpty @Size(max = 500) String content,
                                                Integer replyCommentId,
                                                @Size(max = 50) String imgPath){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        VideoComment comment = new VideoComment();
        comment.setUserId(tokenUserInfoDto.getUserId());
        comment.setAvatar(tokenUserInfoDto.getAvatar());
        comment.setUserName(tokenUserInfoDto.getUserName());
        comment.setVideoId(videoId);
        comment.setContent(content);
        comment.setImgPath(imgPath);

        videoCommentService.postComment(comment, replyCommentId);
        comment.setReplyAvatar(tokenUserInfoDto.getAvatar());
        return ResponseVO.getSuccessResponseVO(comment);
    }

    @RequestMapping("/loadComment")
    public ResponseVO<Object> loadComment(@NotEmpty String videoId,
                                          Integer pageNo,
                                          Integer orderType){
        VideoInfo videoInfo = videoInfoMapper.selectById(videoId);
        if(videoInfo.getInteraction() != null && videoInfo.getInteraction().contains("0")){
            return ResponseVO.getSuccessResponseVO(new ArrayList<>());
        }
        VideoCommentQuery commentQuery = new VideoCommentQuery();

        commentQuery.setVideoId(videoId);
//        pageNo = pageNo == null||pageNo <1?1:pageNo;
        commentQuery.setPageNo(pageNo);
        commentQuery.setPageSize(15);
        commentQuery.setPCommentId(0);
        String orderBy =  orderType == null || orderType ==0?"like_count desc, comment_id desc": "comment_id desc";
        commentQuery.setOrderBy(orderBy);
        commentQuery.setLoadChildren(true);
        PaginationResultVO<VideoComment> commentData = videoCommentService.findListByPage(commentQuery);
        if(pageNo == null){
            List<VideoComment> topCommentList= topComment(videoId);
            if(!topCommentList.isEmpty()){
                List commentList = commentData.getList().stream().filter(item -> !item.getCommentId().equals(topCommentList.get(0).getCommentId())).collect(Collectors.toList());
                commentList.addAll(0, topCommentList);
                commentData.setList(commentList);
            }
        }

        List<UserAction> userActionList = new ArrayList<>();
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        if(tokenUserInfoDto != null){
            UserActionQuery userActionQuery = new UserActionQuery();
            userActionQuery.setVideoId(videoId);
            userActionQuery.setUserId(tokenUserInfoDto.getUserId());
            userActionQuery.setActionTypeArray(new Integer[]{UserActionTypeEnum.COMMENT_LIKE.getType(), UserActionTypeEnum.COMMENT_HATE.getType()});
            userActionList = userActionService.findListByParam(userActionQuery);
        }
        VideoCommentResultVO resultVO = new VideoCommentResultVO();
        resultVO.setCommentData(commentData);
        resultVO.setUserActionList(userActionList);
        return ResponseVO.getSuccessResponseVO(resultVO);
    }

    private List topComment(String videoId){
        VideoCommentQuery commentQuery = new VideoCommentQuery();
        commentQuery.setVideoId(videoId);
        // 1: Top the comment
        commentQuery.setTopType(1);
        commentQuery.setLoadChildren(true);
        return  videoCommentService.findCommentByQuery(commentQuery);
    }

    @RequestMapping("/topComment")
    @GlobalInterceptor(checkLogin = true)
    public void topComment(@NotNull Integer commentId){
        TokenUserInfoDto userInfoDto = getTokenUserInfoDto();
        videoCommentService.topComment(commentId, userInfoDto.getUserId());
    }

    @RequestMapping("/cancelTopComment")
    @GlobalInterceptor(checkLogin = true)
    public void cancelTopComment(@NotNull Integer commentId){
        TokenUserInfoDto userInfoDto = getTokenUserInfoDto();
        videoCommentService.cancelTopComment(commentId, userInfoDto.getUserId());
    }

    @RequestMapping("/userDelComment")
    @GlobalInterceptor(checkLogin = true)
    public void userDelComment(@NotNull Integer commentId){
        TokenUserInfoDto userInfoDto = getTokenUserInfoDto();
        videoCommentService.deleteComment(commentId, userInfoDto.getUserId());
    }

}
