package com.web.webchat.util;

import com.web.webchat.config.threadPool.AsyncPoolConfig;
import com.web.webchat.dto.ResponseDto;
import com.web.webchat.dto.baidutextreview.AssToken;
import com.web.webchat.dto.baidutextreview.BaiduTextReviewResponseDto;
import com.web.webchat.dto.tulingrobot.TuLingRobotRequestDto;
import com.web.webchat.dto.tulingrobot.TuLingRobotResponseDto;
import com.web.webchat.vo.ResultVO;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class RestTemplateUtil {
    private static final Logger logger = LogManager.getLogger(RestTemplateUtil.class.getName());

    private static final int CONNECT_TIME_OUT = 10000;

    private static final int READ_TIME_OUT = 15000;

    private static final int MAX_COUNT = 3000;

    private static final int MAX_ROUT = 100;

    private static final int CONNECTION_VALIDATE_AFTER_INACTIVITY_MS = 10 * 1000;

    private static RestTemplate restTemplate = null;

    private static ThreadPoolTaskExecutor executor;
    {
        restTemplate = null;
        try {
            restTemplate = new RestTemplate(httpRequestFactory());
            //防止响应中文乱码
            restTemplate.getMessageConverters().stream().filter(StringHttpMessageConverter.class::isInstance).map(StringHttpMessageConverter.class::cast).forEach(a -> {
                a.setWriteAcceptCharset(false);
                a.setDefaultCharset(StandardCharsets.UTF_8);
            });
        } catch (Exception e) {
            logger.error("初始化restTemplate失败", e);
        }
        executor = new ThreadPoolTaskExecutor();
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
        // 线程池对拒绝任务的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
    }

    public static ClientHttpRequestFactory httpRequestFactory() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    public static HttpClient httpClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                return true;
            }
        }).build();
        httpClientBuilder.setSSLContext(sslContext);
        HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
                hostnameVerifier);
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslConnectionSocketFactory)
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(MAX_COUNT); // 最大连接数
        connectionManager.setDefaultMaxPerRoute(MAX_ROUT);    //单个路由最大连接数
        connectionManager.setValidateAfterInactivity(CONNECTION_VALIDATE_AFTER_INACTIVITY_MS); // 检查永久链接的可用性(milliseconds 毫秒)
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(READ_TIME_OUT)        //服务器返回数据(response)的时间，超过抛出read timeout
                .setConnectTimeout(CONNECT_TIME_OUT)      //连接上服务器(握手成功)的时间，超出抛出connect timeout
                .setConnectionRequestTimeout(READ_TIME_OUT)//从连接池中获取连接的超时时间，超时间未拿到可用连接，会抛出org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool
                .build();
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .build();
    }

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
        MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<String, String>();
        postParameters.add("text", msg);
        postParameters.add("access_token", assToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(postParameters, headers);
        ResponseEntity<BaiduTextReviewResponseDto> response = restTemplate.postForEntity(url, request, BaiduTextReviewResponseDto.class);
        System.out.println(response.getBody());
        return response.getBody();
    }

    private static AssToken getBaiduASSToken() {
        String authHost = "https://aip.baidubce.com/oauth/2.0/token";
        HttpHeaders headers = getFormHeaders();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
        MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<String, String>();
        postParameters.add("grant_type", "client_credentials");
        postParameters.add("client_id", "vwcHGBp07lb9uKYR4MCpu733");
        postParameters.add("client_secret", "qmbltNTlBziU7nPh47S59ZdAVM5KAsMI");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(postParameters, headers);
        ResponseEntity<AssToken> result = restTemplate.postForEntity(authHost, request, AssToken.class);
        return result.getBody();
    }

//    public static void main(String[] args) {
//        String token = getBaiduASSToken().getAccess_token();
//        System.out.println(token);
//        System.out.println(sendMsgToBaiduTextReview("我是你爹,他妈的", "https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined"));
//    }

    public static void sendMsgToWeChat(ResponseDto request, String url) {
        HttpHeaders headers = getJsonHeaders();
        HttpEntity<ResponseDto> formEntity = new HttpEntity<>(request, headers);
        TuLingRobotResponseDto result = null;
        try {
            result = restTemplate.postForObject(url, formEntity, TuLingRobotResponseDto.class);
        } catch (Exception e) {
            logger.error("回调给微信报错", e);
            throw new RuntimeException("发送消息失败");
        }
    }

    public static void sendMsgToWeChatSync(ResponseDto request, String url) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                HttpHeaders headers = getJsonHeaders();
                HttpEntity<ResponseDto> formEntity = new HttpEntity<>(request, headers);
                TuLingRobotResponseDto result = null;
                try {
                    result = restTemplate.postForObject(url, formEntity, TuLingRobotResponseDto.class);
                } catch (Exception e) {
                    logger.error("回调给微信报错", e);
                    throw new RuntimeException("发送消息失败");
                }
            }
        });

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
