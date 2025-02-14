package com.easybili.entities.po;

import com.easybili.entities.dto.UserMessageExtendDto;
import com.easybili.utils.JsonUtils;
import com.easybili.utils.StringTools;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserMessage {
    private Integer messageId;     // Message ID
    private String userId;         // User ID
    private String videoId;        // Entity ID
    private Integer messageType;    // Message content
    private String sendUserId;     // Message sender ID
    private Integer readType;      // Read type: 0 (not read), 1 (read)
    private LocalDateTime createTime; // Creation time
    private String extendJson;     // Extensions

    private String sendUserAvatar;
    private String sendUserName;
    private String videoName;
    private String videoCover;

    private UserMessageExtendDto extendDto;

    public UserMessageExtendDto getExtendDto(){
        return StringTools.isEmpty(extendJson)?new UserMessageExtendDto(): JsonUtils.convertJson2Obj(extendJson, UserMessageExtendDto.class);
    }

}
