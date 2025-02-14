package com.easybili.entities.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVideoSeriesVideo {
    private Integer seriesId; // Series ID
    private String videoId; // Video ID
    private String userId; // User ID
    private Integer sort; // Sort Order
}