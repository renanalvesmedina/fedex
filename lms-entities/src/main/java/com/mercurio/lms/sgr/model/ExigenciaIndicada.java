package com.mercurio.lms.sgr.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.ControleCarga;

public class ExigenciaIndicada implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idExigenciaIndicada;
	private DomainValue tpExigenciaIndicada;
	private DateTime dhNecessidadeIdentificada;
	private DateTime dhNecessidadeEliminada;
	private ControleCarga controleCarga;
	private ExigenciaGerRisco exigenciaGerRisco;

	public Long getIdExigenciaIndicada() {
		return idExigenciaIndicada;
	}

	public void setIdExigenciaIndicada(Long idExigenciaIndicada) {
		this.idExigenciaIndicada = idExigenciaIndicada;
	}

	public DomainValue getTpExigenciaIndicada() {
		return tpExigenciaIndicada;
	}

	public void setTpExigenciaIndicada(DomainValue tpExigenciaIndicada) {
		this.tpExigenciaIndicada = tpExigenciaIndicada;
	}

	public DateTime getDhNecessidadeIdentificada() {
		return dhNecessidadeIdentificada;
	}

	public void setDhNecessidadeIdentificada(DateTime dhNecessidadeIdentificada) {
		this.dhNecessidadeIdentificada = dhNecessidadeIdentificada;
	}

	public DateTime getDhNecessidadeEliminada() {
		return dhNecessidadeEliminada;
	}

	public void setDhNecessidadeEliminada(DateTime dhNecessidadeEliminada) {
		this.dhNecessidadeEliminada = dhNecessidadeEliminada;
	}

	public ControleCarga getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(ControleCarga controleCarga) {
		this.controleCarga = controleCarga;
	}

	public ExigenciaGerRisco getExigenciaGerRisco() {
		return exigenciaGerRisco;
	}

	public void setExigenciaGerRisco(ExigenciaGerRisco exigenciaGerRisco) {
		this.exigenciaGerRisco = exigenciaGerRisco;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append(idExigenciaIndicada)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof ExigenciaIndicada)) {
			return false;
		}
		ExigenciaIndicada cast = (ExigenciaIndicada) other;
		return new EqualsBuilder()
				.append(idExigenciaIndicada, cast.idExigenciaIndicada)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idExigenciaIndicada)
				.toHashCode();
	}

}
