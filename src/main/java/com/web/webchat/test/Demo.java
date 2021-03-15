package com.web.webchat.test;

import static java.lang.Thread.sleep;

public class Demo {
    public static void main(String[] args) {
//        SwitchControl sc = new SwitchControl();
//        Command functionCommand =  new FunctionSwitch(sc);
        //sc.openFunction("开启");
        //sc.closeFunction("关闭");
        long currentTime = System.currentTimeMillis();
        System.out.println(currentTime);
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long time = System.currentTimeMillis();
        System.out.println(time);
        System.out.println(time-currentTime);

    }
}
