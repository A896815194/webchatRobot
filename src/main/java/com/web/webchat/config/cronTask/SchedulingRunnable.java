/**
 * @projectName gh-elearning-gateway
 * @package com.gold.test
 * @className com.gold.test.SchedulingRunnable
 * @copyright Copyright 2023 Thunisoft, Inc All rights reserved.
 */
package com.web.webchat.config.cronTask;

import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.util.SpringContextUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * SchedulingRunnable
 *
 * @author Administrator
 * @version TODO
 * @description
 * @date 2023/1/5 16:55
 */
public class SchedulingRunnable implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String beanName;

    private String methodName;

    private String params;

    private String chatroomId;

    public SchedulingRunnable(String beanName, String methodName, String params, String chatroomId) {
        this.beanName = beanName;
        this.methodName = methodName;
        this.params = params;
        this.chatroomId = chatroomId;
    }

    @Override
    public void run() {
        logger.info("定时任务开始执行 - bean：{}，方法：{}，参数：{}", beanName, methodName, params);
        long startTime = System.currentTimeMillis();

        try {
            Object target = SpringContextUtil.getBean(beanName);

            Method method = null;
            if (StringUtils.isNotEmpty(params)) {
                method = target.getClass().getDeclaredMethod(methodName, String.class);
            } else {
                method = target.getClass().getDeclaredMethod(methodName);
            }

            ReflectionUtils.makeAccessible(method);
            if (StringUtils.isNotEmpty(params)) {
                method.invoke(target, params);
            } else {
                method.invoke(target);
            }
        } catch (Exception ex) {
            logger.error(String.format("定时任务执行异常 - bean：%s，方法：%s，参数：%s ", beanName, methodName, params), ex);
        }

        long times = System.currentTimeMillis() - startTime;
        logger.info("定时任务执行结束 - bean：{}，方法：{}，参数：{}，耗时：{} 毫秒", beanName, methodName, params, times);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchedulingRunnable that = (SchedulingRunnable) o;
        return Objects.equals(beanName, that.beanName) && Objects.equals(methodName, that.methodName) && Objects.equals(params, that.params) && Objects.equals(chatroomId, that.chatroomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beanName, methodName, params, chatroomId);
    }
}