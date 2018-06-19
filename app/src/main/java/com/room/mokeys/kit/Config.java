
package com.room.mokeys.kit;

public class Config {
    /**
     * true：debug模式，logcat输出debug日志 false：正式发布模式，logcat不输出日志，但是日志记录进后端数据库
     */
    public static final String CHARSET = "UTF-8";

    public static final String SDKNAME_EN = "qhopensdk";// 这个不能随便改，和本地数据库名要一致

    /**
     * 形如:/mnt/sdcard/qhopensdk/ 在Matrix.init中初始化
     */
    public static String BASE_FOLDER = "";
    /**
     * 下载目录，形如:/mnt/sdcard/qhopensdk/download/ 在Matrix.init中初始化
     */
    public static String BASE_FOLDER_DOWNLOAD = "";
    public static String BASE_FOLDER_CACHE = "";
    /**
     * 屏幕的宽高
     */
    public static int SCREEN_WIDTH = 0;
    public static int SCREEN_HEIGHT = 0;
    /**
     * sqlite本地数据库版本号
     */
    public static final int SQLITE_DB_VERSION = 1;
    /**
     * sqlite本地设置的KEYS LS=Local Setting
     */
    public static String LS_is_first_time = "is_first_time";// 是否首次使用本软件
    public static String LS_version = "version_";// 本地存储的当前正在运行的SDK版本号
    public static String LS_md5 = "md5_";// 本地存储md5串，key形如： md_版本号
    public static String LS_game2version = "game2verison_";// 游戏所对应的版本号
    public static String LS_accountCookie = "accountCookie";// 用户登录后的cookie
    public static String LS_resLastModifyTime = "resLastModifyTime_";// res文件夹最后修改时间

    public static final float FONT_SIZE_10_DP = 10;
    public static final float FONT_SIZE_11_DP = 11;
    public static final float FONT_SIZE_12_DP = 12;
    public static final float FONT_SIZE_13_DP = 13;
    public static final float FONT_SIZE_13P3_DP = 13.3f;
    public static final float FONT_SIZE_13P7_DP = 13.7f;
    public static final float FONT_SIZE_14_DP = 14;
    public static final float FONT_SIZE_14P7_DP = 14.7f;
    public static final float FONT_SIZE_16_DP = 16;
    public static final float FONT_SIZE_16P7_DP = 16.7f;
    public static final float FONT_SIZE_17_DP = 17;
    public static final float FONT_SIZE_18_DP = 18;
    public static final float FONT_SIZE_20_DP = 20;
    public static final float FONT_SIZE_22_DP = 22;
    public static final float FONT_SIZE_24_DP = 24;

    // 家长监护URL
    public static final String PARENT_GUADRDIANSHIP_URL = "http://w.qhimg.com/images/v2/wan/gamezone/day/2011/10/13/wcn/index.htm";
    // 防沉迷URL
    public static final String ANTI_ADDICTION_URL = "https://mgame.360.cn/user/check_authentication.json?";

    public static final String DRAWABLE_DEFAULT = "icon";
    public static final String DRAWABLE_LDPI = "icon-ldpi";
    public static final String DRAWABLE_MDPI = "icon-mdpi";
    public static final String DRAWABLE_XDPI = "icon-xdpi";

    public static final int LAYOUT_NORMAL = 320;
    public static final int LAYOUT_MEDIUM = 480;
    public static final int LAYOUT_LARGE = 600;
    public static final int LAYOUT_XLARGE = 720;

    public static final int DENSITY_LDPI = 120;
    public static final int DENSITY_MDPI = 160;
    public static final int DENSITY_HDPI = 240;
    public static final int DENSITY_XDPI = 320;

    /**
     * 金额的精确度：
     * <p>
     * 1 -> 元<br/>
     * 10 -> 角<br/>
     * 100 -> 分<br/>
     * </p>
     */
    public static final int ACCURACY = 100;

    /**
     * 最多显示信用卡的张数
     */
    public static final int MAX_CREDIT_SHOW = 3;

    public static boolean isQikuLoginEnabled = false;
}
