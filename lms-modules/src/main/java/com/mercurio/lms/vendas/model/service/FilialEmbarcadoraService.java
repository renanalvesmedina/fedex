package com.mercurio.lms.vendas.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.FilialEmbarcadora;
import com.mercurio.lms.vendas.model.dao.FilialEmbarcadoraDAO;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.filialEmbarcadoraService"
 */
public class FilialEmbarcadoraService extends CrudService<FilialEmbarcadora, Long> {

	/**
	 * Recupera uma instância de <code>FilialEmbarcadora</code> a partir do ID.
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public FilialEmbarcadora findById(Long id) {
		return getFilialEmbarcadoraDAO().findById(id);
	}

	public Integer getRowCountCustom(TypedFlatMap criteria) {
		return getFilialEmbarcadoraDAO().getRowCount(criteria);
	}

	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
		return getFilialEmbarcadoraDAO().findPaginated(criteria);
	}

	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	public void removeById(Long id) {
		super.removeById(id);
	}

	public List findComboFiliais(Long idCliente){
		return getFilialEmbarcadoraDAO().findComboFiliais(idCliente);
	}
	
	/**
	 * Remove todas as entidade relacionadas ao Cliente.
	 * @param idCliente
	 */
	public void removeByCliente(Long idCliente) {
		getFilialEmbarcadoraDAO().removeByCliente(idCliente);
	}

	public java.io.Serializable store(FilialEmbarcadora bean) {
		return super.store(bean);
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 * @return Instância do DAO.
	 */
	private FilialEmbarcadoraDAO getFilialEmbarcadoraDAO() {
		return (FilialEmbarcadoraDAO) getDao();
	}
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * @param Instância do DAO.
	 */
	public void setFilialEmbarcadoraDAO(FilialEmbarcadoraDAO dao) {
		setDao( dao );
	}

}