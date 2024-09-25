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
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tabelaprecos.generalidadeService"
 */
public class GeneralidadeService extends CrudService<Generalidade, Long> {

	/**
	 * Recupera uma inst�ncia de <code>Generalidade</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public Generalidade findById(java.lang.Long id) {
		return (Generalidade)super.findById(id);
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
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setGeneralidadeDAO(GeneralidadeDAO dao) {
		setDao( dao );
	}

	public Generalidade findPrecificacaoGeneralidadeRefaturamento(Long idTabelaPreco) {
		return getGeneralidadeDAO().findRefaturamentoByIdTabelaPreco(idTabelaPreco);
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private GeneralidadeDAO getGeneralidadeDAO() {
		return (GeneralidadeDAO) getDao();
	}
}