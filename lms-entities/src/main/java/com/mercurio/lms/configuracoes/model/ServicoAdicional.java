package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.lms.expedicao.model.ServAdicionalDocServ;
import com.mercurio.lms.tributos.model.ServicoOficialTributo;

/** @author LMS Custom Hibernate CodeGenerator */
public class ServicoAdicional implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idServicoAdicional;

	/** persistent field */
	private YearMonthDay dtVigenciaInicial;

	/** persistent field */
	private VarcharI18n dsServicoAdicional;

	/** nullable persistent field */
	private YearMonthDay dtVigenciaFinal;

	/** persistent field */
	private ServicoOficialTributo servicoOficialTributo;

	private String cdServicoEDI;

	/** persistent field */
	private List<ServAdicionalDocServ> servAdicionalDocServs;

	public Long getIdServicoAdicional() {
		return this.idServicoAdicional;
	}

	public void setIdServicoAdicional(Long idServicoAdicional) {
		this.idServicoAdicional = idServicoAdicional;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return this.dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public VarcharI18n getDsServicoAdicional() {
		return dsServicoAdicional;
	}

	public void setDsServicoAdicional(VarcharI18n dsServicoAdicional) {
		this.dsServicoAdicional = dsServicoAdicional;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return this.dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public ServicoOficialTributo getServicoOficialTributo() {
		return this.servicoOficialTributo;
	}

	public void setServicoOficialTributo(
			ServicoOficialTributo servicoOficialTributo) {
		this.servicoOficialTributo = servicoOficialTributo;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.ServAdicionalDocServ.class)	 
	public List<ServAdicionalDocServ> getServAdicionalDocServs() {
		return this.servAdicionalDocServs;
	}

	public void setServAdicionalDocServs(
			List<ServAdicionalDocServ> servAdicionalDocServs) {
		this.servAdicionalDocServs = servAdicionalDocServs;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idServicoAdicional",
				getIdServicoAdicional()).toString();
	}

	public String getCdServicoEDI() {
		return cdServicoEDI;
	}

	public void setCdServicoEDI(String cdServicoEDI) {
		this.cdServicoEDI = cdServicoEDI;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ServicoAdicional))
			return false;
		ServicoAdicional castOther = (ServicoAdicional) other;
		return new EqualsBuilder().append(this.getIdServicoAdicional(),
				castOther.getIdServicoAdicional()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdServicoAdicional())
			.toHashCode();
	}
}