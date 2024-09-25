package com.mercurio.lms.vendas.model.dao;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.lms.vendas.model.DivisaoClienteLog;

/**
 * @spring.bean 
 */
public class DivisaoClienteLogDAO extends BaseCrudDao<DivisaoClienteLog, Long> {

	@Override
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("cliente.pessoa", FetchMode.JOIN);
	}

	protected final Class getPersistentClass() {
		return DivisaoClienteLog.class;
	}	
	
	@Override
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		ResultSetPage rsp = super.findPaginated(criteria, findDef);
		FilterResultSetPage filter = new FilterResultSetPage(rsp){
			public Map filterItem(Object obj) {
				DivisaoClienteLog log = (DivisaoClienteLog)obj;
				Map map = new HashMap();
				map.put("idDivisaoClienteLog", log.getIdDivisaoClienteLog());
				map.put("idDivisaoCliente", log.getDivisaoCliente().getIdDivisaoCliente());
				map.put("cdDivisaoCliente", log.getCdDivisaoCliente());
				map.put("dsDivisaoCliente", log.getDsDivisaoCliente());
				map.put("dsNaturezaProduto",  log.getNaturezaProduto()==null? "":log.getNaturezaProduto().getDsNaturezaProduto());
				map.put("nrQtdeDocsRomaneio", log.getNrQtdeDocsRomaneio());
				map.put("tpSituacao", log.getTpSituacao());

				//informações do log
				map.put("dhLog", log.getDhLog());
				map.put("loginLog", log.getLoginLog());
				map.put("tpOrigemLog", log.getTpOrigemLog());
				map.put("opLog", log.getOpLog());
				return map;
			}
		};
		return (ResultSetPage)filter.doFilter();
	}
}
