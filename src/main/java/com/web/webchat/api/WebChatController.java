package com.web.webchat.api;

import com.google.gson.Gson;
import com.web.webchat.abstractclass.ChatBase;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.dto.ResponseDto;
import com.web.webchat.dto.WxBaseDto.HfArticleResponseDto;
import com.web.webchat.dto.WxBaseDto.HfContentResponseDto;
import com.web.webchat.dto.WxBaseDto.WxRequestDto;
import com.web.webchat.util.ServiceFactory;
import com.web.webchat.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.web.webchat.util.XmlUtil.DtoToXmlString;

@RestController
@RequestMapping("webChat")
@Slf4j
public class WebChatController {

    private static final Logger logger = LogManager.getLogger(WebChatController.class.getName());
    @Autowired
    private ServiceFactory serviceFactory;

    @PostMapping("/api")
    public ResponseDto webChat(@RequestBody RequestDto request) {
        ResponseDto response = null;
        ChatBase stragey = serviceFactory.getInvokeStrategy(request.getEvent());
        if (Objects.isNull(stragey)) {
            System.out.println("没有这个策略的逻辑:" + stragey);
            return new ResponseDto();
        }
        stragey.sendToWechat(request);
        return response;
    }

    @PostMapping("new/api")
    public ResponseDto newWebChat(@RequestBody String requestDto) {
        logger.info("请求数据{}", requestDto);
        ResponseDto response = null;
        RequestDto request = new Gson().fromJson(requestDto, RequestDto.class);
        ChatBase stragey = serviceFactory.getInvokeStrategy(request.getEvent());
        if (Objects.isNull(stragey)) {
            logger.error("没有这个策略的逻辑:{}", request.getEvent());
            return new ResponseDto();
        }
        if (Objects.equals(request.getRobot_wxid(), request.getFrom_wxid())) {
            return response;
        }
        stragey.sendToWechat(request);
        return response;
    }


//    public static void main(String[] args) {
//        HttpMethod method = HttpMethod.GET;
//        //将请求头部和参数合成一个请求
//        RestTemplate restTemplate = new RestTemplate();
//        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
//        header.add("Content-Type", "application/json");
//
//        Map<String, String> query = new HashMap<>();
//        query.put("m1", "nima");
//        HttpEntity<Object> requestEntity = new HttpEntity<>(query,header);
//        //执行HTTP请求，将返回的结构使用ResultVO类格式化
//        String url = "http://localhost:8081/webChat/test";
//        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url);
//
////add data as a query params
//        uriComponentsBuilder.queryParam("m1", "abc");
////        ResponseEntity<String> response = restTemplate.exchange(url+"?m1[]=密码", method, requestEntity, String.class);
//
//        ResponseEntity<String> response = restTemplate.exchange(uriComponentsBuilder.toUriString(), method, requestEntity, String.class);
//
//    }

    @GetMapping("wx/auth1")
    public String wxauth1(@RequestParam("signature") String signature, @RequestParam("timestamp") String timestamp, @RequestParam("nonce") String nonce, @RequestParam("echostr") String echostr) {
        logger.info("signature:{},timestamp:{},nonce:{},echostr:{}", signature, timestamp, nonce, echostr);
        String token = "jstest";
        List<String> strings = new ArrayList<>();
        strings.add(token);
        strings.add(timestamp);
        strings.add(nonce);
        List<String> newString = strings.stream().sorted().collect(Collectors.toList());
        String string = newString.get(0) + newString.get(1) + newString.get(2);
        String sign = DigestUtils.shaHex(string);
        logger.info("sign:{}", sign);
        if (Objects.equals(sign, signature)) {
            return echostr;
        }
        return "";
    }

    @GetMapping("wx/auth")
    public String wxauth(@RequestParam("signature") String signature, @RequestParam("timestamp") String timestamp, @RequestParam("nonce") String nonce, @RequestParam("echostr") String echostr) {
        logger.info("signature:{},timestamp:{},nonce:{},echostr:{}", signature, timestamp, nonce, echostr);
        String token = "jstest";
        List<String> strings = new ArrayList<>();
        strings.add(token);
        strings.add(timestamp);
        strings.add(nonce);
        List<String> newString = strings.stream().sorted().collect(Collectors.toList());
        String string = newString.get(0) + newString.get(1) + newString.get(2);
        String sign = DigestUtils.shaHex(string);
        logger.info("sign:{}", sign);
        if (Objects.equals(sign, signature)) {
            return echostr;
        }
        return "";
    }


    @PostMapping("wx/auth")
    public String wxauth(@RequestBody String xmlString) {
        logger.info("公众号内容:" + xmlString);
        WxRequestDto request = null;
        try {
            Map<String, String> testmap = XmlUtil.xmlToMap(xmlString);
            Gson gson = new Gson();
            request = gson.fromJson(gson.toJson(testmap), WxRequestDto.class);
            logger.info("转换实体后:" + request);
        } catch (Exception e) {
            log.error("转换失败", e);
        }
        if (StringUtils.isNotBlank(request.getMsgType()) && request.getMsgType().contains("event") && request.getEvent().contains("subscribe")) {
            // 关注
            String fromWx = request.getFromUserName();
            String gzh = request.getToUserName();
            HfContentResponseDto dto = new HfContentResponseDto();
            dto.setToUserName(fromWx);
            dto.setFromUserName(gzh);
            dto.setMsgType("text");
            dto.setContent("点关注不迷路~\r输入关键字 ”帮助” 查看功能列表“");
            String result = DtoToXmlString(dto);
            log.info("返回结果：{}", result);
            return result;
        }
        if (StringUtils.isNotBlank(request.getMsgType()) && request.getMsgType().contains("text")) {
            String fromWx = request.getFromUserName();
            String gzh = request.getToUserName();
            HfArticleResponseDto dto = new HfArticleResponseDto();
            dto.setToUserName(fromWx);
            dto.setFromUserName(gzh);
            dto.setMsgType("news");
            dto.setArticleCount("1");
            List<HfArticleResponseDto.Article> item = new ArrayList<>();
            HfArticleResponseDto.Article a = new HfArticleResponseDto.Article();
            a.setDescription("test");
            a.setTitle("测试地址");
            a.setUrl("http://124.223.2.76/50x.html");
            item.add(a);
            dto.setArticles(item);
            String result = DtoToXmlString(dto);
            log.info("返回结果：{}", result);
            return result;
        }
        return "";
    }

}
