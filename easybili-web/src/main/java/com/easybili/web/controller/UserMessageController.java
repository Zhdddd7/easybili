package com.easybili.web.controller;


import com.easybili.entities.dto.TokenUserInfoDto;
import com.easybili.entities.dto.UserMessageCountDto;
import com.easybili.entities.enums.MessageReadTypeEnum;
import com.easybili.entities.po.UserMessage;
import com.easybili.entities.query.UserMessageQuery;
import com.easybili.entities.vo.PaginationResultVO;
import com.easybili.entities.vo.ResponseVO;
import com.easybili.service.UserMessageServiceImpl;
import com.easybili.web.annotation.GlobalInterceptor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/message")
public class UserMessageController extends BaseController<Objects> {

    @Resource
    private UserMessageServiceImpl userMessageService;

    @RequestMapping("/getNoReadCount")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Integer> getNoReadCount(){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        UserMessageQuery userMessageQuery = new UserMessageQuery();
        userMessageQuery.setUserId(tokenUserInfoDto.getUserId());
        userMessageQuery.setReadType(MessageReadTypeEnum.NO_READ.getType());
        Integer count = userMessageService.findCountByParam(userMessageQuery);
        return ResponseVO.getSuccessResponseVO(count);
    }

    @RequestMapping("/getNoReadCountGroup")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<List<UserMessageCountDto>> getNoReadCountGroup(){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        List<UserMessageCountDto> dataList = userMessageService.getMessageTypeNoReadCount(tokenUserInfoDto.getUserId());
        return ResponseVO.getSuccessResponseVO(dataList);
    }

    @RequestMapping("/readAll")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Objects> readAll(@NotNull Integer messageType){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        UserMessageQuery userMessageQuery = new UserMessageQuery();
        userMessageQuery.setUserId(tokenUserInfoDto.getUserId());
        userMessageQuery.setMessageType(messageType);

        UserMessage userMessage = new UserMessage();
        userMessage.setReadType(MessageReadTypeEnum.READ.getType());
        userMessageService.updateByQuery(userMessageQuery, userMessage);

        return ResponseVO.getSuccessResponseVO(null);
    }

    @RequestMapping("/loadMessage")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<PaginationResultVO<UserMessage>> loadMessage(@NotNull Integer messageType, Integer pageNo){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();

        UserMessageQuery userMessageQuery = new UserMessageQuery();
        userMessageQuery.setUserId(tokenUserInfoDto.getUserId());
        userMessageQuery.setMessageType(messageType);
        userMessageQuery.setOrderBy("message_id desc");
        userMessageQuery.setPageNo(pageNo);

        PaginationResultVO<UserMessage> resultVO = userMessageService.findByPage(userMessageQuery);

        return ResponseVO.getSuccessResponseVO(resultVO);
    }


    @RequestMapping("/delMessage")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Objects> delMessage(@NotNull Integer messageId){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        UserMessageQuery userMessageQuery = new UserMessageQuery();
        userMessageQuery.setUserId(tokenUserInfoDto.getUserId());
        userMessageQuery.setMessageId(messageId);
        userMessageService.deleteByQuery(userMessageQuery);
        return ResponseVO.getSuccessResponseVO(null);
    }



}
