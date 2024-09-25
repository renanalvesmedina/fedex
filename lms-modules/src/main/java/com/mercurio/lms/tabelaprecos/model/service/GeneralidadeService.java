package com.mercurio.lms.tabelaprecos.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.Generalidade;
import com.mercurio.lms.tabelaprecos.model.dao.GeneralidadeDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tabelaprecos.generalidadeService"
 */
public class GeneralidadeService extends CrudService<Generalidade, Long> {

	/**
	 * Recupera uma instância de <code>Generalidade</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public Generalidade findById(java.lang.Long id) {
		return (Generalidade)super.findById(id);
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
	public java.io.Serializable store(Generalidade bean) {
		return super.store(bean);
	}

	public TypedFlatMap findByIdTabelaPrecoParcelaIdTabelaPreco(Long idTabelaPrecoParcela, Long idTabelaPreco) {
		return getGeneralidadeDAO().findByIdTabelaPrecoParcelaIdTabelaPreco(idTabelaPrecoParcela, idTabelaPreco);
	}

	public Generalidade findGeneralidade(Long idTabelaPreco, Long idParcelaPreco) {
		return getGeneralidadeDAO().findGeneralidade(idTabelaPreco, idParcelaPreco);
	}

	public ResultSetPage findPaginatedByIdTabelaPreco(TypedFlatMap criteria) {
		Long idTabelaPreco = criteria.getLong("tabelaPreco.idTabelaPreco");
		FindDefinition def = FindDefinition.createFindDefinition(criteria);
		return getGeneralidadeDAO().findPaginatedByIdTabelaPreco(idTabelaPreco, def);
	}

	public Integer getRowCountByIdTabelaPreco(TypedFlatMap criteria) {
		Long idTabelaPreco = criteria.getLong("tabelaPreco.idTabelaPreco");
		return getGeneralidadeDAO().getRowCountByIdTabelaPreco(idTabelaPreco);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setGeneralidadeDAO(GeneralidadeDAO dao) {
		setDao( dao );
	}

	public Generalidade findPrecificacaoGeneralidadeRefaturamento(Long idTabelaPreco) {
		return getGeneralidadeDAO().findRefaturamentoByIdTabelaPreco(idTabelaPreco);
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private GeneralidadeDAO getGeneralidadeDAO() {
		return (GeneralidadeDAO) getDao();
	}
}