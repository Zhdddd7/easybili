package com.easybili.entities.query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class VideoInfoFileQuery {
    private String videoId;
    private String orderBy;
}
