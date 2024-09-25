package com.mercurio.lms.rnc.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class DescricaoPadraoNc implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDescricaoPadraoNc;

    /** persistent field */
    private VarcharI18n dsPadraoNc;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private com.mercurio.lms.rnc.model.MotivoAberturaNc motivoAberturaNc;

    /** persistent field */
    private List ocorrenciaNaoConformidades;

    public Long getIdDescricaoPadraoNc() {
        return this.idDescricaoPadraoNc;
    }

    public void setIdDescricaoPadraoNc(Long idDescricaoPadraoNc) {
        this.idDescricaoPadraoNc = idDescricaoPadraoNc;
    }

    public VarcharI18n getDsPadraoNc() {
		return dsPadraoNc;
    }

	public void setDsPadraoNc(VarcharI18n dsPadraoNc) {
        this.dsPadraoNc = dsPadraoNc;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.rnc.model.MotivoAberturaNc getMotivoAberturaNc() {
        return this.motivoAberturaNc;
    }

	public void setMotivoAberturaNc(
			com.mercurio.lms.rnc.model.MotivoAberturaNc motivoAberturaNc) {
        this.motivoAberturaNc = motivoAberturaNc;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade.class)     
    public List getOcorrenciaNaoConformidades() {
        return this.ocorrenciaNaoConformidades;
    }

    public void setOcorrenciaNaoConformidades(List ocorrenciaNaoConformidades) {
        this.ocorrenciaNaoConformidades = ocorrenciaNaoConformidades;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDescricaoPadraoNc",
				getIdDescricaoPadraoNc()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DescricaoPadraoNc))
			return false;
        DescricaoPadraoNc castOther = (DescricaoPadraoNc) other;
		return new EqualsBuilder().append(this.getIdDescricaoPadraoNc(),
				castOther.getIdDescricaoPadraoNc()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDescricaoPadraoNc())
            .toHashCode();
    }

}
