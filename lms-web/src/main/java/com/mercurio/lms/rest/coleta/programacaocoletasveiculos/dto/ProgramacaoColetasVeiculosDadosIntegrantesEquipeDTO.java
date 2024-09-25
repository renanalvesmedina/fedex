package com.mercurio.lms.rest.coleta.programacaocoletasveiculos.dto;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;

public class ProgramacaoColetasVeiculosDadosIntegrantesEquipeDTO extends BaseDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long idIntegranteEqOperac;
	private DomainValue tpIntegrante;
	private String dsCargo;
	private String nmPessoa;
	private String nrMatricula;
	private String nrIdentificacaoFormatado;
	private String nmEmpresa;
	
	public Long getIdIntegranteEqOperac() {
		return idIntegranteEqOperac;
	}
	public void setIdIntegranteEqOperac(Long idIntegranteEqOperac) {
		this.idIntegranteEqOperac = idIntegranteEqOperac;
	}
	public DomainValue getTpIntegrante() {
		return tpIntegrante;
	}
	public void setTpIntegrante(DomainValue tpIntegrante) {
		this.tpIntegrante = tpIntegrante;
	}
	public String getDsCargo() {
		return dsCargo;
	}
	public void setDsCargo(String dsCargo) {
		this.dsCargo = dsCargo;
	}
	public String getNmPessoa() {
		return nmPessoa;
	}
	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}
	public String getNrMatricula() {
		return nrMatricula;
	}
	public void setNrMatricula(String nrMatricula) {
		this.nrMatricula = nrMatricula;
	}
	public String getNrIdentificacaoFormatado() {
		return nrIdentificacaoFormatado;
	}
	public void setNrIdentificacaoFormatado(String nrIdentificacaoFormatado) {
		this.nrIdentificacaoFormatado = nrIdentificacaoFormatado;
	}

	public String getNmEmpresa() {
		return nmEmpresa;
	}
	public void setNmEmpresa(String nmEmpresa) {
		this.nmEmpresa = nmEmpresa;
	}

}
