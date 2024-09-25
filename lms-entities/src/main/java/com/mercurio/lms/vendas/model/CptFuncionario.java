package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.joda.time.YearMonthDay;

public class CptFuncionario implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idCptFuncionario;

	private CptTipoValor cptTipoValor;

	private Cliente cliente;

	private YearMonthDay dtVigenciaInicial;

	private YearMonthDay dtVigenciaFinal;
	
	private Boolean blVigenciaInicial;
	
	private Boolean blVigenciaFinal;	

	private String nrMatricula;

	public Long getIdCptFuncionario() {
		return idCptFuncionario;
	}

	public void setIdCptFuncionario(Long idCptFuncionario) {
		this.idCptFuncionario = idCptFuncionario;
	}

	public CptTipoValor getCptTipoValor() {
		return cptTipoValor;
	}

	public void setCptTipoValor(CptTipoValor cptTipoValor) {
		this.cptTipoValor = cptTipoValor;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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

	public String getNrMatricula() {
		return nrMatricula;
	}

	public void setNrMatricula(String nrMatricula) {
		this.nrMatricula = nrMatricula;
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
