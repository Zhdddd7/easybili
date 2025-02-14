package com.easybili.entities.vo;

import com.easybili.entities.po.UserAction;
import com.easybili.entities.po.VideoComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoCommentResultVO {
    private PaginationResultVO commentData;
    private List userActionList;
}
