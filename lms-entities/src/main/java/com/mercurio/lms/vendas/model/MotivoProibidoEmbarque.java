package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class MotivoProibidoEmbarque implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMotivoProibidoEmbarque;

    /** persistent field */
    private VarcharI18n dsMotivoProibidoEmbarque;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private Boolean blFinanceiro;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Empresa empresa;

    /** persistent field */
    private List proibidoEmbarques;

    public Long getIdMotivoProibidoEmbarque() {
        return this.idMotivoProibidoEmbarque;
    }

    public void setIdMotivoProibidoEmbarque(Long idMotivoProibidoEmbarque) {
        this.idMotivoProibidoEmbarque = idMotivoProibidoEmbarque;
    }

    public VarcharI18n getDsMotivoProibidoEmbarque() {
		return dsMotivoProibidoEmbarque;
    }

	public void setDsMotivoProibidoEmbarque(VarcharI18n dsMotivoProibidoEmbarque) {
        this.dsMotivoProibidoEmbarque = dsMotivoProibidoEmbarque;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public Boolean getBlFinanceiro() {
        return this.blFinanceiro;
    }

    public void setBlFinanceiro(Boolean blFinanceiro) {
        this.blFinanceiro = blFinanceiro;
    }

    public com.mercurio.lms.municipios.model.Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(com.mercurio.lms.municipios.model.Empresa empresa) {
        this.empresa = empresa;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ProibidoEmbarque.class)     
    public List getProibidoEmbarques() {
        return this.proibidoEmbarques;
    }

    public void setProibidoEmbarques(List proibidoEmbarques) {
        this.proibidoEmbarques = proibidoEmbarques;
    }    

    public String toString() {
		return new ToStringBuilder(this).append("idMotivoProibidoEmbarque",
				getIdMotivoProibidoEmbarque()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MotivoProibidoEmbarque))
			return false;
        MotivoProibidoEmbarque castOther = (MotivoProibidoEmbarque) other;
		return new EqualsBuilder().append(this.getIdMotivoProibidoEmbarque(),
				castOther.getIdMotivoProibidoEmbarque()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMotivoProibidoEmbarque())
            .toHashCode();
    }

}
