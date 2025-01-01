package com.easybili.web.vo;

import lombok.Data;


@Data
public class UserInfoVO {
    private String userId;
    private String userName;
    private String email;
    private String sex; // "Male", "Female", "Other"
}
