package com.easybili.entities.enums;

public enum SearchOrderTypeEnum {
    VIDEO_PLAY(0, "playCount", "video play count"),
    VIDEO_TIME(1, "createTime", "video create time"),
    VIDEO_DANMU(2, "danmuCount", "count of danmus"),
    VIDEO_COLLECT(3, "collectCount", "video collect count");

    private Integer type;
    private String field;
    private String desc;

    SearchOrderTypeEnum(Integer type, String field, String desc) {
        this.type = type;
        this.field = field;
        this.desc = desc;
    }

    public static SearchOrderTypeEnum getByType(Integer type) {
        for (SearchOrderTypeEnum item : SearchOrderTypeEnum.values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public String getField() {
        return field;
    }
}

