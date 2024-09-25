package com.mercurio.lms.edi.swt.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.ClienteEDIFilial;
import com.mercurio.lms.edi.model.service.ClienteEDIFilialService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.vendas.model.Cliente;

public class ManterClienteEDIFiliaisAction {
	
	private ClienteEDIFilialService clienteEDIFilialService;
	
	/**
	 * Persiste a entidade ClienteEDIFilial
	 * 
	 * @param param
	 * @return Map
	 */
	public Map<String, Object> store(Map<String, Object> param) {
		
		ClienteEDIFilial pojo = new ClienteEDIFilial();
		
		/*cliente*/
		Cliente cliente = new Cliente();
		cliente.setIdCliente((Long)param.get("idClienteEDI"));
		pojo.setCliente(cliente);
		
		/*clienteFilial*/
		Cliente clienteFilial = new Cliente();
		clienteFilial.setIdCliente((Long)param.get("idCliente"));
		pojo.setClienteFilial(clienteFilial);
		
		/*filial*/
		Filial filial = new Filial();
		filial.setIdFilial((Long)param.get("idFilial"));
		pojo.setFilial(filial);
	
		/*seleção*/
		pojo.setTpSelecao(new DomainValue((String)param.get("selecao")));
		
		clienteEDIFilialService.store(pojo);
		
		return param;				
	}	
	
	
	/**
	 * Paginação da Aba Filiais (Tela Manter Cliente EDI) 
	 * 
	 * @param criteria
	 * @return ResultSetPage
	 */
	public ResultSetPage<Map<String, Object>> findPaginated(Map<String, Object> criteria) {
		ResultSetPage rsp = clienteEDIFilialService.findPaginated(new PaginatedQuery(criteria));					
		
		List<ClienteEDIFilial> list = rsp.getList();		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(list.size());
		
		for(ClienteEDIFilial filial : list){			
 			retorno.add(filial.getMapped());
		}
				
		rsp.setList(retorno);
		return rsp;		
	}

	/**
	 * Remove uma lista de Embarcadoras
	 * @param ids
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		clienteEDIFilialService.removeByIds(ids);
	}		

	public void setClienteEDIFilialService(
			ClienteEDIFilialService clienteEDIFilialService) {
		this.clienteEDIFilialService = clienteEDIFilialService;
	}

}
