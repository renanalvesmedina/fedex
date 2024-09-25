package com.mercurio.lms.rest.portaria.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class MacroZonaDTO extends BaseDTO {
	private static final long serialVersionUID = 1L;
	private String dsMacroZona;
	private String nmPessoaTerminal;
	private String sgFilialTerminal;
	
	public String getDsMacroZona() {
		return dsMacroZona;
	}
	
	public void setDsMacroZona(String dsMacroZona) {
		this.dsMacroZona = dsMacroZona;
	}
	
	public String getNmPessoaTerminal() {
		return nmPessoaTerminal;
	}
	
	public void setNmPessoaTerminal(String nmPessoaTerminal) {
		this.nmPessoaTerminal = nmPessoaTerminal;
	}
	
	public String getSgFilialTerminal() {
		return sgFilialTerminal;
	}
	
	public void setSgFilialTerminal(String sgFilialTerminal) {
		this.sgFilialTerminal = sgFilialTerminal;
	}
}