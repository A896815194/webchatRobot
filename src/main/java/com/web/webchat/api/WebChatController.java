package com.web.webchat.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.web.webchat.abstractclass.ChatBase;
import com.web.webchat.config.PropertiesEntity;
import com.web.webchat.dto.KunPengRequestDto;
import com.web.webchat.dto.RequestDto;
import com.web.webchat.dto.ResponseDto;
import com.web.webchat.dto.WxBaseDto.HfContentResponseDto;
import com.web.webchat.dto.WxBaseDto.WxRequestDto;
import com.web.webchat.enums.gzh.WeChatConstat;
import com.web.webchat.factory.ServiceFactory;
import com.web.webchat.util.KunpengToLoveCatUtil;
import com.web.webchat.util.ReflectionService;
import com.web.webchat.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.web.webchat.util.XmlUtil.DtoToXmlString;

@RestController
@RequestMapping("webChat")
@Slf4j
public class WebChatController {

    private static final Logger logger = LogManager.getLogger(WebChatController.class.getName());
    @Autowired
    private ServiceFactory serviceFactory;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PropertiesEntity propertiesEntity;

    @PostMapping("/api")
    public ResponseDto webChat(@RequestBody String req) {
        ResponseDto response = null;
        logger.info("请求数据{}", req);
        KunPengRequestDto requestDto = new Gson().fromJson(req, KunPengRequestDto.class);
        KunPengRequestDto.build(requestDto);
        RequestDto request = KunpengToLoveCatUtil.kpConverCat(requestDto, propertiesEntity);
        logger.info("转换后为{}", request);
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


    @Autowired
    private ReflectionService reflectionService;

    @Autowired
    private PlatformTransactionManager transactionManager;

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
        String content = request.getContent();
        String fromWx = request.getFromUserName();
        String gzh = request.getToUserName();
        if (StringUtils.isNotBlank(request.getMsgType()) && request.getMsgType().contains("event") && request.getEvent().contains("subscribe")) {
            // 关注
            HfContentResponseDto dto = new HfContentResponseDto();
            dto.setToUserName(fromWx);
            dto.setFromUserName(gzh);
            dto.setMsgType("text");
            dto.setContent("感谢你的关注！输入【使用说明】查看命令");
            String result = DtoToXmlString(dto);
            log.info("返回结果：{}", result);
            return result;
        }
        if (StringUtils.isNotBlank(request.getMsgType()) && request.getMsgType().contains("text")) {
            if (Objects.equals(content, "使用说明")) {
                HfContentResponseDto dto = new HfContentResponseDto();
                dto.setToUserName(fromWx);
                dto.setFromUserName(gzh);
                dto.setMsgType("text");
                dto.setContent(WeChatConstat.HELP_NEW);
                String result = DtoToXmlString(dto);
                log.info("返回结果：{}", result);
                return result;
            }
        }
        if (StringUtils.isNotBlank(request.getMsgType()) && request.getMsgType().contains("text")) {

            AtomicReference<String> result = new AtomicReference("");
            // 如果是抽卡有关的命令
            if (StringUtils.isNotBlank(content) && WeChatConstat.COMMAND_CARD_LIST.stream().anyMatch(content::startsWith)) {
                TransactionDefinition definition = new DefaultTransactionDefinition();
                TransactionStatus status = transactionManager.getTransaction(definition);// 获得事务状态
                try {
                    String beanName = WeChatConstat.getBeanNameByKey(WeChatConstat.COMMAND_SAVE_CARD);
                    String command = getCommandString(content);
                    String methodName = getMethodString(content);
                        Map<String, Object> paramMap = new HashMap<>();
                        paramMap.put("content", content.replace(command + WeChatConstat.COMMAND_SPLIT_JIA, ""));
                        try {
                            Object data = reflectionService.invokeService(beanName, methodName, paramMap);
                            String resultData = (String) data;
                            if (StringUtils.isBlank(resultData)) {
                                throw new RuntimeException("异常");
                            }
                            HfContentResponseDto dto = new HfContentResponseDto();
                            dto.setToUserName(fromWx);
                            dto.setFromUserName(gzh);
                            dto.setMsgType("text");
                            dto.setContent(resultData);
                            result.set(DtoToXmlString(dto));
                            log.info("返回结果：{}", result);
                        } catch (Exception e) {
                            logger.error("反射异常", e);
                            result.set("操作失败");
                            throw new RuntimeException("操作异常");
                        }
                    transactionManager.commit(status);
                } catch (Exception e) {
                    transactionManager.rollback(status);
                    logger.error("操作失败", e);
                    result.set("操作失败");
                }
                return result.get();
            }
            // 每日歌单命令
            if (StringUtils.isNotBlank(request.getMsgType()) && request.getMsgType().contains("text")) {
                AtomicReference<String> result1 = new AtomicReference("");
                // 如果是歌单的命令
                    TransactionDefinition definition = new DefaultTransactionDefinition();
                    TransactionStatus status = transactionManager.getTransaction(definition);// 获得事务状态
                    try {
                        String beanName = WeChatConstat.getBeanNameByKey(WeChatConstat.COMMAND_SING_DAILY);
                        String command = getCommandString(content);
                        String methodName = getMethodString(content);
                            Map<String, Object> paramMap = new HashMap<>();
                            paramMap.put("content", content.replace(command + WeChatConstat.COMMAND_SPLIT_JIA, ""));
                            try {
                                Object data = reflectionService.invokeService(beanName, methodName, paramMap);
                                String resultData = (String) data;
                                if (StringUtils.isBlank(resultData)) {
                                    throw new RuntimeException("异常");
                                }
                                HfContentResponseDto dto = new HfContentResponseDto();
                                dto.setToUserName(fromWx);
                                dto.setFromUserName(gzh);
                                dto.setMsgType("text");
                                dto.setContent(resultData);
                                result1.set(DtoToXmlString(dto));
                                log.info("返回结果：{}", result1);
                            } catch (Exception e) {
                                logger.error("反射异常", e);
                                result1.set("操作失败");
                                throw new RuntimeException("操作异常");
                            }
                        transactionManager.commit(status);
                    } catch (Exception e) {
                        transactionManager.rollback(status);
                        logger.error("操作失败", e);
                        result1.set("操作失败");
                    }
                    return result1.get();
                }

//            HfArticleResponseDto dto = new HfArticleResponseDto();
//            dto.setToUserName(fromWx);
//            dto.setFromUserName(gzh);
//            dto.setMsgType("news");
//            dto.setArticleCount("1");
//            List<HfArticleResponseDto.Article> item = new ArrayList<>();
//            HfArticleResponseDto.Article a = new HfArticleResponseDto.Article();
//            a.setDescription("test");
//            a.setTitle("测试地址");
//            a.setUrl("http://124.223.2.76/50x.html");
//            item.add(a);
//            dto.setArticles(item);
//            String result = DtoToXmlString(dto);
//            log.info("返回结果：{}", result);
//            return result;
            }
        return "";
    }

