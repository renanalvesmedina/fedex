package com.mercurio.lms.vendas.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.OcorrenciaPce;
import com.mercurio.lms.vendas.model.dao.OcorrenciaPceDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.ocorrenciaPceService"
 */
public class OcorrenciaPceService extends CrudService<OcorrenciaPce, Long> {
	private EventoPceService eventoPceService;
	private ProcessoPceService processoPceService;

	/**
	 * Recupera uma instância de <code>OcorrenciaPce</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public OcorrenciaPce findById(java.lang.Long id) {
		return (OcorrenciaPce)super.findById(id);
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
	public OcorrenciaPce store(OcorrenciaPce bean) {
		Long id = (Long)super.store(bean);
		OcorrenciaPce toReturn = findById(id);
		toReturn.setEventoPce(eventoPceService.findById(toReturn.getEventoPce().getIdEventoPce()));
		toReturn.getEventoPce().setProcessoPce(processoPceService.findById(toReturn.getEventoPce().getProcessoPce().getIdProcessoPce()));
		return toReturn;
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setOcorrenciaPceDAO(OcorrenciaPceDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private OcorrenciaPceDAO getOcorrenciaPceDAO() {
		return (OcorrenciaPceDAO) getDao();
	}

	public void setEventoPceService(EventoPceService eventoPceService) {
		this.eventoPceService = eventoPceService;
	}

	public void setProcessoPceService(ProcessoPceService processoPceService) {
		this.processoPceService = processoPceService;
	}

}
