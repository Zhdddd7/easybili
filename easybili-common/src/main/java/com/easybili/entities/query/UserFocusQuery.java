package com.easybili.entities.query;

import com.easybili.entities.vo.SimplePage;
import lombok.Data;

@Data
public class UserFocusQuery {
    private String userId;
    private String focusUserId;
    private Integer pageNo;
    private Integer pageSize;
    private String orderBy;
    private SimplePage simplePage;

    private Integer queryType; // 0: query for focus 1: query for fans
}
