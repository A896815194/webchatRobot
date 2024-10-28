package com.web.webchat.api;

import com.google.gson.Gson;
import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.dto.WxBaseDto.HfContentResponseDto;
import com.web.webchat.dto.WxBaseDto.WxRequestDto;
import com.web.webchat.enums.gzh.WeChatConstat;
import com.web.webchat.function.gzh.SingDailyZbj;
import com.web.webchat.init.SystemInit;
import com.web.webchat.util.ReflectionService;
import com.web.webchat.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.web.webchat.util.XmlUtil.DtoToXmlString;

@RestController
@RequestMapping("webChat")
@Slf4j
public class GzhController {

    private static final Logger logger = LogManager.getLogger(GzhController.class.getName());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PropertiesEntity propertiesEntity;

    @GetMapping("fetch/config")
    public Map<String, Object> wxauth() {
        logger.info("调用获取配置");
        Map<String, Object> result = new HashMap<>();
        result.put("live_id", propertiesEntity.getLiveId());
        result.put("zb_id", propertiesEntity.getZbId());
        String[] strArray = propertiesEntity.getManagerDouYIds().split(",");
        long[] intArray = new long[strArray.length];
        for (int i = 0; i < strArray.length; i++) {
            intArray[i] = Long.valueOf(strArray[i]);
        }
        result.put("manager_user", intArray);
        result.put("notify_url", propertiesEntity.getNotifyUrl());
        result.put("signjs_url", propertiesEntity.getSignJsPath());
        logger.info("调用获取配置成功:" + result);
        return result;
    }


    @PostMapping("gzh/notify")
    public Map<String, Object> wxauth(@RequestBody Map<String, Object> param) {
        logger.info("receive notify param:{}", param);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        // 歌曲记录
        if (param.containsKey("type") && Objects.equals(param.get("type"), WeChatConstat.COMMAND_TYPE_GQ)) {
            if (param.containsKey("content") && !Objects.isNull(param.get("content"))) {
                String content = (String) param.get("content");
                // 如果是以歌曲-开头的
                if (content.startsWith(WeChatConstat.COMMAND_DY_GQ)) {
                    String songName = content.split(WeChatConstat.COMMAND_DY_GQ)[1];
                    String command = WeChatConstat.COMMAND_SING_DAILY + WeChatConstat.COMMAND_SPLIT_JIA + songName;
                    AtomicReference<String> data = new AtomicReference("");
                    String resultData = commandHandle(command, data, WeChatConstat.COMMAND_SING_DAILY);
                    if (StringUtils.isBlank(resultData) || !resultData.contains("成功")) {
                        result.put("success", false);
                    } else {
                        result.put("success", true);
                    }
                }
            }
        }
        // 自动续接监控
        if (param.containsKey("type") && Objects.equals(param.get("type"), WeChatConstat.COMMAND_TYPE_JK_FAIL)) {
            AtomicReference<String> data = new AtomicReference("");
            SingDailyZbj.chatroomProccessMap.put(propertiesEntity.getLiveId(), false);
            // python 后台服务 一个进程a  ，调用接口时 执行了监听直播间 是  进程a的子进程b
            // 如果失败了，有可能是b子进程没结束呢，然后调过来了，所以得杀掉非服务的进程,不杀掉再调用python服务时发现多余1个进程不会再调用
            SingDailyZbj.killPythonPID(propertiesEntity.getLiveId());
            // 杀掉除了python本身保护进程，然后重新拉起一个新的，
            String resultData = commandHandle(WeChatConstat.COMMAND_SING_DAILY_OPEN, data, WeChatConstat.COMMAND_AUTO_SING_DAILY);
            if (StringUtils.isBlank(resultData) || !resultData.contains("完成")) {
                result.put("success", false);
            } else {
                result.put("success", true);
            }
        }
        return result;
    }

