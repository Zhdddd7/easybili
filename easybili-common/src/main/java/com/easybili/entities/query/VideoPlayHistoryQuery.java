package com.easybili.entities.query;

import com.easybili.entities.vo.SimplePage;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VideoPlayHistoryQuery {
    private String userId;
    private String videoId;
    private Integer fileIndex;
    private LocalDateTime lastUpdateTimeStart;
    private LocalDateTime lastUpdateTimeEnd;
    private SimplePage simplePage;
    private String orderBy;

    private Integer pageNo;
    private Integer pageSize;

    private Boolean queryVideoDetail;
}
