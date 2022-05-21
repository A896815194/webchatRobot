package com.web.webchat.util;

import com.web.webchat.entity.ChatroomMemberMoney;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GifUtil {
    private static final Logger logger = LogManager.getLogger(GifUtil.class.getName());

    //随机计个人加钱
    public static List<String> addMemberMoney(List<ChatroomMemberMoney> memberMonies, Integer randCount, Integer beforeMoney, Integer endMoney) {
        List<String> wxids = new ArrayList<>();
        if(CollectionUtils.isEmpty(memberMonies)){
            new ArrayList<>();
        }
        if (randCount > memberMonies.size()) {
            randCount = memberMonies.size();
        }
        AtomicInteger count = new AtomicInteger(randCount);
        memberMonies.forEach(item -> {
            //百分之50的概率加
            if (count.intValue() == 0) {
                return;
            }
            if (Calculate.fixedPercentage(50)) {
                Long itemMoney = item.getMoney();
                Integer jl = Calculate.randBewteewn(beforeMoney, endMoney);
                logger.info("获得额外奖励:{}", jl);
                Long newMoney = itemMoney + jl;
                item.setMoney(newMoney);
                wxids.add(item.getWxidId());
                count.decrementAndGet();
            }
        });
        return wxids;
    }

    //随机计个人扣钱
    public static List<String> deAddMemberMoney(List<ChatroomMemberMoney> memberMonies, Integer randCount, Integer beforeMoney, Integer endMoney) {
        List<String> wxids = new ArrayList<>();
        if(CollectionUtils.isEmpty(memberMonies)){
            new ArrayList<>();
        }
        if (randCount > memberMonies.size()) {
            randCount = memberMonies.size();
        }
        AtomicInteger count = new AtomicInteger(randCount);
        memberMonies.forEach(item -> {
            //百分之50的概率
            if (count.intValue() == 0) {
                return;
            }
            if (Calculate.fixedPercentage(50)) {
                Long itemMoney = item.getMoney();
                Long newMoney = itemMoney - Calculate.randBewteewn(beforeMoney, endMoney);
                if (newMoney <= 0) {
                    item.setMoney(0L);
                } else {
                    item.setMoney(newMoney);
                }
                wxids.add(item.getWxidId());
                count.decrementAndGet();
            }
        });
        return wxids;
    }


    //随机计个人清0
    public static List<String> deAddMemberMoneyZero(List<ChatroomMemberMoney> memberMonies, Integer randCount) {
        List<String> wxids = new ArrayList<>();
        if(CollectionUtils.isEmpty(memberMonies)){
            new ArrayList<>();
        }
        if (randCount > memberMonies.size()) {
            randCount = memberMonies.size();
        }
        AtomicInteger count = new AtomicInteger(randCount);
        memberMonies.forEach(item -> {
            //百分之50的概率
            if (count.intValue() == 0) {
                return;
            }
            if (Calculate.fixedPercentage(50)) {
                item.setMoney(0L);
                count.decrementAndGet();
                wxids.add(item.getWxidId());
            }
        });
        return wxids;
    }
}
