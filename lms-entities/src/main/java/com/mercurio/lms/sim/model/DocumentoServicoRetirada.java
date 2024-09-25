package com.mercurio.lms.sim.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class DocumentoServicoRetirada implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDocumentoServicoRetirada;
    
    /** identifier field */
    private Integer versao;
    
    /** persistent field */
    private com.mercurio.lms.sim.model.SolicitacaoRetirada solicitacaoRetirada;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.DoctoServico doctoServico;
    
    /** persistent field */
    private com.mercurio.lms.workflow.model.Pendencia pendencia;
    
    public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer nrVersao) {
		this.versao = nrVersao;
	}

	public com.mercurio.lms.workflow.model.Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(com.mercurio.lms.workflow.model.Pendencia pendencia) {
		this.pendencia = pendencia;
	}
	
    public Long getIdDocumentoServicoRetirada() {
        return this.idDocumentoServicoRetirada;
    }

    public void setIdDocumentoServicoRetirada(Long idDocumentoServicoRetirada) {
        this.idDocumentoServicoRetirada = idDocumentoServicoRetirada;
    }

    public com.mercurio.lms.sim.model.SolicitacaoRetirada getSolicitacaoRetirada() {
        return this.solicitacaoRetirada;
    }

	public void setSolicitacaoRetirada(
			com.mercurio.lms.sim.model.SolicitacaoRetirada solicitacaoRetirada) {
        this.solicitacaoRetirada = solicitacaoRetirada;
    }

    public com.mercurio.lms.expedicao.model.DoctoServico getDoctoServico() {
        return this.doctoServico;
    }

	public void setDoctoServico(
			com.mercurio.lms.expedicao.model.DoctoServico doctoServico) {
        this.doctoServico = doctoServico;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDocumentoServicoRetirada",
				getIdDocumentoServicoRetirada()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DocumentoServicoRetirada))
			return false;
        DocumentoServicoRetirada castOther = (DocumentoServicoRetirada) other;
		return new EqualsBuilder().append(this.getIdDocumentoServicoRetirada(),
				castOther.getIdDocumentoServicoRetirada()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDocumentoServicoRetirada())
            .toHashCode();
    }

}
