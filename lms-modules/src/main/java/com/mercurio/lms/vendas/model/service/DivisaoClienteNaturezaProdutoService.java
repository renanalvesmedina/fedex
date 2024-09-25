package com.mercurio.lms.vendas.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.DivisaoClienteNaturezaProduto;
import com.mercurio.lms.vendas.model.dao.DivisaoClienteNaturezaProdutoDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.divisaoClienteNaturezaProdutoService"
 */
public class DivisaoClienteNaturezaProdutoService extends CrudService<DivisaoClienteNaturezaProduto, Long> {

	/**
	 * Recupera uma inst�ncia de <code>DivisaoClienteNaturezaProduto</code> a partir do ID.
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public DivisaoClienteNaturezaProduto findById(java.lang.Long id) {
		return getDivisaoClienteNaturezaProdutoDAO().findById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 * @param ids lista com as entidades que dever�o ser removida.
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(DivisaoClienteNaturezaProduto bean) {
		return super.store(bean);
	}

	public Integer getRowCountNaturezaProduto(TypedFlatMap criteria) {
		return getDivisaoClienteNaturezaProdutoDAO().getRowCountNaturezaProduto(criteria);
	}
	public ResultSetPage findPaginatedNaturezaProduto(TypedFlatMap criteria) {	
		return getDivisaoClienteNaturezaProdutoDAO().findPaginatedNaturezaProduto(criteria);
	}

	public void setDivisaoClienteNaturezaProdutoDAO(DivisaoClienteNaturezaProdutoDAO dao) {
		setDao( dao );
	}
	private DivisaoClienteNaturezaProdutoDAO getDivisaoClienteNaturezaProdutoDAO() {
		return (DivisaoClienteNaturezaProdutoDAO) getDao();
	}
}