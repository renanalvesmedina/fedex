package com.mercurio.lms.configuracoes.model.service;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.mercurio.adsm.framework.model.CrudService;

/**
 * @author regisn
 * @spring.bean id="lms.configuracoes.consultarJobsService"
 */
public class ConsultarJobsServiceImpl extends CrudService implements BeanFactoryAware, ConsultarJobsService {


	private BeanFactory beanFactory;
	
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}
}
