package com.easybili.web.controller;

import com.easybili.entities.component.RedisComponent;
import com.easybili.entities.config.AppConfig;
import com.easybili.entities.enums.ResponseCodeEnum;
import com.easybili.entities.exception.BusinessException;
import com.easybili.entities.vo.ResponseVO;
import com.easybili.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequestMapping("/api/sysSetting")
@Validated
@Slf4j
public class SysSettingController {
    @Resource
    private RedisComponent redisComponent;

    @RequestMapping("/getSetting")
    public ResponseVO<Object> getSetting() {
        return ResponseVO.getSuccessResponseVO(redisComponent.getSysSettingDto());
    }

}
