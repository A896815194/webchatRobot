package com.web.webchat.enums;

public class Message {
    /**
     * 系统错误提示
     **/
    public static final String SYSTEM_ERROR_MSG = "魔法世界不太稳定，请稍后再试..";
    /**
     * 重复签到提示
     **/
    public static final String REPEAT_SINGIN_MSG = "今天已经签过到了,不要太贪心哦!";
    /**
     * 没有存款
     **/
    public static final String MY_MONEYBAG_EMPTY = "你没有魔法能量呀！";
    /**
     * 钱不够
     **/
    public static final String MY_MONEYBAG_NOT_ENOUGH = "你的魔法能量不够呀！";
    /**
     * 我的魔法背包模板
     **/
    public static final String MY_MONEYBAG_TEMPLATE = "【%s】的魔法背包:\n魔法能量:%s";
    /**
     * 我的魔法物品模板
     **/
    public static final String MY_MONEYBAG_THING_TEMPLATE = "【%s】   ×%s";
    /**
     * 签收收益模板
     **/
    public static final String GET_MONEY_TEMPALTE = "签到NO.%s【%s】\n得到%s魔法能量";
    /**
     * 签到额外收益模板
     **/
    public static final String GET_EXTRA_MONEY_TEMPLATE = ";同时得到大魔法师的青睐，额外获得%S魔法能量奖励!!";
    /**
     * 商城为空提示
     **/
    public static final String GET_SHOP_EMPTY_MSG = "魔法商城空空如也,很是期待上货~";
    /**
     * 商城购买没写数字
     **/
    public static final String GET_SHOP_ERROR_MSG = "魔法商城中识别不到这个商品呀";
    /**
     * 商城购买成功
     **/
    public static final String GET_SHOP_SUCCESS = "【%s】通过魔法商城成功购买了【%s】!\r【%s】";
    /**
     * 商城购买存在限定
     **/
    public static final String GET_SHOP_REPEAT_ERROR_MSG = "你已经买过一次限定商品了不能再购买！";

    /**
     * 幸运天气
     **/
    public static final String LUCK_DAY_MONEY = "阳光的【%s】很是幸运，魔法能量得到%s点提升!";

    /**
     * 失落天气
     **/
    public static final String NO_LUCK_DAY_MONEY = "沮丧的【%s】有些失落，魔法能量损失了%s点..";

    /**
     * 倒霉蛋天气
     **/
    public static final String DIED_DAY_MONEY = "倒霉蛋【%s】不幸被雷电击中，魔法能量被折半~";

    /**
     * 妙手空空失败
     **/
    public static final String MSKK_FAIL_1 = "【%s】使用妙手空空被发现,被拧断胳膊,损失【%s】魔法能量才恢复健康~";

    /**
     * 妙手空空失败
     **/
    public static final String MSKK_FAIL_2 = "【%s】使用妙手空空被发现,被打断腿,损失【%s】魔法能量才恢复健康~";

    /**
     * 妙手空空失败
     **/
    public static final String MSKK_FAIL_3 = "【%s】使用妙手空空被发现,被打到半身不遂,损失【%s】魔法能量才恢复健康~";

    /**
     * 妙手空空失败
     **/
    public static final String MSKK_FAIL_4 = "【%s】使用妙手空空被发现,被打到生活不能自理,损失了全部【%s】魔法能量才恢复健康!";
    /**
     * 妙手空空失败
     **/
    public static final String MSKK_FAIL = "没有发现携带魔法水晶的勇者,妙手空空本次被消耗";
    /**
     * 妙手空空失败
     **/
    public static final String MSKK_FAIL_5 = "【%s】使用妙手空空被发现,由于没有魔法能量并没有造成不良影响";

    /**
     * 妙手空空成功
     **/
    public static final String MSKK_SUCCESS = "【%s】使用妙手空空,来无影去无踪,成功偷取【%s】魔法能量!\r%s";

    /**
     * 妙手空空成功被偷
     **/
    public static final String MSKK_SUCCESS_BT = "【%s】被偷取【%s】魔法能量,竟毫无察觉~";

}
