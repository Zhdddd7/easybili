package com.easybili.entities.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserAction {
    private Integer actionId;       // Primary ID
    private String videoId;         // Video ID
    private String videoUserId;     // Uploader User ID
    private Integer commentId;      // Comment ID
    private Integer actionType;     // Action type: 0 - No action; 1 - Like; 2 - Dislike; 3 - Coin; 4 - Favorite
    private Integer actionCount;    // Action count
    private String userId;          // User ID
    private LocalDateTime actionTime; // Action timestamp

    private String videoCover;
    private String videoName;

}