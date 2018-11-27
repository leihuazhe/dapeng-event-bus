package com.today.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * 描述: BeanFactoryPostProcessor
 *
 * @author hz.lei
 * @date 2018年04月27日 上午9:01
 */
//@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("<-----------------> begin to 初始化 bean");
        String[] names = beanFactory.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println("definition bean name:" + name);
        }
        System.out.println("<-----------------> end to 初始化 bean");
    }
}
