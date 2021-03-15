//package com.web.webchat.Interceptor;
//
//import com.google.gson.Gson;
//import com.web.webchat.config.RequestWrapper;
//import com.web.webchat.dto.RequestDto;
//import com.web.webchat.inteface.Verification;
//import com.web.webchat.verifiaction.EventFriendMsgVerification;
//import com.web.webchat.verifiaction.EventGroupMsgVerification;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.MediaType;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Arrays;
//
//@Slf4j
//public class VerificationInterceptor implements HandlerInterceptor  {
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        log.info("[preHandle] executing... request uri is {}", request.getRequestURI());
//        if (isJson(request)) {
//            // 获取json字符串
//            String jsonParam = new RequestWrapper(request).getBodyString();
//            RequestDto req = new Gson().fromJson(jsonParam,RequestDto.class);
//            System.out.println(jsonParam);
//
//        }
//
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//
//    }
//
//    /**
//     * 判断本次请求的数据类型是否为json
//     *
//     * @param request request
//     * @return boolean
//     */
//    private boolean isJson(HttpServletRequest request) {
//        if (request.getContentType() != null) {
//            System.out.println(request.getContentType());
//            //application/json; charset=utf-8
//            String[] contentType = request.getContentType().split(";");
//            return Arrays.stream(contentType).anyMatch(item -> item.trim().contains(MediaType.APPLICATION_JSON_VALUE) ||
//                    item.trim().contains(MediaType.APPLICATION_JSON_UTF8_VALUE));
//        }
//
//        return false;
//    }
//
//}
