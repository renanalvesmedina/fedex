package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author Hibernate CodeGenerator */
public class TipoLocalizacaoMunicipio implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idTipoLocalizacaoMunicipio;

	/** persistent field */
	private VarcharI18n dsTipoLocalizacaoMunicipio;

	/** persistent field */
	private DomainValue tpSituacao;

	/** persistent field */
	private DomainValue tpLocalizacao;

	public Long getIdTipoLocalizacaoMunicipio() {
		return this.idTipoLocalizacaoMunicipio;
	}
	
	public void setIdTipoLocalizacaoMunicipio(Long idTipoLocalizacaoMunicipio) {
		this.idTipoLocalizacaoMunicipio = idTipoLocalizacaoMunicipio;
	}

	public VarcharI18n getDsTipoLocalizacaoMunicipio() {
		return dsTipoLocalizacaoMunicipio;
	}
	
	public void setDsTipoLocalizacaoMunicipio(
			VarcharI18n dsTipoLocalizacaoMunicipio) {
		this.dsTipoLocalizacaoMunicipio = dsTipoLocalizacaoMunicipio;
	}

	public DomainValue getTpSituacao() {
		return this.tpSituacao;
	}
	
	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public DomainValue getTpLocalizacao() {
		return tpLocalizacao;
	}

	public void setTpLocalizacao(DomainValue tpLocalizacao) {
		this.tpLocalizacao = tpLocalizacao;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idTipoLocalizacaoMunicipio",
				getIdTipoLocalizacaoMunicipio()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoLocalizacaoMunicipio))
			return false;
		TipoLocalizacaoMunicipio castOther = (TipoLocalizacaoMunicipio) other;
		return new EqualsBuilder().append(this.getIdTipoLocalizacaoMunicipio(),
				castOther.getIdTipoLocalizacaoMunicipio()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoLocalizacaoMunicipio())
			.toHashCode();
	}
}
