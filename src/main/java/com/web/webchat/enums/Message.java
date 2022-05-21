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
    public static final String MY_MONEYBAG_THING_TEMPLATE = "【%s】   ×1";
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
    public static final String GET_SHOP_SUCCESS = "【%s】通过魔法商城成功购买了【%s】!";
    /**
     * 商城购买存在限定
     **/
    public static final String GET_SHOP_REPEAT_ERROR_MSG = "你已经买过一次限定商品了不能再购买！";

    /**
     * 幸运天气
     **/
    public static final String LUCK_DAY_MONEY = "阳光的【%s】很是幸运，魔法能量得到提升!";

    /**
     * 失落天气
     **/
    public static final String NO_LUCK_DAY_MONEY = "沮丧的【%s】有些失落，魔法能量损失了一些..";

    /**
     * 倒霉蛋天气
     **/
    public static final String DIED_DAY_MONEY = "倒霉蛋【%s】不幸被雷电击中，魔法能量全部丢失~";

}
