package com.itheima;

import com.itheima.interceptor.WmTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册Token拦截器，拦截所有请求
        registry.addInterceptor(new WmTokenInterceptor())
                .addPathPatterns("/**");
    }
}