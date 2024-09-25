package com.mercurio.lms.sgr.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

public class ExigenciaGerRisco implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idExigenciaGerRisco;
	private Long nrNivel;
	private DomainValue tpSituacao;
	private VarcharI18n dsResumida;
	private VarcharI18n dsCompleta;
	private TipoExigenciaGerRisco tipoExigenciaGerRisco;
	@JsonIgnore
	private List<ExigenciaIndicada> exigenciaIndicadas;
	@JsonIgnore
	private List<ExigenciaSmp> exigenciaSmps;
	@JsonIgnore
	private List<ExigenciaFaixaValor> exigenciaFaixaValors;

	// LMS-6847
	private DomainValue tpCriterioAgrupamento;
	private Boolean blAreaRisco;
	private List<PerifericoExigenciaGerRisco> perifericosRastreador;
	// LMS-7255
	private String cdExigenciaGerRisco;

	public Long getIdExigenciaGerRisco() {
		return idExigenciaGerRisco;
	}

	public void setIdExigenciaGerRisco(Long idExigenciaGerRisco) {
		this.idExigenciaGerRisco = idExigenciaGerRisco;
	}

	public Long getNrNivel() {
		return nrNivel;
	}

	public void setNrNivel(Long nrNivel) {
		this.nrNivel = nrNivel;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public VarcharI18n getDsResumida() {
		return dsResumida;
	}

	public void setDsResumida(VarcharI18n dsResumida) {
		this.dsResumida = dsResumida;
	}

	public VarcharI18n getDsCompleta() {
		return dsCompleta;
	}

	public void setDsCompleta(VarcharI18n dsCompleta) {
		this.dsCompleta = dsCompleta;
	}

	public TipoExigenciaGerRisco getTipoExigenciaGerRisco() {
		return tipoExigenciaGerRisco;
	}

	public void setTipoExigenciaGerRisco(TipoExigenciaGerRisco tipoExigenciaGerRisco) {
		this.tipoExigenciaGerRisco = tipoExigenciaGerRisco;
	}

	@ParametrizedAttribute(type = ExigenciaIndicada.class)
	public List<ExigenciaIndicada> getExigenciaIndicadas() {
		return exigenciaIndicadas;
	}

	public void setExigenciaIndicadas(List<ExigenciaIndicada> exigenciaIndicadas) {
		this.exigenciaIndicadas = exigenciaIndicadas;
	}

	@ParametrizedAttribute(type = ExigenciaSmp.class)
	public List<ExigenciaSmp> getExigenciaSmps() {
		return exigenciaSmps;
	}

	public void setExigenciaSmps(List<ExigenciaSmp> exigenciaSmps) {
		this.exigenciaSmps = exigenciaSmps;
	}

	@ParametrizedAttribute(type = ExigenciaFaixaValor.class)
	public List<ExigenciaFaixaValor> getExigenciaFaixaValors() {
		return exigenciaFaixaValors;
	}

	public void setExigenciaFaixaValors(List<ExigenciaFaixaValor> exigenciaFaixaValors) {
		this.exigenciaFaixaValors = exigenciaFaixaValors;
	}

	public DomainValue getTpCriterioAgrupamento() {
		return tpCriterioAgrupamento;
	}

	public void setTpCriterioAgrupamento(DomainValue tpCriterioAgrupamento) {
		this.tpCriterioAgrupamento = tpCriterioAgrupamento;
	}

	public Boolean getBlAreaRisco() {
		return blAreaRisco;
	}

	public void setBlAreaRisco(Boolean blAreaRisco) {
		this.blAreaRisco = blAreaRisco;
	}

	@ParametrizedAttribute(type = PerifericoExigenciaGerRisco.class)
	public List<PerifericoExigenciaGerRisco> getPerifericosRastreador() {
		return perifericosRastreador;
	}

	public void setPerifericosRastreador(List<PerifericoExigenciaGerRisco> perifericosRastreador) {
		this.perifericosRastreador = perifericosRastreador;
	}

	public String getCdExigenciaGerRisco() {
		return cdExigenciaGerRisco;
	}

	public void setCdExigenciaGerRisco(String cdExigenciaGerRisco) {
		this.cdExigenciaGerRisco = cdExigenciaGerRisco;
	}

	public String toString() {
		ToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
		return new ToStringBuilder(this)
				.append(idExigenciaGerRisco)
				.append(nrNivel)
				.append(dsResumida != null ? dsResumida.getValue() : null)
				.append(tipoExigenciaGerRisco)
				.append(tpCriterioAgrupamento != null ? tpCriterioAgrupamento.getValue() : null)
				.append(blAreaRisco)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof ExigenciaGerRisco)) {
			return false;
		}
		ExigenciaGerRisco cast = (ExigenciaGerRisco) other;
		return new EqualsBuilder()
				.append(idExigenciaGerRisco, cast.idExigenciaGerRisco)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idExigenciaGerRisco)
				.toHashCode();
	}

}
