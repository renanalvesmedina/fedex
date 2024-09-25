package com.mercurio.lms.vendas.model.service;

import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.TipoClassificacaoCliente;
import com.mercurio.lms.vendas.model.dao.TipoClassificacaoClienteDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.tipoClassificacaoClienteService"
 */
public class TipoClassificacaoClienteService extends CrudService<TipoClassificacaoCliente, Long> {
	
	private EmpresaService empresaService;

	/**
	 * Recupera uma inst�ncia de <code>TipoClassificacaoCliente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	@Override
	public TipoClassificacaoCliente findById(java.lang.Long id) {
		return (TipoClassificacaoCliente)super.findById(id);
	}
	
	/**
	 * 
	 * @param idClassificacaoCliente
	 * @return
	 */
	public TipoClassificacaoCliente findByDescClassificacaoCliente(Long idClassificacaoCliente){
		return getTipoClassificacaoClienteDAO().findByDescClassificacaoCliente(idClassificacaoCliente);
	}

	/**
	 * Executado antes de inserir um novo tipo de classifica��o de cliente
	 * 
	 * @param Object bean
	 * @return Object bean
	 */
	@Override
	protected TipoClassificacaoCliente beforeInsert(TipoClassificacaoCliente bean) {
		bean.setEmpresa(SessionUtils.getEmpresaSessao());
		return bean;
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @param list 
	 * @return entidade que foi armazenada.
	 */
	public TipoClassificacaoCliente store(TipoClassificacaoCliente bean, ItemList items) {
		boolean rollbackMasterId = bean.getIdTipoClassificacaoCliente() == null;

		try {
			if (!items.hasItems())
				throw new BusinessException("LMS-01074");
			
			this.beforeStore(bean);
			bean = this.getTipoClassificacaoClienteDAO().store(bean,items);
			
		} catch (RuntimeException e) {
			this.rollbackMasterState(bean, rollbackMasterId, e);
			items.rollbackItemsState();
			throw e;			
		}
		return bean;
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}


	public List findDescClassificacaoCliente(Long masterId) {
		return getTipoClassificacaoClienteDAO().findDescClassificacaoClienteByTipoClassificacaoClienteId(masterId);
	}
	
	public Integer getRowCountDescClassificacaoCliente(Long masterId) {
		return getTipoClassificacaoClienteDAO().getRowCountDescClassificacaoClienteByTipoClassificacaoClienteId(masterId);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setTipoClassificacaoClienteDAO(TipoClassificacaoClienteDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private TipoClassificacaoClienteDAO getTipoClassificacaoClienteDAO() {
		return (TipoClassificacaoClienteDAO) getDao();
	}

	public EmpresaService getEmpresaService() {
		return empresaService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

}
