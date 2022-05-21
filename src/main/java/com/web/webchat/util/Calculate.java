package com.web.webchat.util;

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

}
