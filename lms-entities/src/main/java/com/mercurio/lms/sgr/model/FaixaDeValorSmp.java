package com.mercurio.lms.sgr.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.municipios.model.Filial;

public class FaixaDeValorSmp implements Serializable {
	private static final long serialVersionUID = 3410098488347250754L;
	
	private Long idFaixaDeValorSmp;
	private SolicMonitPreventivo solicMonitPreventivo;
	private ExigenciaGerRisco exigenciaGerRisco;
	private FaixaDeValor faixaDeValor;
	private Integer qtExigida;
	private Filial filialInicio;
	private Integer vlKmFranquia;

	public Long getIdFaixaDeValorSmp() {
		return idFaixaDeValorSmp;
	}

	public void setIdFaixaDeValorSmp(Long idFaixaDeValorSmp) {
		this.idFaixaDeValorSmp = idFaixaDeValorSmp;
	}

	public SolicMonitPreventivo getSolicMonitPreventivo() {
		return solicMonitPreventivo;
	}

	public void setSolicMonitPreventivo(SolicMonitPreventivo solicMonitPreventivo) {
		this.solicMonitPreventivo = solicMonitPreventivo;
	}

	public ExigenciaGerRisco getExigenciaGerRisco() {
		return exigenciaGerRisco;
	}

	public void setExigenciaGerRisco(ExigenciaGerRisco exigenciaGerRisco) {
		this.exigenciaGerRisco = exigenciaGerRisco;
	}

	public FaixaDeValor getFaixaDeValor() {
		return faixaDeValor;
	}

	public void setFaixaDeValor(FaixaDeValor faixaDeValor) {
		this.faixaDeValor = faixaDeValor;
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
		return new ToStringBuilder(this).append(idFaixaDeValorSmp).toString();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(idFaixaDeValorSmp).toHashCode();
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FaixaDeValorSmp other = (FaixaDeValorSmp) obj;
		return new EqualsBuilder().append(idFaixaDeValorSmp, other.idFaixaDeValorSmp).isEquals();
	}
	
}
