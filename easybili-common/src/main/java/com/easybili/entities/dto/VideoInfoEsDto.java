package com.easybili.entities.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.apache.lucene.index.IndexFormatTooNewException;

import java.time.LocalDateTime;
import java.util.Date;
@Data
public class VideoInfoEsDto {
    private String videoId;
    private String videoCover;
    private String videoName;
    private String userId;
    private LocalDateTime createTime;
    private String tags;
    private Integer playCount;
    private Integer danmuCount;
    private Integer collectCount;

}
