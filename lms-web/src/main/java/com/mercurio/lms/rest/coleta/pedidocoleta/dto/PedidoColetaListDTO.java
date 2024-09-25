package com.mercurio.lms.rest.coleta.pedidocoleta.dto;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;

public class PedidoColetaListDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private String coleta;
	private ClienteSuggestDTO cliente;
	private DomainValue tipoColeta;
	private DateTime solicitacao;
	private BigDecimal peso;
	private Integer volumes;
	private String moeda;
	private BigDecimal valor;
	private DomainValue status;
	private DomainValue modoColeta;
	private String funcionario;

	public String getColeta() {
		return coleta;
	}

	public void setColeta(String coleta) {
		this.coleta = coleta;
	}

	public ClienteSuggestDTO getCliente() {
		return cliente;
	}

	public void setCliente(ClienteSuggestDTO cliente) {
		this.cliente = cliente;
	}

	public DomainValue getTipoColeta() {
		return tipoColeta;
	}

	public void setTipoColeta(DomainValue tipoColeta) {
		this.tipoColeta = tipoColeta;
	}

	public DateTime getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(DateTime solicitacao) {
		this.solicitacao = solicitacao;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}

	public Integer getVolumes() {
		return volumes;
	}

	public void setVolumes(Integer volumes) {
		this.volumes = volumes;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public DomainValue getStatus() {
		return status;
	}

	public void setStatus(DomainValue status) {
		this.status = status;
	}

	public DomainValue getModoColeta() {
		return modoColeta;
	}

	public void setModoColeta(DomainValue modoColeta) {
		this.modoColeta = modoColeta;
	}

	public String getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(String funcionario) {
		this.funcionario = funcionario;
	}

	public String getMoeda() {
		return moeda;
	}

	public void setMoeda(String moeda) {
		this.moeda = moeda;
	}
	
}
