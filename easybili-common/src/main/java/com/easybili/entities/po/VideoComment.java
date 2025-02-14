package com.easybili.entities.po;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class VideoComment {
    private Integer commentId;       // Comment ID
    private Integer pCommentId;      // Parent comment ID
    private String videoId;          // Video ID
    private String videoUserId;      // Uploader User ID
    private String content;          // Comment content
    private String imgPath;          // Image path
    private String userId;           // User ID
    private String replyUserId;      // Replyer ID
    private Integer topType;         // Top type: 0 (not top), 1 (top)
    private LocalDateTime postTime;  // Post time
    private Integer likeCount;       // Like count
    private Integer hateCount;       // Hate count

    private String avatar;
    private String userName;
    private String replyAvatar;
    private String replyUserName;
    private String videoCover;
    private String videoName;
    private List<VideoComment> children;
}
