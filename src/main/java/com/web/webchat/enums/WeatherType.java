package com.web.webchat.enums;

import java.util.Objects;

/**
 * 天气和奖惩枚举
 **/
public enum WeatherType {

    rain("雨", 1, "惩罚", "calssName", "methodName"),
    sun("晴", 2, "奖励", "calssName", "methodName"),
    wind("风", 1, "惩罚", "calssName", "methodName"),
    snow("雪", 1, "惩罚", "calssName", "methodName"),
    smog("雾霾", 1, "惩罚", "calssName", "methodName"),
    thunder("雷电", 1, "惩罚", "calssName", "methodName"),
    sandstorm("沙", 1, "惩罚", "calssName", "methodName"),
    ;


    private String name;

    private Integer type;

    private String typeName;

    private String className;

    private String methodName;

    WeatherType(String name, Integer type, String typeName, String className, String methodName) {
        this.name = name;
        this.type = type;
        this.typeName = typeName;
        this.className = className;
        this.methodName = methodName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

//    public static boolean isFuncationValue(String ml) {
//        for (WeatherType value : values()) {
//            if (Objects.equals(ml, value.getText())) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public static String getFunctionTypeByMl(String ml) {
//        for (WeatherType value : values()) {
//            if (Objects.equals(ml, value.getText())) {
//                return value.name();
//            }
//        }
//        return "";
//    }

}
