package com.easybili.entities.enums;

public enum StatisticsTypeEnum {
    PLAY(0, "Playback"),
    FANS(1, "User"),
    LIKE(2, "Like"),
    COLLECTION(3, "Collection"),
    COIN(4, "Coin"),
    COMMENT(5, "Comment"),
    DANMU(6, "Danmaku");

    private Integer type;
    private String desc;

    StatisticsTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static StatisticsTypeEnum getByType(Integer type) {
        for (StatisticsTypeEnum item : StatisticsTypeEnum.values()) {
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

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

