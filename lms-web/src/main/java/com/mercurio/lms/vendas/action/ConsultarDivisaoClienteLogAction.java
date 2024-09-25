package com.mercurio.lms.vendas.action;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.DivisaoClienteLogService;

/**
 * @spring.bean id="lms.vendas.consultarDivisaoClienteLogAction"
 */
public class ConsultarDivisaoClienteLogAction extends CrudAction {

	private ClienteService clienteService;

	public void setService(DivisaoClienteLogService service) {

		this.defaultService = service;
	}
	
	public List findCliente(TypedFlatMap criteria){
		
		return clienteService.findClienteByNrIdentificacao(criteria.getString("pessoa.nrIdentificacao"));
	}
	
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
}