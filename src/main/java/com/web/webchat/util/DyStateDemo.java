package com.web.webchat.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * @author 葡萄
 * @description 调用接口获取开播状态
 * @date 2024-10-8 17:18
 */
public class DyStateDemo {

    public static final String DOU_YIN_CAST_STATE_URL = "https://live.douyin.com/webcast/room/web/enter/?aid=6383&app_name=douyin_web&device_platform=web&language=zh-CN&enter_from=page_refresh&cookie_enabled=true&screen_width=1536&screen_height=864&browser_language=zh-CN&browser_platform=Win32&browser_name=Edge&browser_version=120.0.0.0&web_rid=%s&enter_source=&is_need_double_stream=false";

//    public static void main(String[] args) {
//        // 发起GET请求并设置请求头
//        HttpResponse response1 = HttpRequest.get("https://live.douyin.com")
//                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
//                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36")
//                .header("cookie ", "__ac_nonce=0638733a400869171be51")
//                .execute();
//        // 获取返回的cookie 一定要调用一下，否则这次请求没有cookie 调用下面的接口拿不到信息
//        HttpCookie cookieValue = response1.getCookie("ttwid");
//        // 调其他接口可能用的到
//        String ttwid = cookieValue.getValue();
//        // 用户抖音id
//        String userId = "Sy980317";
//        String url = String.format(DOU_YIN_CAST_STATE_URL, userId);
//        HttpResponse response = HttpRequest.get(url)
//                .execute();
//        String content = response.body();
//        System.out.println("接口返回：" + content);
//        JSONObject result = JSONUtil.toBean(content, JSONObject.class);
//        Map<String, Object> map = new HashMap<>();
//        if (result.containsKey("status_code") && Objects.equals(0, result.getInt("status_code"))) {
//            JSONObject data = result.getJSONObject("data").getJSONArray("data").getJSONObject(0);
//            map.put("state", data.getInt("status"));
//            map.put("data", data);
//            map.put("roomId", result.getJSONObject("data").getStr("enter_room_id"));
//            map.put("user", result.getJSONObject("data").getJSONObject("user"));
//            if (Objects.equals(2, map.get("state"))) {
//                System.out.println("已经开播");
//            } else {
//                System.out.println("未开播");
//            }
//        } else {
//            System.out.println("出现异常了，报错");
//        }
//
//    }

    // 调别的接口可能用得到
    public static String generateMsToken(int length) {
        StringBuilder randomStr = new StringBuilder();
        String baseStr = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789=_";
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(baseStr.length());
            randomStr.append(baseStr.charAt(index));
        }

        return randomStr.toString();
    }

    public static void main(String[] args) {
        try {
            // 指定Python解释器路径和要执行的Python脚本文件路径
            String pythonPath = "python";
            String scriptPath = "src/main/resources/python/main.py";

            // 构建命令行参数
            String[] command = new String[]{pythonPath, "-u", scriptPath};

            // 创建进程并执行Python脚本
            Process process = Runtime.getRuntime().exec(command);

            // 获取进程的输入流，用于读取Python脚本的输出信息
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 获取进程的错误流，用于读取Python脚本的错误信息
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.err.println(errorLine);
            }

            // 等待进程执行结束
            process.waitFor();

            // 获取进程的退出状态
            int exitCode = process.exitValue();
            System.out.println("Python脚本执行完毕，退出状态码：" + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}