package com.easybili.entities.enums;

public enum VideoStatusEnum {
    STATUS0(0, "Transferring"),
    STATUS1(1, "Transfer failed"),
    STATUS2(2, "Pending"),
    STATUS3(3, "Audit passed"),
    STATUS4(4, "Audit denied");

    private final Integer status;
    private final String desc;

    VideoStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    public static VideoStatusEnum getByStatus(Integer status) {
        for (VideoStatusEnum statusEnum : VideoStatusEnum.values()) {
            if (statusEnum.getStatus().equals(status)) {
                return statusEnum;
            }
        }
        return null;
    }
}
