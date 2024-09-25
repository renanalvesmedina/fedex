package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class CentralizadoraFaturamento implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idCentralizadoraFaturamento;

    /** persistent field */
    private DomainValue tpModal;

    /** persistent field */
    private DomainValue tpAbrangencia;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialCentralizada;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialCentralizadora;

    public Long getIdCentralizadoraFaturamento() {
        return this.idCentralizadoraFaturamento;
    }

    public void setIdCentralizadoraFaturamento(Long idCentralizadoraFaturamento) {
        this.idCentralizadoraFaturamento = idCentralizadoraFaturamento;
    }

    public DomainValue getTpModal() {
        return this.tpModal;
    }

    public void setTpModal(DomainValue tpModal) {
        this.tpModal = tpModal;
    }

    public DomainValue getTpAbrangencia() {
        return this.tpAbrangencia;
    }

    public void setTpAbrangencia(DomainValue tpAbrangencia) {
        this.tpAbrangencia = tpAbrangencia;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialCentralizada() {
        return this.filialByIdFilialCentralizada;
    }

	public void setFilialByIdFilialCentralizada(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialCentralizada) {
        this.filialByIdFilialCentralizada = filialByIdFilialCentralizada;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialCentralizadora() {
        return this.filialByIdFilialCentralizadora;
    }

	public void setFilialByIdFilialCentralizadora(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialCentralizadora) {
        this.filialByIdFilialCentralizadora = filialByIdFilialCentralizadora;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idCentralizadoraFaturamento",
				getIdCentralizadoraFaturamento()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof CentralizadoraFaturamento))
			return false;
        CentralizadoraFaturamento castOther = (CentralizadoraFaturamento) other;
		return new EqualsBuilder().append(
				this.getIdCentralizadoraFaturamento(),
				castOther.getIdCentralizadoraFaturamento()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdCentralizadoraFaturamento())
            .toHashCode();
    }

}
