package com.web.webchat.config.listen;

import com.web.webchat.abstractclass.ChatBase;
import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.config.threadPool.AsyncPoolConfig;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.entity.UserBagEntity;
import com.web.webchat.entity.UserThing;
import com.web.webchat.enums.ApiType;
import com.web.webchat.enums.Message;
import com.web.webchat.repository.UserBagRepository;
import com.web.webchat.util.ReflectionService;
import com.web.webchat.util.RestTemplateUtil;
import com.web.webchat.util.WeChatUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class AllTypeListener {

    @Autowired
    private UserBagRepository userBagRepository;
    @Autowired
    private ReflectionService reflectionService;
    @Autowired
    private PropertiesEntity propertiesEntity;

    @Async(value = AsyncPoolConfig.TASK_EXECUTOR_NAME)
    //@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = ThingEvent.class)
    @EventListener(classes = {ThingEvent.class})
    public void listener(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ThingEvent) {
            ThingEvent thingEvent = (ThingEvent) applicationEvent;
            RequestDto request = thingEvent.getRequestDto();
            String wxid = request.getFinal_from_wxid();
            if (StringUtils.isBlank(wxid)) {
                return;
            }
            List<UserThing> userThingss = userBagRepository.getUserThingsCanUse(wxid);
            if (CollectionUtils.isEmpty(userThingss)) {
                return;
            }
            for (UserThing ut : userThingss) {
                boolean use = calculateUse(ut);
                if (use) {
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
                            userBagRepository.save(userBag);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(e);
                        request.setMsg(Message.SYSTEM_ERROR_MSG);
                        RestTemplateUtil.sendMsgToWeChat(WeChatUtil.handleResponse(request, ApiType.SendTextMsg.name()), propertiesEntity.getWechatUrl());
                    }
                }
            }

        }
    }

    private boolean calculateUse(UserThing ut) {
        Integer autoUse = ut.getAutoUse();
        boolean autoFlag = false;
        // 如果自动使用就是true
        autoFlag = autoUse == 1;
        boolean timeFlag = false;
        timeFlag = calculateTimeOk(ut);
        boolean functionFlag = false;
        functionFlag = calculateFunction(ut);
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
