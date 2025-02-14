package com.easybili.entities.enums;

public enum VideoFileUpdateTypeEnum {
    NO_UPDATE(0, "No update"),
    UPDATE(1, "Update");

    private final Integer status;
    private final String desc;

    VideoFileUpdateTypeEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
