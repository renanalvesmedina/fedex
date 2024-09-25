package com.mercurio.lms.rest.vendas.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class GrupoEconomicoClienteDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private ClienteSuggestDTO cliente;

	public GrupoEconomicoClienteDTO() {
		super();
	}

	public ClienteSuggestDTO getCliente() {
		return cliente;
	}

	public void setCliente(ClienteSuggestDTO cliente) {
		this.cliente = cliente;
	}

}
