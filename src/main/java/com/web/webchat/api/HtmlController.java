package com.web.webchat.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("ali")
@Slf4j
public class HtmlController {

    @GetMapping("/index")
    public String index() {

        return "welcome";
    }

    @GetMapping("/pt")
    public String pintu(@RequestParam("id")String id) {
        return "pintu";
    }

}
