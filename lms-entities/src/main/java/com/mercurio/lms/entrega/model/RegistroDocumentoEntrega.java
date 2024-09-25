package com.mercurio.lms.entrega.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class RegistroDocumentoEntrega implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRegistroDocumentoEntrega;

    /** persistent field */
    private Boolean blComprovanteRecolhido;

    /** nullable persistent field */
    private String nrComprovante;

    /** nullable persistent field */
    private String obComprovante;
    
    /** nullable persistent field */
    private DomainValue tpSituacaoRegistro;

    /** persistent field */
    private com.mercurio.lms.entrega.model.TipoDocumentoEntrega tipoDocumentoEntrega;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.DoctoServico doctoServico;

    /** persistent field */
    private List documentoMirs;

    public Long getIdRegistroDocumentoEntrega() {
        return this.idRegistroDocumentoEntrega;
    }

    public void setIdRegistroDocumentoEntrega(Long idRegistroDocumentoEntrega) {
        this.idRegistroDocumentoEntrega = idRegistroDocumentoEntrega;
    }

    public Boolean getBlComprovanteRecolhido() {
        return this.blComprovanteRecolhido;
    }

    public void setBlComprovanteRecolhido(Boolean blComprovanteRecolhido) {
        this.blComprovanteRecolhido = blComprovanteRecolhido;
    }

    public String getNrComprovante() {
        return this.nrComprovante;
    }

    public void setNrComprovante(String nrComprovante) {
        this.nrComprovante = nrComprovante;
    }

    public String getObComprovante() {
        return this.obComprovante;
    }

    public void setObComprovante(String obComprovante) {
        this.obComprovante = obComprovante;
    }

    public com.mercurio.lms.entrega.model.TipoDocumentoEntrega getTipoDocumentoEntrega() {
        return this.tipoDocumentoEntrega;
    }

	public void setTipoDocumentoEntrega(
			com.mercurio.lms.entrega.model.TipoDocumentoEntrega tipoDocumentoEntrega) {
        this.tipoDocumentoEntrega = tipoDocumentoEntrega;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.expedicao.model.DoctoServico getDoctoServico() {
		return doctoServico;
	}

	public void setDoctoServico(
			com.mercurio.lms.expedicao.model.DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.entrega.model.DocumentoMir.class)     
    public List getDocumentoMirs() {
        return this.documentoMirs;
    }

    public void setDocumentoMirs(List documentoMirs) {
        this.documentoMirs = documentoMirs;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idRegistroDocumentoEntrega",
				getIdRegistroDocumentoEntrega()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RegistroDocumentoEntrega))
			return false;
        RegistroDocumentoEntrega castOther = (RegistroDocumentoEntrega) other;
		return new EqualsBuilder().append(this.getIdRegistroDocumentoEntrega(),
				castOther.getIdRegistroDocumentoEntrega()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRegistroDocumentoEntrega())
            .toHashCode();
    }

	public DomainValue getTpSituacaoRegistro() {
		return tpSituacaoRegistro;
	}

	public void setTpSituacaoRegistro(DomainValue tpSituacaoRegistro) {
		this.tpSituacaoRegistro = tpSituacaoRegistro;
	}

}
