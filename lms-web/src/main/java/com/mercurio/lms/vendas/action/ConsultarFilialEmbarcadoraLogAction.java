package com.mercurio.lms.vendas.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.FilialEmbarcadora;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.FilialEmbarcadoraLogService;
import com.mercurio.lms.vendas.model.service.FilialEmbarcadoraService;

/**
 * @spring.bean id="lms.vendas.consultarFilialEmbarcadoraLogAction"
 */
public class ConsultarFilialEmbarcadoraLogAction extends CrudAction {

	private ClienteService clienteService;
	private FilialEmbarcadoraService filialEmbarcadoraService;

	public void setService(FilialEmbarcadoraLogService service) {
		this.defaultService = service;
	}
	
	public List findCliente(TypedFlatMap criteria){
		return clienteService.findClienteByNrIdentificacao(criteria.getString("pessoa.nrIdentificacao"));
	}
	
	public FilialEmbarcadoraService getFilialEmbarcadoraService() {
		return filialEmbarcadoraService;
	}

	public void setFilialEmbarcadoraService(FilialEmbarcadoraService filialEmbarcadoraService) {
		this.filialEmbarcadoraService = filialEmbarcadoraService;
	}

	public List findComboFiliais(TypedFlatMap data){
		List list = getFilialEmbarcadoraService().findComboFiliais(data.getLong("idCliente")); 
		
		FilterList filter = new FilterList(list){
			public Map filterItem(Object item){
				FilialEmbarcadora filial = (FilialEmbarcadora)item;
				HashMap map = new HashMap();
				map.put("idFilialEmbarcadora", filial.getIdFilialEmbarcadora());
				map.put("sgFilial",filial.getFilial().getSgFilial());
				return map;
			}
		};
		
		return (List)filter.doFilter(); 
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria) {
		TypedFlatMap newCriteria = new TypedFlatMap();
		newCriteria.put("idFilialEmbarcadora", criteria.get("idFilialEmbarcadora"));
		return super.findPaginated(criteria);
	}
	
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
}