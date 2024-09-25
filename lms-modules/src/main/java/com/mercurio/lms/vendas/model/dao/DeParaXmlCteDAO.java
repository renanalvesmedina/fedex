package com.mercurio.lms.vendas.model.dao;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.DeParaXmlCte;

public class DeParaXmlCteDAO extends BaseCrudDao<DeParaXmlCte, Long> {
	public ResultSetPage<Map<String, Object>> findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate sqlTemplate = getSqlTemplate(criteria);
		return getAdsmHibernateTemplate().findPaginated(sqlTemplate.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sqlTemplate.getCriteria());
	}

	@Override
	protected Class<DeParaXmlCte> getPersistentClass() {
		return DeParaXmlCte.class;
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		SqlTemplate sql = this.getSqlTemplateFindPaginated(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
	}

	public SqlTemplate getSqlTemplate(TypedFlatMap criteria) {
		SqlTemplate sqlTemplate = this.getSqlTemplateFindPaginated(criteria);
		sqlTemplate.addProjection("new map ("
				+ "deParaXmlCte.idDeParaXmlCte as idDeParaXmlCte, "
				+ "cliente.idCliente as idCliente, "
				+ "pessoa.tpIdentificacao as tpIdentificacao, "
				+ "pessoa.nrIdentificacao as nrIdentificacao, "
				+ "pessoa.nmPessoa as nmPessoa, "
				+ "pessoa.nmFantasia as nmFantasia, "
				+ "deParaXmlCte.blMatriz as blMatriz, "
				+ "deParaXmlCte.tpCampo as tpCampo, "
				+ "deParaXmlCte.nmTnt as nmTnt, "
				+ "deParaXmlCte.nmCliente as nmCliente "
				+ " ) ");
		sqlTemplate.addOrderBy("deParaXmlCte.nmTnt", "asc");
		return sqlTemplate;
	}

	private SqlTemplate getSqlTemplateFindPaginated(TypedFlatMap criteria) {
		StringBuffer from = new StringBuffer();
		/**
		 * from
		 */
		from.append(DeParaXmlCte.class.getName() + " as deParaXmlCte ");
		from.append(" inner join deParaXmlCte.cliente as cliente ");
		from.append(" inner join cliente.pessoa as pessoa ");
		SqlTemplate sqlTemplate = new SqlTemplate();
		sqlTemplate.addFrom(from.toString());
		/**
		 * where
		 */
		if(criteria!=null){
			sqlTemplate.addCriteria("cliente.idCliente", "=", criteria.getLong("idCliente"));
			sqlTemplate.addCriteria("deParaXmlCte.tpCampo", "=", criteria.getString("tpCampo"));
		}
		return sqlTemplate;
	}

	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
	}

	public String getByXNome(String xNome, Long idCliente) {
		SqlTemplate sqlTemplate = this.getSqlTemplateFindPaginated(null);
		sqlTemplate.addProjection("new map ("
				+ "deParaXmlCte.nmCliente as nmCliente "
				+ " ) ");
		sqlTemplate.addCriteria("cliente.idCliente", "=", idCliente);
		sqlTemplate.addCriteria("deParaXmlCte.nmTnt", "=", xNome);
		List<Map<String, Object>> campo = getAdsmHibernateTemplate().find(sqlTemplate.getSql(),sqlTemplate.getCriteria());
		return campo.size()>0?(String)campo.get(0).get("nmCliente"):null;
	}

	public boolean verificaSeJaExiste(String xNome, Long idCliente,Boolean blMatriz) {
		SqlTemplate sqlTemplate = this.getSqlTemplateFindPaginated(null);
		sqlTemplate.addProjection("new map ("
				+ "deParaXmlCte.nmCliente as nmCliente "
				+ " ) ");
		sqlTemplate.addCriteria("cliente.idCliente", "=", idCliente);
		sqlTemplate.addCriteria("deParaXmlCte.nmTnt", "=", xNome);
		sqlTemplate.addCriteria("deParaXmlCte.blMatriz", "=", blMatriz);
		List<Map<String, Object>> campo = getAdsmHibernateTemplate().find(sqlTemplate.getSql(),sqlTemplate.getCriteria());
		return campo!=null && campo.size()>0;
	}
}
