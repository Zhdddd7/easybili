package com.easybili.entities.enums;

public enum UserSexEnum {

    MALE(0, "male"),
    FEMALE(1, "female"),
    OTHER(2, "other");

    private Integer sex;
    private String desc;

    UserSexEnum(Integer sex, String desc){
        this.sex = sex;
        this.desc = desc;
    }
    public Integer getSex(){
        return this.sex;
    }

    public String getDesc(){
        return this.desc;
    }

    public void setDesc(String desc){
        this.desc = desc;
    }

}
