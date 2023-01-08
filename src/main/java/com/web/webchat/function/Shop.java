package com.web.webchat.function;

import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.dto.ResponseDto;
import com.web.webchat.entity.*;
import com.web.webchat.enums.ApiType;
import com.web.webchat.enums.Message;
import com.web.webchat.repository.ChatroomMemberMoneyRepository;
import com.web.webchat.repository.ShopRepository;
import com.web.webchat.repository.ThingRepository;
import com.web.webchat.repository.UserBagRepository;
import com.web.webchat.util.DateUtil;
import com.web.webchat.util.InitCommonUtil;
import com.web.webchat.util.RestTemplateUtil;
import com.web.webchat.util.WeChatUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Component("Shop")
public class Shop {
    private static final Logger logger = LogManager.getLogger(Shop.class.getName());

    @Autowired
    private PropertiesEntity propertiesEntity;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private PlatformTransactionManager manager;
    @Autowired
    private ChatroomMemberMoneyRepository chatroomMemberMoneyRepository;
    @Autowired
    private UserBagRepository userBagRepository;
    @Autowired
    private ThingRepository thingRepository;

    //魔法商城
    public ResponseDto magicShop(RequestDto request) {
        logger.info("wxid:{},name:{}执行魔法商城", request.getFinal_from_wxid(), request.getFinal_from_name());
        List<ShopThing> shopThings = shopRepository.getShopThings();
        String msg = "";
        if (CollectionUtils.isEmpty(shopThings)) {
            msg = Message.GET_SHOP_EMPTY_MSG;
            request.setMsg(msg);
            RestTemplateUtil.sendMsgToWeChatSync(WeChatUtil.handleResponse(request, ApiType.SendTextMsg), propertiesEntity.getWechatUrl());
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("【魔法商城】\n");
        for (ShopThing st : shopThings) {
            sb.append("no_");
            sb.append(st.getThingId());
            sb.append(".");
            sb.append("【");
            sb.append(st.getThingName());
            sb.append("】");
            sb.append(isOne(st.getIsOne()));
            sb.append(" ×");
            sb.append(st.getThingCount());
            sb.append("\r售价:");
            sb.append(st.getThingPrice());
            sb.append("魔法能量");
            sb.append("\r");
        }
        request.setMsg(sb.toString());
        RestTemplateUtil.sendMsgToWeChatSync(WeChatUtil.handleResponse(request, ApiType.SendTextMsg), propertiesEntity.getWechatUrl());
        return null;
    }

    private String isOne(Integer isOne) {
        return isOne == 1 ? "（限购）" : "";
    }

    //魔法购买
    public ResponseDto magicBuy(RequestDto request) {
        logger.info("wxid:{},name:{}执行魔法购买", request.getFinal_from_wxid(), request.getFinal_from_name());
        String msg = request.getMsg();
        String wxid = request.getFinal_from_wxid();
        String number = InitCommonUtil.getMsgNumber(msg);
        // 数字不对
        if (Strings.isBlank(number)) {
            msg = Message.GET_SHOP_ERROR_MSG;
            request.setMsg(msg);
            RestTemplateUtil.sendMsgToWeChatSync(WeChatUtil.handleResponse(request, ApiType.SendTextMsg), propertiesEntity.getWechatUrl());
            return null;
        }
        bugThing(request, wxid, number);
        return null;
    }

    private synchronized void bugThing(RequestDto request, String wxid, String number) {
        String msg;
        List<ChatroomMemberMoney> memberMonies = chatroomMemberMoneyRepository.findAllByWxidId(wxid);
        // 钱包是空
        if (CollectionUtils.isEmpty(memberMonies)) {
            msg = Message.MY_MONEYBAG_EMPTY;
            request.setMsg(msg);
            RestTemplateUtil.sendMsgToWeChatSync(WeChatUtil.handleResponse(request, ApiType.SendTextMsg), propertiesEntity.getWechatUrl());
            return;
        }
        List<ShopEntity> shopThings = shopRepository.findAllByThingId(number);
        //没有商品
        if (CollectionUtils.isEmpty(shopThings)) {
            msg = Message.GET_SHOP_ERROR_MSG;
            request.setMsg(msg);
            RestTemplateUtil.sendMsgToWeChatSync(WeChatUtil.handleResponse(request, ApiType.SendTextMsg), propertiesEntity.getWechatUrl());
            return;
        }
        ChatroomMemberMoney memberMoney = memberMonies.get(0);
        ShopEntity shopThing = shopThings.get(0);
        Integer isOne = shopThing.getIsOne();
        //如果只能买一个
        if (isOne == 1) {
            List<UserBagEntity> userBagHad = userBagRepository.findAllByWxidIdAndEntityIdAndIsDelete(wxid, shopThing.getThingId(), 0);
            if (!CollectionUtils.isEmpty(userBagHad)) {
                msg = Message.GET_SHOP_REPEAT_ERROR_MSG;
                request.setMsg(msg);
                RestTemplateUtil.sendMsgToWeChatSync(WeChatUtil.handleResponse(request, ApiType.SendTextMsg), propertiesEntity.getWechatUrl());
                return;
            }
        }
        Long shopPrice = shopThing.getThingPrice();
        Long myMoney = memberMoney.getMoney();
        Long leftMoney = myMoney - shopPrice;
        //钱不够
        if (leftMoney < 0) {
            msg = Message.MY_MONEYBAG_NOT_ENOUGH;
            request.setMsg(msg);
            RestTemplateUtil.sendMsgToWeChatSync(WeChatUtil.handleResponse(request, ApiType.SendTextMsg), propertiesEntity.getWechatUrl());
            return;
        }
        //商品数量减少
        Integer count = shopThing.getThingCount();
        if (count == 0) {
            msg = Message.GET_SHOP_EMPTY_MSG;
            request.setMsg(msg);
            RestTemplateUtil.sendMsgToWeChatSync(WeChatUtil.handleResponse(request, ApiType.SendTextMsg), propertiesEntity.getWechatUrl());
            return;
        }
        count = count - 1;
        if (count < 0) {
            count = 0;
        }
        shopThing.setThingCount(count);
        memberMoney.setMoney(leftMoney);
        ThingEntity thing = thingRepository.findAllById(Long.valueOf(shopThing.getThingId()));
        String entityName = shopThing.getThingName();
        String entityType = "thing";
        Long times = null;
        if (thing != null) {
            entityName = thing.getThingName();
            entityType = thing.getThingType();
            times = thing.getDuration();
        }
        List<UserBagEntity> userHasBag = userBagRepository.findAllByWxidIdAndEntityIdAndIsDelete(wxid, shopThing.getThingId(), 0);
        UserBagEntity userBag = null;
        // 如果没有这个东西
        if (CollectionUtils.isEmpty(userHasBag)) {
            userBag = UserBagEntity.builder()
                    .wxidId(wxid)
                    .entityId(String.valueOf(shopThing.getThingId()))
                    .entityName(entityName)
                    .entityType(entityType)
                    .isDelete(0)
                    .useCount(1)
                    .createTime(new Date())
                    .startTime(new Date())
                    .build();
            if (times == null) {
                userBag.setEndTime(null);
            } else {
                userBag.setEndTime(DateUtil.toadyAfterMillions(times * 1000));
            }
        } else {
            userBag = userHasBag.get(0);
            Integer c = userBag.getUseCount();
            userBag.setUseCount(c+1);
        }
        TransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus status = manager.getTransaction(definition);
        try {
            shopRepository.save(shopThing);
            chatroomMemberMoneyRepository.save(memberMoney);
            userBagRepository.save(userBag);
            msg = getShopSuccessMsg(request.getFinal_from_name(), shopThing.getThingName(), thing.getThingDesc());
            request.setMsg(msg);
            RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg), propertiesEntity.getWechatUrl());
            manager.commit(status);
            return;
        } catch (Exception e) {
            manager.rollback(status);
            msg = Message.SYSTEM_ERROR_MSG;
        }
        request.setMsg(msg);
        RestTemplateUtil.sendMsgToWeChatSync(WeChatUtil.handleResponse(request, ApiType.SendTextMsg), propertiesEntity.getWechatUrl());
    }

    private String getShopSuccessMsg(String wxidName, String thingName, String thingDesc) {
        return String.format(Message.GET_SHOP_SUCCESS, wxidName, thingName, thingDesc);
    }

}
