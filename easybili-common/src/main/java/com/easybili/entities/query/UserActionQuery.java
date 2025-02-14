package com.easybili.entities.query;

import com.easybili.entities.vo.SimplePage;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

@Data
public class UserActionQuery {
    private String videoId;       // Video ID
    private Integer commentId;    // Comment ID
    private Integer actionType;   // Action type
    private String userId;        // User ID
    private Integer pageNo;       // Page number (starts from 1)
    private Integer pageSize;     // Page size
    private String orderBy;       // Custom order by clause (e.g., "action_time DESC")

    private Integer[] actionTypeArray;
    private SimplePage simplePage;
    private Boolean queryVideoInfo;
}
