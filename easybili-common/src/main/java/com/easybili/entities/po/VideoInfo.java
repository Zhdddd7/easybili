package com.easybili.entities.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VideoInfo implements Serializable {
    private String videoId; // Video ID
    private String videoCover; // Video cover
    private String videoName; // Video name
    private String userId; // User ID
    private LocalDateTime createTime; // Creation time
    private LocalDateTime lastUpdateTime; // Last update time
    private Integer pCategoryId; // Parent category ID
    private Integer categoryId; // Category ID
    private Integer postType; // Post type
    private String originInfo; // Original source description
    private String tags; // Tags
    private String introduction; // Introduction
    private String interaction; // Interaction settings
    private Integer duration; // Duration (seconds)
    private Integer playCount; // Play count
    private Integer likeCount; // Like count
    private Integer danmuCount; // Danmu count
    private Integer commentCount; // Comment count
    private Integer coinCount; // Coin count
    private Integer collectCount; // Collection count
    private Integer recommendType; // Recommendation type
    private LocalDateTime lastPlayTime; // Last playback time
    private String userName;
    private String avatar;
}
