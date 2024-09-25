package com.mercurio.lms.rest.vendas.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class TerritorioSuggestDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private String nmTerritorio;

	public TerritorioSuggestDTO() {
	}

	public TerritorioSuggestDTO(Long idTerritorio, String nmTerritorio) {
		super();
		setId(idTerritorio);
		this.nmTerritorio = nmTerritorio;
	}

	public String getNmTerritorio() {
		return nmTerritorio;
	}

	public void setNmTerritorio(String nmTerritorio) {
		this.nmTerritorio = nmTerritorio;
	}

}
