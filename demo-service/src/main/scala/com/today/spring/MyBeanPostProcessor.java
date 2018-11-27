package com.today.spring;

import com.today.event.EventBus;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 描述:
 *
 * @author hz.lei
 * @date 2018年04月27日 上午9:12
 */
//@Component
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("call BeanPostProcessor interface postProcessAfterInitialization method; :" + beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("call BeanPostProcessor interface postProcessBeforeInitialization method ::" + beanName);
        if (bean instanceof EventBus) {
            System.out.println("bean instanceof BeanTest");
        }
        return bean;
    }
}
