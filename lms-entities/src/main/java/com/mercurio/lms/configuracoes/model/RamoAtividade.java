package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class RamoAtividade implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRamoAtividade;

    /** persistent field */
    private VarcharI18n dsRamoAtividade;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private com.mercurio.lms.tributos.model.CodigoFiscalOperacao codigoFiscalOperacao;

    /** persistent field */
    private List clientes;

    public Long getIdRamoAtividade() {
        return this.idRamoAtividade;
    }

    public void setIdRamoAtividade(Long idRamoAtividade) {
        this.idRamoAtividade = idRamoAtividade;
    }

    public VarcharI18n getDsRamoAtividade() {
		return dsRamoAtividade;
    }

	public void setDsRamoAtividade(VarcharI18n dsRamoAtividade) {
        this.dsRamoAtividade = dsRamoAtividade;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.tributos.model.CodigoFiscalOperacao getCodigoFiscalOperacao() {
        return this.codigoFiscalOperacao;
    }

	public void setCodigoFiscalOperacao(
			com.mercurio.lms.tributos.model.CodigoFiscalOperacao codigoFiscalOperacao) {
        this.codigoFiscalOperacao = codigoFiscalOperacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.Cliente.class)     
    public List getClientes() {
        return this.clientes;
    }

    public void setClientes(List clientes) {
        this.clientes = clientes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idRamoAtividade",
				getIdRamoAtividade()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RamoAtividade))
			return false;
        RamoAtividade castOther = (RamoAtividade) other;
		return new EqualsBuilder().append(this.getIdRamoAtividade(),
				castOther.getIdRamoAtividade()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRamoAtividade()).toHashCode();
    }

}
