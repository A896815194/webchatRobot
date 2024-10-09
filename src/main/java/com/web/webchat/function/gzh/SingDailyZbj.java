package com.web.webchat.function.gzh;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.web.webchat.config.PropertiesEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component("singDailyZbj")
public class SingDailyZbj {

    @Autowired
    private PropertiesEntity propertiesEntity;
    // 查询直播状态url
    public static final String DOU_YIN_CAST_STATE_URL = "https://live.douyin.com/webcast/room/web/enter/?aid=6383&app_name=douyin_web&device_platform=web&language=zh-CN&enter_from=page_refresh&cookie_enabled=true&screen_width=1536&screen_height=864&browser_language=zh-CN&browser_platform=Win32&browser_name=Edge&browser_version=120.0.0.0&web_rid=%s&enter_source=&is_need_double_stream=false";
    // 抖音直播首页
    public static final String DOU_YIN_CAST_INDEX_URL = "https://live.douyin.com";

    private static final Logger logger = LogManager.getLogger(SingDailyZbj.class.getName());

    //private static final Map<String, Boolean> chatroomStateMap = new ConcurrentHashMap<>();

    private static final Map<String, Process> chatroomProccessMap = new ConcurrentHashMap<>();

    private static final ExecutorService executor = Executors.newFixedThreadPool(1);

    //监控开启
    public String miniorOpen(String content) {
        logger.info("开启监控:content{}", content);
        // 主播id
        String castUserId = propertiesEntity.getZbId();
        String liveId = propertiesEntity.getLiveId();
        // 先判断主播是否开播,开播了才能监控
        boolean castOpen = miniorCastState(castUserId);
        if (!castOpen) {
            //chatroomStateMap.put(liveId, false);
            return "主播现在没有开播,不能开启监控直播间弹幕";
        }
        //chatroomStateMap.put(liveId, true);
        executor.submit(() -> {
            pythonProccess(chatroomProccessMap, propertiesEntity.getPythonScriptPath(), "python", liveId, propertiesEntity.getManagerDouYIds(), propertiesEntity.getNotifyUrl(), propertiesEntity.getSignJsPath(), propertiesEntity.nodeModulesPath, propertiesEntity.getCastLogPath());
        });
        return "操作完成";
    }


    //监控关闭
    public String miniorClose(String content) {
        logger.info("开启监控:content{}", content);
        // 主播id
        String castUserId = propertiesEntity.getZbId();
        String liveId = propertiesEntity.getLiveId();
        if (!chatroomProccessMap.containsKey(liveId)) {
            return "操作完成";
        }
        if (chatroomProccessMap.get(liveId) != null) {
            logger.info("当前任务执行状态：" + chatroomProccessMap.get(liveId).isAlive());
            chatroomProccessMap.get(liveId).destroy();
            chatroomProccessMap.remove(liveId);
            return "当前监控在进行中,关闭完成";
        }
        return "操作完成";
    }


    public static boolean miniorCastState(String userId) {
        // 发起GET请求并设置请求头
        HttpResponse response1 = HttpRequest.get(DOU_YIN_CAST_INDEX_URL)
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36")
                .header("cookie ", "__ac_nonce=0638733a400869171be51")
                .execute();
        // 获取返回的cookie 一定要调用一下，否则这次请求没有cookie 调用下面的接口拿不到信息
        //HttpCookie cookieValue = response1.getCookie("ttwid");
        // 调其他接口可能用的到
        //String ttwid = cookieValue.getValue();
        // 用户抖音id
        String url = String.format(DOU_YIN_CAST_STATE_URL, userId);
        HttpResponse response = HttpRequest.get(url)
                .execute();
        String content = response.body();
        logger.info("接口返回：" + content);
        JSONObject result = JSONUtil.toBean(content, JSONObject.class);
        Map<String, Object> map = new HashMap<>();
        if (result.containsKey("status_code") && Objects.equals(0, result.getInt("status_code"))) {
            JSONObject data = result.getJSONObject("data").getJSONArray("data").getJSONObject(0);
            map.put("state", data.getInt("status"));
            map.put("data", data);
            map.put("roomId", result.getJSONObject("data").getStr("enter_room_id"));
            map.put("user", result.getJSONObject("data").getJSONObject("user"));
            if (Objects.equals(2, map.get("state"))) {
                logger.info(userId + "已经开播");
                return true;
            } else {
                logger.info(userId + "未开播");
                return false;
            }
        }
        logger.info("查询直播间状态出现异常了，报错");
        return false;

    }

//    public static void main(String[] args) {
//        String pythonScriptPath= "M:\\爬抖音"+File.separator+"DouyinLiveWebFetcher-main"+File.separator+"DouyinLiveWebFetcher-main"+File.separator+"minior.py";
//        String command = "python";
//        String liveId = "125853211144";
//        String managerDouYIds = "1,2";
//        String notifyUrl ="test";
//        String signJsPath ="M:\\爬抖音"+File.separator+"DouyinLiveWebFetcher-main"+File.separator+"DouyinLiveWebFetcher-main"+File.separator+"sign.js";
//        String nodeMoudlePath ="M:\\爬抖音"+File.separator+"DouyinLiveWebFetcher-main"+File.separator+"DouyinLiveWebFetcher-main"+File.separator+"node_modules";
//        String logPath = "M:\\爬抖音"+File.separator+"DouyinLiveWebFetcher-main"+File.separator+"DouyinLiveWebFetcher-main"+File.separator+"log";
//        pythonProccess(chatroomProccessMap,pythonScriptPath,command,liveId,managerDouYIds,notifyUrl,signJsPath,nodeMoudlePath,logPath);
//    }

