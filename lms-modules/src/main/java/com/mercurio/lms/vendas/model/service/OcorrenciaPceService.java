package com.mercurio.lms.vendas.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.OcorrenciaPce;
import com.mercurio.lms.vendas.model.dao.OcorrenciaPceDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.ocorrenciaPceService"
 */
public class OcorrenciaPceService extends CrudService<OcorrenciaPce, Long> {
	private EventoPceService eventoPceService;
	private ProcessoPceService processoPceService;

	/**
	 * Recupera uma inst�ncia de <code>OcorrenciaPce</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public OcorrenciaPce findById(java.lang.Long id) {
		return (OcorrenciaPce)super.findById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
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
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setOcorrenciaPceDAO(OcorrenciaPceDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
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
