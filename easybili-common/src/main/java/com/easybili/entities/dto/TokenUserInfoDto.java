package com.easybili.entities.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TokenUserInfoDto implements Serializable {

    private static final long serialVersionUID = 202401010001L; // 年月日加编号

    private String userId;
    private String userName;
    private String avatar;
    private Long expireAt;
    private String token;

    private Integer fansCount;
    private Integer currentCoinCount;
    private Integer focusCount;
}
