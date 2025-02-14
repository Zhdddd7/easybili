package com.easybili.entities.enums;

public enum MessageReadTypeEnum {
    NO_READ(0, "Unread"),
    READ(1, "Read");

    private Integer type;
    private String desc;

    MessageReadTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static MessageReadTypeEnum getByStatus(Integer status) {
        for (MessageReadTypeEnum statusEnum : MessageReadTypeEnum.values()) {
            if (statusEnum.getType().equals(status)) {
                return statusEnum;
            }
        }
        return null;
    }
}
