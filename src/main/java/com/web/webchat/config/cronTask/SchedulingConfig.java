/**
 * @projectName gh-elearning-gateway
 * @package com.gold.test
 * @className com.gold.test.SchedulingConfig
 * @copyright Copyright 2023 Thunisoft, Inc All rights reserved.
 */
package com.web.webchat.config.cronTask;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * SchedulingConfig
 * @description
 * @author Administrator
 * @date 2023/1/5 16:44
 * @version TODO
 */
@Configuration
public class SchedulingConfig {

    @Bean
    public TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(4);
        taskScheduler.setRemoveOnCancelPolicy(true);
        taskScheduler.setThreadNamePrefix("timerThreadl-");
        return taskScheduler;
    }
}