package com.web.webchat.function.gzh;

import com.web.webchat.entity.gzh.CardUserGzhEntity;
import com.web.webchat.enums.gzh.WeChatConstat;
import com.web.webchat.repository.gzh.CardUserGzhRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("cardGzh")
public class CardGzh {

    private static final Logger logger = LogManager.getLogger(CardGzh.class.getName());

    @Autowired
    private CardUserGzhRepository cardUserGzhRepository;

    //记录抽卡
    public String saveCard(String content) {
        logger.info("抽卡记录:content{}", content);
        if (StringUtils.isBlank(content) || !content.contains(WeChatConstat.COMMAND_SPLIT_JIA)) {
            return WeChatConstat.MSG_ERROR_BHF;
        }
        String name = content.split("\\" + WeChatConstat.COMMAND_SPLIT_JIA)[0];
        String card = content.split("\\" + WeChatConstat.COMMAND_SPLIT_JIA)[1];
        CardUserGzhEntity cardUser = new CardUserGzhEntity();
        cardUser.setIsUse(0);
        cardUser.setIsDelete(0);
        cardUser.setCreateTime(new Date());
        Date currentDate = new Date();
        // 创建一个Calendar对象，并设置为当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        // 将Calendar对象的日期加上3天
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        // 获取计算后的时间
        Date futureDate = calendar.getTime();
        cardUser.setExpireTime(futureDate);
        cardUser.setCardName(card);
        cardUser.setUserName(name);
        cardUser.setUpdateTime(new Date());
        cardUser.setCardType(cardType(card));
        cardUserGzhRepository.save(cardUser);
        List<CardUserGzhEntity> userCard = cardUserGzhRepository.findByIsDeleteNotAndUserNameOrderByCreateTimeAsc(1, name);
        return convertCardUserMsg(userCard, name);

    }

    private String convertCardUserMsg(List<CardUserGzhEntity> userCard, String name) {
        StringBuilder sb = new StringBuilder();
        if (CollectionUtils.isEmpty(userCard)) {
            if(StringUtils.isBlank(name)){
                return "暂时没有卡片记录";
            }
            sb.append(String.format(WeChatConstat.MSG_CARD_TOTAL_MODE, name, 0));
            return sb.toString();
        }
        Map<String, List<CardUserGzhEntity>> cardByUser = userCard.stream().collect(Collectors.groupingBy(CardUserGzhEntity::getUserName, Collectors.toList()));
        cardByUser.forEach((k,v)->{
            sb.append(String.format(WeChatConstat.MSG_CARD_TOTAL_MODE, k, v.size()));
            v.forEach(card -> {
                        Date currentTime = new Date();
                        Date expireTime = card.getExpireTime();
                        long diffInMillies = expireTime.getTime() - currentTime.getTime();
                        double diffInHours = diffInMillies / (60 * 60 * 1000.0); // 将毫秒转换为小时，并保留一位小数
                        double diffInHoursRounded = Math.round(diffInHours * 10.0) / 10.0;
                        sb.append(String.format(WeChatConstat.MSG_CARD_MODE, card.getCardName(), diffInHoursRounded));
                    }
            );
        });
        return sb.toString();
    }

    private int cardType(String cardName) {
        if (StringUtils.contains(cardName, "暴击")) {
            return 1;
        }
        if (StringUtils.contains(cardName, "迷雾")) {
            return 2;
        }
        if (StringUtils.contains(cardName, "借力")) {
            return 3;
        }
        return 0;
    }


    //记录抽卡
    public String useCard(String content) {
        logger.info("用卡记录:content{}", content);
        if (StringUtils.isBlank(content) || !content.contains(WeChatConstat.COMMAND_SPLIT_JIA)) {
            return WeChatConstat.MSG_ERROR_BHF;
        }
        String name = content.split("\\" + WeChatConstat.COMMAND_SPLIT_JIA)[0];
        String card = content.split("\\" + WeChatConstat.COMMAND_SPLIT_JIA)[1];
        Integer cardType = cardType(card);
        List<CardUserGzhEntity> userCard = cardUserGzhRepository.findByIsDeleteNotAndUserNameAndCardTypeOrderByCreateTimeAsc(1, name, cardType);
        if (CollectionUtils.isEmpty(userCard)) {
            return "没有发现对应的卡片所以记录失败";
        }
        CardUserGzhEntity useCard = userCard.get(0);
        useCard.setUpdateTime(new Date());
        useCard.setIsUse(1);
        useCard.setIsDelete(1);
        cardUserGzhRepository.save(useCard);
        List<CardUserGzhEntity> userCardSource = cardUserGzhRepository.findByIsDeleteNotAndUserNameOrderByCreateTimeAsc(1, name);
        return convertCardUserMsg(userCardSource, name);

    }


    //查抽卡记录
    public String searchCard(String content) {
        logger.info("查卡记录:content{}", content);
        List<CardUserGzhEntity> userCardSource = cardUserGzhRepository.findByIsDeleteNotOrderByCreateTimeAsc(1);
        return convertCardUserMsg(userCardSource, null);

    }
}
