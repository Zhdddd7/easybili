package com.easybili.entities.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVideoSeries {
    private Integer seriesId; // Series ID
    private String seriesName; // Series Name
    private String seriesDescription; // Description
    private String userId; // User ID
    private Integer sort; // Sort Order
    private LocalDateTime updateTime; // Update Time
}
