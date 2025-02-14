package com.easybili.utils;


import com.easybili.entities.po.UserInfo;
import com.easybili.entities.vo.UserInfoVO;

public class UserInfoConverter {

    /**
     * 将 UserInfo 转换为 UserInfoVO
     */
    public static UserInfoVO toUserInfoVO(UserInfo userInfo) {
        if (userInfo == null) {
            return null;
        }
        UserInfoVO vo = new UserInfoVO();
        vo.setUserId(userInfo.getUserId());
        vo.setUserName(userInfo.getUserName());
        vo.setEmail(userInfo.getEmail());
        vo.setSex(convertSexToString(userInfo.getSex())); // 转换性别字段
        return vo;
    }

    /**
     * 将性别代码转换为可读字符串
     */
    private static String convertSexToString(Integer sex) {
        if (sex == null) return "Unknown";
        switch (sex) {
            case 0:
                return "Male";
            case 1:
                return "Female";
            case 2:
                return "Other";
            default:
                return "Unknown";
        }
    }
}
