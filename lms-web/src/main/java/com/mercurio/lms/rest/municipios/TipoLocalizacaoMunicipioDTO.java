package com.mercurio.lms.rest.municipios;

import com.mercurio.adsm.rest.BaseDTO;


public class TipoLocalizacaoMunicipioDTO extends BaseDTO {
	
	private static final long serialVersionUID = 1L;
	
	private Long idTipoLocalizacaoMunicipio;
	private String dsTipoLocalizacaoMunicipio;
	
	public TipoLocalizacaoMunicipioDTO() {
		
	}

	public TipoLocalizacaoMunicipioDTO(Long idTipoLocalizacaoMunicipio,
			String dsTipoLocalizacaoMunicipio) {
		super();
		this.idTipoLocalizacaoMunicipio = idTipoLocalizacaoMunicipio;
		this.dsTipoLocalizacaoMunicipio = dsTipoLocalizacaoMunicipio;
	}

	public Long getIdTipoLocalizacaoMunicipio() {
		return idTipoLocalizacaoMunicipio;
	}

	public void setIdTipoLocalizacaoMunicipio(Long idTipoLocalizacaoMunicipio) {
		this.idTipoLocalizacaoMunicipio = idTipoLocalizacaoMunicipio;
	}

	public String getDsTipoLocalizacaoMunicipio() {
		return dsTipoLocalizacaoMunicipio;
	}

	public void setDsTipoLocalizacaoMunicipio(String dsTipoLocalizacaoMunicipio) {
		this.dsTipoLocalizacaoMunicipio = dsTipoLocalizacaoMunicipio;
	}


}
