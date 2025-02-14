package com.easybili.entities.query;

import com.easybili.entities.vo.SimplePage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class VideoInfoQuery {
    private Integer categoryId;
    private Integer pCategoryId;
    private Integer recommendType;
    private String tags;
    private String userId;
    private LocalDateTime createTime;
    private LocalDateTime lastPlayTime;
    private String orderBy;
    private String videoId;

    private Boolean queryUserInfo;
    private Integer pageNo;
    private Integer pageSize;
    private SimplePage simplePage;
    private String videoNameFuzzy;
    private Integer lastPlayHour;

    private Integer categoryIdOrPCategoryId;
    private String[] videoIdArray;


}
