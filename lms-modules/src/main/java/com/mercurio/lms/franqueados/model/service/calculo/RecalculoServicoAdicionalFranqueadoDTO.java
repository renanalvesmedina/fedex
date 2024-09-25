package com.mercurio.lms.franqueados.model.service.calculo;

import org.joda.time.YearMonthDay;

public class RecalculoServicoAdicionalFranqueadoDTO extends ConhecimentoFranqueadoDTO {
	private static final long serialVersionUID = 1L;

	private YearMonthDay dtCompetenciaRecalculo; 
	public YearMonthDay getDtCompetenciaRecalculo() {
		return dtCompetenciaRecalculo;
	}
	public void setDtCompetenciaRecalculo(YearMonthDay dtCompetencia) {
		this.dtCompetenciaRecalculo = dtCompetencia;
	}

	public RecalculoServicoAdicionalFranqueados createCalculo() {
		return new RecalculoServicoAdicionalFranqueados();
	}

}
