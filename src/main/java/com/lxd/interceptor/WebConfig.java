package com.lxd.interceptor;

import com.lxd.util.CurrentUserMethodArgumentResolver;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.List;

/**
 * Created by Cris on 2020/3/24
 */
@Configuration     //识别为配置类
public class WebConfig implements WebMvcConfigurer {      //采用JavaBean的形式来代替传统的xml配置文件形式进行针对框架个性化定制


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Logininterceptor())
                .addPathPatterns("/admin/**")      //加过滤的路径
                .excludePathPatterns("/admin")      //添加排除的文件
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/admin/register");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //获取文件的真实路径
        /*ApplicationHome home = new ApplicationHome(getClass());
        File sysfile = home.getSource();
        String jarPath = sysfile.getParentFile().toString();*/
        String path = "/usr/images/";
        String path1 = "path";//System.getProperty("user.dir") + "\\src\\main\\webapp\\uptextimg\\";
        registry.addResourceHandler("/static/images/**").addResourceLocations("file:" + path);
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        WebMvcConfigurer.super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(new CurrentUserMethodArgumentResolver());
    }
}