    private String getCommandString(String content) {
        if (content.startsWith(WeChatConstat.COMMAND_SAVE_CARD + WeChatConstat.COMMAND_SPLIT_JIA)) {
            return WeChatConstat.COMMAND_SAVE_CARD;
        }
        if (content.startsWith(WeChatConstat.COMMAND_USE_CARD + WeChatConstat.COMMAND_SPLIT_JIA)) {
            return WeChatConstat.COMMAND_USE_CARD;
        }
        if (Objects.equals(content, WeChatConstat.COMMAND_SEARCH_CARD)) {
            return WeChatConstat.COMMAND_SEARCH_CARD;
        }
        if (content.startsWith(WeChatConstat.COMMAND_SING_DAILY + WeChatConstat.COMMAND_SPLIT_JIA)) {
            return WeChatConstat.COMMAND_SING_DAILY ;
        }
        if (content.startsWith(WeChatConstat.COMMAND_SEARCH_SING_DAILY + WeChatConstat.COMMAND_SPLIT_JIA)) {
            return WeChatConstat.COMMAND_SEARCH_SING_DAILY ;
        }
        if (Objects.equals(content, WeChatConstat.COMMAND_SEARCH_SING_DAILY_3)) {
            return WeChatConstat.COMMAND_SEARCH_SING_DAILY_3;
        }
        return "";
    }

    private String getMethodString(String content) {
        if (content.startsWith(WeChatConstat.COMMAND_SAVE_CARD + WeChatConstat.COMMAND_SPLIT_JIA)) {
            return WeChatConstat.getBeanMethodByKey(WeChatConstat.COMMAND_SAVE_CARD);
        }
        if (content.startsWith(WeChatConstat.COMMAND_USE_CARD + WeChatConstat.COMMAND_SPLIT_JIA)) {
            return WeChatConstat.getBeanMethodByKey(WeChatConstat.COMMAND_USE_CARD);
        }
        if (Objects.equals(content, WeChatConstat.COMMAND_SEARCH_CARD)) {
            return WeChatConstat.getBeanMethodByKey(WeChatConstat.COMMAND_SEARCH_CARD);
        }
        if (content.startsWith(WeChatConstat.COMMAND_SING_DAILY + WeChatConstat.COMMAND_SPLIT_JIA)) {
            return WeChatConstat.getBeanMethodByKey(WeChatConstat.COMMAND_SING_DAILY);
        }
        if (content.startsWith(WeChatConstat.COMMAND_SEARCH_SING_DAILY + WeChatConstat.COMMAND_SPLIT_JIA)) {
            return WeChatConstat.getBeanMethodByKey(WeChatConstat.COMMAND_SEARCH_SING_DAILY);
        }
        if (Objects.equals(content, WeChatConstat.COMMAND_SEARCH_SING_DAILY_3)) {
            return WeChatConstat.getBeanMethodByKey(WeChatConstat.COMMAND_SEARCH_SING_DAILY_3);
        }
        return "";
    }


    public static class se implements Serializable {

    }

    public void test() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> formData = addSignatureToRequestObject(new se(), "666");
        ResponseEntity<String> entity = restTemplate.postForEntity("omniHttpConfig.getRootUrl()", new HttpEntity<>(formData, headers), String.class);
        JSONObject body = (null == entity.getBody()) ? null : JSON.parseObject(entity.getBody());

    }

    public MultiValueMap<String, String> addSignatureToRequestObject(Serializable serializable, String method) {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("test", method);
        return formData;
    }
}
