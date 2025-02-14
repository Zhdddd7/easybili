package com.easybili.entities.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VideoDanmu {
    private Integer danmuId;       // Primary ID
    private String videoId;        // Video ID
    private String fileId;         // File ID
    private String userId;         // User ID
    private LocalDateTime postTime; // Post time
    private String text;           // Danmaku text
    private Integer mode;          // Display location
    private Integer time;          // Display time
    private String color;          // Text color

    private String videoName;
    private String videoCover;
    private String userName;
}