package com.easybili.entities.po;

import com.easybili.entities.enums.VideoStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VideoInfoPost extends VideoInfo implements Serializable {
    private String videoId; // Video ID
    private String videoCover; // Video cover image
    private String videoName; // Video name
    private String userId; // User ID
    private LocalDateTime createTime; // Creation time
    private LocalDateTime lastUpdate; // Last update time
    private Integer pCategoryId; // Parent category ID
    private Integer categoryId; // Category ID
    private Integer status; // Status: 0 = draft; 1 = pending approval; 2 = approved; 3 = approval failed
    private Integer postType; // Post type: 0 = original; 1 = repost
    private String originInfo; // Original information description
    private String tags; // Tags
    private String introduction; // Introduction
    private String interaction; // Interaction count
    private Integer duration; // Video duration (seconds)

    private String statusName;

    public String getStatusName(){
        VideoStatusEnum videoStatusEnum = VideoStatusEnum.getByStatus(status);
        return videoStatusEnum == null?"": videoStatusEnum.getDesc();
    }
}
