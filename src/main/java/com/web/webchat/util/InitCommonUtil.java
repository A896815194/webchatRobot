package com.web.webchat.util;

import com.web.webchat.entity.FunctionRoleCommand;
import com.web.webchat.init.SystemInit;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InitCommonUtil {

    private static final Logger logger = LogManager.getLogger(InitCommonUtil.class.getName());

    private static final String CHINES_NUMER_REGE = "[\\u4e00-\\u9fa5].*[0-9].*";

    private static final String CHINES_REGE = "[\\u4e00-\\u9fa5]*";

    private static final String NUMER_REGE = "[0-9]+";

    //通过 聊天内容判断是否是功能类型
    public static String getFunctionTypeByMsg(String msg) {
        //是不是文字+数字
        AtomicReference<String> msgFunctionType = new AtomicReference<>("");
//        if (isChineseAndNumber(msg)) {
//            String command = getChinesString(msg);
//            String number = getMsgNumber(msg);
//            if (Strings.isNotBlank(number)) {
//                command = command + "%s";
//                logger.debug("通过命令:【{}】获得功能类型", command);
//                msgFunctionType.set(SystemInit.commandFunctionType.get(command));
//                logger.debug("通过命令:【{}】获得功能类型:{}", msg, msgFunctionType);
//                return msgFunctionType.get();
//            }
//        }
        SystemInit.commandFunctionType.forEach((k, v) -> {
            if (k.endsWith("*")) {
                String command = StringUtils.substringBefore(k, "*");
                if (StringUtils.isNotBlank(command)) {
                    if (msg.startsWith(command)) {
                        msgFunctionType.set(v);
                    }
                }
            } else if (k.endsWith("%s")) {
                String command = StringUtils.substringBefore(k, "%s");
                if (StringUtils.isNotBlank(command)) {
                    if (msg.startsWith(command)) {
                        if (isChineseAndNumber(msg)) {
                            String number = getMsgNumber(msg);
                            if (Strings.isNotBlank(number)) {
                                logger.debug("通过命令:【{}】获得功能类型:{}", msg, v);
                                msgFunctionType.set(v);
                            }
                        }
                    }
                }
            }
        });
        if (StringUtils.isNotBlank(msgFunctionType.get())) {
            logger.debug("通过命令:【{}】获得功能类型:{}", msg, msgFunctionType);
            return msgFunctionType.get();
        }
        msgFunctionType.set(SystemInit.commandFunctionType.get(msg));
        logger.debug("通过命令:【{}】获得功能类型:{}", msg, msgFunctionType);
        return msgFunctionType.get();
    }


    // 判断是不是中文和数字的组合
    public static boolean isChineseAndNumber(String msg) {
        Pattern pattern = Pattern.compile(CHINES_NUMER_REGE);
        Matcher m = pattern.matcher(msg);
        //如果匹配到了
        if (m.find()) {
            return !Strings.isBlank(m.group());
        }
        return false;
    }

    // 获取文字中的中文
    public static String getChinesString(String msg) {
        Pattern pattern = Pattern.compile(CHINES_REGE);
        Matcher m = pattern.matcher(msg);
        if (m.find()) {
            return m.group();
        }
        return "";
    }

    // 获取文字中的数字
    public static String getMsgNumber(String msg) {
        Pattern pattern = Pattern.compile(NUMER_REGE);
        Matcher m = pattern.matcher(msg);
        //如果匹配到了
        if (m.find()) {
            return m.group();
        }
        return "";
    }

    // 通过内容得到命令集合
    public static List<FunctionRoleCommand> getCommandsByMsg(String msg) {
        List<FunctionRoleCommand> commands = new ArrayList<>();
        SystemInit.commandFunctionType.forEach((k, v) -> {
            if (k.endsWith("*")) {
                String command = StringUtils.substringBefore(k, "*");
                if (StringUtils.isNotBlank(command)) {
                    if (msg.startsWith(command)) {
                        commands.addAll(SystemInit.commandByFunctionType.get(k));
                    }
                }
            } else if (k.endsWith("%s")) {
                String command = StringUtils.substringBefore(k, "%s");
                if (StringUtils.isNotBlank(command)) {
                    if (msg.startsWith(command)) {
                        if (isChineseAndNumber(msg)) {
                            String number = getMsgNumber(msg);
                            if (Strings.isNotBlank(number)) {
                                logger.debug("通过命令:【{}】获得功能类型:{}", msg, v);
                                commands.addAll(SystemInit.commandByFunctionType.get(k));
                            }
                        }
                    }
                }
            }
        });
        if (CollectionUtils.isEmpty(commands)) {
            return SystemInit.commandByFunctionType.get(msg);
        }
        return commands;
    }

    // 计算艾特到的wxid集合
    public static List<String> getAtWxidsFromMsg(String msg) {
        List<String> wxids = new ArrayList<>();
        String[] str = StringUtils.split(msg, "]");
        for (String str1 : str) {
            String[] wxid = str1.split("wxid=");
            if (wxid.length > 1) {
                wxids.add(wxid[1]);
            }
        }
        return wxids;
    }
}
