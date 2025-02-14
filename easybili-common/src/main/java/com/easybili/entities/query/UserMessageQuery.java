package com.easybili.entities.query;

import com.easybili.entities.vo.SimplePage;
import lombok.Data;

@Data
public class UserMessageQuery {
    private String userId;         // User ID
    private String videoId;        // Entity ID
    private Integer messageType;    // Message type
    private Integer readType;      // Read type: 0 (not read), 1 (read)
    private Integer pageNo;        // Page number
    private Integer pageSize;      // Page size
    private String orderBy;        // Order by clause

    private SimplePage simplePage;
    private Integer messageId;
}
