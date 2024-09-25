package com.mercurio.lms.vendas.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.model.service.TabelaDivisaoClienteLogService;
import com.mercurio.lms.vendas.model.service.TabelaDivisaoClienteService;

/**
 * @spring.bean id="lms.vendas.consultarTabelaDivisaoClienteLogAction"
 */
public class ConsultarTabelaDivisaoClienteLogAction extends CrudAction {
	TabelaDivisaoClienteService tabelaDivisaoClienteService;
	
	public TabelaDivisaoClienteService getTabelaDivisaoClienteService() {
		return tabelaDivisaoClienteService;
	}

	public void setTabelaDivisaoClienteService(
			TabelaDivisaoClienteService tabelaDivisaoClienteService) {
		this.tabelaDivisaoClienteService = tabelaDivisaoClienteService;
	}

	public void setService(TabelaDivisaoClienteLogService service) {
		this.defaultService = service;
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria) {
		return super.findPaginated(criteria);
	}
	
	public List findComboTabelas(TypedFlatMap criteria){
		List list = getTabelaDivisaoClienteService().findByDivisaoCliente(criteria.getLong("idDivisaoCliente"));

		
		FilterList filter = new FilterList(list) {
			public Map filterItem(Object obj) {
				TabelaDivisaoCliente tdc = (TabelaDivisaoCliente)obj;
				HashMap map = new HashMap();
				map.put("idTabelaDivisaoCliente", tdc.getIdTabelaDivisaoCliente());
				map.put("tabelaPrecoString", tdc.getTabelaPreco().getTabelaPrecoString());
				return map;
			}
		};
		return (List)filter.doFilter();
	}
}