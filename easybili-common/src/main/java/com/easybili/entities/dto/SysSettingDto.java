package com.easybili.entities.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SysSettingDto implements Serializable {
    private static final long serialVersionUID = 202401010001L; // 年月日加编号
    Integer registerCoinCount = 20;
    Integer postVideoCoinCount = 5;
    Integer videoSize = 200;
    Integer videoPCount = 10;
    Integer videoCount = 10;
    Integer commentCount = 20;
    Integer danmuCount = 20;


}
