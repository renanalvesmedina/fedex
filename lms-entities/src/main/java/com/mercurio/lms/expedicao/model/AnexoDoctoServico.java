package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class AnexoDoctoServico implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idAnexoDoctoServico;

	/** persistent field */
	private VarcharI18n dsAnexoDoctoServico;

	/** persistent field */
	private DomainValue tpSituacao;

	public Long getIdAnexoDoctoServico() {
		return this.idAnexoDoctoServico;
	}

	public void setIdAnexoDoctoServico(Long idAnexoDoctoServico) {
		this.idAnexoDoctoServico = idAnexoDoctoServico;
	}

	public VarcharI18n getDsAnexoDoctoServico() {
		return dsAnexoDoctoServico;
	}

	public void setDsAnexoDoctoServico(VarcharI18n dsAnexoDoctoServico) {
		this.dsAnexoDoctoServico = dsAnexoDoctoServico;
	}

	public DomainValue getTpSituacao() {
		return this.tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idAnexoDoctoServico",
				getIdAnexoDoctoServico()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AnexoDoctoServico))
			return false;
		AnexoDoctoServico castOther = (AnexoDoctoServico) other;
		return new EqualsBuilder().append(this.getIdAnexoDoctoServico(),
				castOther.getIdAnexoDoctoServico()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdAnexoDoctoServico())
			.toHashCode();
	}

}
