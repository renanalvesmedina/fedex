package com.mercurio.lms.pendencia.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

/** @author LMS Custom Hibernate CodeGenerator */
public class NfCartaMercadoria implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idNfCartaMercadoria;

    /** persistent field */
    private YearMonthDay dtSaida;

    /** persistent field */
    private com.mercurio.lms.pendencia.model.CartaMercadoriaDisposicao cartaMercadoriaDisposicao;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.NotaFiscalConhecimento notaFiscalConhecimento;

    public Long getIdNfCartaMercadoria() {
        return this.idNfCartaMercadoria;
    }

    public void setIdNfCartaMercadoria(Long idNfCartaMercadoria) {
        this.idNfCartaMercadoria = idNfCartaMercadoria;
    }

    public YearMonthDay getDtSaida() {
        return this.dtSaida;
    }

    public void setDtSaida(YearMonthDay dtSaida) {
        this.dtSaida = dtSaida;
    }

    public com.mercurio.lms.pendencia.model.CartaMercadoriaDisposicao getCartaMercadoriaDisposicao() {
        return this.cartaMercadoriaDisposicao;
    }

	public void setCartaMercadoriaDisposicao(
			com.mercurio.lms.pendencia.model.CartaMercadoriaDisposicao cartaMercadoriaDisposicao) {
        this.cartaMercadoriaDisposicao = cartaMercadoriaDisposicao;
    }

    public com.mercurio.lms.expedicao.model.NotaFiscalConhecimento getNotaFiscalConhecimento() {
        return this.notaFiscalConhecimento;
    }

	public void setNotaFiscalConhecimento(
			com.mercurio.lms.expedicao.model.NotaFiscalConhecimento notaFiscalConhecimento) {
        this.notaFiscalConhecimento = notaFiscalConhecimento;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idNfCartaMercadoria",
				getIdNfCartaMercadoria()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof NfCartaMercadoria))
			return false;
        NfCartaMercadoria castOther = (NfCartaMercadoria) other;
		return new EqualsBuilder().append(this.getIdNfCartaMercadoria(),
				castOther.getIdNfCartaMercadoria()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdNfCartaMercadoria())
            .toHashCode();
    }

}