    private String commandHandle(String content, AtomicReference<String> result, String commandBeanKey) {
        TransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(definition);// 获得事务状态
        try {
            String beanName = WeChatConstat.getBeanNameByKey(commandBeanKey);
            String command = getCommandString(content);
            String methodName = getMethodString(content);
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("content", content.replace(command + WeChatConstat.COMMAND_SPLIT_JIA, "").trim());
            try {
                Object data = reflectionService.invokeService(beanName, methodName, paramMap);
                String resultData = (String) data;
                if (StringUtils.isBlank(resultData)) {
                    throw new RuntimeException("异常");
                }
                log.info("返回结果：{}", resultData);
                result.set("操作成功");
                transactionManager.commit(status);
                return resultData;
            } catch (Exception e) {
                logger.error("反射异常", e);
                result.set("操作失败");
                throw new RuntimeException("操作异常");
            }
        } catch (Exception e) {
            transactionManager.rollback(status);
            logger.error("操作失败", e);
            result.set("操作失败");
        }
        return result.get();
    }

    @GetMapping("wx/auth")
    public String wxauth(@RequestParam("signature") String signature, @RequestParam("timestamp") String timestamp, @RequestParam("nonce") String nonce, @RequestParam("echostr") String echostr) {
        logger.info("signature:{},timestamp:{},nonce:{},echostr:{}", signature, timestamp, nonce, echostr);
        String token = "jstest";
        List<String> strings = new ArrayList<>();
        strings.add(token);
        strings.add(timestamp);
        strings.add(nonce);
        List<String> newString = strings.stream().sorted().collect(Collectors.toList());
        String string = newString.get(0) + newString.get(1) + newString.get(2);
        String sign = DigestUtils.shaHex(string);
        logger.info("sign:{}", sign);
        if (Objects.equals(sign, signature)) {
            return echostr;
        }
        return "";
    }


