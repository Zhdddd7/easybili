package com.easybili.entities.query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class VideoInfoFilePostQuery {
    private String videoId;
    private String uploadId;
    private String userId;
    private Integer transferResult;

    private String orderBy;
}
