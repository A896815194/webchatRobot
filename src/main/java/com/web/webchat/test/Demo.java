package com.web.webchat.test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Demo {
    //    public static void main(String[] args) {
////        SwitchControl sc = new SwitchControl();
////        Command functionCommand =  new FunctionSwitch(sc);
//        //sc.openFunction("开启");
//        //sc.closeFunction("关闭");
//        long currentTime = System.currentTimeMillis();
//        System.out.println(currentTime);
//        try {
//            sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        long time = System.currentTimeMillis();
//        System.out.println(time);
//        System.out.println(time-currentTime);
//
//    }
//public static void main(String[] args) throws Exception {
//    String[] test =  com.alibaba.druid.filter.config.ConfigTools.genKeyPair(600);
//    System.out.println("priKey:"+test[0]);
//    System.out.println("pubKey:"+test[1]);
//    System.out.println("password:"+com.alibaba.druid.filter.config.ConfigTools.encrypt(test[0],"Sjk@2021!)!!"));
//}
    public static void main(String[] args) {
        String json = "[{\n" +
                "\t\t\t\"代理人\": [\"孙律\"],\n" +
                "\t\t\t\"住址\": [\"大连\"],\n" +
                "\t\t\t\"出生日期\": [\"2000年11月08日\"],\n" +
                "\t\t\t\"姓名\": [\"罗伊\"],\n" +
                "\t\t\t\"当事人名称\": \"罗伊\",\n" +
                "\t\t\t\"当事人类型\": \"自然人\",\n" +
                "\t\t\t\"性别\": [\"男性\"],\n" +
                "\t\t\t\"民族\": [\"汉族\"],\n" +
                "\t\t\t\"联系方式\": {\n" +
                "\t\t\t\t\"移动电话\": [\"18500000000\"]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"证件\": {\n" +
                "\t\t\t\t\"证件号码\": [\"500106200011089898\"]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"诉讼身份\": [\"原告\"]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"姓名\": [\"孙律\"],\n" +
                "\t\t\t\"当事人名称\": \"孙律\",\n" +
                "\t\t\t\"当事人类型\": \"自然人\",\n" +
                "\t\t\t\"联系方式\": {\n" +
                "\t\t\t\t\"移动电话\": [\"15632895633\"]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"证件\": {\n" +
                "\t\t\t\t\"执业证号\": [\"20211213\", \"20211213\"],\n" +
                "\t\t\t\t\"证件号码\": [\"611023199704150120\", \"611023199704150120\"]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"诉讼身份\": [\"代理人\"]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"姓名\": [\"鲸与海\"],\n" +
                "\t\t\t\"当事人名称\": \"鲸与海\",\n" +
                "\t\t\t\"当事人类型\": \"自然人\",\n" +
                "\t\t\t\"联系方式\": {\n" +
                "\t\t\t\t\"移动电话\": [\"15632895633\"]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"证件\": {\n" +
                "\t\t\t\t\"执业证号\": [\"20211213\", \"20211213\"],\n" +
                "\t\t\t\t\"证件号码\": [\"611023199704150120\", \"611023199704150120\"]\n" +
                "\t\t\t}\n" +
                "\t\t}, {\n" +
                "\t\t\t\"代理人\": [\"布谷布谷\"],\n" +
                "\t\t\t\"代表人\": [\"谢媛\"],\n" +
                "\t\t\t\"住址\": [\"河北省秦皇岛市北戴河区234\"],\n" +
                "\t\t\t\"单位\": [\"开元饭店\"],\n" +
                "\t\t\t\"当事人名称\": \"开元饭店\",\n" +
                "\t\t\t\"当事人类型\": \"法人\",\n" +
                "\t\t\t\"诉讼身份\": [\"原告\"]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"姓名\": [\"谢媛\"],\n" +
                "\t\t\t\"当事人名称\": \"谢媛\",\n" +
                "\t\t\t\"当事人类型\": \"自然人\",\n" +
                "\t\t\t\"联系方式\": {\n" +
                "\t\t\t\t\"移动电话\": [\"15104150715\"]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"证件\": {\n" +
                "\t\t\t\t\"身份证\": [\"500381199607151002\"]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"证件类型\": [\"统一社会信用代码\"],\n" +
                "\t\t\t\"诉讼身份\": [\"代表人\"]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"姓名\": [\"布谷布谷\"],\n" +
                "\t\t\t\"当事人名称\": \"布谷布谷\",\n" +
                "\t\t\t\"当事人类型\": \"自然人\",\n" +
                "\t\t\t\"职业\": [\"律师\"],\n" +
                "\t\t\t\"联系方式\": {\n" +
                "\t\t\t\t\"移动电话\": [\"15120220921\"]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"证件\": {\n" +
                "\t\t\t\t\"执业证号\": [\"20220921\"],\n" +
                "\t\t\t\t\"证件号码\": [\"445101199108030105\"]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"诉讼身份\": [\"代理人\"]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"住址\": [\"山西省长治市潞城市34\"],\n" +
                "\t\t\t\"单位\": [\"虾球旅馆\"],\n" +
                "\t\t\t\"当事人名称\": \"虾球旅馆\",\n" +
                "\t\t\t\"当事人类型\": \"法人\",\n" +
                "\t\t\t\"诉讼身份\": [\"原告\"],\n" +
                "\t\t\t\"负责人\": [\"庞楷\"]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"姓名\": [\"庞楷\"],\n" +
                "\t\t\t\"当事人名称\": \"庞楷\",\n" +
                "\t\t\t\"当事人类型\": \"自然人\",\n" +
                "\t\t\t\"联系方式\": {\n" +
                "\t\t\t\t\"移动电话\": [\"13956820046\"]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"证件\": {\n" +
                "\t\t\t\t\"组织代码\": [\"L9211125-3\"],\n" +
                "\t\t\t\t\"身份证\": [\"500381199204152115\"]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"诉讼身份\": [\"负责人\"]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"住址\": [\"河北省秦皇岛市北戴河区345\"],\n" +
                "\t\t\t\"出生日期\": [\"1990年06月10日\"],\n" +
                "\t\t\t\"姓名\": [\"乌桕\"],\n" +
                "\t\t\t\"当事人名称\": \"乌桕\",\n" +
                "\t\t\t\"当事人类型\": \"自然人\",\n" +
                "\t\t\t\"性别\": [\"男性\"],\n" +
                "\t\t\t\"民族\": [\"汉族\"],\n" +
                "\t\t\t\"职业\": [\"职业\"],\n" +
                "\t\t\t\"职务\": [\"工人\"],\n" +
                "\t\t\t\"联系方式\": {\n" +
                "\t\t\t\t\"移动电话\": [\"15144596328\"]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"证件\": {\n" +
                "\t\t\t\t\"证件号码\": [\"522222199006100111\"]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"诉讼身份\": [\"被告\"]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"代表人\": [\"贝佳\"],\n" +
                "\t\t\t\"住址\": [\"天津市南开区345\"],\n" +
                "\t\t\t\"姓名\": [\"被倍加\"],\n" +
                "\t\t\t\"当事人名称\": \"被倍加\",\n" +
                "\t\t\t\"当事人类型\": \"自然人\",\n" +
                "\t\t\t\"诉讼身份\": [\"被告\"]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"姓名\": [\"贝佳\"],\n" +
                "\t\t\t\"当事人名称\": \"贝佳\",\n" +
                "\t\t\t\"当事人类型\": \"自然人\",\n" +
                "\t\t\t\"证件\": {\n" +
                "\t\t\t\t\"统一社会信用代码\": [\"99854868MA2NOABGXH\"],\n" +
                "\t\t\t\t\"身份证\": [\"441501199207300164\"]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"证件号码\": [\"18630225486\"],\n" +
                "\t\t\t\"诉讼身份\": [\"代表人\"]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"住址\": [\"河北省秦皇岛市请选择区23\"],\n" +
                "\t\t\t\"姓名\": [\"诗歌湾\"],\n" +
                "\t\t\t\"当事人名称\": \"诗歌湾\",\n" +
                "\t\t\t\"当事人类型\": \"自然人\",\n" +
                "\t\t\t\"诉讼身份\": [\"被告\"],\n" +
                "\t\t\t\"负责人\": [\"时诗\"]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"姓名\": [\"时诗\"],\n" +
                "\t\t\t\"当事人名称\": \"时诗\",\n" +
                "\t\t\t\"当事人类型\": \"自然人\",\n" +
                "\t\t\t\"联系方式\": {\n" +
                "\t\t\t\t\"移动电话\": [\"15164899852\"]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"证件\": {\n" +
                "\t\t\t\t\"身份证\": [\"520102199207170068\"]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"证件类型\": [\"统一社会信用代码\"],\n" +
                "\t\t\t\"诉讼身份\": [\"负责人\"]\n" +
                "\t\t}]";
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(json, JsonArray.class);
        System.out.println(jsonArray);

        System.out.println(getList(DlrVo.class, jsonArray));

    }

    public static <T> List<T> getList(Class<T> classzz, JsonArray arr) {
        Field[] fields1 = classzz.getDeclaredFields();
        List<AnnotationField> fiedLists = new ArrayList<>();
        List result = new ArrayList();
        for (Field field : fields1) {
            Convert convert = field.getDeclaredAnnotation(Convert.class);
            if (!Objects.isNull(convert)) {
                AnnotationField annotationField = AnnotationField.builder()
                        .annotation(convert)
                        .fieldType(field.getType())
                        .field(field.getName()).build();
                fiedLists.add(annotationField);
            }
        }
        System.out.println(fiedLists);
        arr.forEach(item -> {
            fiedLists.forEach(fiedPojo -> {
                Convert convert = (Convert) fiedPojo.getAnnotation();
                DsrSbConstant.PerSonInfo key = convert.KEY();
                DsrSbConstant.PerSonInfoType type = convert.TYPE();
                DsrSbConstant.PerSonInfoValueNum keyNum = convert.KEYNUM();
                DsrSbConstant.PerSonInfo2KEY key2 = convert.KEY2();
                DsrSbConstant.PerSonInfo2Type type2 = convert.TYPE2();
                if (item.getAsJsonObject().has(key.getDesc())) {
                    JsonElement jsonElement = item.getAsJsonObject().get(key.getDesc());
                    if ("STRING".equals(type.name())) {
                        System.out.println(jsonElement.toString().replaceAll("\"([^\"]*)\"", "$1"));
                        fiedPojo.setValue(jsonElement.toString().replaceAll("\"([^\"]*)\"", "$1"));
                        fiedPojo.setFieldType(String.class);
                        fiedPojo.setValueNum(keyNum.getType());
                    }
                    if ("ARR".equals(type.name())) {
                        Gson gson = new Gson();
                        String[] arrs = gson.fromJson(jsonElement, String[].class);
                        List<String> arrList = Arrays.asList(arrs);
                        arrList.subList(0,keyNum.getType());
                        fiedPojo.setValue(String.join("、",arrList));
                        fiedPojo.setFieldType(String.class);
                        fiedPojo.setValueNum(keyNum.getType());
                    }
                    if ("JSONOBJECT".equals(type.name())) {
                        System.out.println(jsonElement);
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(jsonElement, JsonObject.class);
                        fiedPojo.setValue(jsonObject);
                        fiedPojo.setFieldType(JsonArray.class);
                        fiedPojo.setValueNum(keyNum.getType());
                    }
                }
            });
        });
        fiedLists.forEach(item->{
            try {
                Object object = classzz.newInstance();
                Field[] fields = object.getClass().getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    //成员变量为private,故必须进行此操作
                    //以通过反射获取私有变量的值，在访问时会忽略访问修饰符的检查
                    if (fields[i].getName().equals(item.getField())) {
                        System.out.println(fields[i].getName());
                        fields[i].setAccessible(true);
                        if (item.getFieldType().equals(String.class)){
                            fields[i].set(object, (String) item.getValue());
                        }
                        if (item.getFieldType().equals(JsonArray.class)){
                            //fields[i].set(object, (JsonArray) item.getValue());
                            fields[i].set(object, String.valueOf(item.getValue()));
                        }
                        result.add((DlrVo)object);
                    }
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return result;
    }


}
