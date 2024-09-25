package com.mercurio.lms.vendas.model.dao;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.FilialEmbarcadoraLog;

/**
 * @spring.bean 
 */
public class FilialEmbarcadoraLogDAO extends BaseCrudDao<FilialEmbarcadoraLog, Long> {

	protected final Class getPersistentClass() {

		return FilialEmbarcadoraLog.class;
	}
	
	@Override
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("cliente.pessoa", FetchMode.SELECT);
		lazyFindPaginated.put("filial", FetchMode.SELECT);
		super.initFindPaginatedLazyProperties(lazyFindPaginated);
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		ResultSetPage rsp = super.findPaginated(criteria, findDef);
		
		FilterResultSetPage filter = new FilterResultSetPage(rsp){
			public Map filterItem(Object item){
				FilialEmbarcadoraLog log = (FilialEmbarcadoraLog) item;
				HashMap map = new HashMap();
				map.put("idFilialEmbarcadoraLog", log.getIdFilialEmbarcadoraLog());
				map.put("idFilialEmbarcadora", log.getFilialEmbarcadora().getIdFilialEmbarcadora());
				map.put("nmPessoa", log.getCliente().getPessoa().getNmPessoa());
				map.put("nrIdentificacao", FormatUtils.formatIdentificacao(log.getCliente().getPessoa()));
				map.put("sgFilial", log.getFilial().getSgFilial());
				
				map.put("dhLog", log.getDhLog());
				map.put("loginLog", log.getLoginLog());
				map.put("opLog", log.getOpLog());
				map.put("tpOrigemLog", log.getTpOrigemLog());
				
				return map;
			}
		};
		return (ResultSetPage)filter.doFilter();
	}
}