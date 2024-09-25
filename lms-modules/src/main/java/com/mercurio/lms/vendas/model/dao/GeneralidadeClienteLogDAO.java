package com.mercurio.lms.vendas.model.dao;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.lms.vendas.model.GeneralidadeClienteLog;

/**
 * @spring.bean 
 */
public class GeneralidadeClienteLogDAO extends BaseCrudDao<GeneralidadeClienteLog, Long> {

	protected final Class getPersistentClass() {
		return GeneralidadeClienteLog.class;
	}
	
	@Override
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("parcelaPreco", FetchMode.SELECT);
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		ResultSetPage rsp = super.findPaginated(criteria, findDef);
		FilterResultSetPage filter = new FilterResultSetPage(rsp){
			public Map filterItem(Object obj){
				GeneralidadeClienteLog log = (GeneralidadeClienteLog)obj;
				HashMap map = new HashMap();
				map.put("dhLog", log.getDhLog());
				map.put("loginLog", log.getLoginLog());
				map.put("tpOrigemLog", log.getTpOrigemLog());
				map.put("opLog", log.getOpLog());
				
				map.put("nmParcelaPreco", log.getParcelaPreco().getNmParcelaPreco());
				map.put("tpIndicador", log.getTpIndicador());
				map.put("vlGeneralidade", log.getVlGeneralidade());
				map.put("pcReajGeneralidade", log.getPcReajGeneralidade());
				return map;
			}
		};
		
		return (ResultSetPage) filter.doFilter();
	}
}
