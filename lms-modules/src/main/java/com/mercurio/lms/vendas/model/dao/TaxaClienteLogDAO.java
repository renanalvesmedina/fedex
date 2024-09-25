package com.mercurio.lms.vendas.model.dao;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.lms.vendas.model.TaxaClienteLog;

/**
 * @spring.bean 
 */
public class TaxaClienteLogDAO extends BaseCrudDao<TaxaClienteLog, Long> {

	protected final Class getPersistentClass() {

		return TaxaClienteLog.class;
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		ResultSetPage rsp = super.findPaginated(criteria, findDef);
		FilterResultSetPage filter = new FilterResultSetPage(rsp){
			public Map filterItem(Object obj) {
				TaxaClienteLog log = (TaxaClienteLog)obj;
				
				HashMap map = new HashMap();
				map.put("idTaxaClienteLog", log.getIdTaxaClienteLog());
				map.put("idTaxaCliente", log.getTaxaCliente().getIdTaxaCliente());
				map.put("pcReajTaxa", log.getPcReajTaxa());
				map.put("pcReajVlExcedente", log.getPcReajVlExcedente());
				map.put("psMinimo", log.getPsMinimo());
				map.put("tpTaxaIndicador", log.getTpTaxaIndicador());
				map.put("vlExcedente", log.getVlExcedente());
				map.put("vlTaxa", log.getVlTaxa());
				
				//informações do log\
				map.put("loginLog" , log.getLoginLog());
				map.put("dhLog" , log.getDhLog());
				map.put("opLog" , log.getOpLog());
				map.put("tpOrigemLog" , log.getTpOrigemLog());
				return map;
			}
		};
		return (ResultSetPage)filter.doFilter();
	}
}
