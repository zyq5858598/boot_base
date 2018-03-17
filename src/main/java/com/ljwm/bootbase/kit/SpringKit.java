package com.ljwm.bootbase.kit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * JKhaled created by yunqisong@foxmail.com 2017/11/16
 * FOR : 全局获取Spring 上下文工具类
 */
@Component
@SuppressWarnings("unchecked")
public class SpringKit implements ApplicationRunner {

    private static ApplicationContext applicationContext = null;

    @Autowired
    private ApplicationContext context;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        SpringKit.applicationContext = context;   // 设置上下文对象
    }

    public static void clearHolder() {
        applicationContext = null;
    }

    private static void assertContextInjected() {
        if (applicationContext == null) {
            throw new RuntimeException("Spring 上下文 获取失败!");
        }
    }

    public static ApplicationContext getApplicationContext() {
        assertContextInjected();
        return applicationContext;
    }

    public static <T> T getBean(String name) {
        assertContextInjected();
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) {
        assertContextInjected();
        return applicationContext.getBean(requiredType);
    }
}
