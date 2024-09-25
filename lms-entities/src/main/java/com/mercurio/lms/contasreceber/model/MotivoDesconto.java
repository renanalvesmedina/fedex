package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class MotivoDesconto implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMotivoDesconto;

    /** persistent field */
    private VarcharI18n dsMotivoDesconto;
    
    /** persistent field */
    private String cdMotivoDesconto;

    private DomainValue tpMotivoDesconto;
    
    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List descontos;

    public Long getIdMotivoDesconto() {
        return this.idMotivoDesconto;
    }

    public void setIdMotivoDesconto(Long idMotivoDesconto) {
        this.idMotivoDesconto = idMotivoDesconto;
    }

	public VarcharI18n getDsMotivoDesconto() {
		return dsMotivoDesconto;
    }

	public void setDsMotivoDesconto(VarcharI18n dsMotivoDesconto) {
        this.dsMotivoDesconto = dsMotivoDesconto;
    }

	public DomainValue getTpMotivoDesconto() {
		return tpMotivoDesconto;
	}

	public void setTpMotivoDesconto(DomainValue tpMotivoDesconto) {
		this.tpMotivoDesconto = tpMotivoDesconto;
	}

	public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.Desconto.class)     
    public List getDescontos() {
        return this.descontos;
    }

    public void setDescontos(List descontos) {
        this.descontos = descontos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMotivoDesconto",
				getIdMotivoDesconto()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MotivoDesconto))
			return false;
        MotivoDesconto castOther = (MotivoDesconto) other;
		return new EqualsBuilder().append(this.getIdMotivoDesconto(),
				castOther.getIdMotivoDesconto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMotivoDesconto()).toHashCode();
    }

	public String getCdMotivoDesconto() {
		return cdMotivoDesconto;
	}

	public void setCdMotivoDesconto(String cdMotivoDesconto) {
		this.cdMotivoDesconto = cdMotivoDesconto;
	}

}
