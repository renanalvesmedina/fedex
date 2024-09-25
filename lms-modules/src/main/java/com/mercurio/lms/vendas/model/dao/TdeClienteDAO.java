package com.mercurio.lms.vendas.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.vendas.model.TdeCliente;
import com.mercurio.lms.vendas.model.DestinatarioTdeCliente;

public class TdeClienteDAO extends BaseCrudDao<TdeCliente, Long> {

    @Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("divisaoCliente", FetchMode.JOIN);
		lazyFindById.put("divisaoCliente.cliente", FetchMode.JOIN);		
		lazyFindById.put("divisaoCliente.cliente.pessoa", FetchMode.JOIN);		
		lazyFindById.put("destinatarioTdeClientes", FetchMode.JOIN);
		lazyFindById.put("destinatarioTdeClientes.cliente", FetchMode.JOIN);
		lazyFindById.put("destinatarioTdeClientes.cliente.pessoa", FetchMode.JOIN);
	}
    
	@Override
	protected Class getPersistentClass() {
		return TdeCliente.class;
	}

	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate sql = getSqlTemplateFindPaginated(criteria);
		
		StringBuilder countDestinatario = new StringBuilder();
		countDestinatario
		.append("(SELECT COUNT(dtc2) FROM ")
		.append(DestinatarioTdeCliente.class.getSimpleName())
		.append(" dtc2 ")
		.append(" JOIN dtc2.tdeCliente tde2")
		.append(" WHERE tde2.idTdeCliente=tde.idTdeCliente ")
		.append(")")
		.append(" as totalDestinatario");
		
		
    	StringBuilder projecao = new StringBuilder();
		projecao
		.append("new map( ")
		.append("tde.idTdeCliente as idTdeCliente, ")
		.append("p.nmPessoa as nmCliente, ")
		.append("dc.dsDivisaoCliente as dsDivisaoCliente, ")
		.append("tde.dtVigenciaInicial as dtVigenciaInicial, ")
		.append("tde.dtVigenciaFinal as dtVigenciaFinal, ")
		.append(countDestinatario)
		.append(")")
		;
		
		sql.addProjection(projecao.toString());
		
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria());
	}

	private SqlTemplate getSqlTemplateFindPaginated(TypedFlatMap criteria) {
		StringBuilder from = new StringBuilder();
		
		from
		.append(getPersistentClass().getSimpleName()).append(" tde ")
		.append("JOIN tde.divisaoCliente dc ")
		.append("JOIN dc.cliente c ")
		.append("JOIN c.pessoa p ")
		.append("JOIN tde.destinatarioTdeClientes dtc ")
		.append("JOIN dtc.cliente cDest ")
		.append("JOIN cDest.pessoa pDest ")
		;
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addFrom(from.toString());
		sql.addCriteria("dc.idDivisaoCliente", "=", criteria.getLong("divisaoCliente.idDivisaoCliente"));
		
		return sql;
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		SqlTemplate sql = getSqlTemplateFindPaginated(criteria);
		
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
	}

	public TdeCliente findByIdDivisaoCliente(Long idDivisaoCliente, Long idCliente, YearMonthDay dtVigente) {
		StringBuilder sql = new StringBuilder();
		
		sql
		.append("SELECT tde ")
		.append("FROM ")
		.append(getPersistentClass().getSimpleName()).append(" tde ")
		.append("JOIN tde.divisaoCliente dc ")
		.append("JOIN dc.cliente c ")
		.append("WHERE ")
		.append("	 tde.dtVigenciaInicial  <=:dtVigente ")
		.append("AND tde.dtVigenciaFinal >=:dtVigente ")
		;
		
		TypedFlatMap map = new TypedFlatMap();
		map.put("dtVigente", dtVigente);

		if (LongUtils.hasValue(idDivisaoCliente)) {
			sql.append(" AND dc.idDivisaoCliente =:idDivisaoCliente ");
			map.put("idDivisaoCliente", idDivisaoCliente);
		}else{
			sql.append(" AND dc.idDivisaoCliente is null ");
		}
		
		if (LongUtils.hasValue(idCliente)) {
			sql.append(" AND c.idCliente =:idCliente ");
			map.put("idCliente", idCliente);
		}
		
		List<TdeCliente> list = getAdsmHibernateTemplate().findByNamedParam(sql.toString(), map);
		
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		
		return null;
	}
	
	
}
