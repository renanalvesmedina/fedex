package com.mercurio.lms.rest.vendas.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class TerritorioGridDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private String dsRegional;
	private String sgFilial;
	private String nmFilial;
	private String nmTerritorio;

	public String getDsRegional() {
		return dsRegional;
	}

	public void setDsRegional(String dsRegional) {
		this.dsRegional = dsRegional;
	}

	public String getSgFilial() {
		return sgFilial;
	}

	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}

	public String getNmTerritorio() {
		return nmTerritorio;
	}

	public void setNmTerritorio(String nmTerritorio) {
		this.nmTerritorio = nmTerritorio;
	}

	public String getNmFilial() {
		return nmFilial;
	}

	public void setNmFilial(String nmFilial) {
		this.nmFilial = nmFilial;
	}

}
