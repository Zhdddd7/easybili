package com.easybili.constants;

public class Constants {

    // the default theme for a user
    public static final Integer THEME_ONE = 1;
    // the len of the user_id
    public static final Integer LENGTH_ID = 10;
    // 1. password should have at least 1 digit
    // 2. password should have at least one char
    // 3. the range should be [0-9], English char, ~!@#$%^&*_
    // 4. len range[0, 18]
    public static final String REGEX_PASSWORD = "^(?=.*\\d)(?=.*[a-zA-Z])[\\da-zA-Z~!@#$%^&*_]{0,18}$";
    // 1min = 60000ms
    public static final Integer REDIS_KEY_EXPIRES_ONE_MIN = 60000;
    // 1 day = 1440 min
    public static final Integer REDIS_KEY_EXPIRES_ONE_DAY = REDIS_KEY_EXPIRES_ONE_MIN * 1440;
    // the name of the project
    public static final String REDIS_KEY_PREFIX = "easybili:";
    // construct key for checkcode
    public static  String REDIS_KEY_CHECK_CODE = REDIS_KEY_PREFIX + "checkcode:";
    // construct key for web token
    public static final String REDIS_KEY_TOKEN_WEB = REDIS_KEY_PREFIX + "token:web";
    // the token head
    public static final String TOKEN_HEAD = "token";
}
