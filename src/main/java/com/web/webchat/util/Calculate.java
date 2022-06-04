package com.web.webchat.util;

import java.math.BigDecimal;
import java.util.Random;

public class Calculate {
    //生成范围的随机数
    public static Integer randBewteewn(Integer start, Integer end) {
        Random random = new Random();
        return random.nextInt(end - start + 1) + start;
    }

    //生成百分比
    public static boolean fixedPercentage(Integer percent) {
        Integer randMumber = randBewteewn(1, 100);
        return randMumber <= percent;
    }

    // 加
    public static BigDecimal add(Long sourceCount, Long preCount) {
        BigDecimal sumAdd = BigDecimal.valueOf(0);
        String sourceCountStr = String.valueOf(sourceCount);
        String preCountStr = String.valueOf(preCount);
        BigDecimal sourceBig = new BigDecimal(sourceCountStr);
        BigDecimal preBig = new BigDecimal(preCountStr);
        sumAdd = sourceBig.add(preBig);
        return sumAdd;
    }

    // 减
    public static BigDecimal subtract(Long sourceCount, Long preCount) {
        BigDecimal sumSub = BigDecimal.valueOf(0);
        String sourceCountStr = String.valueOf(sourceCount);
        String preCountStr = String.valueOf(preCount);
        BigDecimal sourceBig = new BigDecimal(sourceCountStr);
        BigDecimal preBig = new BigDecimal(preCountStr);
        sumSub = sourceBig.subtract(preBig);
        return sumSub;
    }

    // 乘
    public static BigDecimal multiply(Long sourceCount, Long preCount, int scale, int roundHalf) {
        BigDecimal sumSub = BigDecimal.valueOf(0);
        String sourceCountStr = String.valueOf(sourceCount);
        String preCountStr = String.valueOf(preCount);
        BigDecimal sourceBig = new BigDecimal(sourceCountStr);
        BigDecimal preBig = new BigDecimal(preCountStr);
        sumSub = sourceBig.multiply(preBig).setScale(scale, roundHalf);
        return sumSub;
    }

    // 乘
    public static BigDecimal multiply(Long sourceCount, BigDecimal preCount, int scale, int roundHalf) {
        BigDecimal sumSub = BigDecimal.valueOf(0);
        String sourceCountStr = String.valueOf(sourceCount);
        BigDecimal sourceBig = new BigDecimal(sourceCountStr);
        sumSub = sourceBig.multiply(preCount).setScale(scale, roundHalf);
        return sumSub;
    }

    public static BigDecimal divide(Long sourceCount, Long preCount, int scale, int roundHalf) {
        BigDecimal sumSub = BigDecimal.valueOf(0);
        String sourceCountStr = String.valueOf(sourceCount);
        String preCountStr = String.valueOf(preCount);
        BigDecimal sourceBig = new BigDecimal(sourceCountStr);
        BigDecimal preBig = new BigDecimal(preCountStr);
        sumSub = sourceBig.divide(preBig).setScale(scale, roundHalf);
        return sumSub;
    }

    // 除
    public static BigDecimal divide(Long sourceCount, BigDecimal preCount, int scale, int roundHalf) {
        BigDecimal sumSub = BigDecimal.valueOf(0);
        String sourceCountStr = String.valueOf(sourceCount);
        BigDecimal sourceBig = new BigDecimal(sourceCountStr);
        sumSub = sourceBig.divide(preCount).setScale(scale, roundHalf);
        return sumSub;
    }

//    public static void main(String[] args) {
//        Long a = 11L;
//        Long b = 5L;
//        System.out.println(add(a,b));
//        System.out.println(subtract(a,b));
//        System.out.println(multiply(a,b));
//        System.out.println(divide(a,b,4));
//    }
}
