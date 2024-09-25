package com.mercurio.lms.rest.vendas.dto;

import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.RegionalSuggestDTO;

public class TerritorioDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private RegionalSuggestDTO regional;
	private FilialSuggestDTO filial;
	private String nmTerritorio;

	public RegionalSuggestDTO getRegional() {
		return regional;
	}

	public void setRegional(RegionalSuggestDTO regional) {
		this.regional = regional;
	}

	public FilialSuggestDTO getFilial() {
		return filial;
	}

	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}

	public String getNmTerritorio() {
		return nmTerritorio;
	}

	public void setNmTerritorio(String nmTerritorio) {
		this.nmTerritorio = nmTerritorio;
	}

}
