package com.mercurio.lms.edi.dto;

import java.io.Serializable;
import java.util.Map;

import com.mercurio.lms.vendas.model.Cliente;

/**
 * @author JonasFE
 *
 */
public class ProcessarEdiDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Cliente clienteRemetente;
	private Long nrProcessamento;
	private Map<String, Object> parameters;

	public Cliente getClienteRemetente() {
		return clienteRemetente;
	}

	public void setClienteRemetente(Cliente clienteRemetente) {
		this.clienteRemetente = clienteRemetente;
	}

	public Long getNrProcessamento() {
		return nrProcessamento;
	}

	public void setNrProcessamento(Long nrProcessamento) {
		this.nrProcessamento = nrProcessamento;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

}
