package com.mercurio.lms.tributos.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class CodigoFiscalOperacao implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idCodigoFiscalOperacao;

    /** persistent field */
    private Long cdCfop;

    /** persistent field */
    private String dsCfop;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List ramoAtividades;

    public Long getIdCodigoFiscalOperacao() {
        return this.idCodigoFiscalOperacao;
    }

    public void setIdCodigoFiscalOperacao(Long idCodigoFiscalOperacao) {
        this.idCodigoFiscalOperacao = idCodigoFiscalOperacao;
    }

    public Long getCdCfop() {
        return this.cdCfop;
    }

    public void setCdCfop(Long cdCfop) {
        this.cdCfop = cdCfop;
    }

    public String getDsCfop() {
        return this.dsCfop;
    }

    public void setDsCfop(String dsCfop) {
        this.dsCfop = dsCfop;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.RamoAtividade.class)     
    public List getRamoAtividades() {
        return this.ramoAtividades;
    }

    public void setRamoAtividades(List ramoAtividades) {
        this.ramoAtividades = ramoAtividades;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idCodigoFiscalOperacao",
				getIdCodigoFiscalOperacao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof CodigoFiscalOperacao))
			return false;
        CodigoFiscalOperacao castOther = (CodigoFiscalOperacao) other;
		return new EqualsBuilder().append(this.getIdCodigoFiscalOperacao(),
				castOther.getIdCodigoFiscalOperacao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdCodigoFiscalOperacao())
            .toHashCode();
    }

}
