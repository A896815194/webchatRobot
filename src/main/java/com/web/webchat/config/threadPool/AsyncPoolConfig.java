package com.web.webchat.config.threadPool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncPoolConfig implements AsyncConfigurer {

    private static final Logger logger = LogManager.getLogger(AsyncPoolConfig.class.getName());

    public static final String TASK_EXECUTOR_NAME = "thingTaskExecutor";

    @Override
    @Bean(TASK_EXECUTOR_NAME)
    public AsyncTaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数（默认线程数）
        executor.setCorePoolSize(10);
        // 最大线程数
        executor.setMaxPoolSize(20);
        // 缓冲队列数
        executor.setQueueCapacity(200);
        // 允许线程空闲时间（单位：默认为秒）
        executor.setKeepAliveSeconds(60);
        // 线程池名前缀
        executor.setThreadNamePrefix("asyncExecutor-");
        // 设置是否等待计划任务在关闭时完成
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置此执行器应该阻止的最大秒数
        executor.setAwaitTerminationSeconds(60);
        // 增加 TaskDecorator 属性的配置
        executor.setTaskDecorator(new ContextDecorator());
        // 线程池对拒绝任务的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }


    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        logger.error("异步池子报错");
        return null;
    }


    /**
     * 任务装饰器
     */
    class ContextDecorator implements TaskDecorator {
        @Override
        public Runnable decorate(Runnable runnable) {
            RequestAttributes context = RequestContextHolder.currentRequestAttributes();
            return () -> {
                try {
                    // 传递上下文
                    RequestContextHolder.setRequestAttributes(context);
                    runnable.run();
                } finally {
                    RequestContextHolder.resetRequestAttributes();
                }
            };
        }
    }
}
