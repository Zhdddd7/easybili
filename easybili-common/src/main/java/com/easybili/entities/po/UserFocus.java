package com.easybili.entities.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserFocus {
    private String userId;
    private String focusUserId;
    private LocalDateTime focusTime;

    private String otherUserName;
    private String otherUserId;
    private String otherAvatar;
    private Integer focusType;
}