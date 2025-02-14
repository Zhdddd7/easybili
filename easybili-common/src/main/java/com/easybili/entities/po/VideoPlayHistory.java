package com.easybili.entities.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoPlayHistory {
    private String userId;
    private String videoId;
    private Integer fileIndex;
    private LocalDateTime lastUpdateTime;

    private String videoName;
    private String videoCover;
}
