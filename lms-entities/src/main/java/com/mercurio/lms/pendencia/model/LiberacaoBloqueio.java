package com.mercurio.lms.pendencia.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class LiberacaoBloqueio implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idLiberacaoBloqueio;

    /** persistent field */
    private com.mercurio.lms.pendencia.model.OcorrenciaPendencia ocorrenciaPendenciaByIdOcorrenciaLiberacao;

    /** persistent field */
    private com.mercurio.lms.pendencia.model.OcorrenciaPendencia ocorrenciaPendenciaByIdOcorrenciaBloqueio;

    public Long getIdLiberacaoBloqueio() {
        return this.idLiberacaoBloqueio;
    }

    public void setIdLiberacaoBloqueio(Long idLiberacaoBloqueio) {
        this.idLiberacaoBloqueio = idLiberacaoBloqueio;
    }

    public com.mercurio.lms.pendencia.model.OcorrenciaPendencia getOcorrenciaPendenciaByIdOcorrenciaLiberacao() {
        return this.ocorrenciaPendenciaByIdOcorrenciaLiberacao;
    }

	public void setOcorrenciaPendenciaByIdOcorrenciaLiberacao(
			com.mercurio.lms.pendencia.model.OcorrenciaPendencia ocorrenciaPendenciaByIdOcorrenciaLiberacao) {
        this.ocorrenciaPendenciaByIdOcorrenciaLiberacao = ocorrenciaPendenciaByIdOcorrenciaLiberacao;
    }

    public com.mercurio.lms.pendencia.model.OcorrenciaPendencia getOcorrenciaPendenciaByIdOcorrenciaBloqueio() {
        return this.ocorrenciaPendenciaByIdOcorrenciaBloqueio;
    }

	public void setOcorrenciaPendenciaByIdOcorrenciaBloqueio(
			com.mercurio.lms.pendencia.model.OcorrenciaPendencia ocorrenciaPendenciaByIdOcorrenciaBloqueio) {
        this.ocorrenciaPendenciaByIdOcorrenciaBloqueio = ocorrenciaPendenciaByIdOcorrenciaBloqueio;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idLiberacaoBloqueio",
				getIdLiberacaoBloqueio()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof LiberacaoBloqueio))
			return false;
        LiberacaoBloqueio castOther = (LiberacaoBloqueio) other;
		return new EqualsBuilder().append(this.getIdLiberacaoBloqueio(),
				castOther.getIdLiberacaoBloqueio()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdLiberacaoBloqueio())
            .toHashCode();
    }

}
