package com.easybili.entities.po;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfo implements Serializable {
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
    private String avatar;

    private Integer fansCount;
    private Integer focusCount;
    private  Integer likeCount;
    private Integer playCount;
    private Boolean haveFocus;
}
