package com.mercurio.lms.rest.tabeladeprecos.dto;

import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.municipios.dto.MunicipioDTO;
import com.mercurio.lms.rest.municipios.dto.UnidadeFederativaDTO;

public class TabelaMunicipioEMEXFilterDTO extends BaseFilterDTO {
	
	TabelaPrecoDTO tabelaPreco;
	MunicipioDTO municipio;
	UnidadeFederativaDTO unidadeFederativa;
	
	public TabelaPrecoDTO getTabelaPreco() {
		return tabelaPreco;
	}
	public void setTabelaPreco(TabelaPrecoDTO tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}
	public MunicipioDTO getMunicipio() {
		return municipio;
	}
	public void setMunicipio(MunicipioDTO municipio) {
		this.municipio = municipio;
	}
	public UnidadeFederativaDTO getUnidadeFederativa() {
		return unidadeFederativa;
	}
	public void setUnidadeFederativa(UnidadeFederativaDTO unidadeFederativa) {
		this.unidadeFederativa = unidadeFederativa;
	}
	
	
	

	

	
}
