package com.mercurio.lms.configuracoes.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.ParametroPais;
import com.mercurio.lms.configuracoes.model.dao.ParametroPaisDAO;

/**
 * Classe de serviço para CRUD: 
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.parametroPaisService"
 */ 
public class ParametroPaisService extends CrudService<ParametroPais, Long> {

	/**
	 * Recupera uma instância de <code>ParametroPais</code> a partir do id.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public ParametroPais findById(java.lang.Long id) {
		return (ParametroPais) super.findById(id);
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
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setParametroPaisDAO(ParametroPaisDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private ParametroPaisDAO getParametroPaisDAO() {
		return (ParametroPaisDAO) getDao();
	}

}