    @Autowired
    private ReflectionService reflectionService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @PostMapping("wx/auth")
    public String wxauth(@RequestBody String xmlString) {
        logger.info("公众号内容:" + xmlString);
        WxRequestDto request = null;
        try {
            Map<String, String> testmap = XmlUtil.xmlToMap(xmlString);
            Gson gson = new Gson();
            request = gson.fromJson(gson.toJson(testmap), WxRequestDto.class);
            logger.info("转换实体后:" + request);
        } catch (Exception e) {
            log.error("转换失败", e);
        }
        String content = request.getContent();
        String fromWx = request.getFromUserName();
        String gzh = request.getToUserName();
        if (StringUtils.isNotBlank(request.getMsgType()) && request.getMsgType().contains("event") && request.getEvent().contains("subscribe")) {
            // 关注
            HfContentResponseDto dto = new HfContentResponseDto();
            dto.setToUserName(fromWx);
            dto.setFromUserName(gzh);
            dto.setMsgType("text");
            dto.setContent("欢迎来到萝卜地！\n可以输入【帮助】查看命令指引");
            String result = DtoToXmlString(dto);
            log.info("返回结果：{}", result);
            return result;
        }
        if (StringUtils.isNotBlank(request.getMsgType()) && request.getMsgType().contains("text")) {
            if (Objects.equals(content, "帮助")) {
                HfContentResponseDto dto = new HfContentResponseDto();
                dto.setToUserName(fromWx);
                dto.setFromUserName(gzh);
                dto.setMsgType("text");
                dto.setContent(WeChatConstat.HELP_NEW);
                String result = DtoToXmlString(dto);
                log.info("返回结果：{}", result);
                return result;
            }
        }
        if (StringUtils.isNotBlank(request.getMsgType()) && request.getMsgType().contains("text")) {
            AtomicReference<String> result = new AtomicReference("");
            // 输入PID+  就是直接追加，如果没有就加，有就不做处理   应对可能有多个人要启动这个python脚本
            if (content.startsWith(WeChatConstat.COMMAND_ADD_PID)) {
                logger.info("添加pid:" + content);
                String resultContent = "";
                String pid = content.substring(4);
                resultContent = handlePid(resultContent, pid);
                HfContentResponseDto dto = new HfContentResponseDto();
                dto.setToUserName(fromWx);
                dto.setFromUserName(gzh);
                dto.setMsgType("text");
                dto.setContent(resultContent);
                return DtoToXmlString(dto);
            }
            // 输入PID-  直接替换PID 应对python服务坏了,重启python服务了，这样java不用重启
            if (content.startsWith(WeChatConstat.COMMAND_COVER_PID)) {
                logger.info("重置pid:" + content);
                String resultContent = "";
                String pid = content.substring(4);;
                SystemInit.pythonPID.clear();
                SystemInit.pythonPID.add(pid);
                resultContent = "重置成功,目前不被杀掉的PID是:" + pid;
                logger.info(resultContent);
                HfContentResponseDto dto = new HfContentResponseDto();
                dto.setToUserName(fromWx);
                dto.setFromUserName(gzh);
                dto.setMsgType("text");
                dto.setContent(resultContent);
                return DtoToXmlString(dto);
            }
            // 查pid
            if (content.startsWith(WeChatConstat.COMMAND_SEARCH_PID)) {
                HfContentResponseDto dto = new HfContentResponseDto();
                dto.setToUserName(fromWx);
                dto.setFromUserName(gzh);
                dto.setMsgType("text");
                dto.setContent(SystemInit.pythonPID.toString());
                return DtoToXmlString(dto);
            }
            // 如果是抽卡有关的命令
            if (StringUtils.isNotBlank(content) && WeChatConstat.COMMAND_CARD_LIST.stream().anyMatch(content::startsWith)) {
                return commandHandle(content, fromWx, gzh, result, WeChatConstat.COMMAND_SAVE_CARD);
            }
            // 每日歌单命令
            if (StringUtils.isNotBlank(content) && WeChatConstat.COMMAND_SING_DAILY_LIST.stream().anyMatch(content::startsWith)) {
                // 如果是歌单的命令
                return commandHandle(content, fromWx, gzh, result, WeChatConstat.COMMAND_SING_DAILY);
            }
            // 直播间监控
            if (StringUtils.isNotBlank(content) && WeChatConstat.COMMAND_SING_DAILY_ZBJ_LIST.stream().anyMatch(content::startsWith)) {
                // 监控直播间
                return commandHandle(content, fromWx, gzh, result, WeChatConstat.COMMAND_AUTO_SING_DAILY);
            }
            // 弹幕
            if (StringUtils.isNotBlank(content) && WeChatConstat.COMMAND_DANMU_LIST.stream().anyMatch(content::startsWith)) {
                // 监控直播间
                return commandHandle(content, fromWx, gzh, result, WeChatConstat.COMMAND_DANMU);
            }
        }
        HfContentResponseDto dto = new HfContentResponseDto();
        dto.setToUserName(fromWx);
        dto.setFromUserName(gzh);
        dto.setMsgType("text");
        dto.setContent("");
        return DtoToXmlString(dto);
    }

    private String handlePid(String resultContent, String pid) {
        if (CollectionUtils.isEmpty(SystemInit.pythonPID)) {
            SystemInit.pythonPID.add(pid);
            resultContent = "PID" + pid + "追加成功";
            logger.info(resultContent);
            return resultContent;
        }
        if (!CollectionUtils.isEmpty(SystemInit.pythonPID) && !SystemInit.pythonPID.contains(pid)) {
            SystemInit.pythonPID.add(pid);
            resultContent = "PID" + pid + "追加成功";
            logger.info(resultContent);
            return resultContent;
        }
        if (!CollectionUtils.isEmpty(SystemInit.pythonPID) && SystemInit.pythonPID.contains(pid)) {
            resultContent = "PID" + pid + "已经存在不追加";
            logger.info(resultContent);
        }
        return resultContent;
    }

