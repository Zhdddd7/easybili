package com.easybili.admin.controller;

import com.easybili.entities.component.RedisComponent;
import com.easybili.entities.dto.SysSettingDto;
import com.easybili.entities.query.UserInfoQuery;
import com.easybili.entities.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/setting")
@Validated
@Slf4j
public class SettingController extends BaseController<Object>{
    @Resource
    private RedisComponent redisComponent;

    @RequestMapping("/getSetting")
    public ResponseVO<Object> getSetting() {
        return ResponseVO.getSuccessResponseVO(redisComponent.getSysSettingDto());
    }

    @RequestMapping("/saveSetting")
    public ResponseVO<Object> saveSetting(SysSettingDto sysSettingDto) {
        redisComponent.saveSysSettingDto(sysSettingDto);
        return ResponseVO.getSuccessResponseVO();
    }

}
