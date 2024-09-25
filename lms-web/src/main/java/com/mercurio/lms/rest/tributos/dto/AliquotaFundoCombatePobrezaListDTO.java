package com.mercurio.lms.rest.tributos.dto;

import java.math.BigDecimal;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.rest.BaseDTO;

public class AliquotaFundoCombatePobrezaListDTO extends BaseDTO {
	private static final long serialVersionUID = 1L;

	private String ufDestino;
	private BigDecimal pcAliquota;
	private YearMonthDay dtVigenciaInicial;
	private YearMonthDay dtVigenciaFinal;

	public String getUfDestino() {
		return ufDestino;
	}

	public void setUfDestino(String ufDestino) {
		this.ufDestino = ufDestino;
	}

	public BigDecimal getPcAliquota() {
		return pcAliquota;
	}

	public void setPcAliquota(BigDecimal pcAliquota) {
		this.pcAliquota = pcAliquota;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}
}
