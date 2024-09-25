package com.mercurio.lms.sgr.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mercurio.lms.municipios.model.Filial;

public class ExigenciaFaixaValor implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idExigenciaFaixaValor;
	@JsonIgnore
	private FaixaDeValor faixaDeValor;
	private ExigenciaGerRisco exigenciaGerRisco;

	// LMS-6848
	private Integer qtExigida;
	private Filial filialInicio;
	private Integer vlKmFranquia;

	public Long getIdExigenciaFaixaValor() {
		return idExigenciaFaixaValor;
	}

	public void setIdExigenciaFaixaValor(Long idExigenciaFaixaValor) {
		this.idExigenciaFaixaValor = idExigenciaFaixaValor;
	}

	public FaixaDeValor getFaixaDeValor() {
		return faixaDeValor;
	}

	public void setFaixaDeValor(FaixaDeValor faixaDeValor) {
		this.faixaDeValor = faixaDeValor;
	}

	public ExigenciaGerRisco getExigenciaGerRisco() {
		return exigenciaGerRisco;
	}

	public void setExigenciaGerRisco(ExigenciaGerRisco exigenciaGerRisco) {
		this.exigenciaGerRisco = exigenciaGerRisco;
	}

	public Integer getQtExigida() {
		return qtExigida;
	}

	public void setQtExigida(Integer qtExigida) {
		this.qtExigida = qtExigida;
	}

	public Filial getFilialInicio() {
		return filialInicio;
	}

	public void setFilialInicio(Filial filialInicio) {
		this.filialInicio = filialInicio;
	}

	public Integer getVlKmFranquia() {
		return vlKmFranquia;
	}

	public void setVlKmFranquia(Integer vlKmFranquia) {
		this.vlKmFranquia = vlKmFranquia;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append(idExigenciaFaixaValor)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof ExigenciaFaixaValor)) {
			return false;
		}
		ExigenciaFaixaValor cast = (ExigenciaFaixaValor) other;
		return new EqualsBuilder()
				.append(idExigenciaFaixaValor, cast.idExigenciaFaixaValor)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idExigenciaFaixaValor)
				.toHashCode();
	}

}
