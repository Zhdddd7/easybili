package com.easybili.entities.query;

import com.easybili.entities.vo.SimplePage;
import lombok.Data;

@Data
public class VideoDanmuQuery {
    private String videoId;      // Video ID
    private String fileId;       // File ID
    private String userId;       // User ID
    private Integer mode;        // Display location
    private String text;         // Text filter
    private Integer pageNo;      // Page number (starts from 1)
    private Integer pageSize;    // Page size
    private String orderBy;      // Custom order by clause (e.g., "post_time DESC")
    private SimplePage simplePage;
    private String videoUserId;

    private Boolean queryVideoInfo;
    private String videoNameFuzzy;

}
