package com.easybili.entities.enums;


public enum UserStatusEnum {

    BANNED(0, "banned"),
    ACTIVE(1, "active");

    private Integer status;
    private String desc;

    UserStatusEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus(){
        return this.status;
    }

    public String getDesc(){
        return this.desc;
    }

    public void setDesc(String desc){
        this.desc = desc;
    }
}
