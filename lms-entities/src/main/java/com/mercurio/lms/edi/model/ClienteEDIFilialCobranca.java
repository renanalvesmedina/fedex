package com.mercurio.lms.edi.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.mercurio.lms.vendas.model.Cliente;

public class ClienteEDIFilialCobranca implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long idClienteEDIFilialCobranca;
	
	private Cliente cliente;
	
	private Cliente clienteCobranca;

	public Long getIdClienteEDIFilialCobranca() {
		return idClienteEDIFilialCobranca;
	}

	public void setIdClienteEDIFilialCobranca(Long idClienteEDIFilialCobranca) {
		this.idClienteEDIFilialCobranca = idClienteEDIFilialCobranca;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Cliente getClienteCobranca() {
		return clienteCobranca;
	}

	public void setClienteCobranca(Cliente clienteCobranca) {
		this.clienteCobranca = clienteCobranca;
	}
	
	public Map<String, Object> getMapped(){
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("idClienteEDIFilialCobranca",
				this.getIdClienteEDIFilialCobranca());
		/*identificacao*/
		param.put("identificacao", this.clienteCobranca.getPessoa()
				.getNrIdentificacao());
		/*cliente*/
		param.put("cliente", this.clienteCobranca.getPessoa().getNmFantasia());
		
		return param;
	}

}
