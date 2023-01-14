/**
 * @projectName gh-elearning-gateway
 * @package com.gold.test
 * @className com.gold.test.SysJobRunner
 * @copyright Copyright 2023 Thunisoft, Inc All rights reserved.
 */
package com.web.webchat.config.cronTask;

import com.google.gson.Gson;
import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.entity.SysJob;
import com.web.webchat.enums.FunctionType;
import com.web.webchat.repository.SysJobRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * SysJobRunner
 *
 * @author Administrator
 * @version TODO
 * @description
 * @date 2023/1/5 17:41
 */
@Component
@Slf4j
public class SysJobRunner implements CommandLineRunner {

    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;

    @Autowired
    private SysJobRepository sysJobRepository;

    @Autowired
    private PropertiesEntity propertiesEntity;

    @Override
    public void run(String... strings) throws Exception {
        log.info("查询可以执行的定时任务");
        List<SysJob> sysJobs = sysJobRepository.findAllByJobStatus(1);
        log.info("需要定时启动的任务有:{}条", sysJobs.size());
        sysJobs.forEach(item -> {
            if (item.getFunctionType().equals(FunctionType.BlindDate.name())) {
                propertiesEntity.setChatroomId(item.getChatroomId());
                propertiesEntity.setWxid(item.getWxid());
                String request = new Gson().toJson(propertiesEntity);
                SchedulingRunnable task = new SchedulingRunnable(item.getBeanName(), item.getMethodName(), request, item.getChatroomId());
                cronTaskRegistrar.addCronTask(task, item.getCronExpression());
            } else {
                SchedulingRunnable task = new SchedulingRunnable(item.getBeanName(), item.getMethodName(), item.getMethodParams(), item.getChatroomId());
                cronTaskRegistrar.addCronTask(task, item.getCronExpression());
            }
        });
        log.info("定时启动的任务添加完毕");

    }
}