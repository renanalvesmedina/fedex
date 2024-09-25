package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ObservacaoDoctoServico implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idObservacaoDoctoServico;

    /** persistent field */
    private String dsObservacaoDoctoServico;

    /** nullable persistent field */
    private Boolean blPrioridade;

    /** persistent field */
    private String cdEmbLegalMastersaf;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.DoctoServico doctoServico;

	public ObservacaoDoctoServico(Long idObservacaoDoctoServico, String dsObservacaoDoctoServico, Boolean blPrioridade, String cdEmbLegalMastersaf, DoctoServico doctoServico) {
		this.idObservacaoDoctoServico = idObservacaoDoctoServico;
		this.dsObservacaoDoctoServico = dsObservacaoDoctoServico;
		this.blPrioridade = blPrioridade;
		this.cdEmbLegalMastersaf = cdEmbLegalMastersaf;
		this.doctoServico = doctoServico;
	}

	public ObservacaoDoctoServico(Boolean prioridade, String servico) {
		blPrioridade = prioridade;
		dsObservacaoDoctoServico = servico;
	}

	public ObservacaoDoctoServico() {
	}

	public ObservacaoDoctoServico(String dsObservacaoDoctoServico) {
		this.dsObservacaoDoctoServico = dsObservacaoDoctoServico;
	}

	public Long getIdObservacaoDoctoServico() {
        return this.idObservacaoDoctoServico;
    }

    public void setIdObservacaoDoctoServico(Long idObservacaoDoctoServico) {
        this.idObservacaoDoctoServico = idObservacaoDoctoServico;
    }

    public String getDsObservacaoDoctoServico() {
        return this.dsObservacaoDoctoServico;
    }

    public void setDsObservacaoDoctoServico(String dsObservacaoDoctoServico) {
        this.dsObservacaoDoctoServico = dsObservacaoDoctoServico;
    }

    public Boolean getBlPrioridade() {
        return this.blPrioridade;
    }

    public void setBlPrioridade(Boolean blPrioridade) {
        this.blPrioridade = blPrioridade;
    }

    public com.mercurio.lms.expedicao.model.DoctoServico getDoctoServico() {
        return this.doctoServico;
    }

	public void setDoctoServico(
			com.mercurio.lms.expedicao.model.DoctoServico doctoServico) {
        this.doctoServico = doctoServico;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idObservacaoDoctoServico",
				getIdObservacaoDoctoServico()).toString();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((doctoServico == null) ? 0 : doctoServico.hashCode());
		result = prime
				* result
				+ ((idObservacaoDoctoServico == null) ? 0
						: idObservacaoDoctoServico.hashCode());
		return result;
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObservacaoDoctoServico other = (ObservacaoDoctoServico) obj;
		if (doctoServico == null) {
			if (other.doctoServico != null)
				return false;
		} else if (!doctoServico.equals(other.doctoServico))
			return false;
		if (idObservacaoDoctoServico == null) {
			if (other.idObservacaoDoctoServico != null)
				return false;
		} else if (!idObservacaoDoctoServico
				.equals(other.idObservacaoDoctoServico))
			return false;
		return true;
    }

	/**
	 * @return the cdEmbLegalMastersaf
	 */
	public String getCdEmbLegalMastersaf() {
		return cdEmbLegalMastersaf;
}

	/**
	 * @param cdEmbLegalMastersaf
	 *            the cdEmbLegalMastersaf to set
	 */
	public void setCdEmbLegalMastersaf(String cdEmbLegalMastersaf) {
		this.cdEmbLegalMastersaf = cdEmbLegalMastersaf;
	}

}
