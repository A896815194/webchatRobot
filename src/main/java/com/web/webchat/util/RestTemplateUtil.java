package com.web.webchat.util;

import com.web.webchat.dto.ResponseDto;
import com.web.webchat.dto.baidutextreview.AssToken;
import com.web.webchat.dto.baidutextreview.BaiduTextReviewResponseDto;
import com.web.webchat.dto.tulingrobot.TuLingRobotRequestDto;
import com.web.webchat.dto.tulingrobot.TuLingRobotResponseDto;
import com.web.webchat.vo.ResultVO;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateUtil {


    public static ResultVO sendPostRequest(String url, MultiValueMap<String, String> params, MultiValueMap<String, String> headers) {
        HttpMethod method = HttpMethod.POST;
        RestTemplate restTemplate = new RestTemplate();
        //将请求头部和参数合成一个请求
        HttpEntity<Object> requestEntity = new HttpEntity<>(params, headers);
        //执行HTTP请求，将返回的结构使用ResultVO类格式化
        ResponseEntity<ResultVO> response = restTemplate.exchange(url, method, requestEntity, ResultVO.class);

        return response.getBody();
    }

    public static TuLingRobotResponseDto sendPostRequest(String url, Object params) {
        HttpMethod method = HttpMethod.POST;
        //将请求头部和参数合成一个请求
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<>(params, headers);
        //执行HTTP请求，将返回的结构使用ResultVO类格式化
        ResponseEntity<TuLingRobotResponseDto> response = restTemplate.exchange(url, method, requestEntity, TuLingRobotResponseDto.class);

        return response.getBody();
    }

    public static TuLingRobotResponseDto sendMsgToTuLingRobot(TuLingRobotRequestDto request, String url) {
        HttpHeaders headers = getJsonHeaders();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
        HttpEntity<TuLingRobotRequestDto> formEntity = new HttpEntity<>(request, headers);
        TuLingRobotResponseDto result = null;
        try {
            result = restTemplate.postForObject(url, formEntity, TuLingRobotResponseDto.class);
        } catch (Exception e) {
            if (result != null) {
                System.out.println("错了");
            } else {
                System.out.println("错了");
            }
        }
        return result;
    }

    public static BaiduTextReviewResponseDto sendMsgToBaiduTextReview(String msg, String url) {
        String assToken = getBaiduASSToken().getAccess_token();
        HttpHeaders headers = getFormHeaders();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
        MultiValueMap<String, String> postParameters= new LinkedMultiValueMap<String, String>();
        postParameters.add("text",msg);
        postParameters.add("access_token",assToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(postParameters, headers);
        ResponseEntity<BaiduTextReviewResponseDto> response = restTemplate.postForEntity(url, request, BaiduTextReviewResponseDto.class);
        System.out.println(response.getBody());
        return response.getBody();
    }
    private static AssToken getBaiduASSToken(){
            String authHost = "https://aip.baidubce.com/oauth/2.0/token";
            HttpHeaders headers = getFormHeaders();
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
            MultiValueMap<String, String> postParameters= new LinkedMultiValueMap<String, String>();
            postParameters.add("grant_type","client_credentials");
            postParameters.add("client_id","vwcHGBp07lb9uKYR4MCpu733");
            postParameters.add("client_secret","qmbltNTlBziU7nPh47S59ZdAVM5KAsMI");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(postParameters, headers);
            ResponseEntity<AssToken> result = restTemplate.postForEntity(authHost, request, AssToken.class);
        return result.getBody();
    }
    public static void main(String[] args) {
        String token = getBaiduASSToken().getAccess_token();
        System.out.println(token);
        System.out.println(sendMsgToBaiduTextReview("我是你爹,他妈的","https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined"));
    }
    public static void sendMsgToWeChat(ResponseDto request, String url) {
        HttpHeaders headers = getJsonHeaders();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
        HttpEntity<ResponseDto> formEntity = new HttpEntity<>(request, headers);
        TuLingRobotResponseDto result = null;
        try {
             result = restTemplate.postForObject(url, formEntity, TuLingRobotResponseDto.class);
        } catch (Exception e) {
            System.out.println("报错了");
            e.printStackTrace();
            throw new RuntimeException("发送消息失败");
        }
    }

    public static HttpHeaders getFormHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    public static HttpHeaders getJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

}
