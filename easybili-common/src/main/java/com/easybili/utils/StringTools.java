package com.easybili.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

public class StringTools {
    // judge if a string is null/empty/"null"/Unicode Null
    public static boolean isEmpty(String str) {
        if (null == str || str.isEmpty() || "null".equals(str) || "\u0000".equals(str)) {
            return true;
        } else if (str.trim().isEmpty()) {
            return true;
        }
        return false;
    }

    // can have digits and chars
    public static final String getRandomString(Integer count){
        return RandomStringUtils.random(count, true, true);
    }

    // only have digits
    public static final String getRandomNumber(Integer count){
        return RandomStringUtils.random(count, false, true);
    }

    public static final String encodeByMd5(String s){
        return StringTools.isEmpty(s)?null: DigestUtils.md5Hex(s);
    }

    public static boolean pathIsOk(String path){
        if(StringTools.isEmpty(path)){
            return true;
        }
        if (path.contains("../") || path.contains("..\\")){
            return false;
        }
        return true;
    }

    public static String getSuffix(String fileName){
        if(StringTools.isEmpty(fileName) || !fileName.contains(".")) {
            return null;
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public static String upperCaseFirstLetter(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

}
