package com.easybili.web.controller;

import com.easybili.annotation.RecordUserMessage;
import com.easybili.entities.enums.MessageTypeEnum;
import com.easybili.entities.po.UserAction;
import com.easybili.entities.vo.ResponseVO;
import com.easybili.service.UserActionServiceImpl;
import com.easybili.web.annotation.GlobalInterceptor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.*;

@RestController
@RequestMapping("/api/userAction")
@Validated
public class UserActionController extends BaseController<Object>{
    @Resource
    private UserActionServiceImpl userActionService;

    @RequestMapping("/doAction")
    @GlobalInterceptor(checkLogin = true)
    @RecordUserMessage(messageType = MessageTypeEnum.LIKE)
    public ResponseVO<Object> doAction(@NotEmpty String videoId, @NotNull Integer actionType,
                               @Max(2) @Min(1)Integer actionCount,
                               Integer commentId){
        UserAction userAction = new UserAction();
        userAction.setUserId(getTokenUserInfoDto().getUserId());
        userAction.setActionType(actionType);
        userAction.setVideoId(videoId);
        commentId = commentId == null?0:commentId;
        userAction.setCommentId(commentId);
        actionCount = actionCount == null?1:actionCount;
        userAction.setActionCount(actionCount);
        userActionService.saveAction(userAction);
        return ResponseVO.getSuccessResponseVO();
    }
}
