package com.mercurio.lms.edi.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.vendas.model.Cliente;

public class ClienteEDIFilialEmbarcadora implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long idClienteEDIFilialEmbarcadora;
	
	private Cliente cliente;
	
	private Cliente clienteEmbarcador;
	
	private Filial filial;

	public Long getIdClienteEDIFilialEmbarcadora() {
		return idClienteEDIFilialEmbarcadora;
	}

	public void setIdClienteEDIFilialEmbarcadora(
			Long idClienteEDIFilialEmbarcadora) {
		this.idClienteEDIFilialEmbarcadora = idClienteEDIFilialEmbarcadora;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Cliente getClienteEmbarcador() {
		return clienteEmbarcador;
	}

	public void setClienteEmbarcador(Cliente clienteEmbarcador) {
		this.clienteEmbarcador = clienteEmbarcador;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}
	
	public Map<String, Object> getMapped(){
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		/*idClienteEDIFilialEmbarcadora*/
		param.put("idClienteEDIFilialEmbarcadora",
				this.getIdClienteEDIFilialEmbarcadora());
		/*identificacao*/
		param.put("identificacao", this.clienteEmbarcador.getPessoa()
				.getNrIdentificacao());
		/*cliente*/
		param.put("cliente", this.clienteEmbarcador.getPessoa().getNmFantasia());
		/*filial*/
		param.put("filial", this.filial.getPessoa().getNmFantasia());
		
		return param;
	}

}
