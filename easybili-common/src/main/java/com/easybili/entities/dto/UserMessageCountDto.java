package com.easybili.entities.dto;

import lombok.Data;

@Data
public class UserMessageCountDto {
    private Integer messageType;
    private Integer messageCount;
}
