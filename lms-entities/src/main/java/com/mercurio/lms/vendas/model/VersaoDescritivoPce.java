package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class VersaoDescritivoPce implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idVersaoDescritivoPce;
    
    private Integer versao;

    /** persistent field */
    private com.mercurio.lms.vendas.model.DescritivoPce descritivoPce;

    /** persistent field */
    private com.mercurio.lms.vendas.model.VersaoPce versaoPce;

    /** persistent field */
    private List versaoContatoPces;

    public Long getIdVersaoDescritivoPce() {
        return this.idVersaoDescritivoPce;
    }

    public void setIdVersaoDescritivoPce(Long idVersaoDescritivoPce) {
        this.idVersaoDescritivoPce = idVersaoDescritivoPce;
    }

    public com.mercurio.lms.vendas.model.DescritivoPce getDescritivoPce() {
        return this.descritivoPce;
    }

	public void setDescritivoPce(
			com.mercurio.lms.vendas.model.DescritivoPce descritivoPce) {
        this.descritivoPce = descritivoPce;
    }

    public com.mercurio.lms.vendas.model.VersaoPce getVersaoPce() {
        return this.versaoPce;
    }

    public void setVersaoPce(com.mercurio.lms.vendas.model.VersaoPce versaoPce) {
        this.versaoPce = versaoPce;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.VersaoContatoPce.class)     
    public List getVersaoContatoPces() {
        return this.versaoContatoPces;
    }

    public void setVersaoContatoPces(List versaoContatoPces) {
        this.versaoContatoPces = versaoContatoPces;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idVersaoDescritivoPce",
				getIdVersaoDescritivoPce()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof VersaoDescritivoPce))
			return false;
        VersaoDescritivoPce castOther = (VersaoDescritivoPce) other;
		return new EqualsBuilder().append(this.getIdVersaoDescritivoPce(),
				castOther.getIdVersaoDescritivoPce()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdVersaoDescritivoPce())
            .toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

}
