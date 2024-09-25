package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class CiaAereaCliente implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idCiaAereaCliente;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Empresa empresa;

    public Long getIdCiaAereaCliente() {
        return this.idCiaAereaCliente;
    }

    public void setIdCiaAereaCliente(Long idCiaAereaCliente) {
        this.idCiaAereaCliente = idCiaAereaCliente;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public com.mercurio.lms.municipios.model.Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(com.mercurio.lms.municipios.model.Empresa empresa) {
        this.empresa = empresa;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idCiaAereaCliente",
				getIdCiaAereaCliente()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof CiaAereaCliente))
			return false;
        CiaAereaCliente castOther = (CiaAereaCliente) other;
		return new EqualsBuilder().append(this.getIdCiaAereaCliente(),
				castOther.getIdCiaAereaCliente()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdCiaAereaCliente())
            .toHashCode();
    }

}
