package com.easybili.entities.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatisticsInfo {
    private String statisticsDate; // Statistics date
    private Integer dataType;      // Data type
    private Integer statisticsCount; // Statistics count
    private String userId;         // User ID

}