    private String commandHandle(String content, String fromWx, String gzh, AtomicReference<String> result, String commandBeanKey) {
        TransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(definition);// 获得事务状态
        try {
            String beanName = WeChatConstat.getBeanNameByKey(commandBeanKey);
            String command = getCommandString(content);
            String methodName = getMethodString(content);
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("content", content.replace(command + WeChatConstat.COMMAND_SPLIT_JIA, "").trim());
            try {
                Object data = reflectionService.invokeService(beanName, methodName, paramMap);
                String resultData = (String) data;
                if (StringUtils.isBlank(resultData)) {
                    throw new RuntimeException("异常");
                }
                HfContentResponseDto dto = new HfContentResponseDto();
                dto.setToUserName(fromWx);
                dto.setFromUserName(gzh);
                dto.setMsgType("text");
                dto.setContent(resultData);
                result.set(DtoToXmlString(dto));
                log.info("返回结果：{}", result);
            } catch (Exception e) {
                logger.error("反射异常", e);
                result.set("操作失败");
                throw new RuntimeException("操作异常");
            }
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            logger.error("操作失败", e);
            result.set("操作失败");
        }
        return result.get();
    }

    private String getCommandString(String content) {
        if (content.startsWith(WeChatConstat.COMMAND_SAVE_CARD + WeChatConstat.COMMAND_SPLIT_JIA)) {
            return WeChatConstat.COMMAND_SAVE_CARD;
        }
        if (content.startsWith(WeChatConstat.COMMAND_USE_CARD + WeChatConstat.COMMAND_SPLIT_JIA)) {
            return WeChatConstat.COMMAND_USE_CARD;
        }
        if (Objects.equals(content, WeChatConstat.COMMAND_SEARCH_CARD)) {
            return WeChatConstat.COMMAND_SEARCH_CARD;
        }
        if (content.startsWith(WeChatConstat.COMMAND_SING_DAILY + WeChatConstat.COMMAND_SPLIT_JIA)) {
            return WeChatConstat.COMMAND_SING_DAILY;
        }
        if (content.startsWith(WeChatConstat.COMMAND_SEARCH_SING_DAILY + WeChatConstat.COMMAND_SPLIT_JIA)) {
            return WeChatConstat.COMMAND_SEARCH_SING_DAILY;
        }
        if (Objects.equals(content, WeChatConstat.COMMAND_SEARCH_SING_DAILY_3)) {
            return WeChatConstat.COMMAND_SEARCH_SING_DAILY_3;
        }
        if (content.startsWith(WeChatConstat.COMMAND_DANMU + WeChatConstat.COMMAND_SPLIT_JIA)) {
            return WeChatConstat.COMMAND_DANMU;
        }
        return "";
    }

    private String getMethodString(String content) {
        if (content.startsWith(WeChatConstat.COMMAND_SAVE_CARD + WeChatConstat.COMMAND_SPLIT_JIA)) {
            return WeChatConstat.getBeanMethodByKey(WeChatConstat.COMMAND_SAVE_CARD);
        }
        if (content.startsWith(WeChatConstat.COMMAND_USE_CARD + WeChatConstat.COMMAND_SPLIT_JIA)) {
            return WeChatConstat.getBeanMethodByKey(WeChatConstat.COMMAND_USE_CARD);
        }
        if (Objects.equals(content, WeChatConstat.COMMAND_SEARCH_CARD)) {
            return WeChatConstat.getBeanMethodByKey(WeChatConstat.COMMAND_SEARCH_CARD);
        }
        if (content.startsWith(WeChatConstat.COMMAND_SING_DAILY + WeChatConstat.COMMAND_SPLIT_JIA)) {
            return WeChatConstat.getBeanMethodByKey(WeChatConstat.COMMAND_SING_DAILY);
        }
        if (content.startsWith(WeChatConstat.COMMAND_SEARCH_SING_DAILY + WeChatConstat.COMMAND_SPLIT_JIA)) {
            return WeChatConstat.getBeanMethodByKey(WeChatConstat.COMMAND_SEARCH_SING_DAILY);
        }
        if (content.startsWith(WeChatConstat.COMMAND_DANMU + WeChatConstat.COMMAND_SPLIT_JIA)) {
            return WeChatConstat.getBeanMethodByKey(WeChatConstat.COMMAND_DANMU);
        }
        return WeChatConstat.getBeanMethodByKey(content);

    }


}
