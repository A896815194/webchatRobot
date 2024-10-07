package com.web.webchat.enums.gzh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeChatConstat {

    public final static String COMMAND_OPEN_MONTH_MEMBER = "开月会";

    public final static String COMMAND_OPEN_QUARTER_MEMBER = "开季会";

    public final static String COMMAND_SAVE_QUARTER_MEMBER = "记季会";

    public final static String COMMAND_OPEN_YEAR_MEMBER = "开年会";

    public final static String COMMAND_OPEN_YEAR_T_MEMBER = "开年*会";

    public final static String COMMAND_SAVE_YEAR_MEMBER = "记年会";

    public final static String COMMAND_SING_SONG = "点歌";

    public final static String COMMAND_STUDY_SONG = "学歌";

    public final static String COMMAND_GIFT_STUDY_SONG = "礼物学歌";

    public final static String COMMAND_FINISH_STUDY_SONG = "交作业";

    public final static String COMMAND_SAVE_CARD = "抽卡";

    public final static String COMMAND_USE_CARD = "用卡";

    public final static String COMMAND_SING_DAILY = "每日歌单";

    public final static String COMMAND_SEARCH_SING_DAILY = "查歌单";

    public final static String COMMAND_SEARCH_SING_DAILY_3 = "查3天歌单";

    public final static String COMMAND_SEARCH_CARD = "查抽卡";

    public final static String COMMAND_SEARCH_MONTH_MEMBER = "查抽会";
    public final static String COMMAND_SEARCH_QUARTER_MEMBER = "查季会";
    public final static String COMMAND_SEARCH_YEAR_MEMBER = "查年会";

    public final static String COMMAND_SPLIT_JIA = "+";

    public final static String COMMAND_SPLIT_DOU = "@";
    /** 抽卡命令 **/
    public final static List<String> COMMAND_CARD_LIST = new ArrayList<>();

    public final static Map<String, String> COMMAND_BEAN_MAP = new HashMap<>();

    public final static String COMMAND_CARD_BEAN = "cardGzh";

    public final static Map<String, String> COMMAND_METHOD_MAP = new HashMap<>();
    /** 每日歌单 **/
    public final static List<String> COMMAND_SING_DAILY_LIST = new ArrayList<>();

    public final static String COMMAND_SING_DAILY_BEAN = "singDailyGzh";

    static {
        // 抽卡
        COMMAND_CARD_LIST.add(COMMAND_SAVE_CARD);
        // 用卡
        COMMAND_CARD_LIST.add(COMMAND_USE_CARD);
        // 查卡
        COMMAND_CARD_LIST.add(COMMAND_SEARCH_CARD);
        // 每日歌单
        COMMAND_SING_DAILY_LIST.add(COMMAND_SING_DAILY);
        // 查歌单
        COMMAND_SING_DAILY_LIST.add(COMMAND_SEARCH_SING_DAILY);
        // 查三天歌单
        COMMAND_SING_DAILY_LIST.add(COMMAND_SEARCH_SING_DAILY_3);


        COMMAND_BEAN_MAP.put(COMMAND_SAVE_CARD, COMMAND_CARD_BEAN);
        COMMAND_BEAN_MAP.put(COMMAND_SING_DAILY, COMMAND_SING_DAILY_BEAN);



        COMMAND_METHOD_MAP.put(COMMAND_SAVE_CARD, "saveCard");
        COMMAND_METHOD_MAP.put(COMMAND_USE_CARD, "useCard");
        COMMAND_METHOD_MAP.put(COMMAND_SEARCH_CARD, "searchCard");

        COMMAND_METHOD_MAP.put(COMMAND_SING_DAILY, "saveSongDaily");
        COMMAND_METHOD_MAP.put(COMMAND_SEARCH_SING_DAILY, "searchSongDaily");
        COMMAND_METHOD_MAP.put(COMMAND_SEARCH_SING_DAILY_3, "searchSongDailyRecent3");
    }

    public static String getBeanNameByKey(String key) {
        return COMMAND_BEAN_MAP.get(key);
    }

    public static String getBeanMethodByKey(String key) {
        return COMMAND_METHOD_MAP.get(key);
    }

   public final static String MSG_ERROR_BHF ="命令格式不合法";

    public final static String HELP="命令汇总\n" +
            "【一】：月度会员有关的命令\n" +
            "1.开月会+名字+歌名\n" +
            "例如:开月会+剪辑君+如愿\n" +
            "作用：登记月度会员人名歌名\n" +
            "【二】：季度会员有关命令\n" +
            "1.开季会+人名\n" +
            "2.记季会+人名+歌名,歌名  \n" +
            "【三】：年度会员有关的命令\n" +
            "1. 开年会+人名\n" +
            "2. 开年*会+人名                               \n" +
            "3. 记年会+人名+点歌+歌名,歌名                 \n" +
            "【四】：礼物学歌命令\n" +
            "1. 礼物学歌 +人名+ 歌名  作用：直接送礼物让学歌的命令\n" +
            "【五】：交作业命令\n" +
            "1.交作业+人名+歌名     作用：将学歌的状态修改成已交付/针对礼物学歌和年度会员学歌\n" +
            "【六】：抽卡有关的命令\n" +
            "1.抽卡+人名+卡类别\n" +
            "  例如： 抽卡+剪辑君+暴击卡    作用：剪辑君有一张暴击卡，然后会从记录时间自动往后加3天计算失效时间\n" +
            "2.用卡+人名+卡类别\n" +
            "  例如： 用卡+剪辑君+暴击卡    作用：剪辑君用掉一张暴击卡\n" +
            "【七】：每日歌单命令\n" +
            "1.每日歌单+歌名 \n" +
            "例如： 每日歌单+ 曹操     作用：记录当天的每日歌单  会根据输入命令的时间记录上午（6——12点）或者下午（12-18:00）或者晚上(18：00-6：00) \n" +
            "2.查歌单+2024年10月6日   作用：查询当天的歌单    上午，下午，晚上\n" +
            "3.查3天歌单             作用：查询当前开始算起到前3天的歌单\n" +
            "【八】：查询明细\n" +
            "1.查+人名+点歌\n" +
            "2.查+人名+学歌\n" +
            "3.查抽卡\n";


    public final static String HELP_NEW="命令汇总\n" +
            "【一】：抽卡有关的命令\n" +
            "1.抽卡+人名+卡类别\n" +
            "2.用卡+人名+卡类别\n" +
            "【二】：每日歌单命令\n" +
            "1.每日歌单+歌名 \n" +
            "2.查歌单+2024年10月6日\n" +
            "3.查3天歌单\n" +
            "【三】：查询明细\n" +
            "1.查抽卡\n";

    public final static String MSG_CARD_TOTAL_MODE ="【%s】持卡数:%s\n";
    public final static String MSG_CARD_MODE ="%s  距离%s小时\n";

    public final static String MSG_SING_DAILY_TOTAL_MODE ="【%s】共%s首\n";
    public final static String MSG_SING_DAILY_MODE ="【%s】 %s\n";
}
