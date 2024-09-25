package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class DocumentoAnexo implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDocumentoAnexo;

    /** nullable persistent field */
    private String dsDocumento;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.CtoInternacional ctoInternacional;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.AnexoDoctoServico anexoDoctoServico;

    public Long getIdDocumentoAnexo() {
        return this.idDocumentoAnexo;
    }

    public void setIdDocumentoAnexo(Long idDocumentoAnexo) {
        this.idDocumentoAnexo = idDocumentoAnexo;
    }

    public String getDsDocumento() {
        return this.dsDocumento;
    }

    public void setDsDocumento(String dsDocumento) {
        this.dsDocumento = dsDocumento;
    }

    public com.mercurio.lms.expedicao.model.CtoInternacional getCtoInternacional() {
        return this.ctoInternacional;
    }

	public void setCtoInternacional(
			com.mercurio.lms.expedicao.model.CtoInternacional ctoInternacional) {
        this.ctoInternacional = ctoInternacional;
    }

    public com.mercurio.lms.expedicao.model.AnexoDoctoServico getAnexoDoctoServico() {
        return this.anexoDoctoServico;
    }

	public void setAnexoDoctoServico(
			com.mercurio.lms.expedicao.model.AnexoDoctoServico anexoDoctoServico) {
        this.anexoDoctoServico = anexoDoctoServico;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDocumentoAnexo",
				getIdDocumentoAnexo()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DocumentoAnexo))
			return false;
        DocumentoAnexo castOther = (DocumentoAnexo) other;
		return new EqualsBuilder().append(this.getIdDocumentoAnexo(),
				castOther.getIdDocumentoAnexo()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDocumentoAnexo()).toHashCode();
    }

}
