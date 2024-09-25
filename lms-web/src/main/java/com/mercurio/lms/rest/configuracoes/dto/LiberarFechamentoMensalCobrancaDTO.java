package com.mercurio.lms.rest.configuracoes.dto;

import com.mercurio.adsm.rest.BaseDTO;
 
public class LiberarFechamentoMensalCobrancaDTO extends BaseDTO { 
	private static final long serialVersionUID = 1L; 
 
	private String competencia;
	private boolean fechamentoHabilitado;
	
	public LiberarFechamentoMensalCobrancaDTO(Boolean fechamentoHabilitado, String competencia) {
		this.fechamentoHabilitado = fechamentoHabilitado;
		this.competencia = competencia;
	}
	public String getCompetencia() {
		return competencia;
	}
	public void setCompetencia(String competencia) {
		this.competencia = competencia;
	}
	public boolean isFechamentoHabilitado() {
		return fechamentoHabilitado;
	}
	public void setFechamentoHabilitado(boolean fechamentoHabilitado) {
		this.fechamentoHabilitado = fechamentoHabilitado;
	}

	
} 
