package com.mercurio.lms.prestcontasciaaerea.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ValorPrestacaoConta implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idValorPrestacaoConta;

    /** persistent field */
    private BigDecimal vlTipoPrestacaoConta;

    /** persistent field */
    private String tpValor;

    /** persistent field */
    private String tpFormaPagamento;

    /** persistent field */
    private com.mercurio.lms.prestcontasciaaerea.model.PrestacaoConta prestacaoConta;

    /** full constructor */
	public ValorPrestacaoConta(
			BigDecimal vlTipoPrestacaoConta,
			String tpValor,
			String tpFormaPagamento,
			com.mercurio.lms.prestcontasciaaerea.model.PrestacaoConta prestacaoConta) {
        this.vlTipoPrestacaoConta = vlTipoPrestacaoConta;
        this.tpValor = tpValor;
        this.tpFormaPagamento = tpFormaPagamento;
        this.prestacaoConta = prestacaoConta;
    }

    /** default constructor */
    public ValorPrestacaoConta() {
    }

    public Long getIdValorPrestacaoConta() {
        return this.idValorPrestacaoConta;
    }

    public void setIdValorPrestacaoConta(Long idValorPrestacaoConta) {
        this.idValorPrestacaoConta = idValorPrestacaoConta;
    }

    public BigDecimal getVlTipoPrestacaoConta() {
        return this.vlTipoPrestacaoConta;
    }

    public void setVlTipoPrestacaoConta(BigDecimal vlTipoPrestacaoConta) {
        this.vlTipoPrestacaoConta = vlTipoPrestacaoConta;
    }

    public String getTpValor() {
        return this.tpValor;
    }

    public void setTpValor(String tpValor) {
        this.tpValor = tpValor;
    }

    public String getTpFormaPagamento() {
        return this.tpFormaPagamento;
    }

    public void setTpFormaPagamento(String tpFormaPagamento) {
        this.tpFormaPagamento = tpFormaPagamento;
    }

    public com.mercurio.lms.prestcontasciaaerea.model.PrestacaoConta getPrestacaoConta() {
        return this.prestacaoConta;
    }

	public void setPrestacaoConta(
			com.mercurio.lms.prestcontasciaaerea.model.PrestacaoConta prestacaoConta) {
        this.prestacaoConta = prestacaoConta;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idValorPrestacaoConta",
				getIdValorPrestacaoConta()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ValorPrestacaoConta))
			return false;
        ValorPrestacaoConta castOther = (ValorPrestacaoConta) other;
		return new EqualsBuilder().append(this.getIdValorPrestacaoConta(),
				castOther.getIdValorPrestacaoConta()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdValorPrestacaoConta())
            .toHashCode();
    }

}
