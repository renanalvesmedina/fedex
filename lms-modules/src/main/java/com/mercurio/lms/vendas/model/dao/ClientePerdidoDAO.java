package com.mercurio.lms.vendas.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.ClientePerdido;

public class ClientePerdidoDAO extends BaseCrudDao<ClientePerdido, Long>{
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return ClientePerdido.class;
	}
	
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("cliente", FetchMode.JOIN);
		lazyFindById.put("cliente.pessoa", FetchMode.JOIN);
		lazyFindById.put("segmentoMercado", FetchMode.JOIN);
		lazyFindById.put("ramoAtividade", FetchMode.JOIN);
		
		lazyFindById.put("filial", FetchMode.JOIN);
		lazyFindById.put("moeda", FetchMode.JOIN);
		lazyFindById.put("filial.pessoa", FetchMode.JOIN);
		
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		SqlTemplate sql = montaSqlPaginated(criteria);
		int total = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria()).size();
		return Integer.valueOf(total);
	}

	public ResultSetPage findPaginatedCustom(Map criteria, FindDefinition findDef) {
		SqlTemplate sql = montaSqlPaginated((TypedFlatMap) criteria);
		sql.addProjection("new Map(f.sgFilial", "sgFilial");
		sql.addProjection("p_c.nmPessoa", "nmPessoa");
		sql.addProjection("cp.dtPerda", "dtPerda");
		sql.addProjection("cp.idClientePerdido", "idClientePerdido");
		sql.addProjection("cp.tpMotivoPerda", "tpMotivoPerda)");
		sql.addOrderBy("f.sgFilial");
		sql.addOrderBy("p_c.nmPessoa");
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
	}	
		
	private SqlTemplate montaSqlPaginated(TypedFlatMap criteria){
		SqlTemplate sql = new SqlTemplate();
		
		StringBuffer from = new StringBuffer(ClientePerdido.class.getName() + " cp ");
		from.append(" inner join cp.filial f " +
				"inner join cp.segmentoMercado sm " +
				"inner join cp.cliente c " +
				"inner join c.pessoa p_c ");
		
		sql.addFrom(from.toString());
		if(criteria.getLong("filial.idFilial")!= null){
			sql.addCriteria("f.idFilial", "=", criteria.getLong("filial.idFilial"));
		}
		if(criteria.getLong("cliente.idCliente")!= null){
			sql.addCriteria("c.idCliente", "=", criteria.getLong("cliente.idCliente"));
		}
		if(!criteria.getDomainValue("tpAbrangencia").getValue().equalsIgnoreCase("")){
			sql.addCriteria("cp.tpAbrangencia", "=", criteria.getDomainValue("tpAbrangencia").getValue());
		}
		if(!criteria.getDomainValue("tpMotivoPerda").getValue().equalsIgnoreCase("")){
			sql.addCriteria("cp.tpMotivoPerda", "=", criteria.getDomainValue("tpMotivoPerda").getValue());
		}
		if(criteria.getLong("segmentoMercado.idSegmentoMercado")!= null){
			sql.addCriteria("sm.idSegmentoMercado", "=", criteria.getLong("segmentoMercado.idSegmentoMercado"));
		}
		if(criteria.getYearMonthDay("dtInicial")!= null){
			sql.addCriteria("cp.dtPerda", ">=", criteria.getYearMonthDay("dtInicial"));
		}
		if(criteria.getYearMonthDay("dtFinal")!= null){
			sql.addCriteria("cp.dtPerda", "<=", criteria.getYearMonthDay("dtFinal"));
		}
		return sql;
	}	
	
}
