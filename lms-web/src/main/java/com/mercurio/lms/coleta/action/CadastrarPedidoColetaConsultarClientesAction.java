package com.mercurio.lms.coleta.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.coleta.cadastrarPedidoColetaConsultarClientesAction"
 */

public class CadastrarPedidoColetaConsultarClientesAction extends CrudAction {
	
	/**
	 * Declaração serviço principal da Action.
	 * 
	 * @param pedidoColetaService
	 */
	public void setService(ClienteService clienteService) {
		this.defaultService = clienteService;
	}	
	public ClienteService getService() {
		return (ClienteService)this.defaultService;
	}

	/**
	 * Método que retorna uma list de clientes.
	 * Possui telefone como criteria, sendo opcional. 
	 * @param TypedFlatMap criteria
	 * @return Integer
	 */
	public ResultSetPage findPaginatedClientesByTelefone(TypedFlatMap criteria) {		
		return this.getService().findPaginatedClientesByTelefone(criteria);
	}

	/**
	 * Método que retorna a quantidade de clientes. 
	 * Possui telefone como criteria, sendo opcional. 
	 * @param TypedFlatMap criteria
	 * @return Integer
	 */
	public Integer getRowCountClientesByTelefone(TypedFlatMap criteria) {		
		return this.getService().getRowCountClientesByTelefone(criteria);
	}

}
