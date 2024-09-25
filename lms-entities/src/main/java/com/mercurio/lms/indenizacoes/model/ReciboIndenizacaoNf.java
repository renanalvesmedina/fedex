package com.mercurio.lms.indenizacoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ReciboIndenizacaoNf implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idReciboIndenizacaoNf;

    /** persistent field */
    private com.mercurio.lms.indenizacoes.model.DoctoServicoIndenizacao doctoServicoIndenizacao;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.NotaFiscalConhecimento notaFiscalConhecimento;

    public Long getIdReciboIndenizacaoNf() {
        return this.idReciboIndenizacaoNf;
    }

    public void setIdReciboIndenizacaoNf(Long idReciboIndenizacaoNf) {
        this.idReciboIndenizacaoNf = idReciboIndenizacaoNf;
    }

    public com.mercurio.lms.indenizacoes.model.DoctoServicoIndenizacao getDoctoServicoIndenizacao() {
        return this.doctoServicoIndenizacao;
    }

	public void setDoctoServicoIndenizacao(
			com.mercurio.lms.indenizacoes.model.DoctoServicoIndenizacao doctoServicoIndenizacao) {
        this.doctoServicoIndenizacao = doctoServicoIndenizacao;
    }

    public com.mercurio.lms.expedicao.model.NotaFiscalConhecimento getNotaFiscalConhecimento() {
        return this.notaFiscalConhecimento;
    }

	public void setNotaFiscalConhecimento(
			com.mercurio.lms.expedicao.model.NotaFiscalConhecimento notaFiscalConhecimento) {
        this.notaFiscalConhecimento = notaFiscalConhecimento;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idReciboIndenizacaoNf",
				getIdReciboIndenizacaoNf()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ReciboIndenizacaoNf))
			return false;
        ReciboIndenizacaoNf castOther = (ReciboIndenizacaoNf) other;
		return new EqualsBuilder().append(this.getIdReciboIndenizacaoNf(),
				castOther.getIdReciboIndenizacaoNf()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdReciboIndenizacaoNf())
            .toHashCode();
    }

}
