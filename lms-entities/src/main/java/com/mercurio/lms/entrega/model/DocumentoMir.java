package com.mercurio.lms.entrega.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class DocumentoMir implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDocumentoMir;

    /** field */
    private Integer versao;
    
    /** persistent field */
    private com.mercurio.lms.entrega.model.RegistroDocumentoEntrega registroDocumentoEntrega;

    /** persistent field */
    private com.mercurio.lms.entrega.model.Mir mir;

    /** persistent field */
    private com.mercurio.lms.entrega.model.ReciboReembolso reciboReembolso;

    public Long getIdDocumentoMir() {
        return this.idDocumentoMir;
    }

    public void setIdDocumentoMir(Long idDocumentoMir) {
        this.idDocumentoMir = idDocumentoMir;
    }

    public com.mercurio.lms.entrega.model.RegistroDocumentoEntrega getRegistroDocumentoEntrega() {
        return this.registroDocumentoEntrega;
    }

	public void setRegistroDocumentoEntrega(
			com.mercurio.lms.entrega.model.RegistroDocumentoEntrega registroDocumentoEntrega) {
        this.registroDocumentoEntrega = registroDocumentoEntrega;
    }

    public com.mercurio.lms.entrega.model.Mir getMir() {
        return this.mir;
    }

    public void setMir(com.mercurio.lms.entrega.model.Mir mir) {
        this.mir = mir;
    }

    public com.mercurio.lms.entrega.model.ReciboReembolso getReciboReembolso() {
        return this.reciboReembolso;
    }

	public void setReciboReembolso(
			com.mercurio.lms.entrega.model.ReciboReembolso reciboReembolso) {
        this.reciboReembolso = reciboReembolso;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDocumentoMir",
				getIdDocumentoMir()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DocumentoMir))
			return false;
        DocumentoMir castOther = (DocumentoMir) other;
		return new EqualsBuilder().append(this.getIdDocumentoMir(),
				castOther.getIdDocumentoMir()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDocumentoMir()).toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

}
