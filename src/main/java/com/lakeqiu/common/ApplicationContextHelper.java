package com.lakeqiu.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author lakeqiu
 */
@Component("ApplicationContextHelper")
public class ApplicationContextHelper implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    public static <T> T getBean(Class<T> clazz){
        return null == applicationContext ? null : applicationContext.getBean(clazz);
    }

    public static <T> T getBean(String beanName, Class<T> clazz){
        return null == applicationContext ? null : applicationContext.getBean(beanName, clazz);
    }
}
