package com.easybili.entities.vo;

import com.easybili.entities.po.VideoInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VideoInfoResultVO {
    private VideoInfo videoInfo;
    private List userActionList;

}
