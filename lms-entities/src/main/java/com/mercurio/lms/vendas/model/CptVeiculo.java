package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.joda.time.YearMonthDay;

public class CptVeiculo implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long idCptVeiculo;
	
	private CptTipoValor cptTipoValor;
	
	private YearMonthDay dtVigenciaInicial;

	private YearMonthDay dtVigenciaFinal;
	
	private Cliente cliente;
	
	private String nrFrota;
	
	private Boolean blVigenciaInicial;
	
	private Boolean blVigenciaFinal;

	public Long getIdCptVeiculo() {
		return idCptVeiculo;
	}

	public void setIdCptVeiculo(Long idCptVeiculo) {
		this.idCptVeiculo = idCptVeiculo;
	}

	public CptTipoValor getCptTipoValor() {
		return cptTipoValor;
	}

	public void setCptTipoValor(CptTipoValor cptTipoValor) {
		this.cptTipoValor = cptTipoValor;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getNrFrota() {
		return nrFrota;
	}

	public void setNrFrota(String nrFrota) {
		this.nrFrota = nrFrota;
	}

	public Boolean getBlVigenciaInicial() {
		return blVigenciaInicial;
	}

	public void setBlVigenciaInicial(Boolean blVigenciaInicial) {
		this.blVigenciaInicial = blVigenciaInicial;
	}

	public Boolean getBlVigenciaFinal() {
		return blVigenciaFinal;
	}

	public void setBlVigenciaFinal(Boolean blVigenciaFinal) {
		this.blVigenciaFinal = blVigenciaFinal;
	}

}
