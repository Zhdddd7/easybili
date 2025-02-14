package com.easybili.entities.query;

import com.easybili.entities.vo.SimplePage;
import lombok.Data;

@Data
public class StatisticsInfoQuery {
    private String statisticsDate; // Statistics date
    private Integer dataType;      // Data type
    private String userId;         // User ID
    private SimplePage simplePage; // Pagination

    private String statisticsDateStart;
    private String statisticsDateEnd;
    private String orderBy;

}
