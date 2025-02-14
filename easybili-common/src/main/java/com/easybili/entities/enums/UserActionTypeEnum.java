package com.easybili.entities.enums;

public enum UserActionTypeEnum {

    COMMENT_LIKE(0, "like_count", "Number of comment likes"),
    COMMENT_HATE(1, "hate_count", "Number of comment dislikes"),
    VIDEO_LIKE(2, "like_count", "Number of video likes"),
    VIDEO_COLLECT(3, "collect_count", "Number of video collections"),
    VIDEO_COIN(4, "coin_count", "Number of video coins"),
    VIDEO_COMMENT(5, "comment_count", "Number of video comments"),
    VIDEO_DANMU(6, "danmu_count", "Number of video danmaku"),
    VIDEO_PLAY(7, "play_count", "Number of video plays");

    private Integer type;
    private String field;
    private String desc;

    UserActionTypeEnum(Integer type, String field, String desc) {
        this.type = type;
        this.field = field;
        this.desc = desc;
    }

    public static UserActionTypeEnum getByType(Integer type) {
        for (UserActionTypeEnum item : UserActionTypeEnum.values()) {
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

