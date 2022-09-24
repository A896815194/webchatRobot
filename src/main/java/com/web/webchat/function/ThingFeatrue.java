package com.web.webchat.function;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.dto.ResponseDto;
import com.web.webchat.entity.ChatroomMemberMoney;
import com.web.webchat.entity.ChatroomMemberSign;
import com.web.webchat.entity.UserThing;
import com.web.webchat.enums.ApiType;
import com.web.webchat.enums.Message;
import com.web.webchat.repository.ChatroomMemberMoneyRepository;
import com.web.webchat.repository.ChatroomMemberSignRepository;
import com.web.webchat.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component("ThingFeatrue")
public class ThingFeatrue {
    private static final Logger logger = LogManager.getLogger(SignIn.class.getName());

    @Autowired
    private PropertiesEntity propertiesEntity;

    @Autowired
    private ChatroomMemberMoneyRepository chatroomMemberMoneyRepository;
    @Autowired
    private ChatroomMemberSignRepository chatroomMemberSignRepository;

    private final static Map<Integer, String> MSKK_MAP = new HashMap<>();
    private final static Map<Integer, Long> MSKK_DEADD_MAP = new HashMap<>();

    {
        MSKK_MAP.put(70, Message.MSKK_FAIL_1);
        MSKK_MAP.put(50, Message.MSKK_FAIL_2);
        MSKK_MAP.put(25, Message.MSKK_FAIL_3);
        MSKK_MAP.put(15, Message.MSKK_FAIL_4);
        MSKK_DEADD_MAP.put(70, 5L);
        MSKK_DEADD_MAP.put(50, 15L);
        MSKK_DEADD_MAP.put(25, 50L);
        MSKK_DEADD_MAP.put(15, 80L);

    }

    //广播
    public ResponseDto guangbo(RequestDto request) {
        logger.info("执行wxid:{},name:{}的物品效果", request.getFinal_from_wxid(), request.getFinal_from_name());
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        UserThing userThing = gson.fromJson(gson.toJson(request.getObject()), UserThing.class);
        String msg = String.format(userThing.getThingTemplate(), request.getFinal_from_name());
        request.setMsg(msg);
        RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), propertiesEntity.getWechatUrl());
        return null;
    }

    //使用妙手空空
    public ResponseDto miaoshoukongkong(RequestDto request) {
        String msg = request.getMsg();
        String result = "";
        List<String> wxids = InitCommonUtil.getAtWxidsFromMsg(msg);
        logger.info("wxid:{},name:{}对:wxids{}使用妙手空空的道具", request.getFinal_from_wxid(), request.getFinal_from_name(), wxids);
        // 获取对谁使用了技能的列表
        if (CollectionUtils.isEmpty(wxids)) {
            return null;
        }
        wxids = wxids.stream().distinct().collect(Collectors.toList());
        int successRate = miaoshoukongkongRate(wxids.size());
        logger.info("目标{}人,成功率:{}", wxids.size(), successRate);
        //当前群参加过嵌套的人
        List<String> signRoomId = Stream.of(request.getFrom_wxid()).collect(Collectors.toList());
        List<ChatroomMemberSign> chatroomMemberSigns = chatroomMemberSignRepository.findAllByChatroomIdIn(signRoomId);
        if (CollectionUtils.isEmpty(chatroomMemberSigns)) {
            result = Message.MSKK_FAIL;
        }
        // 获取签到人名称
        Map<String, List<ChatroomMemberSign>> memberSingByWxid = chatroomMemberSigns.stream().collect(Collectors.groupingBy(ChatroomMemberSign::getWxidId, Collectors.toList()));
        result = xskkCalculateAndSaveMoney(request, wxids, successRate, memberSingByWxid);
        request.setMsg(result);
        RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), propertiesEntity.getWechatUrl());
        return null;
    }

    private synchronized String xskkCalculateAndSaveMoney(RequestDto request, List<String> wxids, int successRate, Map<String, List<ChatroomMemberSign>> memberSingByWxid) {
        String result;
        if (Calculate.fixedPercentage(successRate)) {
            //查到有权限的这些微信的钱包
            //加上自己的钱包
            wxids.add(request.getFinal_from_wxid());
            List<ChatroomMemberMoney> memberMonies = chatroomMemberMoneyRepository.findAllByWxidIdIn(wxids);
            List<String> resutlStr = GifUtil.mskkMemberMoney(memberMonies, request.getFinal_from_wxid(), 500, 1000);
            result = createResultMsg(request.getFinal_from_wxid(), resutlStr, memberSingByWxid);
            chatroomMemberMoneyRepository.saveAll(memberMonies);
        } else {
            //失败了
            String failMessage = MSKK_MAP.get(successRate);
            List<ChatroomMemberMoney> userMoney = chatroomMemberMoneyRepository.findAllByWxidId(request.getFinal_from_wxid());
            if (CollectionUtils.isEmpty(userMoney)) {
                ChatroomMemberMoney memberMoney = ChatroomMemberMoney.builder()
                        .money(0L)
                        .wxidId(request.getFinal_from_wxid())
                        .build();
                chatroomMemberMoneyRepository.save(memberMoney);
                result = String.format(failMessage, request.getFinal_from_name());
            } else {
                ChatroomMemberMoney memberMoney = userMoney.get(0);
                Long money = memberMoney.getMoney();
                Long deAdd = MSKK_DEADD_MAP.get(successRate);
                logger.info("减少百分比:{}", deAdd);
                BigDecimal deCount = Calculate.multiply(money, Calculate.divide(deAdd, 100L, 2,BigDecimal.ROUND_HALF_UP),2,BigDecimal.ROUND_HALF_UP);
                money = money - deCount.longValue();
                if (money < 0) {
                    money = 0L;
                }
                memberMoney.setMoney(money);
                chatroomMemberMoneyRepository.save(memberMoney);
                result = String.format(failMessage, request.getFinal_from_name(), deCount);
            }
        }
        return result;
    }

    private String createResultMsg(String userWxid, List<String> resutlStr, Map<String, List<ChatroomMemberSign>> wxNameMap) {
        StringBuilder sb = new StringBuilder();
        String mskk = Message.MSKK_SUCCESS;
        String userMsg = "";
        String userName = wxNameMap.get(userWxid).get(0).getWxidName();
        for (String msg : resutlStr) {
            if (msg.startsWith(userWxid)) {
                userMsg = msg.split("@")[1];
                userMsg = StringUtils.substringAfter(userMsg, "+");
            } else {
                String wxid = msg.split("@")[0];
                String wxName = wxNameMap.get(wxid).get(0).getWxidName();
                String wxidDeMoneyStr = msg.split("@")[1];
                String deMoney = StringUtils.substringAfter(wxidDeMoneyStr, "-");
                sb.append(String.format(Message.MSKK_SUCCESS_BT, wxName, deMoney));
            }
        }
        String btString = sb.toString();
        return String.format(mskk, userName, userMsg, btString);
    }

    private Integer miaoshoukongkongRate(int size) {
        if (size == 1) {
            //打断胳膊   扣10%
            return 70;
        }
        if (size == 2) {
            //打断腿    扣30%
            return 50;
        }
        if (size == 3) {
            //半身不遂  扣80%
            return 25;
        }
        if (size > 3) {
            //生活不能自理 清零
            return 15;
        }
        return 1;
    }

}
