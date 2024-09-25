package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class Beneficiario implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idBeneficiario;

    /** nullable persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa pessoa;

    /** persistent field */
    private List reciboFreteCarreteiros;

    /** persistent field */
    private List beneficiarioProprietarios;

    public Long getIdBeneficiario() {
        return this.idBeneficiario;
    }

    public void setIdBeneficiario(Long idBeneficiario) {
        this.idBeneficiario = idBeneficiario;
    }

    public com.mercurio.lms.configuracoes.model.Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(com.mercurio.lms.configuracoes.model.Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro.class)     
    public List getReciboFreteCarreteiros() {
        return this.reciboFreteCarreteiros;
    }

    public void setReciboFreteCarreteiros(List reciboFreteCarreteiros) {
        this.reciboFreteCarreteiros = reciboFreteCarreteiros;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.BeneficiarioProprietario.class)     
    public List getBeneficiarioProprietarios() {
        return this.beneficiarioProprietarios;
    }

    public void setBeneficiarioProprietarios(List beneficiarioProprietarios) {
        this.beneficiarioProprietarios = beneficiarioProprietarios;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idBeneficiario",
				getIdBeneficiario()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Beneficiario))
			return false;
        Beneficiario castOther = (Beneficiario) other;
		return new EqualsBuilder().append(this.getIdBeneficiario(),
				castOther.getIdBeneficiario()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdBeneficiario()).toHashCode();
    }

}
