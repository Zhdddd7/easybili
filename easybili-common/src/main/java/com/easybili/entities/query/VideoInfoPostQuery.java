package com.easybili.entities.query;

import com.easybili.entities.vo.SimplePage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class VideoInfoPostQuery {
    private Integer categoryId;
    private Integer pCategoryId;
    private String userId;
    private Integer status;
    private Integer pageNo;
    private String orderBy;
    private Integer[] excludeStatusArray;
    private String videoNameFuzzy;
    // designed for table join
    private Boolean queryCountInfo;
    private Boolean queryUserInfo;
    private SimplePage simplePage;
    private String videoId;


}
