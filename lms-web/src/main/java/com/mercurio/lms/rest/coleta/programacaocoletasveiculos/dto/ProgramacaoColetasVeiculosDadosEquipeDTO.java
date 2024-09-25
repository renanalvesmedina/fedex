package com.mercurio.lms.rest.coleta.programacaocoletasveiculos.dto;

import org.joda.time.DateTime;

import com.mercurio.adsm.rest.BaseDTO;

public class ProgramacaoColetasVeiculosDadosEquipeDTO extends BaseDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long idEquipeOperacao;
	private DateTime dhInicioOperacao;
	private DateTime dhFimOperacao;
	private String dsEquipe;
	
	public DateTime getDhInicioOperacao() {
		return dhInicioOperacao;
	}
	public void setDhInicioOperacao(DateTime dhInicioOperacao) {
		this.dhInicioOperacao = dhInicioOperacao;
	}
	public DateTime getDhFimOperacao() {
		return dhFimOperacao;
	}
	public void setDhFimOperacao(DateTime dhFimOperacao) {
		this.dhFimOperacao = dhFimOperacao;
	}
	public String getDsEquipe() {
		return dsEquipe;
	}
	public void setDsEquipe(String dsEquipe) {
		this.dsEquipe = dsEquipe;
	}
	public long getIdEquipeOperacao() {
		return idEquipeOperacao;
	}
	public void setIdEquipeOperacao(long idEquipeOperacao) {
		this.idEquipeOperacao = idEquipeOperacao;
	}

}