    public static synchronized void pythonProccess(Map<String, Process> chatroomProccessMap, String pythonScriptPath, String command, String liveId, String managerDouYIds, String notifyUrl, String signJsPath, String nodeMoudlePath, String castLogPath) {
        try {
            // 如果当前直播间监控脚本，有且还存在,则不进行处理
            if (chatroomProccessMap.containsKey(liveId) && chatroomProccessMap.get(liveId) != null && chatroomProccessMap.get(liveId).isAlive()) {
                logger.info(liveId + "直播间,存在正在执行的监控任务");
                return;
            }
            if(chatroomProccessMap.containsKey(liveId) && chatroomProccessMap.get(liveId) != null && !chatroomProccessMap.get(liveId).isAlive()){
                chatroomProccessMap.remove(liveId);
            }
//            String filePath = castLogPath + File.separator +  DateUtil.format(new Date(), "yyyy-MM-dd_HH:mm:ss") + ".txt";
//            File file = new File(filePath);
//            if (!file.exists()) {
//                file.getParentFile().mkdirs(); // 创建父目录
//                file.createNewFile(); // 创建文件
//            }
            ProcessBuilder pb = new ProcessBuilder(command, pythonScriptPath, liveId, managerDouYIds, notifyUrl, signJsPath);
            Map<String, String> env = pb.environment();
            // 配置环境变量
            env.put("NODE_PATH", nodeMoudlePath);
            Process process = pb.start();
            logger.info(liveId + "直播间,开始执行的监控任务");
            chatroomProccessMap.put(liveId, process);
//            FileWriter writer = new FileWriter(filePath, true); // 设置为追加写入模式
//            不输出所有log
//            InputStream inputStream = process.getInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
//            String line;
//            while ((line = reader.readLine()) != null) {
//               // writer.write(line + "\n"); // 写入文件并换行
//                System.out.println(line + "\n");
//            }
            // 获取进程的错误流，用于读取Python脚本的错误信息
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "GBK"));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                logger.info(errorLine + "\n"); // 写入文件并换行
            }
            int exitCode = process.waitFor();
            process.destroy();
            chatroomProccessMap.remove(liveId);
            //           writer.close(); // 关闭写入流
            logger.info("Python script execution finished with exit code: " + exitCode);
        } catch (Exception e) {
            logger.error("执行python 异常", e);
        }
    }

    // 杀所有python进程，目前用不到
    public static void killPythonPID() {
        try {
            Process process = Runtime.getRuntime().exec("tasklist /FI \"IMAGENAME eq python.exe\"");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("python.exe")) {
                    String[] tokens = line.split("\\s+");
                    String pid = tokens[1];
                    Process killProcess = Runtime.getRuntime().exec("taskkill /F /PID " + pid);
                    killProcess.waitFor();
                    logger.info("Killed Python process with PID: " + pid);
                }
            }
            reader.close();
        } catch (Exception e) {
            logger.error("杀进程失败", e);
        }
    }


}
