package com.mercurio.lms.edi.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.vendas.model.Cliente;

public class ClienteEDIFilial implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idClienteEDIFilial;
		
	private DomainValue tpSelecao;
	
	private Cliente cliente;

	private Cliente clienteFilial;
	
	private Filial filial;

	public Long getIdClienteEDIFilial() {
		return idClienteEDIFilial;
	}

	public void setIdClienteEDIFilial(Long idClienteEDIFilial) {
		this.idClienteEDIFilial = idClienteEDIFilial;
	}

	public DomainValue getTpSelecao() {
		return tpSelecao;
	}

	public void setTpSelecao(DomainValue tpSelecao) {
		this.tpSelecao = tpSelecao;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Cliente getClienteFilial() {
		return clienteFilial;
	}

	public void setClienteFilial(Cliente clienteFilial) {
		this.clienteFilial = clienteFilial;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}
	
	public Map<String , Object> getMapped(){
		
		Map<String , Object> param = new HashMap<String, Object>();
		
		/*idClienteEDIFilial*/
		param.put("idClienteEDIFilial", this.getIdClienteEDIFilial());
		/*identificacao*/
		param.put("identificacao", this.clienteFilial.getPessoa()
				.getNrIdentificacao());
		/*cliente*/
		param.put("cliente", this.clienteFilial.getPessoa().getNmFantasia());
		/*filial*/
		param.put("filial", this.filial.getPessoa().getNmFantasia());
		/*selecao*/
		param.put("selecao", this.tpSelecao.getDescription().getValue());
		
		return param;
	}

}
