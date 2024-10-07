package com.web.webchat.api;

import com.web.webchat.dto.hook.HookDto;
import com.web.webchat.dto.hook.HookResp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("api/v1")
public class HookController {

    private static final Logger logger = LogManager.getLogger(HookController.class.getName());


    @PostMapping("/recieve")
    public HookResp webChat(@RequestBody List<HookDto> req) {
        logger.info("请求数据{}", req);
        HookDto dto = req.get(0);
        return new HookResp().success(dto);
    }
}
