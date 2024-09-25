package com.mercurio.lms.tributos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.municipios.model.UnidadeFederativa;

@Entity
@Table(name = "ALIQUOTA_ICMS_INTERNA")
@SequenceGenerator(name = "ALIQUOTA_ICMS_INTERNA_SQ", sequenceName = "ALIQUOTA_ICMS_INTERNA_SQ", allocationSize=1)
public class AliquotaIcmsInterna implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_ALIQUOTA_ICMS_INTERNA", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ALIQUOTA_ICMS_INTERNA_SQ")
	private Long idAliquotaICMSInterna;
	
	@Column(name = "PC_ALIQUOTA")
	private BigDecimal pcAliquota;
	
	@Column(name = "DT_VIGENCIA_INICIAL", nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaInicial;

	@Column(name = "DT_VIGENCIA_FINAL", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaFinal;
	
	@ManyToOne
	@JoinColumn(name = "ID_UNIDADE_FEDERATIVA")
	private UnidadeFederativa unidadeFederativa;

	public Long getIdAliquotaICMSInterna() {
		return idAliquotaICMSInterna;
	}

	public void setIdAliquotaICMSInterna(Long idAliquotaICMSInterna) {
		this.idAliquotaICMSInterna = idAliquotaICMSInterna;
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
	
	public UnidadeFederativa getUnidadeFederativa() {
		return unidadeFederativa;
	}

	public void setUnidadeFederativa(UnidadeFederativa unidadeFederativa) {
		this.unidadeFederativa = unidadeFederativa;
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("idAliquotaICMSInterna", getIdAliquotaICMSInterna()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AliquotaIcmsInterna))
			return false;
		
		AliquotaIcmsInterna castOther = (AliquotaIcmsInterna) other;
		return new EqualsBuilder().append(this.getIdAliquotaICMSInterna(), castOther.getIdAliquotaICMSInterna()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdAliquotaICMSInterna()).toHashCode();
	}
}
