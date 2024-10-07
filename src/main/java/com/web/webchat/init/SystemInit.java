package com.web.webchat.init;

import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.entity.*;
import com.web.webchat.entity.gzh.CardUserGzhEntity;
import com.web.webchat.enums.ApiType;
import com.web.webchat.enums.FunctionType;
import com.web.webchat.enums.Message;
import com.web.webchat.repository.*;
import com.web.webchat.repository.gzh.CardUserGzhRepository;
import com.web.webchat.strategy.TuLingRobotMsg;
import com.web.webchat.util.FileUtil;
import com.web.webchat.util.GifUtil;
import com.web.webchat.util.RestTemplateUtil;
import com.web.webchat.util.WeChatUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;
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
    @Autowired
    private ChatroomMemberSignRepository chatroomMemberSignRepository;
    @Autowired
    private ChatroomMemberMoneyRepository chatroomMemberMoneyRepository;
    @Autowired
    private PropertiesEntity propertiesEntity;
    @Autowired
    private ThingRepository thingRepository;

    public static Map<String, RequestDto> lastRequestMap = new HashMap<>();
    // 命令, 方法类型
    public static Map<String, String> commandFunctionType = new HashMap<>();
    // 命令，方法实体
    public static Map<String, List<FunctionRoleCommand>> commandByFunctionType = new HashMap<>();

    public static List<FunctionRoleCommand> functionRoleCommands = new ArrayList<>();

    // 名称，物品 map
    public static Map<String, ThingEntity> nameThingMap = new HashMap<>();

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private TuLingRobotMsg robotMsg;

    //获取初始化功能列表
    @PostConstruct
    public void init() {
        logger.info("初始化开启了权限的列表");
        functionRoleRole = functionRoleRepository.findAll();
        functionRoleCommands = functionRoleCommandRepository.findAll();
        if (!CollectionUtils.isEmpty(functionRoleCommands)) {
            logger.info("初始化功能权限命令集合");
            commandByFunctionType = functionRoleCommands.stream().collect(Collectors.groupingBy(FunctionRoleCommand::getCommand));
            logger.info("初始化功能权限类型集合");
            commandFunctionType = functionRoleCommands.stream().collect(Collectors.toMap(FunctionRoleCommand::getCommand, FunctionRoleCommand::getFunctionType));
        }
        List<ThingEntity> thingNoAutoUse = thingRepository.findAllByAutoUseIs(0);
        logger.info("初始化物品名字映射");
        for (ThingEntity thing : thingNoAutoUse) {
            nameThingMap.put(thing.getThingName(), thing);
        }
    }

    //  @Scheduled(cron = "0 0 0 * * ?")
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


