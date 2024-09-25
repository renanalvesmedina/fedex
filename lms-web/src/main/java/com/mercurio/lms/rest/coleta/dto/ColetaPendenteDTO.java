package com.mercurio.lms.rest.coleta.dto;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import com.mercurio.adsm.rest.BaseDTO;
 
public class ColetaPendenteDTO extends BaseDTO { 
	private static final long serialVersionUID = 1L;
	
	private String coleta;
	private String preAwbAwb;
	private Boolean entregaDireta;
	private String otd;
	private String cliente;
	private String endereco;
	private Long volume;
	private BigDecimal peso;
	private BigDecimal valor;
	private DateTime horarioColeta;
	private String veiculo;
	private String equipe;
	private Long idAwb;
	private Long idPedidoColeta;
	private Integer nrDocumentos;
	private Long idMeioTransporte;
	
	public String getColeta() {
		return coleta;
	}
	public void setColeta(String coleta) {
		this.coleta = coleta;
	}
	public String getPreAwbAwb() {
		return preAwbAwb;
	}
	public void setPreAwbAwb(String preAwbAwb) {
		this.preAwbAwb = preAwbAwb;
	}
	public Boolean getEntregaDireta() {
		return entregaDireta;
	}
	public void setEntregaDireta(Boolean entregaDireta) {
		this.entregaDireta = entregaDireta;
	}
	public String getOtd() {
		return otd;
	}
	public void setOtd(String otd) {
		this.otd = otd;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public Long getVolume() {
		return volume;
	}
	public void setVolume(Long volume) {
		this.volume = volume;
	}
	public Long getIdAwb() {
		return idAwb;
	}
	public void setIdAwb(Long idAwb) {
		this.idAwb = idAwb;
	}
	public Long getIdPedidoColeta() {
		return idPedidoColeta;
	}
	public void setIdPedidoColeta(Long idPedidoColeta) {
		this.idPedidoColeta = idPedidoColeta;
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
	public DateTime getHorarioColeta() {
		return horarioColeta;
	}
	public void setHorarioColeta(DateTime horarioColeta) {
		this.horarioColeta = horarioColeta;
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
	public Integer getNrDocumentos() {
		return nrDocumentos;
	}
	public void setNrDocumentos(Integer nrDocumentos) {
		this.nrDocumentos = nrDocumentos;
	}
	public Long getIdMeioTransporte() {
		return idMeioTransporte;
	}
	public void setIdMeioTransporte(Long idMeioTransporte) {
		this.idMeioTransporte = idMeioTransporte;
	}
	
} 
