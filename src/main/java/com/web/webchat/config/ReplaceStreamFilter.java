//package com.web.webchat.config;
//
//import lombok.extern.slf4j.Slf4j;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
//@Slf4j
//public class ReplaceStreamFilter implements Filter {
//    @Override
//    public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {
//        log.info("StreamFilter初始化...");
//    }
//
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        ServletRequest requestWrapper = new RequestWrapper((HttpServletRequest) request);
//        chain.doFilter(requestWrapper, response);
//    }
//
//    @Override
//    public void destroy() {
//        log.info("StreamFilter销毁...");
//    }
//}