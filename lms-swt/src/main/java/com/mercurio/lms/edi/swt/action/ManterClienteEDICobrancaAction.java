package com.mercurio.lms.edi.swt.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.edi.model.ClienteEDIFilialCobranca;
import com.mercurio.lms.edi.model.service.ClienteEDIFilialCobrancaService;
import com.mercurio.lms.vendas.model.Cliente;

public class ManterClienteEDICobrancaAction {
	
	private ClienteEDIFilialCobrancaService clienteEDIFilialCobrancaService;

	
	/**
	 * Persiste a entidade ClienteEDIFilialCobranca
	 * 
	 * @param param
	 * @return Map
	 */
	public Map<String, Object> store(Map<String, Object> param) {
		
		ClienteEDIFilialCobranca pojo = new ClienteEDIFilialCobranca();
		
		/*cliente*/
		Cliente cliente = new Cliente();
		cliente.setIdCliente((Long)param.get("idClienteEDI"));
		pojo.setCliente(cliente);
		
		/*clienteEmbarcador*/
		Cliente clienteCobranca = new Cliente();
		clienteCobranca.setIdCliente((Long)param.get("idCliente"));
		pojo.setClienteCobranca(clienteCobranca);
		
		clienteEDIFilialCobrancaService.store(pojo);
		
		return param;				
	}	
	
	/**
	 * Paginação da Aba Cobranca (Tela Manter Cliente EDI) 
	 * 
	 * @param criteria
	 * @return ResultSetPage
	 */
	public ResultSetPage<Map<String, Object>> findPaginated(Map<String, Object> criteria) {
		ResultSetPage rsp = clienteEDIFilialCobrancaService.findPaginated(new PaginatedQuery(criteria));					
		
		List<ClienteEDIFilialCobranca > list = rsp.getList();		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(list.size());
		
		for(ClienteEDIFilialCobranca  cobranca : list){
 			retorno.add(cobranca.getMapped());
		}
				
		rsp.setList(retorno);
		return rsp;		
	}	
	
	/**
	 * Remove uma lista de Cobrancas
	 * @param ids
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		clienteEDIFilialCobrancaService.removeByIds(ids);
	}	
	
	public ClienteEDIFilialCobrancaService getClienteEDIFilialCobrancaService() {
		return clienteEDIFilialCobrancaService;
	}

	public void setClienteEDIFilialCobrancaService(
			ClienteEDIFilialCobrancaService clienteEDIFilialCobrancaService) {
		this.clienteEDIFilialCobrancaService = clienteEDIFilialCobrancaService;
	}

}
