package com.easybili.entities.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVideoSeriesVideoQuery {
    private Integer seriesId; // Series ID
    private String videoId; // Video ID
    private String userId; // User ID
    private Integer pageNo; // Page Number
    private Integer pageSize; // Page Size
    private String orderBy; // Order By Field
}