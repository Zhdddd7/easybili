package com.easybili.entities.enums;

public enum VideoOrderTypeEnum {
    CREATE_TIME(0, "v.create_time", "publish time"),
    PLAY_COUNT(1, "v.play_count", "order by play count"),
    COLLECT_COUNT(2, "v.collect_count", "order by collect count");

    private Integer type;
    private String field;
    private String desc;

    VideoOrderTypeEnum(Integer type, String field, String desc) {
        this.type = type;
        this.field = field;
        this.desc = desc;
    }

    public static VideoOrderTypeEnum getByType(Integer type) {
        for (VideoOrderTypeEnum item : VideoOrderTypeEnum.values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }

    public Integer getType() {
        return type;
    }

    public String getField() {
        return field;
    }

    public String getDesc() {
        return desc;
    }
}

