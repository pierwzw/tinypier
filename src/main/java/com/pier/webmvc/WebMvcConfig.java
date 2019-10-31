package com.pier.webmvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @author zhongweiwu
 * @date 2019/4/3 11:42
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //配置静态资源处理
        registry.addResourceHandler("/js/**")
                .addResourceLocations("file:/root/webapps/assets/js")
                //缓存一年（ 365 天）
                .setCacheControl (CacheControl.maxAge(365, TimeUnit.DAYS));
        registry.addResourceHandler("/plugin/**")
                .addResourceLocations("file:/root/webapps/assets/plugin");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins("*")
                .allowedHeaders("*");
        /*super.addCorsMappings(registry);*/
    }
}
