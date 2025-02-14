package com.easybili.constants;

public class Constants {

    // the default theme for a user
    public static final Integer THEME_ONE = 1;
    // the len of the user_id
    public static final Integer LENGTH_ID = 10;
    // MB
    public static final Long MB_SIZE = 1024 * 1024L;
    // 1. password should have at least 1 digit
    // 2. password should have at least one char
    // 3. the range should be [0-9], English char, ~!@#$%^&*_
    // 4. len range[0, 18]
    public static final String REGEX_PASSWORD = "^(?=.*\\d)(?=.*[a-zA-Z])[\\da-zA-Z~!@#$%^&*_]{0,18}$";
    // 1min = 60000ms
    public static final Integer REDIS_KEY_EXPIRES_ONE_MIN = 60000;

    public static final Integer REDIS_KEY_EXPIRES_ONE_SECOND = 1000;
    // 1 day = 1440 min
    public static final Integer REDIS_KEY_EXPIRES_ONE_DAY = REDIS_KEY_EXPIRES_ONE_MIN * 1440;
    // the name of the project
    public static final String REDIS_KEY_PREFIX = "easybili:";
    // suffix for video name while doing transferring: since there will generate a new file
    public static final String VIDEO_CODE_FILE_SUFFIX = "_temp" ;
    // 10 pcs of data on the user video center
    public static final int PAGE_SIZE = 10;

    public static final Integer INIT_COINS = 20 ;

    public static final Integer UPDATE_USER_NAME_COIN = 1;
    // construct key for checkcode
    public static  String REDIS_KEY_CHECK_CODE = REDIS_KEY_PREFIX + "checkcode:";
    // construct key for web token
    public static final String REDIS_KEY_TOKEN_WEB = REDIS_KEY_PREFIX + "token:web";
    // construct key for admin token
    public static final String REDIS_KEY_TOKEN_ADMIN = REDIS_KEY_PREFIX + "token:admin";
    // the token head
    public static final String TOKEN_HEAD = "token";
    // the token head for admin login
    public static final String TOKEN_ADMIN = "adminToken";
    // category list key in admin management
    public static final String REDIS_KEY_CATEGORY_LIST = REDIS_KEY_PREFIX + "category:list";
    // admin file upload path
    public static final String FILE_FOLDER = "file/";
    // admin cover file upload path
    public static final String COVER_FOLDER = "cover/";
    // admin video file upload path
    public static final String VIDEO_FOLDER = "video/";
    // temp file folder upload path
    public static final String TEMP_FOLDER = "temp/";
    // thumbnail img suffix
    public static final String IMAGE_THUMBNAIL_SUFFIX = "_thumbnail.jpg";

    public static final String REDIS_KEY_UPLOADING_FILE = REDIS_KEY_PREFIX + "uploading:";

    public static final String REDIS_KEY_SYS_SETTING = REDIS_KEY_PREFIX + "sysSetting:";
    // add the deleting files to the message queue
    public static final String REDIS_KEY_FILE_DEL = REDIS_KEY_PREFIX + "file:list:del:";

    public static final String REDIS_KEY_QUEUE_TRANSFER = REDIS_KEY_PREFIX + "queue:transfer:";

    public static final String REDIS_KEY_QUEUE_VIDEO_PLAY = REDIS_KEY_PREFIX + "queue:video:play:";

    public static final String TEMP_VIDEO_NAME = "/temp.mp4";

    public static final String VIDEO_CODE_HEVC = "hevc";

    public static final String TS_NAME = "index.ts";

    public static final String M3U8_NAME = "index.m3u8";

    // online play stats
    public static final String REDIS_KEY_VIDEO_PLAY_COUNT_ONLINE_PREFIX = REDIS_KEY_PREFIX + "video:play:online";

    public static final String REDIS_KEY_VIDEO_PLAY_COUNT_ONLINE = REDIS_KEY_VIDEO_PLAY_COUNT_ONLINE_PREFIX + "count:%s";

    public static final String REDIS_KEY_VIDEO_PLAY_COUNT_USER_PREFIX = "user:";

    public static final String REDIS_KEY_VIDEO_PLAY_COUNT_USER = REDIS_KEY_VIDEO_PLAY_COUNT_ONLINE_PREFIX + REDIS_KEY_VIDEO_PLAY_COUNT_USER_PREFIX + "%s:%s";

    // search trending
    public static final String REDIS_KEY_VIDEO_SEARCH_COUNT = REDIS_KEY_PREFIX + "video:search:";

    public static final String REDIS_KEY_VIDEO_PLAY_COUNT = REDIS_KEY_PREFIX + "video:playcount:";

}
