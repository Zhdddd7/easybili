package com.easybili.entities.enums;

public enum VideoRecommendTypeEnum {
    NO_RECOMMEND(0, "Not recommend"),
    RECOMMEND(1, "Recommend");

    private Integer type;
    private String desc;

    VideoRecommendTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static VideoRecommendTypeEnum getByType(Integer type) {
        for (VideoRecommendTypeEnum typeEnum : VideoRecommendTypeEnum.values()) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum;
            }
        }
        return null;
    }
}

