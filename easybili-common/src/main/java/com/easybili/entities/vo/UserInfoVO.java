package com.easybili.entities.vo;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;


@Data
public class UserInfoVO {
    private String userId;
    private String userName;
    private String email;
    private String sex; // "Male", "Female", "Other"
    private String avatar;
    private String personIntroduction;
    private String noticeInfo;

    private String birthday;
    private String school;
    private Integer fansCount;
    private Integer focusCount;
    private  Integer likeCount;
    private Integer playCount;
    private Boolean haveFocus;
    private Integer theme;
}
