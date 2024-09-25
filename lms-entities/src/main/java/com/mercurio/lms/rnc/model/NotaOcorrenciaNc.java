package com.mercurio.lms.rnc.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class NotaOcorrenciaNc implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idNotaOcorrenciaNc;

    /** persistent field */
    private Integer nrNotaFiscal;
    
    /** persistent field */
    private com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade ocorrenciaNaoConformidade;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.NotaFiscalConhecimento notaFiscalConhecimento;

    public Long getIdNotaOcorrenciaNc() {
        return this.idNotaOcorrenciaNc;
    }

    public void setIdNotaOcorrenciaNc(Long idNotaOcorrenciaNc) {
        this.idNotaOcorrenciaNc = idNotaOcorrenciaNc;
    }

    public Integer getNrNotaFiscal() {
		return nrNotaFiscal;
	}

	public void setNrNotaFiscal(Integer nrNotaFiscal) {
		this.nrNotaFiscal = nrNotaFiscal;
	}

	public com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade getOcorrenciaNaoConformidade() {
        return this.ocorrenciaNaoConformidade;
    }

	public void setOcorrenciaNaoConformidade(
			com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade ocorrenciaNaoConformidade) {
        this.ocorrenciaNaoConformidade = ocorrenciaNaoConformidade;
    }

    public com.mercurio.lms.expedicao.model.NotaFiscalConhecimento getNotaFiscalConhecimento() {
        return this.notaFiscalConhecimento;
    }

	public void setNotaFiscalConhecimento(
			com.mercurio.lms.expedicao.model.NotaFiscalConhecimento notaFiscalConhecimento) {
        this.notaFiscalConhecimento = notaFiscalConhecimento;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idNotaOcorrenciaNc",
				getIdNotaOcorrenciaNc()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof NotaOcorrenciaNc))
			return false;
        NotaOcorrenciaNc castOther = (NotaOcorrenciaNc) other;
		return new EqualsBuilder().append(this.getIdNotaOcorrenciaNc(),
				castOther.getIdNotaOcorrenciaNc()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdNotaOcorrenciaNc())
            .toHashCode();
    }
}