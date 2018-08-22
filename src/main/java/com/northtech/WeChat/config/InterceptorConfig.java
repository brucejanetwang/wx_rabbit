package com.northtech.WeChat.config;

import com.northtech.WeChat.wxmetainfo.interceptor.WXViewUrlInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class InterceptorConfig   extends WebMvcConfigurerAdapter {
    @Bean
    public HandlerInterceptor getWXViewUrlInterceptor(){
        return new WXViewUrlInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getWXViewUrlInterceptor()).addPathPatterns("/wxaction_menuview/**");
        super.addInterceptors(registry);
    }

}
