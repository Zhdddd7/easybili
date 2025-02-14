package com.easybili.entities.query;

import com.easybili.entities.vo.SimplePage;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

@Data
public class VideoCommentQuery {
    private String videoId;        // Video ID
    private String userId;         // User ID
    private Integer topType;       // Top type
    private String content;        // Content filter (optional)
    private String replyUserId;    // Replyer ID (optional)
    private Integer pageNo;    // Page number for pagination
    private Integer pageSize;      // Page size for pagination
    private String orderBy;
    private Integer pCommentId;
    private SimplePage simplePage;
    private Boolean loadChildren; // whether we should load children
    private String videoUserId;

    private Boolean queryVideoInfo;
    private String videoNameFuzzy;
}

