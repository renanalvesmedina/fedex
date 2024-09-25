package com.mercurio.lms.vendas.action;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.service.ClienteLogService;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * @spring.bean id="lms.vendas.consultarClienteLogAction"
 */
public class ConsultarClienteLogAction extends CrudAction {
	
	private ClienteService clienteService;

	public void setService(ClienteLogService service) {

		this.defaultService = service;
	}
	
	public List findCliente(TypedFlatMap criteria){
		
		return clienteService.findClienteByNrIdentificacao(criteria.getString("pessoa.nrIdentificacao"));
	}
	
	public void setClienteService(ClienteService clienteService) {

		this.clienteService = clienteService;
	}
}