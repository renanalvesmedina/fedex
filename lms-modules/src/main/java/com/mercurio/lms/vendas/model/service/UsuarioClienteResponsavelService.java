package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.UsuarioClienteResponsavel;
import com.mercurio.lms.vendas.model.dao.UsuarioClienteResponsavelDAO;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.usuarioClienteResponsavelService"
 */
public class UsuarioClienteResponsavelService extends CrudService<UsuarioClienteResponsavel, Long> {
	
	private static final String TypedFlatMap = null;

	/**
	 * Recupera uma inst�ncia de <code>OcorrenciaCliente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public UsuarioClienteResponsavel findById(java.lang.Long id) {
		return (UsuarioClienteResponsavel)super.findById(id);
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
	public Serializable store(UsuarioClienteResponsavel entity) {
		return super.store(entity);
	}


	public List<UsuarioClienteResponsavel> findUsuariosResponsaveisByCliente(TypedFlatMap parameters) {
		// TODO Auto-generated method stub
		return getUsuarioClienteResponsavelDAO().findUsuariosResponsaveisByCliente(parameters);
	}
	
	public List<UsuarioClienteResponsavel> findByUsuarioByCliente(TypedFlatMap bean) {
		return getUsuarioClienteResponsavelDAO().findByUsuarioByCliente(bean);
	}
	
	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setUsuarioClienteResponsavelDAO(UsuarioClienteResponsavelDAO dao) {
		setDao(dao);
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criteria){
		return getUsuarioClienteResponsavelDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
	}
	
	public Integer getRowCount(TypedFlatMap criteria){
		return getUsuarioClienteResponsavelDAO().getRowCount(criteria);
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private UsuarioClienteResponsavelDAO getUsuarioClienteResponsavelDAO() {
		return (UsuarioClienteResponsavelDAO) getDao();
	}
}