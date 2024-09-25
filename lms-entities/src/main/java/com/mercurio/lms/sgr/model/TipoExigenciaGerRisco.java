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

public class TipoExigenciaGerRisco implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idTipoExigenciaGerRisco;
	private VarcharI18n dsTipoExigenciaGerRisco;
	private DomainValue tpSituacao;

	@JsonIgnore
	private List<ExigenciaGerRisco> exigenciaGerRiscos;

	// LMS-6847
	private DomainValue tpExigencia;
	private Boolean blExigeQuantidade;
	private Boolean blRestrito;
	private Boolean blControleNivel;
	private Boolean blTravaSistema;

	public Long getIdTipoExigenciaGerRisco() {
		return idTipoExigenciaGerRisco;
	}

	public void setIdTipoExigenciaGerRisco(Long idTipoExigenciaGerRisco) {
		this.idTipoExigenciaGerRisco = idTipoExigenciaGerRisco;
	}

	public VarcharI18n getDsTipoExigenciaGerRisco() {
		return dsTipoExigenciaGerRisco;
	}

	public void setDsTipoExigenciaGerRisco(VarcharI18n dsTipoExigenciaGerRisco) {
		this.dsTipoExigenciaGerRisco = dsTipoExigenciaGerRisco;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	@ParametrizedAttribute(type = ExigenciaGerRisco.class)
	public List<ExigenciaGerRisco> getExigenciaGerRiscos() {
		return exigenciaGerRiscos;
	}

	public void setExigenciaGerRiscos(List<ExigenciaGerRisco> exigenciaGerRiscos) {
		this.exigenciaGerRiscos = exigenciaGerRiscos;
	}

	public DomainValue getTpExigencia() {
		return tpExigencia;
	}

	public void setTpExigencia(DomainValue tpExigencia) {
		this.tpExigencia = tpExigencia;
	}

	public Boolean getBlRestrito() {
		return blRestrito;
	}

	public void setBlRestrito(Boolean blRestrito) {
		this.blRestrito = blRestrito;
	}

	public Boolean getBlExigeQuantidade() {
		return blExigeQuantidade;
	}

	public void setBlExigeQuantidade(Boolean blExigeQuantidade) {
		this.blExigeQuantidade = blExigeQuantidade;
	}

	public Boolean getBlControleNivel() {
		return blControleNivel;
	}

	public void setBlControleNivel(Boolean blControleNivel) {
		this.blControleNivel = blControleNivel;
	}

	public Boolean getBlTravaSistema() {
		return blTravaSistema;
	}

	public void setBlTravaSistema(Boolean blTravaSistema) {
		this.blTravaSistema = blTravaSistema;
	}

	public String toString() {
		ToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
		return new ToStringBuilder(this)
				.append(idTipoExigenciaGerRisco)
				.append(dsTipoExigenciaGerRisco != null ? dsTipoExigenciaGerRisco.getValue() : null)
				.append(tpExigencia != null ? tpExigencia.getValue() : null)
				.append(blRestrito)
				.append(blExigeQuantidade)
				.append(blControleNivel)
				.append(blTravaSistema)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof TipoExigenciaGerRisco)) {
			return false;
		}
		TipoExigenciaGerRisco cast = (TipoExigenciaGerRisco) other;
		return new EqualsBuilder()
				.append(idTipoExigenciaGerRisco, cast.idTipoExigenciaGerRisco)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idTipoExigenciaGerRisco)
				.toHashCode();
	}

}
