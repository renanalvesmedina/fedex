package com.mercurio.lms.rest.tributos.dto;

import java.math.BigDecimal;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.rest.BaseDTO;

public class AliquotaFundoCombatePobrezaDTO extends BaseDTO {
	private static final long serialVersionUID = 1L;

	private Map<String, Object> unidadeFederativa;
	private Long idUsuarioInclusao;
	private BigDecimal pcAliquota;
	private YearMonthDay dtVigenciaInicial;
	private YearMonthDay dtVigenciaFinal;
	private DateTime dhInclusao;

	private Boolean blDisableSalvar;
	private Boolean blDisableExcluir;
	private Boolean blDisableUfDestino;
	private Boolean blDisableAliquota;
	private Boolean blDisableDtVigenciaInicial;
	private Boolean blDisableDtVigenciaFinal;

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

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public Long getIdUsuarioInclusao() {
		return idUsuarioInclusao;
	}

	public void setIdUsuarioInclusao(Long idUsuarioInclusao) {
		this.idUsuarioInclusao = idUsuarioInclusao;
	}

	public Map<String, Object> getUnidadeFederativa() {
		return unidadeFederativa;
	}

	public void setUnidadeFederativa(Map<String, Object> unidadeFederativa) {
		this.unidadeFederativa = unidadeFederativa;
	}

	public Boolean getBlDisableSalvar() {
		return blDisableSalvar;
	}

	public void setBlDisableSalvar(Boolean blDisableSalvar) {
		this.blDisableSalvar = blDisableSalvar;
	}

	public Boolean getBlDisableExcluir() {
		return blDisableExcluir;
	}

	public void setBlDisableExcluir(Boolean blDisableExcluir) {
		this.blDisableExcluir = blDisableExcluir;
	}

	public Boolean getBlDisableUfDestino() {
		return blDisableUfDestino;
	}

	public void setBlDisableUfDestino(Boolean blDisableUfDestino) {
		this.blDisableUfDestino = blDisableUfDestino;
	}

	public Boolean getBlDisableAliquota() {
		return blDisableAliquota;
	}

	public void setBlDisableAliquota(Boolean blDisableAliquota) {
		this.blDisableAliquota = blDisableAliquota;
	}

	public Boolean getBlDisableDtVigenciaInicial() {
		return blDisableDtVigenciaInicial;
	}

	public void setBlDisableDtVigenciaInicial(Boolean blDisableDtVigenciaInicial) {
		this.blDisableDtVigenciaInicial = blDisableDtVigenciaInicial;
	}

	public Boolean getBlDisableDtVigenciaFinal() {
		return blDisableDtVigenciaFinal;
	}

	public void setBlDisableDtVigenciaFinal(Boolean blDisableDtVigenciaFinal) {
		this.blDisableDtVigenciaFinal = blDisableDtVigenciaFinal;
	}
}