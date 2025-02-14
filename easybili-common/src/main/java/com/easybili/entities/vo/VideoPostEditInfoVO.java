package com.easybili.entities.vo;

import com.easybili.entities.po.VideoInfoFilePost;
import com.easybili.entities.po.VideoInfoPost;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoPostEditInfoVO {
    private VideoInfoPost videoInfo;
    private List<VideoInfoFilePost> videoInfoFileList;
}
