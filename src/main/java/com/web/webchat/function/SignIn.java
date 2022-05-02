package com.web.webchat.function;

import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.dto.ResponseDto;
import com.web.webchat.entity.ChatroomMemberMoney;
import com.web.webchat.entity.ChatroomMemberSign;
import com.web.webchat.enums.ApiType;
import com.web.webchat.repository.ChatroomMemberMoneyRepository;
import com.web.webchat.repository.ChatroomMemberSignRepository;
import com.web.webchat.util.RestTemplateUtil;
import com.web.webchat.util.WeChatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Component("SignIn")
public class SignIn {

    @Autowired
    private PropertiesEntity propertiesEntity;
    @Autowired
    private ChatroomMemberMoneyRepository chatroomMemberMoneyRepository;
    @Autowired
    private ChatroomMemberSignRepository chatroomMemberSignRepository;
    @Autowired
    private PlatformTransactionManager manager;

    public ResponseDto signin(RequestDto request) {
        String wxid = request.getFinal_from_wxid();
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayStart = dateTime.format(formatter) + " 00:00:00";
        String todayEnd = dateTime.format(formatter) + " 23:59:59";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date start = null;
        Date end = null;
        try {
            start = sdf.parse(todayStart);
            end = sdf.parse(todayEnd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<ChatroomMemberSign> signDataSource = chatroomMemberSignRepository.findAllByWxidIdAndSignTimeBetween(wxid, start, end);
        String msg = "";
        if (!CollectionUtils.isEmpty(signDataSource)) {
            msg = "今天已经签过到了,不要太贪心哦";
            request.setMsg(msg);
            RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), propertiesEntity.getWechatUrl());
            return null;
        }
        List<ChatroomMemberSign> allSignData = chatroomMemberSignRepository.findAllBySignTimeBetween(start, end);
        Integer rank = CollectionUtils.isEmpty(allSignData) ? 1 : allSignData.size() + 1;
        ChatroomMemberSign memberSign = ChatroomMemberSign.builder()
                .chatroomId(request.getFrom_wxid())
                .wxidId(request.getFinal_from_wxid())
                .wxidName(request.getFinal_from_name())
                .rankDay(rank)
                .build();
        TransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus status = manager.getTransaction(definition);
        Long money = getBasicMoney(rank);
        Long extraMoney = getExtraMoney(rank);
        try {
            chatroomMemberSignRepository.save(memberSign);
            List<ChatroomMemberMoney> userMoneys = chatroomMemberMoneyRepository.findAllByWxidId(request.getFinal_from_wxid());
            if (CollectionUtils.isEmpty(userMoneys)) {
                ChatroomMemberMoney memberMoney = ChatroomMemberMoney.builder()
                        .money(money + extraMoney)
                        .wxidId(request.getFinal_from_wxid())
                        .build();
                chatroomMemberMoneyRepository.save(memberMoney);
            } else {
                ChatroomMemberMoney newUserMoney = userMoneys.get(0);
                Long userMoney = newUserMoney.getMoney();
                newUserMoney.setMoney(userMoney + money + extraMoney);
                chatroomMemberMoneyRepository.save(newUserMoney);
            }
            manager.commit(status);
        } catch (Exception e) {
            manager.rollback(status);
            return null;
        }
        msg = getMoneyMsg(request.getFinal_from_name(), money, extraMoney, rank);
        request.setMsg(msg);
        RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), propertiesEntity.getWechatUrl());
        return null;
    }

    private String getMoneyMsg(String name, Long money, Long extraMoney, Integer rank) {
        String template = "签到NO.%s【 %s】得到【 %s】魔法能量";
        String moneyMsg = String.format(template, rank, name, money);
        if (extraMoney != null && extraMoney > 0l) {
            moneyMsg = moneyMsg + ";同时得到大魔法师的青睐，额外获得【" + extraMoney + "】魔法能量奖励!!";
        }
        return moneyMsg;
    }

    // 100-200
    // 50-150
    private static long getBasicMoney(Integer rank) {
        Random random = new Random();
        if (rank < 5) {
            long r = random.nextInt(100) + 100;
            return r;
        }
        return (random.nextInt(100) + 50);
    }

    private static long getExtraMoney(Integer rank) {
        Random random = new Random();
        if (rank < 5) {
            long r = random.nextInt(350) + 150;
            return r;
        }
        return 0;
    }

}
