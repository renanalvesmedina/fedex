package com.mercurio.lms.vol.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vol.model.VolFabricante;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class VolFabricanteDAO extends BaseCrudDao<VolFabricante, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return VolFabricante.class;
	}

	public Integer getRowCountFabricante(TypedFlatMap criteria) {
		SqlTemplate sql = this.createHQLFabricante(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(),
				sql.getCriteria());
	}

	public ResultSetPage findPaginatedFabricante(TypedFlatMap criteria,
			FindDefinition fd) {
		SqlTemplate sql = this.createHQLFabricante(criteria);

		StringBuffer projecao = new StringBuffer()
			.append("new map(")
			.append("p.idPessoa as idPessoa, ")
			.append("f.idFabricante as idFabricante, ")
			.append("p.tpIdentificacao as tpIdentificacao, ")
			.append("p.nrIdentificacao as nrIdentificacao, ")
			.append("p.nmPessoa as nmPessoa, ")
			.append("f.tpSituacao as tpSituacao, ")
			.append("p.dsEmail as dsEmail) ");
		sql.addProjection(projecao.toString());
		sql.addOrderBy("p.nmPessoa");

		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),
				fd.getCurrentPage(), fd.getPageSize(), sql.getCriteria());
	}

	private SqlTemplate createHQLFabricante(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate();

		sql.addFrom(VolFabricante.class.getName(), "as f INNER JOIN f.pessoa as p LEFT JOIN f.contato as c");

		sql.addCriteria("p.nrIdentificacao", "=", criteria.get("pessoa.nrIdentificacao"));
		sql.addCriteria("lower(p.nmPessoa)", "LIKE", criteria.getString("nmPessoa").toLowerCase());
		sql.addCriteria("f.tpSituacao", "=", criteria.get("tpSituacao"));
		sql.addCriteria("p.tpIdentificacao", "=", criteria.get("pessoa.tpIdentificacao"));

		return sql;

	}

	public List findLookupFabricante(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate() ;

		StringBuffer sb = new StringBuffer();
		sb.append(" VolFabricante vf ");
		sb.append("inner join vf.pessoa pes ");

		sql.addFrom(sb.toString());

		sql.addCriteria("pes.nrIdentificacao","=", criteria.getString("pessoa.nrIdentificacao"));

	 	StringBuffer sbf = new StringBuffer();
	 	sbf.append("new Map(vf.idFabricante as idFabricante,");
	 	sbf.append("pes.nmPessoa as nmPessoa,");
	 	sbf.append("pes.tpIdentificacao as tpIdentificacao,");
	 	sbf.append("pes.nrIdentificacao as nrIdentificacao)");
	 	sql.addProjection(sbf.toString());

		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria()); 
	}

	public void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("pessoa", FetchMode.JOIN);
	}

	public void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("pessoa", FetchMode.JOIN);
		lazyFindById.put("contato", FetchMode.JOIN);
	}

}