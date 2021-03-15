//package com.web.webchat.config;
//
//import com.web.webchat.Interceptor.VerificationInterceptor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebAppConfigurer implements WebMvcConfigurer {
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 拦截所有请求，通过判断是否有 @LoginRequired 注解 决定是否需要登录
//        registry.addInterceptor(verificationInterceptor()).addPathPatterns("/**");
//    }
//
//    @Bean
//    public VerificationInterceptor verificationInterceptor() {
//        return new VerificationInterceptor();
//    }
//
//}
