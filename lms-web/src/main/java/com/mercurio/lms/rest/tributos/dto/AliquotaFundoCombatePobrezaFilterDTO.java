package com.mercurio.lms.rest.tributos.dto;

import java.math.BigDecimal;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.rest.BaseFilterDTO;

public class AliquotaFundoCombatePobrezaFilterDTO extends BaseFilterDTO {
	private static final long serialVersionUID = 1L;

	private Map<String, Object> unidadeFederativa;
	private BigDecimal pcAliquota;
	private YearMonthDay dtVigencia;

	public BigDecimal getPcAliquota() {
		return pcAliquota;
	}

	public void setPcAliquota(BigDecimal pcAliquota) {
		this.pcAliquota = pcAliquota;
	}

	public YearMonthDay getDtVigencia() {
		return dtVigencia;
	}

	public void setDtVigencia(YearMonthDay dtVigencia) {
		this.dtVigencia = dtVigencia;
	}

	public Map<String, Object> getUnidadeFederativa() {
		return unidadeFederativa;
	}

	public void setUnidadeFederativa(Map<String, Object> unidadeFederativa) {
		this.unidadeFederativa = unidadeFederativa;
	}
}
