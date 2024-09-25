package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ComplementoEmpresaCedente implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idComplementoEmpresaCedente;

    /** persistent field */
    private Long nrIntervaloInicialBoleto;

    /** persistent field */
    private Long nrIntervaloFinalBoleto;

    /** persistent field */
    private Long nrUltimoBoleto;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Empresa empresa;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Cedente cedente;

    public Long getIdComplementoEmpresaCedente() {
        return this.idComplementoEmpresaCedente;
    }

    public void setIdComplementoEmpresaCedente(Long idComplementoEmpresaCedente) {
        this.idComplementoEmpresaCedente = idComplementoEmpresaCedente;
    }

    public Long getNrIntervaloInicialBoleto() {
        return this.nrIntervaloInicialBoleto;
    }

    public void setNrIntervaloInicialBoleto(Long nrIntervaloInicialBoleto) {
        this.nrIntervaloInicialBoleto = nrIntervaloInicialBoleto;
    }

    public Long getNrIntervaloFinalBoleto() {
        return this.nrIntervaloFinalBoleto;
    }

    public void setNrIntervaloFinalBoleto(Long nrIntervaloFinalBoleto) {
        this.nrIntervaloFinalBoleto = nrIntervaloFinalBoleto;
    }

    public Long getNrUltimoBoleto() {
        return this.nrUltimoBoleto;
    }

    public void setNrUltimoBoleto(Long nrUltimoBoleto) {
        this.nrUltimoBoleto = nrUltimoBoleto;
    }

    public com.mercurio.lms.municipios.model.Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(com.mercurio.lms.municipios.model.Empresa empresa) {
        this.empresa = empresa;
    }

    public com.mercurio.lms.contasreceber.model.Cedente getCedente() {
        return this.cedente;
    }

    public void setCedente(com.mercurio.lms.contasreceber.model.Cedente cedente) {
        this.cedente = cedente;
    }

    public String toString() {
		return new ToStringBuilder(this).append("IdComplementoEmpresaCedente",
				getIdComplementoEmpresaCedente()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ComplementoEmpresaCedente))
			return false;
        ComplementoEmpresaCedente castOther = (ComplementoEmpresaCedente) other;
		return new EqualsBuilder().append(
				this.getIdComplementoEmpresaCedente(),
				castOther.getIdComplementoEmpresaCedente()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdComplementoEmpresaCedente())
            .toHashCode();
    }

}
