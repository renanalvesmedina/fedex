package com.mercurio.lms.rest.fretecarreteirocoletaentrega.tabelafretecarreteiro.dto;

import java.util.List;

import com.mercurio.adsm.rest.BaseDTO;

public class FaixasPesoDTO extends BaseDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<TabelaFcFaixaPesoRestDTO> faixas;

	public List<TabelaFcFaixaPesoRestDTO> getFaixas() {
		return faixas;
	}

	public void setFaixas(List<TabelaFcFaixaPesoRestDTO> faixas) {
		this.faixas = faixas;
	}

}
