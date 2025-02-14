package com.easybili.entities.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VideoInfoFile implements Serializable {
    private String fileId; // Unique ID
    private String userId; // User ID
    private String videoId; // Video ID
    private String fileName; // File name
    private Integer fileIndex; // File sequence number
    private Long fileSize; // File size
    private String filePath; // File path
    private Integer duration; // Duration (seconds)
}
