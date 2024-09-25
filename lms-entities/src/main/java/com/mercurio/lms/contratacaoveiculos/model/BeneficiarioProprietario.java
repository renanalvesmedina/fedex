package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class BeneficiarioProprietario implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idBeneficiarioProprietario; 

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.Beneficiario beneficiario;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.Proprietario proprietario;

    public Long getIdBeneficiarioProprietario() {
        return this.idBeneficiarioProprietario;
    }

    public void setIdBeneficiarioProprietario(Long idBeneficiarioProprietario) {
        this.idBeneficiarioProprietario = idBeneficiarioProprietario;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public com.mercurio.lms.contratacaoveiculos.model.Beneficiario getBeneficiario() {
        return this.beneficiario;
    }

	public void setBeneficiario(
			com.mercurio.lms.contratacaoveiculos.model.Beneficiario beneficiario) {
        this.beneficiario = beneficiario;
    }

    public com.mercurio.lms.contratacaoveiculos.model.Proprietario getProprietario() {
        return this.proprietario;
    }

	public void setProprietario(
			com.mercurio.lms.contratacaoveiculos.model.Proprietario proprietario) {
        this.proprietario = proprietario;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idBeneficiarioProprietario",
				getIdBeneficiarioProprietario()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof BeneficiarioProprietario))
			return false;
        BeneficiarioProprietario castOther = (BeneficiarioProprietario) other;
		return new EqualsBuilder().append(this.getIdBeneficiarioProprietario(),
				castOther.getIdBeneficiarioProprietario()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdBeneficiarioProprietario())
            .toHashCode();
    }

}
