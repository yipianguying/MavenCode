package com.crowd.funding.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

// 获取上下文路径的工具类
@Component
public class ApplicationContextUtils implements ApplicationContextAware {
    public static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println( "applicationContext = " + applicationContext );
        ApplicationContextUtils.applicationContext = applicationContext;
    }
}
