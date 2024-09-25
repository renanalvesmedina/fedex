package com.mercurio.lms.vendas.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.EventoPce;
import com.mercurio.lms.vendas.model.dao.EventoPceDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.eventoPceService"
 */
public class EventoPceService extends CrudService<EventoPce, Long> {
	private ProcessoPceService processoPceService;

	/**
	 * Recupera uma instância de <code>EventoPce</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public EventoPce findById(java.lang.Long id) {
		return (EventoPce)super.findById(id);
	}

	public List findCombo(Map criteria) {
		return getEventoPceDAO().findListByCriteriaToCombo(criteria);
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
	// FIXME corrigir para retornar o ID
	public EventoPce store(EventoPce bean) {
		Long id = (Long)super.store(bean);
		EventoPce toReturn = findById(id);
		toReturn.setProcessoPce(processoPceService.findById(toReturn.getProcessoPce().getIdProcessoPce()));
		return toReturn;
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setEventoPceDAO(EventoPceDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private EventoPceDAO getEventoPceDAO() {
		return (EventoPceDAO) getDao();
	}

	public void setProcessoPceService(ProcessoPceService processoPceService) {
		this.processoPceService = processoPceService;
	}
}
