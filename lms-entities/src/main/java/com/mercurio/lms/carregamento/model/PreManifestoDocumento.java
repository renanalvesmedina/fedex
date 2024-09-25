package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import com.mercurio.lms.expedicao.model.DoctoServico;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.expedicao.model.Awb;

/** @author LMS Custom Hibernate CodeGenerator */
public class PreManifestoDocumento implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPreManifestoDocumento;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.DoctoServico doctoServico;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.Manifesto manifesto;
    
    private Integer versao;

    /** nullable persistent field */
    private Integer nrOrdem;
    
    private Awb awb;

    public PreManifestoDocumento() {
    }

    public PreManifestoDocumento(Long idPreManifestoDocumento, DoctoServico doctoServico, Manifesto manifesto, Integer versao, Integer nrOrdem, Awb awb) {
        this.idPreManifestoDocumento = idPreManifestoDocumento;
        this.doctoServico = doctoServico;
        this.manifesto = manifesto;
        this.versao = versao;
        this.nrOrdem = nrOrdem;
        this.awb = awb;
    }

    public Long getIdPreManifestoDocumento() {
        return this.idPreManifestoDocumento;
    }

    public void setIdPreManifestoDocumento(Long idPreManifestoDocumento) {
        this.idPreManifestoDocumento = idPreManifestoDocumento;
    }

    public com.mercurio.lms.expedicao.model.DoctoServico getDoctoServico() {
        return this.doctoServico;
    }

	public void setDoctoServico(
			com.mercurio.lms.expedicao.model.DoctoServico doctoServico) {
        this.doctoServico = doctoServico;
    }

    public com.mercurio.lms.carregamento.model.Manifesto getManifesto() {
        return this.manifesto;
    }

	public void setManifesto(
			com.mercurio.lms.carregamento.model.Manifesto manifesto) {
        this.manifesto = manifesto;
    }
    
	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}      

    public Integer getNrOrdem() {
		return nrOrdem;
	}

	public void setNrOrdem(Integer nrOrdem) {
		this.nrOrdem = nrOrdem;
	}

	public Awb getAwb() {
		return awb;
	}

	public void setAwb(Awb awb) {
		this.awb = awb;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idPreManifestoDocumento",
				getIdPreManifestoDocumento()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof PreManifestoDocumento))
			return false;
        PreManifestoDocumento castOther = (PreManifestoDocumento) other;
		return new EqualsBuilder().append(this.getIdPreManifestoDocumento(),
				castOther.getIdPreManifestoDocumento()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPreManifestoDocumento())
            .toHashCode();
    }

}
