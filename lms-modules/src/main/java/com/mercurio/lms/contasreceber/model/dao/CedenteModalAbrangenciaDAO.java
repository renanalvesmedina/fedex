package com.mercurio.lms.contasreceber.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.CedenteModalAbrangencia;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CedenteModalAbrangenciaDAO extends BaseCrudDao<CedenteModalAbrangencia, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return CedenteModalAbrangencia.class;
	}

	/**
	 * Método que busca as CedenteModalAbrangencia de acordo com os filtros passados
	 * @param criteria
	 * @return ResultSetPage
	 */
	public ResultSetPage findPaginatedByCedenteModalAbrangencia(TypedFlatMap criteria){

		/** Define os parametros para paginação */
		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);

		/** Resgata os parâmetros do TypedFlatMap para serem filtrados no HQL */
		String tpAbrangencia = criteria.getString("tpAbrangencia");
		String tpModal = criteria.getString("tpModal");
		Long idCedente = criteria.getLong("cedente.idCedente");

		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(
				new StringBuffer()
					.append("new Map( cma.idCedenteModalAbrangencia as idCedenteModalAbrangencia, ")
					.append(" b.nmBanco as nmBanco, ")
					.append(" ab.nmAgenciaBancaria as nmAgenciaBancaria, ")
					.append(" c.nrContaCorrente as nrContaCorrente, ")
					.append("  c.dsCedente as dsCedente, ")
					.append(" cma.tpModal as tpModal, ")
					.append(" cma.tpAbrangencia as tpAbrangencia ) ")
					.toString()
			);

		sql.addFrom(getPersistentClass().getName(), " as cma JOIN cma.cedente AS c " +
				"JOIN c.agenciaBancaria as ab " +
				"JOIN ab.banco AS b ");

		/** Monta os filtros */
		sql.addCriteria("cma.tpAbrangencia", "=", tpAbrangencia);
		sql.addCriteria("cma.tpModal", "=", tpModal);
		sql.addCriteria("c.idCedente", "=", idCedente);

		sql.addOrderBy("b.nmBanco, ab.nmAgenciaBancaria, c.cdCedente, cma.tpModal, cma.tpAbrangencia");

		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(
				sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());

		return rsp;
	}

	/**
	 * Método que retorna o número de registros de acordo com os filtros passados
	 * @param criteria
	 * @return Integer
	 */
	public Integer getRowCountByCedenteModalAbrangencia(TypedFlatMap criteria) {
		/** Resgata os parâmetros do TypedFlatMap para serem filtrados no HQL */
		String tpAbrangencia = criteria.getString("tpAbrangencia");
		String tpModal = criteria.getString("tpModal");
		Long idCedente = criteria.getLong("cedente.idCedente");

		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("count(cma.idCedenteModalAbrangencia)");

		sql.addFrom(getPersistentClass().getName(), " as cma JOIN cma.cedente AS c " +
				"JOIN c.agenciaBancaria as ab " +
				"JOIN ab.banco AS b ");

		/** Monta os filtros */
		sql.addCriteria("cma.tpAbrangencia", "=", tpAbrangencia);
		sql.addCriteria("cma.tpModal", "=", tpModal);
		sql.addCriteria("c.idCedente", "=", idCedente);

		sql.addOrderBy("b.nmBanco, ab.nmAgenciaBancaria, c.cdCedente, cma.tpModal, cma.tpAbrangencia");

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(),sql.getCriteria());
		return result.intValue();
	}

	/** Método invocado pelo framework, seta os atributos que estão com lazy = true para false */
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("cedente", FetchMode.JOIN);
		lazyFindById.put("cedente.agenciaBancaria", FetchMode.JOIN);
		lazyFindById.put("cedente.agenciaBancaria.banco", FetchMode.JOIN);
	}

}