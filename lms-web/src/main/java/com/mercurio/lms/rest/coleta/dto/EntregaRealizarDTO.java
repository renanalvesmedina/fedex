package com.mercurio.lms.rest.coleta.dto;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;

public class EntregaRealizarDTO extends BaseDTO {
	
	private static final long serialVersionUID = 1L;
	
	private Long idDoctoServico;
	private String nrDoctoServico;
	private String nrManifesto;
	private String preAwbAwb;
	private String otd;
	private Boolean entregaDireta;
	private String nmCliente; 
	private String endereco;
	private Integer volume;
	private BigDecimal peso;
	private BigDecimal valor;
	private String veiculo;
	private String equipe;
	private String situacao;
	private String dtHrEvento;
	private DomainValue tpStatusManifesto;
	private Short cdOcorrenciaEntrega;
	private DateTime dhOcorrencia;
	private String nmRecebedor;
	private Boolean isEntregaAeroporto;
	
	public Long getIdDoctoServico() {
		return idDoctoServico;
	}
	public void setIdDoctoServico(Long idDoctoServico) {
		this.idDoctoServico = idDoctoServico;
	}
	public String getNrDoctoServico() {
		return nrDoctoServico;
	}
	public void setNrDoctoServico(String nrDoctoServico) {
		this.nrDoctoServico = nrDoctoServico;
	}
	
	public String getPreAwbAwb() {
		return preAwbAwb;
	}
	public void setPreAwbAwb(String preAwbAwb) {
		this.preAwbAwb = preAwbAwb;
	}
	public String getOtd() {
		return otd;
	}
	public void setOtd(String otd) {
		this.otd = otd;
	}
	public Boolean getEntregaDireta() {
		return entregaDireta;
	}
	public void setEntregaDireta(Boolean entregaDireta) {
		this.entregaDireta = entregaDireta;
	}
	public String getNmCliente() {
		return nmCliente;
	}
	public void setNmCliente(String nmCliente) {
		this.nmCliente = nmCliente;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public Integer getVolume() {
		return volume;
	}
	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	public BigDecimal getPeso() {
		return peso;
	}
	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public String getVeiculo() {
		return veiculo;
	}
	public void setVeiculo(String veiculo) {
		this.veiculo = veiculo;
	}
	public String getEquipe() {
		return equipe;
	}
	public void setEquipe(String equipe) {
		this.equipe = equipe;
	}
	public String getNrManifesto() {
		return nrManifesto;
	}
	public void setNrManifesto(String nrManifesto) {
		this.nrManifesto = nrManifesto;
	}
	public String getSituacao() {
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	public String getDtHrEvento() {
		return dtHrEvento;
	}
	public void setDtHrEvento(String dtHrEvento) {
		this.dtHrEvento = dtHrEvento;
	}
	public DomainValue getTpStatusManifesto() {
		return tpStatusManifesto;
	}
	public void setTpStatusManifesto(DomainValue tpStatusManifesto) {
		this.tpStatusManifesto = tpStatusManifesto;
	}
	public Short getCdOcorrenciaEntrega() {
		return cdOcorrenciaEntrega;
	}
	public void setCdOcorrenciaEntrega(Short cdOcorrenciaEntrega) {
		this.cdOcorrenciaEntrega = cdOcorrenciaEntrega;
	}
	public DateTime getDhOcorrencia() {
		return dhOcorrencia;
	}
	public void setDhOcorrencia(DateTime dhOcorrencia) {
		this.dhOcorrencia = dhOcorrencia;
	}
	public String getNmRecebedor() {
		return nmRecebedor;
	}
	public void setNmRecebedor(String nmRecebedor) {
		this.nmRecebedor = nmRecebedor;
	}
	public Boolean getIsEntregaAeroporto() {
		return isEntregaAeroporto;
	}
	public void setIsEntregaAeroporto(Boolean isEntregaAeroporto) {
		this.isEntregaAeroporto = isEntregaAeroporto;
	}
}
