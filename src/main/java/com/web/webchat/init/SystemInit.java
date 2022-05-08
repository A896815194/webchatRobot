package com.web.webchat.init;

import com.web.webchat.dto.RequestDto;
import com.web.webchat.entity.FunctionRoleCommand;
import com.web.webchat.entity.FunctionRoleEntity;
import com.web.webchat.entity.UserBagEntity;
import com.web.webchat.entity.UserThing;
import com.web.webchat.repository.FunctionRoleCommandRepository;
import com.web.webchat.repository.FunctionRoleRepository;
import com.web.webchat.repository.UserBagRepository;
import com.web.webchat.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class SystemInit {
    private static final Logger logger = LogManager.getLogger(SystemInit.class.getName());

    public static List<FunctionRoleEntity> functionRoleRole = new ArrayList<>();

    //托管开关
    public static boolean flag = false;

    @Autowired
    private FunctionRoleRepository functionRoleRepository;

    @Autowired
    private FunctionRoleCommandRepository functionRoleCommandRepository;
    @Autowired
    private UserBagRepository userBagRepository;
    @Value("${api.temp-data-file-path}")
    private String tempFileDataPath;

    public static Map<String, RequestDto> lastRequestMap = new HashMap<>();
    // 命令, 方法类型
    public static Map<String, String> commandFunctionType = new HashMap<>();
    // 命令，方法实体
    public static Map<String, List<FunctionRoleCommand>> commandByFunctionType = new HashMap<>();
    public static List<FunctionRoleCommand> functionRoleCommands = new ArrayList<>();

    private static final String CHINES_NUMER_REGE = "[\\u4e00-\\u9fa5].*[0-9].*";

    private static final String CHINES_REGE = "[\\u4e00-\\u9fa5]*";

    private static final String NUMER_REGE = "[0-9]+";

    //获取初始化功能列表
    @PostConstruct
    public void init() {
        logger.info("初始化信息。。");
        functionRoleRole = functionRoleRepository.findAllByIsOpen(1);
        functionRoleCommands = functionRoleCommandRepository.findAll();
        if (!CollectionUtils.isEmpty(functionRoleCommands)) {
            commandByFunctionType = functionRoleCommands.stream().collect(Collectors.groupingBy(FunctionRoleCommand::getCommand));
            commandFunctionType = functionRoleCommands.stream().collect(Collectors.toMap(FunctionRoleCommand::getCommand, FunctionRoleCommand::getFunctionType));
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void initTask() {
        logger.info("每天0点执行,time:{}", new Date());
        List<UserThing> uts = userBagRepository.getUserNoUseCountThings();
        if (CollectionUtils.isEmpty(uts)) {
            logger.info("没有查到要执行的物品特性");
            return;
        }
        List<String> ids = new ArrayList<>();
        for (UserThing ut : uts) {
            ids.add(ut.getThingId());
        }
        List<UserBagEntity> ubs = userBagRepository.findAllByEntityIdInAndEntityType(ids, "thing");
        logger.info("查到要执行的ubs size:{}", ubs.size());
        for (UserBagEntity ub : ubs) {
            ub.setUseCount(1);
        }
        try {
            userBagRepository.saveAll(ubs);
        } catch (Exception e) {
            logger.error("保存失败");
        }

    }


    public static String getFunctionTypeByMsg(String msg) {
        //是不是文字+数字
        if (isChineseAndNumber(msg)) {
            String command = getChinesString(msg);
            String number = getMsgNumber(msg);
            if (Strings.isNotBlank(number)) {
                command = command + "%s";
                logger.debug("通过命令:【{}】获得功能类型", command);
                return commandFunctionType.get(command);
            }
        }
        logger.debug("通过命令:【{}】获得功能类型", msg);
        return commandFunctionType.get(msg);
    }

    private static boolean isChineseAndNumber(String msg) {
        Pattern pattern = Pattern.compile(CHINES_NUMER_REGE);
        Matcher m = pattern.matcher(msg);
        //如果匹配到了
        if (m.find()) {
            return !Strings.isBlank(m.group());
        }
        return false;
    }

    private static String getChinesString(String msg) {
        Pattern pattern = Pattern.compile(CHINES_REGE);
        Matcher m = pattern.matcher(msg);
        if (m.find()) {
            return m.group();
        }
        return "";
    }

    public static String getMsgNumber(String msg) {
        Pattern pattern = Pattern.compile(NUMER_REGE);
        Matcher m = pattern.matcher(msg);
        //如果匹配到了
        if (m.find()) {
            return m.group();
        }
        return "";
    }


    public static List<FunctionRoleCommand> getCommandsByMsg(String msg) {
        if (isChineseAndNumber(msg)) {
            String command = getChinesString(msg);
            String number = getMsgNumber(msg);
            if (Strings.isNotBlank(number)) {
                command = command + "%s";
                return commandByFunctionType.get(command);
            }
        }
        return commandByFunctionType.get(msg);
    }

    @Scheduled(cron = "0 */10 * * * ?")
    public void clearTempData() {
        logger.info("清理临时目录。。path:{}", tempFileDataPath);
        FileUtil.delFolder(tempFileDataPath, false);
    }
}
