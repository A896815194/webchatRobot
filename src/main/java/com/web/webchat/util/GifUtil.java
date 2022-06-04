package com.web.webchat.util;

import com.web.webchat.entity.ChatroomMemberMoney;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class GifUtil {
    private static final Logger logger = LogManager.getLogger(GifUtil.class.getName());

//    public static void main(String[] args) {
//        ChatroomMemberMoney cmm = new ChatroomMemberMoney();
//        cmm.setMoney(1L);
//        cmm.setWxidId("a");
//        ChatroomMemberMoney c1 = new ChatroomMemberMoney();
//        c1.setMoney(2L);
//        c1.setWxidId("b");
//        ChatroomMemberMoney c2 = new ChatroomMemberMoney();
//        c2.setMoney(3L);
//        c2.setWxidId("c");
//        ChatroomMemberMoney c3 = new ChatroomMemberMoney();
//        c3.setMoney(4L);
//        c3.setWxidId("d");
//        ChatroomMemberMoney c4 = new ChatroomMemberMoney();
//        c4.setMoney(5L);
//        c4.setWxidId("e");
//        ChatroomMemberMoney c5 = new ChatroomMemberMoney();
//        c5.setMoney(6L);
//        c5.setWxidId("f");
//        ChatroomMemberMoney c6 = new ChatroomMemberMoney();
//        c6.setMoney(7L);
//        c6.setWxidId("g");
//        List<ChatroomMemberMoney> test = new ArrayList<>();
//        test.add(cmm);
//        test.add(c1);
//        test.add(c2);
//        test.add(c3);
//        test.add(c4);
//        test.add(c5);
//        test.add(c6);
//        System.out.println(addMemberMoneyMajor(test, 3, 100, 200));
//    }


    //随机计个人加钱优化
    public static List<String> addMemberMoneyMajor(List<ChatroomMemberMoney> memberMonies, Integer randCount, Integer beforeMoney, Integer endMoney) {
        List<String> wxids = new ArrayList<>();
        if (CollectionUtils.isEmpty(memberMonies)) {
            new ArrayList<>();
        }
        List<ChatroomMemberMoney> copyList = new ArrayList<>();
        memberMonies.forEach(item -> {
            copyList.add(item);
        });
        //先生成随机最多有 ranCount 个人中奖
        Integer zjCount = Calculate.randBewteewn(0, randCount);
        //没人中将
        if (zjCount == 0) {
            return new ArrayList<>();
        }
        List<ChatroomMemberMoney> zjList = getRandomByCount(copyList, zjCount);
        List<ChatroomMemberMoney> mbs = memberMonies.stream().filter(item -> {
            return zjList.stream().anyMatch(zj -> {
                return Objects.equals(item.getWxidId(), zj.getWxidId());
            });
        }).collect(Collectors.toList());
        mbs.forEach(item -> {
            Long itemMoney = item.getMoney();
            Integer jl = Calculate.randBewteewn(beforeMoney, endMoney);
            logger.info("wxid:{},原来:{},获得额外奖励:{}", item.getWxidId(), itemMoney, jl);
            Long newMoney = itemMoney + jl;
            item.setMoney(newMoney);
            wxids.add(item.getWxidId() + "@" + jl);
        });

        return wxids;
    }

    public static List<ChatroomMemberMoney> getRandomByCount(List<ChatroomMemberMoney> oldList, Integer count) {
        if (oldList.size() <= count) {
            return oldList;
        }
        List<ChatroomMemberMoney> newList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int index = Calculate.randBewteewn(0, oldList.size() - 1);
            newList.add(oldList.get(index));
            oldList.remove(index);
        }
        return newList;
    }

    //随机计个人扣钱优化算法
    public static List<String> deAddMemberMoneyMajor(List<ChatroomMemberMoney> memberMonies, Integer randCount, Integer beforeMoney, Integer endMoney) {
        List<String> wxids = new ArrayList<>();
        if (CollectionUtils.isEmpty(memberMonies)) {
            new ArrayList<>();
        }
        List<ChatroomMemberMoney> copyList = new ArrayList<>();
        memberMonies.forEach(item -> {
            copyList.add(item);
        });
        //先生成随机最多有 ranCount 个人中奖
        Integer zjCount = Calculate.randBewteewn(0, randCount);
        //没人中将
        if (zjCount == 0) {
            return new ArrayList<>();
        }
        List<ChatroomMemberMoney> zjList = getRandomByCount(copyList, zjCount);
        List<ChatroomMemberMoney> mbs = memberMonies.stream().filter(item -> {
            return zjList.stream().anyMatch(zj -> {
                return Objects.equals(item.getWxidId(), zj.getWxidId());
            });
        }).collect(Collectors.toList());
        mbs.forEach(item -> {
            Long itemMoney = item.getMoney();
            Integer deMoney = Calculate.randBewteewn(beforeMoney, endMoney);
            logger.info("wxid:{},原来:{},扣掉奖励:{}", item.getWxidId(), itemMoney, deMoney);
            Long newMoney = itemMoney - deMoney;
            if (newMoney <= 0) {
                item.setMoney(0L);
            } else {
                item.setMoney(newMoney);
            }
            wxids.add(item.getWxidId() + "@" + deMoney);
        });

        return wxids;
    }

    //随机计个人清0优化算法
    public static List<String> deAddMemberMoneyHalfMajor(List<ChatroomMemberMoney> memberMonies, Integer randCount) {
        List<String> wxids = new ArrayList<>();
        if (CollectionUtils.isEmpty(memberMonies)) {
            new ArrayList<>();
        }
        List<ChatroomMemberMoney> copyList = new ArrayList<>();
        memberMonies.forEach(item -> {
            copyList.add(item);
        });
        //先生成随机最多有 ranCount 个人中奖
        Integer zjCount = Calculate.randBewteewn(0, randCount);
        //没人中将
        if (zjCount == 0) {
            return new ArrayList<>();
        }
        List<ChatroomMemberMoney> zjList = getRandomByCount(copyList, zjCount);
        List<ChatroomMemberMoney> mbs = memberMonies.stream().filter(item -> {
            return zjList.stream().anyMatch(zj -> {
                return Objects.equals(item.getWxidId(), zj.getWxidId());
            });
        }).collect(Collectors.toList());
        mbs.forEach(item -> {
            Long itemMoney = item.getMoney();
            String sMoney = String.valueOf(itemMoney);
            BigDecimal bMoney = new BigDecimal(sMoney);
            BigDecimal newMoney = bMoney.divide(new BigDecimal("2"), BigDecimal.ROUND_UP);
            logger.info("wxid:{},原来:{},折半后为:{}", item.getWxidId(), itemMoney, newMoney);
            item.setMoney(0L);
            wxids.add(item.getWxidId() + "@" + newMoney);
        });

        return wxids;
    }


    //随机计个人加钱
    public static List<String> addMemberMoney(List<ChatroomMemberMoney> memberMonies, Integer randCount, Integer beforeMoney, Integer endMoney) {
        List<String> wxids = new ArrayList<>();
        if (CollectionUtils.isEmpty(memberMonies)) {
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
        if (CollectionUtils.isEmpty(memberMonies)) {
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
        if (CollectionUtils.isEmpty(memberMonies)) {
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


    //妙手空空技能效果
    public static List<String> mskkMemberMoney(List<ChatroomMemberMoney> memberMonies, String userWxid, Integer beforeMoney, Integer endMoney) {
        List<String> wxids = new ArrayList<>();
        if (CollectionUtils.isEmpty(memberMonies)) {
            new ArrayList<>();
        }
        //拿到自己的钱包
        List<ChatroomMemberMoney> userMoney = memberMonies.stream()
                .filter(item -> Objects.equals(item.getWxidId(), userWxid)).collect(Collectors.toList());
        //去掉自己的
        ChatroomMemberMoney memberMoney;
        if (CollectionUtils.isEmpty(userMoney)) {
            memberMoney = ChatroomMemberMoney.builder()
                    .money(0L)
                    .wxidId(userWxid)
                    .build();
        } else {
            memberMoney = userMoney.get(0);
        }
        //去掉自己
        memberMonies.removeIf(item -> Objects.equals(item.getWxidId(), userWxid));
        AtomicReference<Long> addMoney = new AtomicReference<>(0L);
        memberMonies.forEach(item -> {
            Long itemMoney = item.getMoney();
            Integer deMoney = Calculate.randBewteewn(beforeMoney, endMoney);
            logger.info("wxid:{},原来:{},被偷:{}", item.getWxidId(), itemMoney, deMoney);
            UnaryOperator function
                    = (v) -> addMoney.get() + deMoney;
            addMoney.updateAndGet(function);
            Long newMoney = itemMoney - deMoney;
            if (newMoney <= 0) {
                item.setMoney(0L);
            } else {
                item.setMoney(newMoney);
            }
            wxids.add(item.getWxidId() + "@-" + deMoney);
        });
        wxids.add(userWxid + "@+" + addMoney.get());
        memberMoney.setMoney(memberMoney.getMoney() + addMoney.get());
        memberMonies.add(memberMoney);
        return wxids;
    }

//    public static void main(String[] args) {
//        AtomicReference<Long> addMoney = new AtomicReference<>(0L);
//        for (int i = 0; i < 4; i++) {
//            UnaryOperator function
//                    = (v) -> addMoney.get() + 5;
//            addMoney.updateAndGet(function);
//        }
//        System.out.println(addMoney.get());
//    }
}
