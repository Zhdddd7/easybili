package com.easybili.entities.query;

import com.easybili.entities.vo.SimplePage;
import lombok.Data;

import java.util.List;

@Data
public class UserInfoQuery {
    private String userId;
    private Integer pageNo;       // Page number (starts from 1)
    private Integer pageSize;     // Page size
    private String orderBy;       // Custom order by clause

    private List<String> userIdList;
    private SimplePage simplePage;
}
