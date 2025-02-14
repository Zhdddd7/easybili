package com.easybili.entities.enums;


public enum CommentTopTypeEnum {
    TOP(1, "top the comment"),
    NO_TOP(0, "not top the comment");

    private Integer type;
    private String desc;

    CommentTopTypeEnum(Integer type, String desc){
        this.type = type;
        this.desc = desc;
    }

    public Integer getType(){
        return this.type;
    }

    public String getDesc(){
        return this.desc;
    }
}
