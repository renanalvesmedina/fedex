package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class CtoAwb implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Long idCtoAwb;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.Conhecimento conhecimento;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.Awb awb;
    
    /** identifier field */
    private Long idCtoPreAwb;
    
    private BigDecimal vlCusto;

    public Long getIdCtoAwb() {
        return this.idCtoAwb;
    }

    public void setIdCtoAwb(Long idCtoAwb) {
        this.idCtoAwb = idCtoAwb;
    }

    public com.mercurio.lms.expedicao.model.Conhecimento getConhecimento() {
        return this.conhecimento;
    }

	public void setConhecimento(
			com.mercurio.lms.expedicao.model.Conhecimento conhecimento) {
        this.conhecimento = conhecimento;
    }

    public com.mercurio.lms.expedicao.model.Awb getAwb() {
        return this.awb;
    }

    public void setAwb(com.mercurio.lms.expedicao.model.Awb awb) {
        this.awb = awb;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idCtoAwb", getIdCtoAwb())
            .toString();
    }

    public boolean equals(Object other) {
		if (this == other){
			return true;
		}
		if (!(other instanceof CtoAwb)){
			return false;
		}
        CtoAwb castOther = (CtoAwb) other;
		return new EqualsBuilder().append(this.getIdCtoAwb(),
				castOther.getIdCtoAwb()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdCtoAwb()).toHashCode();
    }

	public Long getIdCtoPreAwb() {
		return idCtoPreAwb;
	}

	public void setIdCtoPreAwb(Long idCtoPreAwb) {
		this.idCtoPreAwb = idCtoPreAwb;
	}

	public BigDecimal getVlCusto() {
		return vlCusto;
	}

	public void setVlCusto(BigDecimal vlCusto) {
		this.vlCusto = vlCusto;
	}

}
