package com.mercurio.lms.workflow.model.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.workflow.model.Pendencia;

/**
 * Classe de serviço:   
 *
 * @spring.bean id="lms.workflow.workflowService"
 */

public class WorkflowEngineImpl implements BeanFactoryAware, WorkflowEngine {
    	
	private BeanFactory beanFactory;
	
	private PendenciaService pendenciaService;
	
	private DomainValueService domainValueService;		
	
	private static final Log log = LogFactory.getLog(WorkflowEngineImpl.class);
	
	/**
	 * Chama por refleção a classe ação passando por parametro a lista de idPendencia e o status.
	 * Retorna o string retornado pela classe ação.
	 * 
	 * @param Ocorrencia ocorrencia
	 * @return String
	 * 
	 * */
    public String executeClasseAcao(List pendenciasAbertas, List pendencias, String nmClasseAcao){

		String retorno = null; // texto de retorno, deve ser 'null' que é o retorno que a tela espera, caso contrário é exibida na tela o alert.

    	// Apenas executa se não existir nenhuma pendencia aberta
    	if (pendenciasAbertas.size() == 0) {
    	
        	final int totalPendencias = pendencias.size();
			List idsProcesso = new ArrayList(totalPendencias);
        	List tpsSituacaoProcesso = new ArrayList(totalPendencias);    	    	
        	
	    	//Montar a lista de ids de processo e a situação deles  	
	    	for(Iterator iter = pendencias.iterator(); iter.hasNext();) {
	    		Pendencia pendencia = (Pendencia)iter.next();
	    		idsProcesso.add(pendencia.getIdProcesso());
	    		tpsSituacaoProcesso.add(pendencia.getTpSituacaoPendencia().getValue());    		
	    	}	
	    	
	        Class beanClass = ((RootBeanDefinition) ((DefaultListableBeanFactory) this.beanFactory).getBeanDefinition(nmClasseAcao)).getBeanClass();
	        Object beanExtended = this.beanFactory.getBean(nmClasseAcao);
	        try {
	        	
	        	Method method = beanClass.getMethod("executeWorkflow", new Class[]{List.class, List.class}); // nome do method e tipo dos argumentos do metodo	        	
	        	retorno = (String)method.invoke(beanExtended, new Object[]{idsProcesso,tpsSituacaoProcesso}); // instancia do obj que terá o metodo invocado e argumentos passados para o metodo
	        	
	        } catch (InvocationTargetException e) {
	        	
	        	final Throwable targetException = e.getTargetException();
				if (targetException instanceof BusinessException) {
	        		throw ((BusinessException)targetException);
	        	} else {
	        		throw new InfrastructureException("Erro na execução da ação de workflow: "+nmClasseAcao+" - "+targetException.getMessage(), targetException);
	        	}
	        } catch (NoSuchMethodException e) {
	        	String msg = "Não foi localizado o método executeWorkflow(java.util.List, java.util.List) na classe: "+beanClass.getName();
	        	log.error(msg);
	        	throw new InfrastructureException(msg, e);
	        } catch (Throwable e){
	        	throw new InfrastructureException("Ocorreu um erro interno no Workflow", e);
	        }	
    	        	
    	}

    	return retorno;
    }

	public PendenciaService getPendenciaService() {
		return pendenciaService;
	}

	public void setPendenciaService(PendenciaService pendenciaService) {
		this.pendenciaService = pendenciaService;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}	
}
