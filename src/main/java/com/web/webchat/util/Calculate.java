package com.web.webchat.util;

import java.util.Random;

public class Calculate {

    public static Integer randBewteewn(Integer start, Integer end) {
        Random random = new Random();
        return random.nextInt(end - start + 1) + start;
    }

    public static boolean fixedPercentage(Integer percent) {
        Integer randMumber = randBewteewn(1, 100);
        return randMumber <= percent;
    }

}
