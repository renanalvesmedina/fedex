package com.mercurio.lms.vendas.model.dao;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.TabelaDivisaoClienteLog;

/**
 * @spring.bean 
 */
public class TabelaDivisaoClienteLogDAO extends BaseCrudDao<TabelaDivisaoClienteLog, Long> {

	protected final Class getPersistentClass(){
		return TabelaDivisaoClienteLog.class;
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria,FindDefinition findDef) {
		ResultSetPage rsp = super.findPaginated(criteria, findDef);
		HashMap map = new HashMap();
		map.put("tabelaPreco", FetchMode.SELECT);
		map.put("tabelaPreco.tipoTabelaPreco", FetchMode.SELECT);
		map.put("tabelaPreco.subtipoTabelaPreco", FetchMode.SELECT);
		map.put("servico", FetchMode.SELECT);
		this.initilizeResultSetPage(rsp, map);
		
		FilterResultSetPage frsp = new FilterResultSetPage(rsp){
			public Map filterItem(Object item) {
				TabelaDivisaoClienteLog tc = (TabelaDivisaoClienteLog)item;
				TypedFlatMap row = new TypedFlatMap();
				row.put("tabelaPreco.tabelaPrecoString",tc.getTabelaPreco().getTabelaPrecoString());
				row.put("servico.dsServico",tc.getServico().getDsServico());
				row.put("blAtualizacaoAutomatica",tc.isBlAtualizacaoAutomatica());
				row.put("blObrigaDimensoes",tc.isBlObrigaDimensoes());
				row.put("blPagaFreteTonelada",tc.isBlPagaFreteTonelada());
				row.put("pcAumento",tc.getPcAumento());
				row.put("tpOrigemLog",tc.getTpOrigemLog());
				row.put("loginLog",tc.getLoginLog());
				row.put("dhLog",tc.getDhLog());
				row.put("opLog",tc.getOpLog());
				return row;
			}
		};
		return (ResultSetPage)frsp.doFilter();
	}	
}