//    @Scheduled(cron = "0 */10 * * * ?")
//    public void clearTempData() {
//        logger.info("清理临时目录。。path:{}", tempFileDataPath);
//        FileUtil.delFolder(tempFileDataPath, false);
//    }

    //  @Scheduled(cron = "0 00 07 ? * *")
    //@Scheduled(cron = "*/5 * * * * ?")
    public void sendTianQi() {
        logger.info("每天7点执行");
        if (CollectionUtils.isEmpty(functionRoleRole)) {
            return;
        }
        //通过全开着的过滤掉得到开启了魔法天气的列表
        List<FunctionRoleEntity> magicWeatherRoomIds = functionRoleRole.stream().filter(item -> Objects.equals(item.getFunctionType(), FunctionType.MagicWeather.name()) && Objects.equals(item.getChatType(), "EventGroupMsg") && Objects.equals(item.getIsOpen(), 1)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(magicWeatherRoomIds)) {
            logger.info("没有开启魔法天气的群里");
            return;
        }
        //开启了魔法天气的群里ids
        List<String> chatroomIds = magicWeatherRoomIds.stream().map(FunctionRoleEntity::getChatroomId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(chatroomIds)) {
            return;
        }
        // 开启了魔法天气的群id 和群集合为了获取robotid
        Map<String, List<FunctionRoleEntity>> robotChatRooms = magicWeatherRoomIds.stream().collect(Collectors.groupingBy(FunctionRoleEntity::getChatroomId, Collectors.toList()));
        List<String> tianQi = robotMsg.getTuLingResponseMsg("大连天气");
        if (CollectionUtils.isEmpty(tianQi)) {
            return;
        }
        String tqMsg = getTianMsg(tianQi);
        if (Strings.isBlank(tqMsg)) {
            return;
        }
        // 通过开启魔法天气的群里id查到群签到列表
        List<ChatroomMemberSign> chatroomMemberSigns = chatroomMemberSignRepository.findAllByChatroomIdIn(chatroomIds);
        if (CollectionUtils.isEmpty(chatroomMemberSigns)) {
            return;
        }
        //wxid  群list       得到每个微信所在的多个群里集合
        Map<String, List<ChatroomMemberSign>> memberSingByWxid = chatroomMemberSigns.stream().collect(Collectors.groupingBy(ChatroomMemberSign::getWxidId, Collectors.toList()));
        //所有的 wxidid有多少个
        List<String> wxids = new ArrayList<>();
        memberSingByWxid.forEach((k, v) -> {
            wxids.add(k);
        });
        //查到有权限的这些微信的钱包
        List<ChatroomMemberMoney> memberMonies = chatroomMemberMoneyRepository.findAllByWxidIdIn(wxids);
        sendAndSaveMagicWeather(tqMsg, robotChatRooms, memberSingByWxid, memberMonies);
    }

    private synchronized void sendAndSaveMagicWeather(String tqMsg, Map<String, List<FunctionRoleEntity>> robotChatRooms, Map<String, List<ChatroomMemberSign>> memberSingByWxid, List<ChatroomMemberMoney> memberMonies) {
        List<String> addWxids = new ArrayList<>();
        List<String> deAddWxids = new ArrayList<>();
        List<String> zeroWxids = new ArrayList<>();
        Map<String, List<String>> chatRoomIdMessageMap;
        if (tqMsg.contains("晴")) {
            addWxids = GifUtil.addMemberMoneyMajor(memberMonies, 2, 300, 500);
        } else if ((tqMsg.contains("雨") && tqMsg.contains("雷")) || (tqMsg.contains("雨") && tqMsg.contains("电"))) {
            zeroWxids = GifUtil.deAddMemberMoneyHalfMajor(memberMonies, 1);
        } else {
            deAddWxids = GifUtil.deAddMemberMoneyMajor(memberMonies, 3, 100, 200);
        }
        chatRoomIdMessageMap = createChatRoomIdMessageMap(memberSingByWxid, addWxids, deAddWxids, zeroWxids);
        List<String> robotChatRoomIds = new ArrayList<>();
        robotChatRooms.forEach((chatRoomId, v) -> {
            robotChatRoomIds.add(chatRoomId);
        });
        if (chatRoomIdMessageMap.isEmpty()) {
            onlySendWeater(tqMsg, robotChatRooms);
            return;
        }
        //根据群消息map生成消息并发送
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);//新发起一个事务
        TransactionStatus status = transactionManager.getTransaction(def);// 获得事务状态
        List<String> sendChatroomId = new ArrayList<>();
        try {
            chatRoomIdMessageMap.forEach((k, v) -> {
                sendMoneyChatRoom(tqMsg, robotChatRooms, sendChatroomId, k, v);
            });
            chatroomMemberMoneyRepository.saveAll(memberMonies);
            transactionManager.commit(status);// 手动提交事务
        } catch (Exception e) {
            logger.error("推送或者保存魔法天气异常", e);
            // 异常的时候回滚
            transactionManager.rollback(status);
        }
        //去掉有奖励提示的群里
        robotChatRoomIds.removeAll(sendChatroomId);
        //没有奖励的群也要发天气预报
        robotChatRoomIds.forEach(noSendMoneyRoom -> {
            List<FunctionRoleEntity> robotChatRoomList = robotChatRooms.get(noSendMoneyRoom);
            if (CollectionUtils.isEmpty(robotChatRoomList)) {
                return;
            }
            String robotId = robotChatRoomList.get(0).getRobotId();
            StringBuilder sb = new StringBuilder();
            sb.append("【魔法天气】\r");
            sb.append("魔法森林:" + tqMsg + "\r");
            RequestDto request = new RequestDto();
            request.setMsg(sb.toString());
            request.setRobot_wxid(robotId);
            request.setFrom_wxid(noSendMoneyRoom);
            RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg), propertiesEntity.getWechatUrl());
        });
    }

    private void sendMoneyChatRoom(String tqMsg, Map<String, List<FunctionRoleEntity>> robotChatRooms, List<String> sendChatroomId, String k, List<String> v) {
        List<FunctionRoleEntity> robotChatRoomList = robotChatRooms.get(k);
        if (CollectionUtils.isEmpty(robotChatRoomList)) {
            return;
        }
        String robotId = robotChatRoomList.get(0).getRobotId();
        StringBuilder sb = new StringBuilder();
        sb.append("【魔法天气】\r");
        sb.append("魔法森林:" + tqMsg + "\r");
        v.forEach(msg -> {
            sb.append(msg + "\r");
        });
        sendChatroomId.add(k);
        RequestDto request = new RequestDto();
        request.setMsg(sb.toString());
        request.setRobot_wxid(robotId);
        request.setFrom_wxid(k);
        RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg), propertiesEntity.getWechatUrl());
    }

    private void onlySendWeater(String tqMsg, Map<String, List<FunctionRoleEntity>> robotChatRooms) {
        logger.info("没有产生魔法奖励");
        robotChatRooms.forEach((k, v) -> {
            StringBuilder sb = new StringBuilder();
            sb.append("【魔法天气】\r");
            sb.append("魔法森林:" + tqMsg + "\r");
            RequestDto request = new RequestDto();
            request.setMsg(sb.toString());
            request.setRobot_wxid(v.get(0).getRobotId());
            request.setFrom_wxid(k);
            RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg), propertiesEntity.getWechatUrl());
        });
    }

    private Map<String, List<String>> createChatRoomIdMessageMap(Map<String, List<ChatroomMemberSign>> memberSingByWxid, List<String> addWxids, List<String> deAddWxids, List<String> zeroWxids) {
        Map<String, List<String>> chatRoomIdMessageMap = new HashMap<>();
        addWxids.forEach(wxidAndMoney -> {
            List<ChatroomMemberSign> cms = memberSingByWxid.get(wxidAndMoney.split("@")[0]);
            if (CollectionUtils.isEmpty(cms)) {
                return;
            }
            // roomid  wxid
            Map<String, List<ChatroomMemberSign>> chatRoomWxid = cms.stream().collect(Collectors.groupingBy(ChatroomMemberSign::getChatroomId, Collectors.toList()));
            chatRoomWxid.forEach((roomId, wxidList) -> {
                String wxidName = wxidList.get(0).getWxidName();
                //如果有值往原来的集合里放
                if (!CollectionUtils.isEmpty(chatRoomIdMessageMap.get(roomId))) {
                    List<String> chatRoomMessage = new ArrayList<>();
                    chatRoomMessage.add(String.format(Message.LUCK_DAY_MONEY, wxidName, wxidAndMoney.split("@")[1]));
                    chatRoomIdMessageMap.get(roomId).addAll(chatRoomMessage);
                } else {
                    List<String> chatRoomMessage = new ArrayList<>();
                    chatRoomMessage.add(String.format(Message.LUCK_DAY_MONEY, wxidName, wxidAndMoney.split("@")[1]));
                    chatRoomIdMessageMap.put(roomId, chatRoomMessage);
                }
            });
        });
        deAddWxids.forEach(wxidAndMoney -> {
            List<ChatroomMemberSign> cms = memberSingByWxid.get(wxidAndMoney.split("@")[0]);
            if (CollectionUtils.isEmpty(cms)) {
                return;
            }
            // roomid  wxid
            Map<String, List<ChatroomMemberSign>> chatRoomWxid = cms.stream().collect(Collectors.groupingBy(ChatroomMemberSign::getChatroomId, Collectors.toList()));
            chatRoomWxid.forEach((roomId, wxidList) -> {
                String wxidName = wxidList.get(0).getWxidName();
                //如果有值往原来的集合里放
                if (!CollectionUtils.isEmpty(chatRoomIdMessageMap.get(roomId))) {
                    List<String> chatRoomMessage = new ArrayList<>();
                    chatRoomMessage.add(String.format(Message.NO_LUCK_DAY_MONEY, wxidName, wxidAndMoney.split("@")[1]));
                    chatRoomIdMessageMap.get(roomId).addAll(chatRoomMessage);
                } else {
                    List<String> chatRoomMessage = new ArrayList<>();
                    chatRoomMessage.add(String.format(Message.NO_LUCK_DAY_MONEY, wxidName, wxidAndMoney.split("@")[1]));
                    chatRoomIdMessageMap.put(roomId, chatRoomMessage);
                }
            });
        });
        zeroWxids.forEach(wxidAndMoney -> {
            List<ChatroomMemberSign> cms = memberSingByWxid.get(wxidAndMoney.split("@")[0]);
            if (CollectionUtils.isEmpty(cms)) {
                return;
            }
            // roomid  wxid
            Map<String, List<ChatroomMemberSign>> chatRoomWxid = cms.stream().collect(Collectors.groupingBy(ChatroomMemberSign::getChatroomId, Collectors.toList()));
            chatRoomWxid.forEach((roomId, wxidList) -> {
                String wxidName = wxidList.get(0).getWxidName();
                //如果有值往原来的集合里放
                if (!CollectionUtils.isEmpty(chatRoomIdMessageMap.get(roomId))) {
                    List<String> chatRoomMessage = new ArrayList<>();
                    chatRoomMessage.add(String.format(Message.DIED_DAY_MONEY, wxidName));
                    chatRoomIdMessageMap.get(roomId).addAll(chatRoomMessage);
                } else {
                    List<String> chatRoomMessage = new ArrayList<>();
                    chatRoomMessage.add(String.format(Message.DIED_DAY_MONEY, wxidName));
                    chatRoomIdMessageMap.put(roomId, chatRoomMessage);
                }
            });
        });
        return chatRoomIdMessageMap;
    }


    private String getTianMsg(List<String> tianqi) {
        String msg = null;
        if (checkIsTianqiMessage(tianqi)) {
            msg = tianqi.get(0);
            return msg.split(":")[1];
        }
        return null;
    }

    private boolean checkIsTianqiMessage(List<String> tianqi) {
        if (tianqi.size() == 1) {
            String msg = tianqi.get(0);
            return msg.startsWith("大连");
        }
        return false;
    }


    @Autowired
    private CardUserGzhRepository cardUserGzhRepository;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void initCard() {
        logger.info("每分钟执行,清理抽卡次数,time:{}", new Date());
        List<CardUserGzhEntity> userCardSource = cardUserGzhRepository.findByIsDeleteNotOrderByCreateTimeAsc(1);
        if (CollectionUtils.isEmpty(userCardSource)) {
            return;
        }
        try {
            userCardSource.forEach(card -> {
                Date current = new Date();
                if (current.getTime() > card.getExpireTime().getTime()) {
                    logger.info("当前card失效,cardName:{},userName:{}", card.getCardName(), card.getUserName());
                    card.setUpdateTime(new Date());
                    card.setIsDelete(1);
                    cardUserGzhRepository.save(card);
                }
            });

        } catch (Exception e) {
            logger.error("清除卡信息失败", e);
        }

    }
}
