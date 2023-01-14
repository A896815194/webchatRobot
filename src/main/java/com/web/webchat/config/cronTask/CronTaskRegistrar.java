/**
 * @projectName gh-elearning-gateway
 * @package com.gold.test
 * @className com.gold.test.CronTaskRegistrar
 * @copyright Copyright 2023 Thunisoft, Inc All rights reserved.
 */
package com.web.webchat.config.cronTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CronTaskRegistrar
 *
 * @author Administrator
 * @version TODO
 * @description
 * @date 2023/1/5 17:24
 */
@Component
public class CronTaskRegistrar implements DisposableBean {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Map<Runnable, ScheduledTask> scheduledTaskMap = new ConcurrentHashMap<>(16);

    @Autowired
    private TaskScheduler taskScheduler;

    public TaskScheduler getTaskScheduler() {
        return this.taskScheduler;
    }

    public void addCronTask(Runnable task, String cronExpression) {
        addCronTask(new CronTask(task, cronExpression));
    }

    public void addCronTask(CronTask cronTask) {
        if (cronTask != null) {
            Runnable task = cronTask.getRunnable();
            if (this.scheduledTaskMap.containsKey(task)) {
                removeCronTask(task);
            }
            this.scheduledTaskMap.put(task, scheduleCronTask(cronTask));
        }
    }

    public ScheduledTask scheduleCronTask(CronTask cronTask) {
        ScheduledTask scheduledTask = new ScheduledTask();
        scheduledTask.future = this.taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
        return scheduledTask;
    }

    public void removeCronTask(Runnable task) {
        ScheduledTask scheduledTask = this.scheduledTaskMap.remove(task);
        if (scheduledTask != null)
            scheduledTask.cancel();
    }

    @Override
    public void destroy() throws Exception {
        logger.info("销毁周期取消所有任务");
    }
}