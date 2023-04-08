package com.web.webchat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class CorsConfig implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (request.getRequestURL().toString().matches(".+.ico$")) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            String origin = request.getHeader("Origin");
            // 简单请求跨域，如果是跨域请求在响应头里面添加对应的Origin
            if (!StringUtils.isEmpty(origin)) {
                response.addHeader("Access-Control-Allow-Origin", origin);
            }
            // 非简单请求跨域
            response.addHeader("Access-Control-Allow-Headers", "content-type");
            // 允许跨域请求的方法
            response.addHeader("Access-Control-Allow-Methods", "*");
            // 携带cookie的跨域
            response.addHeader("Access-Control-Allow-Credentials", "true");

            // 放行方法
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}