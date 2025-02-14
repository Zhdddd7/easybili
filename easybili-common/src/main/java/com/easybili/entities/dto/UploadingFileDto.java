package com.easybili.entities.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UploadingFileDto implements Serializable {
    private static final long serialVersionUID = 202401010001L; // 年月日加编号
    private String uploadId;
    private String fileName;
    private Integer chunkIndex;
    private Integer chunks;
    private Long fileSize = 0L;
    private String filePath;
}
