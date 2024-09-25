package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ItemTransferencia implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idItemTransferencia;

    /** nullable persistent field */
    private String obItemTransferencia;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente clienteByIdNovoResponsavel;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente clienteByIdAntigoResponsavel;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Transferencia transferencia;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.MotivoTransferencia motivoTransferencia;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.DevedorDocServFat devedorDocServFat;
    
    /** persistent field */
    private com.mercurio.lms.vendas.model.DivisaoCliente divisaoClienteNovo;
    
    /** persistent field */
    private com.mercurio.lms.vendas.model.DivisaoCliente divisaoClienteAntigo;

    public Long getIdItemTransferencia() {
        return this.idItemTransferencia;
    }

    public void setIdItemTransferencia(Long idItemTransferencia) {
        this.idItemTransferencia = idItemTransferencia;
    }

    public String getObItemTransferencia() {
        return this.obItemTransferencia;
    }

    public void setObItemTransferencia(String obItemTransferencia) {
        this.obItemTransferencia = obItemTransferencia;
    }

    public com.mercurio.lms.vendas.model.Cliente getClienteByIdNovoResponsavel() {
        return this.clienteByIdNovoResponsavel;
    }

	public void setClienteByIdNovoResponsavel(
			com.mercurio.lms.vendas.model.Cliente clienteByIdNovoResponsavel) {
        this.clienteByIdNovoResponsavel = clienteByIdNovoResponsavel;
    }

    public com.mercurio.lms.vendas.model.Cliente getClienteByIdAntigoResponsavel() {
        return this.clienteByIdAntigoResponsavel;
    }

	public void setClienteByIdAntigoResponsavel(
			com.mercurio.lms.vendas.model.Cliente clienteByIdAntigoResponsavel) {
        this.clienteByIdAntigoResponsavel = clienteByIdAntigoResponsavel;
    }

    public com.mercurio.lms.contasreceber.model.Transferencia getTransferencia() {
        return this.transferencia;
    }

	public void setTransferencia(
			com.mercurio.lms.contasreceber.model.Transferencia transferencia) {
        this.transferencia = transferencia;
    }

    public com.mercurio.lms.contasreceber.model.MotivoTransferencia getMotivoTransferencia() {
        return this.motivoTransferencia;
    }

	public void setMotivoTransferencia(
			com.mercurio.lms.contasreceber.model.MotivoTransferencia motivoTransferencia) {
        this.motivoTransferencia = motivoTransferencia;
    }

    public com.mercurio.lms.contasreceber.model.DevedorDocServFat getDevedorDocServFat() {
        return this.devedorDocServFat;
    }

	public void setDevedorDocServFat(
			com.mercurio.lms.contasreceber.model.DevedorDocServFat devedorDocServFat) {
        this.devedorDocServFat = devedorDocServFat;
    }

    public com.mercurio.lms.vendas.model.DivisaoCliente getDivisaoClienteAntigo() {
		return divisaoClienteAntigo;
	}

	public void setDivisaoClienteAntigo(
			com.mercurio.lms.vendas.model.DivisaoCliente divisaoClienteAntigo) {
		this.divisaoClienteAntigo = divisaoClienteAntigo;
	}

	public com.mercurio.lms.vendas.model.DivisaoCliente getDivisaoClienteNovo() {
		return divisaoClienteNovo;
	}

	public void setDivisaoClienteNovo(
			com.mercurio.lms.vendas.model.DivisaoCliente divisaoClienteNovo) {
		this.divisaoClienteNovo = divisaoClienteNovo;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idItemTransferencia",
				getIdItemTransferencia()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ItemTransferencia))
			return false;
        ItemTransferencia castOther = (ItemTransferencia) other;
		return new EqualsBuilder().append(this.getIdItemTransferencia(),
				castOther.getIdItemTransferencia()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdItemTransferencia())
            .toHashCode();
    }

}
