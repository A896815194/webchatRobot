package com.web.webchat.api;

import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.util.ReadExcel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("api/v1")
@Slf4j
public class gameController {

    public final List<String> loginNames = new CopyOnWriteArrayList<>();
    // 用户名   id
    public final static Map<String, String> nameIdMap = new ConcurrentHashMap<>();

    public static final String TGW = "糖果屋";

    @Autowired
    private PropertiesEntity propertiesEntity;

    public static String getUserNameById(String userId){
        AtomicReference<String> id = new AtomicReference<>("");
        nameIdMap.forEach((k,v)->{
            if(Objects.equals(userId,v)){
                id.set(k);
            }
        });
        return id.get();
    }

    @PostConstruct
    public void initTGW() {
        String loginPath = createLoginPath(propertiesEntity);
        File f = new File(loginPath);
        if (!f.exists()) {
            log.info("糖果屋配置文件不存在，需要创建一下");
            ReadExcel.outFile(loginPath, "姓名= ");
            return;
        }
        // 存在将注册人获取出来
        List<String> result = ReadExcel.readFile(loginPath);
        List<String> hasUsernames = ReadExcel.getValuesByDhKey(result, "姓名");
        loginNames.addAll(hasUsernames);
        loginNames.forEach(name -> {
                    nameIdMap.putIfAbsent(name, Objects.toString(UUID.randomUUID()));
                }
        );
    }

    private void updateUserName(List<String> hasUsernames) {
        String loginPath = createLoginPath(propertiesEntity);
        if (!CollectionUtils.isEmpty(hasUsernames)) {
            ReadExcel.outFile(loginPath, "姓名=" + String.join(",", hasUsernames));
            log.info("写入成功");
        }
    }

    /**
     * islogin 是否登录了 游戏
     * success 登录接口调用成没成功 注册失败
     * hasUse  有没有占用 被注册了
     * name    名字
     **/

    @PostMapping("/login")
    public Object login(@RequestBody Map<String, Object> requestMap, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        try {
            String name = Objects.toString(requestMap.get("name"), "");
            log.info("name:{},点击登录按钮", name);
            boolean isjoinChat = WebSocket.isOnline(name);
            // 有没有登录
            result.put("islogin", isjoinChat);
            result.put("success", true);
            String id = Objects.toString(UUID.randomUUID());
            nameIdMap.putIfAbsent(name, id);
            result.put("userNameId", nameIdMap.get(name));
            if (!loginNames.contains(name)) {
                log.info("注册新用户");
                loginNames.add(name);
                result.put("name", name);
                // 名字没被占用
                //result.put("hasUse", false);
                updateUserName(loginNames);
                return result;
            }
            result.put("name", name);
            // 名字被占用了
            //result.put("hasUse", true);
            log.info("判断name是否登录，name:{},isjoin:{}", name, isjoinChat);
            if (!isjoinChat) {
                Cookie cookie = new Cookie("aliName", name);
                cookie.setPath("/");
                //设置Cookie存在最大时间【单位：天】
                cookie.setMaxAge(24 * 3600 * 365);
                //返回值中设置Cookie
//                response.setHeader("Access-Control-Allow-Credentials", "true");
//                response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
                response.addCookie(cookie);
                redirectAttributes.addFlashAttribute("id",id);
                response.sendRedirect("/ali/pt");
            }
            // 当前的用户都

            return result;
        } catch (Exception e) {
            log.error("写入失败", e);
            result.put("success", false);
        }
        return result;
    }


    @PostMapping("/loginform")
    public Object loginform(@RequestParam("name")String name, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        try {
            log.info("name:{},点击登录按钮", name);
            boolean isjoinChat = WebSocket.isOnline(name);
            // 有没有登录
            result.put("islogin", isjoinChat);
            result.put("success", true);
            String id = Objects.toString(UUID.randomUUID());
            nameIdMap.putIfAbsent(name, id);
            result.put("userNameId", nameIdMap.get(name));
            if (!loginNames.contains(name)) {
                log.info("注册新用户");
                loginNames.add(name);
                result.put("name", name);
                // 名字没被占用
                //result.put("hasUse", false);
                updateUserName(loginNames);
                if (!isjoinChat) {
                    response.sendRedirect("/ali/pt?id=" + nameIdMap.get(name));
                }
                return result;
            }
            result.put("name", name);
            // 名字被占用了
            //result.put("hasUse", true);
            log.info("判断name是否登录，name:{},isjoin:{}", name, isjoinChat);
            if (!isjoinChat) {
                response.sendRedirect("/ali/pt?id=" + nameIdMap.get(name));
            }
            // 当前的用户都

            return result;
        } catch (Exception e) {
            log.error("写入失败", e);
            result.put("success", false);
        }
        return result;
    }

    public static String createLoginPath(PropertiesEntity propertiesEntity) {
        return propertiesEntity.getAppFilePath() + "\\" + TGW + "\\" + TGW + ".txt";
    }

    @GetMapping("/login")
    public Object webChat(@RequestParam("name") String name) {
        Map<String, Object> result = new HashMap<>();
        result.put("name", name);
        return result;
    }




    @GetMapping("/checkGame")
    public Object checkGame(@RequestParam("name") String name) {
        Map<String, Object> result = new HashMap<>();
        result.put("isJoin", false);
        result.put("success", true);
        try {
            boolean isjoinChat = WebSocket.isOnline(name);
            result.put("isJoin", isjoinChat);
        } catch (Exception e) {
            log.error("查看游戏状态失败", e);
            result.put("success", false);
        }
        return result;
    }

    @GetMapping("/checkGameById")
    public Object checkGameById(@RequestParam("id") String id) {
        Map<String, Object> result = new HashMap<>();
        result.put("isJoin", false);
        result.put("success", true);
        try {
            String name = getUserNameById(id);
            if(StringUtils.isBlank(name)){
                log.error("没找到对应的人，id:{}",id);
                throw new RuntimeException("没有到对应的人");
            }
            boolean isjoinChat = WebSocket.isOnline(name);
            result.put("isJoin", isjoinChat);
            result.put("username",name);
        } catch (Exception e) {
            log.error("查看游戏状态失败", e);
            result.put("success", false);
        }
        return result;
    }

}
