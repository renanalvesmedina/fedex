package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class NotaFiscalSimulacao implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idNotaFiscalSimulacao;

    /** persistent field */
    private BigDecimal psReal;

    /** persistent field */
    private BigDecimal vlTotalNf;

    /** nullable persistent field */
    private Long nrNotaFiscal;

    /** nullable persistent field */
    private Integer nrVolumes;

    /** nullable persistent field */
    private BigDecimal psCubado;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipioByIdMunicipioOrigem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipioByIdMunicipioDestino;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Simulacao simulacao;

    public Long getIdNotaFiscalSimulacao() {
        return this.idNotaFiscalSimulacao;
    }

    public void setIdNotaFiscalSimulacao(Long idNotaFiscalSimulacao) {
        this.idNotaFiscalSimulacao = idNotaFiscalSimulacao;
    }

    public BigDecimal getPsReal() {
        return this.psReal;
    }

    public void setPsReal(BigDecimal psReal) {
        this.psReal = psReal;
    }

    public BigDecimal getVlTotalNf() {
        return this.vlTotalNf;
    }

    public void setVlTotalNf(BigDecimal vlTotalNf) {
        this.vlTotalNf = vlTotalNf;
    }

    public Long getNrNotaFiscal() {
        return this.nrNotaFiscal;
    }

    public void setNrNotaFiscal(Long nrNotaFiscal) {
        this.nrNotaFiscal = nrNotaFiscal;
    }

    public Integer getNrVolumes() {
        return this.nrVolumes;
    }

    public void setNrVolumes(Integer nrVolumes) {
        this.nrVolumes = nrVolumes;
    }

    public BigDecimal getPsCubado() {
        return this.psCubado;
    }

    public void setPsCubado(BigDecimal psCubado) {
        this.psCubado = psCubado;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipioByIdMunicipioOrigem() {
        return this.municipioByIdMunicipioOrigem;
    }

	public void setMunicipioByIdMunicipioOrigem(
			com.mercurio.lms.municipios.model.Municipio municipioByIdMunicipioOrigem) {
        this.municipioByIdMunicipioOrigem = municipioByIdMunicipioOrigem;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipioByIdMunicipioDestino() {
        return this.municipioByIdMunicipioDestino;
    }

	public void setMunicipioByIdMunicipioDestino(
			com.mercurio.lms.municipios.model.Municipio municipioByIdMunicipioDestino) {
        this.municipioByIdMunicipioDestino = municipioByIdMunicipioDestino;
    }

    public com.mercurio.lms.vendas.model.Simulacao getSimulacao() {
        return this.simulacao;
    }

    public void setSimulacao(com.mercurio.lms.vendas.model.Simulacao simulacao) {
        this.simulacao = simulacao;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idNotaFiscalSimulacao",
				getIdNotaFiscalSimulacao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof NotaFiscalSimulacao))
			return false;
        NotaFiscalSimulacao castOther = (NotaFiscalSimulacao) other;
		return new EqualsBuilder().append(this.getIdNotaFiscalSimulacao(),
				castOther.getIdNotaFiscalSimulacao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdNotaFiscalSimulacao())
            .toHashCode();
    }

}
