package com.easybili.entities.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VideoInfoFilePost implements Serializable {
    private String fileId; // Unique ID
    private String uploadId; // Upload ID
    private String userId; // User ID
    private String videoId; // Video ID
    private Integer fileIndex; // File sequence number
    private String fileName; // File name
    private Long fileSize; // File size
    private String filePath; // File path
    private Integer updateType; // Update type: 0 - No update; 1 - Update available
    private Integer transferResult; // Transfer result: 0 - Transferring; 1 - Success; 2 - Failure
    private Integer duration; // Duration (seconds)
}
