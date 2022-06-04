package com.web.webchat.config.listen;

import com.google.gson.Gson;
import com.web.webchat.abstractclass.ChatBase;
import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.config.threadPool.AsyncPoolConfig;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.entity.ThingEntity;
import com.web.webchat.entity.UserBagEntity;
import com.web.webchat.entity.UserThing;
import com.web.webchat.enums.ApiType;
import com.web.webchat.enums.Message;
import com.web.webchat.repository.UserBagRepository;
import com.web.webchat.util.ReflectionService;
import com.web.webchat.util.RestTemplateUtil;
import com.web.webchat.util.WeChatUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class AllTypeListener {

    private static final Logger logger = LogManager.getLogger(AllTypeListener.class.getName());

    @Autowired
    private UserBagRepository userBagRepository;
    @Autowired
    private ReflectionService reflectionService;
    @Autowired
    private PropertiesEntity propertiesEntity;

    @Autowired
    private PlatformTransactionManager transactionManager;


    @Async(value = AsyncPoolConfig.TASK_EXECUTOR_NAME)
    @EventListener(classes = {ThingEvent.class})
    public void listener(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ThingEvent) {
            ReentrantLock rl = new ReentrantLock();
            try {
                rl.lock();
                ThingEvent thingEvent = (ThingEvent) applicationEvent;
                logger.info("接受物品特性事件内容:{}", new Gson().toJson(thingEvent));
                RequestDto request = thingEvent.getRequestDto();
                String wxid = request.getFinal_from_wxid();
                if (StringUtils.isBlank(wxid)) {
                    return;
                }
                List<UserThing> userThingss = userBagRepository.getUserThingsCanUse(wxid);
                ThingEntity thingEntity = thingEvent.getThing();
                if (CollectionUtils.isEmpty(userThingss)) {
                    return;
                }
                if (thingEntity == null) {
                    logger.info("人物wxid:{},物品数量:{}", wxid, userThingss.size());
                    sendThingMessage(request, wxid, userThingss);
                    return;
                }
                //如果带了thingEntity 说明是主动使用道具
                logger.info("人物wxid:{},使用物品:{}", wxid, new Gson().toJson(thingEntity));
                useThingMessage(request, wxid, userThingss, thingEntity);
            } finally {
                rl.unlock();
            }
        }
    }

    private synchronized void useThingMessage(RequestDto request, String wxid, List<UserThing> userThingss, ThingEntity thingEntity) {
        if (!userHasThing(userThingss, thingEntity)) {
            logger.info("wxid:{}没有这个道具,所有不执行，thingid:{}", wxid, thingEntity.getId());
            return;
        }
        TransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(definition);// 获得事务状态
        try {
            request.setObject(thingEntity);
            Map<String, Object> resultMap = ChatBase.convertRequestToMap(request);
            reflectionService.invokeService(thingEntity.getThingClass(), thingEntity.getThingMethod(), resultMap);
            List<UserBagEntity> userBagEntities = userBagRepository.findAllByWxidIdAndEntityIdAndIsDelete(wxid, String.valueOf(thingEntity.getId()), 0);
            if (!CollectionUtils.isEmpty(userBagEntities)) {
                UserBagEntity userBag = userBagEntities.get(0);
                Integer useCount = userBag.getUseCount();
                if (useCount == null) {
                    useCount = 1;
                }
                useCount = useCount - 1;
                if (useCount < 0) {
                    useCount = 0;
                }
                userBag.setUseCount(useCount);
                if (useCount == 0 && thingEntity.getUseType() != null) {
                    userBagRepository.delete(userBag);
                } else {
                    userBagRepository.save(userBag);
                }
            }
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            logger.error("执行物品自身逻辑失败", e);
            request.setMsg(Message.SYSTEM_ERROR_MSG);
            RestTemplateUtil.sendMsgToWeChatSync(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), propertiesEntity.getWechatUrl());
        }
    }

    private boolean userHasThing(List<UserThing> userThingss, ThingEntity thingEntity) {
        return userThingss.stream().anyMatch(item -> Objects.equals(item.getThingId(), String.valueOf(thingEntity.getId())));
    }

    private synchronized void sendThingMessage(RequestDto request, String wxid, List<UserThing> userThingss) {
        for (UserThing ut : userThingss) {
            boolean use = calculateUse(ut);
            if (use) {
                TransactionDefinition definition = new DefaultTransactionDefinition();
                TransactionStatus status = transactionManager.getTransaction(definition);// 获得事务状态
                try {
                    request.setObject(ut);
                    Map<String, Object> resultMap = ChatBase.convertRequestToMap(request);
                    reflectionService.invokeService(ut.getThingClass(), ut.getThingMethod(), resultMap);
                    List<UserBagEntity> userBagEntities = userBagRepository.findAllByWxidIdAndEntityIdAndIsDelete(wxid, ut.getThingId(), 0);
                    if (!CollectionUtils.isEmpty(userBagEntities)) {
                        UserBagEntity userBag = userBagEntities.get(0);
                        Integer useCount = userBag.getUseCount();
                        if (useCount == null) {
                            useCount = 1;
                        }
                        useCount = useCount - 1;
                        if (useCount < 0) {
                            useCount = 0;
                        }
                        userBag.setUseCount(useCount);
                        if (useCount == 0 && ut.getUseType() != null) {
                            userBagRepository.delete(userBag);
                        } else {
                            userBagRepository.save(userBag);
                        }
                    }
                    transactionManager.commit(status);
                } catch (Exception e) {
                    transactionManager.rollback(status);
                    logger.error("执行物品自身逻辑失败", e);
                    request.setMsg(Message.SYSTEM_ERROR_MSG);
                    RestTemplateUtil.sendMsgToWeChatSync(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), propertiesEntity.getWechatUrl());
                }
            }
        }
    }

    private boolean calculateUse(UserThing ut) {
        Integer autoUse = ut.getAutoUse();
        boolean autoFlag = false;
        // 如果自动使用就是true
        autoFlag = autoUse == 1;
        // 如果是自动使用走这个判断逻辑
        return autoUseCalculate(ut, autoFlag);

    }

    private boolean autoUseCalculate(UserThing ut, boolean autoFlag) {
        boolean timeFlag = false;
        timeFlag = calculateTimeOk(ut);
        boolean functionFlag = false;
        functionFlag = calculateFunction(ut);
        //如果是自动使用物品，并且时间不在范围内直接失效
        if (autoFlag && !timeFlag) {
            Optional<UserBagEntity> useBag = userBagRepository.findById(ut.getUseBagId());
            if (useBag.isPresent()) {
                UserBagEntity ub = useBag.get();
                ub.setIsDelete(1);
                userBagRepository.save(ub);
            }
        }
        //如果是自动使用，且时间在范围内并且有方法才执行
        return autoFlag && timeFlag && functionFlag;
    }

    private boolean calculateFunction(UserThing ut) {
        return StringUtils.isNotBlank(ut.getThingClass()) && StringUtils.isNotBlank(ut.getThingMethod());
    }

    private boolean calculateTimeOk(UserThing ut) {
        Date nowTime = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        Date beginTime = ut.getStartTime();
        Date endTime = ut.getEndTime();
        if (endTime == null) {
            return true;
        }
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        return date.after(begin) && date.before(end);
    }
}
