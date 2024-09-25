package com.mercurio.lms.workflow.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.workflow.model.EventoWorkflow;
import com.mercurio.lms.workflow.model.dao.EventoWorkflowDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.workflow.eventoWorkflowService"
 */
public class EventoWorkflowServiceImpl extends CrudService<EventoWorkflow, Long> implements BeanFactoryAware, EventoWorkflowService {


	private BeanFactory beanFactory;

	/**
	 * Recupera uma instância de <code>EventoWorkflow</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public EventoWorkflow findById(java.lang.Long id) {
        return (EventoWorkflow)super.findById(id);
    }
    
    public EventoWorkflow findByTipoEvento(Short nrTipoEvento) {
        return this.getEventoWorkflowDAO().findByTipoEvento(nrTipoEvento);
    }    
    
    /**
     * Valida classe de ação.<BR>
     * Verifica se o spring bean id existe na bean factory.
     * @param beanID spring bean id
     * @throws com.mercurio.adsm.framework.BusinessException("LMS-39001") se o sprig bean informado (se informado, i.e., diferente de '' e NULL) não for válido
     */
    public void validateClasseAcao(String beanID){
    	if (beanID != null && !"".equals(beanID)){
        	try{
        		this.beanFactory.getBean(beanID);	
        	}catch(NoSuchBeanDefinitionException e){
        		throw new BusinessException("LMS-39001");
        	}
    	}
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 * 
	 */
    @ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(EventoWorkflow bean) {
    	validateClasseAcao(bean.getNmClasseAcao());
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setEventoWorkflowDAO(EventoWorkflowDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private EventoWorkflowDAO getEventoWorkflowDAO() {
        return (EventoWorkflowDAO) getDao();
    }
    
    /**
     * Retorna uma lista EventosWorkflow que se resultem do filtro
     * @param Map map
     * @return List
     */    
    public List findLookupEventoWorkflow(Map criteria){
    	return getEventoWorkflowDAO().findLookupEventoWorkflow(criteria);
    }

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
		
	}
   }