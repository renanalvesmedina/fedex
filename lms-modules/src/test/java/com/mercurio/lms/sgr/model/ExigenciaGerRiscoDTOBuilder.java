package com.mercurio.lms.sgr.model;

import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.sgr.dto.ExigenciaGerRiscoDTO;

public class ExigenciaGerRiscoDTOBuilder {

	public static ExigenciaGerRiscoDTOBuilder newDoctoServicoDTO() {
		return new ExigenciaGerRiscoDTOBuilder();
	}

	private ExigenciaGerRiscoDTO exigencia;

	private ExigenciaGerRiscoDTOBuilder() {
		exigencia = new ExigenciaGerRiscoDTO();
	}

	public ExigenciaGerRiscoDTOBuilder regra(EnquadramentoRegra regra) {
		exigencia.setEnquadramentoRegra(regra);
		return this;
	}

	public ExigenciaGerRiscoDTOBuilder exigencia(ExigenciaGerRisco exigenciaGerRisco) {
		exigencia.setExigenciaGerRisco(exigenciaGerRisco);
		exigencia.setTipoExigenciaGerRisco(exigenciaGerRisco.getTipoExigenciaGerRisco());
		return this;
	}

	public ExigenciaGerRiscoDTOBuilder tipo(TipoExigenciaGerRisco tipo) {
		exigencia.setTipoExigenciaGerRisco(tipo);
		return this;
	}

	public ExigenciaGerRiscoDTOBuilder quantidade(int quantidade) {
		exigencia.setQtExigida(quantidade);
		return this;
	}

	public ExigenciaGerRiscoDTOBuilder filial(Filial filial) {
		exigencia.setFilialInicio(filial);
		return this;
	}

	public ExigenciaGerRiscoDTOBuilder km(int km) {
		exigencia.setVlKmFranquia(km);
		return this;
	}

	public ExigenciaGerRiscoDTO build() {
		return exigencia;
	}

}
