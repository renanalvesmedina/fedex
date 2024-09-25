package com.mercurio.lms.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public final class SpringBeanFactory implements BeanFactoryAware {

    private BeanFactory beanFactory;

    public Object getBean(String beanId) {
        return beanFactory.getBean(beanId);
    }

  
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
