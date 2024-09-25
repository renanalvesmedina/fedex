package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class NfDadosComp implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idNfDadosComp;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.DadosComplemento dadosComplemento;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.NotaFiscalConhecimento notaFiscalConhecimento;

    public Long getIdNfDadosComp() {
        return this.idNfDadosComp;
    }

    public void setIdNfDadosComp(Long idNfDadosComp) {
        this.idNfDadosComp = idNfDadosComp;
    }

    public com.mercurio.lms.expedicao.model.DadosComplemento getDadosComplemento() {
        return this.dadosComplemento;
    }

	public void setDadosComplemento(
			com.mercurio.lms.expedicao.model.DadosComplemento dadosComplemento) {
        this.dadosComplemento = dadosComplemento;
    }

    public com.mercurio.lms.expedicao.model.NotaFiscalConhecimento getNotaFiscalConhecimento() {
        return this.notaFiscalConhecimento;
    }

	public void setNotaFiscalConhecimento(
			com.mercurio.lms.expedicao.model.NotaFiscalConhecimento notaFiscalConhecimento) {
        this.notaFiscalConhecimento = notaFiscalConhecimento;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idNfDadosComp",
				getIdNfDadosComp()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof NfDadosComp))
			return false;
        NfDadosComp castOther = (NfDadosComp) other;
		return new EqualsBuilder().append(this.getIdNfDadosComp(),
				castOther.getIdNfDadosComp()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdNfDadosComp()).toHashCode();
    }

}
