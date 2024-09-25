package com.mercurio.lms.configuracoes.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.ParametroPais;
import com.mercurio.lms.configuracoes.model.dao.ParametroPaisDAO;

/**
 * Classe de servi�o para CRUD: 
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.parametroPaisService"
 */ 
public class ParametroPaisService extends CrudService<ParametroPais, Long> {

	/**
	 * Recupera uma inst�ncia de <code>ParametroPais</code> a partir do id.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public ParametroPais findById(java.lang.Long id) {
		return (ParametroPais) super.findById(id);
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
	public ParametroPais store(ParametroPais bean) {
		super.store(bean);
		return bean;
	}

	/**
	 * Retorna o ParametroPais atraves do id do Pais
	 * 
	 * @param Long idPais
	 * @return ParametroPais
	 */
	public ParametroPais findByIdPais(Long idPais) {
		return getParametroPaisDAO().findByIdPais(idPais, false);
	}

	public ParametroPais findByIdPais(Long idPais, boolean useLock) {
		return getParametroPaisDAO().findByIdPais(idPais, useLock);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setParametroPaisDAO(ParametroPaisDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private ParametroPaisDAO getParametroPaisDAO() {
		return (ParametroPaisDAO) getDao();
	}

}