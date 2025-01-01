package com.easybili.web.po;
import lombok.*;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfo {
    private String userId;
    private String userName;
    private String email;
    private String password;
    private Integer sex;  //0: Male; 1: Female; 2: Other
    private String birthday;
    private String school;
    private String personIntroduction;
    private LocalDateTime joinTime;
    private LocalDateTime lastLoginTime;
    private String lastLoginIp;
    private Integer status;   //1: active; 0: banned',
    private String noticeInfo;   //the notice info on user profile',
    private Integer totalCoinCount;   //the most amount coins',
    private Integer currentCoinCount;   //the coin a user is holding',
    private Integer theme;    //the theme on user profile',


}
