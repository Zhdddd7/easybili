package com.easybili.entities.enums;

public enum VideoFileTransferResultEnum {
    TRANSFER(0, "encoding"),
    SUCCESS(1, "transfer successfully"),
    FAIL(2, "transfer failed");

    private final Integer status;
    private final String desc;

    VideoFileTransferResultEnum(Integer status, String desc) {
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
