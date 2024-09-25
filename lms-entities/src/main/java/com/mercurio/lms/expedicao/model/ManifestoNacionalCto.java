package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ManifestoNacionalCto implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idManifestoNacionalCto;

    /** persistent field */
    private Boolean blGeraFronteiraRapida;

    /** nullable persistent field */
    private com.mercurio.lms.expedicao.model.ManifestoViagemNacional manifestoViagemNacional;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.Conhecimento conhecimento;

    /** nullable persistent field */
    private Long nrIntMovimentoGeracao;

    public ManifestoNacionalCto() {
    }

    public ManifestoNacionalCto(Long idManifestoNacionalCto, Boolean blGeraFronteiraRapida, ManifestoViagemNacional manifestoViagemNacional, Conhecimento conhecimento, Long nrIntMovimentoGeracao) {
        this.idManifestoNacionalCto = idManifestoNacionalCto;
        this.blGeraFronteiraRapida = blGeraFronteiraRapida;
        this.manifestoViagemNacional = manifestoViagemNacional;
        this.conhecimento = conhecimento;
        this.nrIntMovimentoGeracao = nrIntMovimentoGeracao;
    }

    public Long getIdManifestoNacionalCto() {
        return this.idManifestoNacionalCto;
    }

    public void setIdManifestoNacionalCto(Long idManifestoNacionalCto) {
        this.idManifestoNacionalCto = idManifestoNacionalCto;
    }
    
    public Boolean getBlGeraFronteiraRapida() {
        return this.blGeraFronteiraRapida;
    }

    public void setBlGeraFronteiraRapida(Boolean blGeraFronteiraRapida) {
        this.blGeraFronteiraRapida = blGeraFronteiraRapida;
    }

    public com.mercurio.lms.expedicao.model.ManifestoViagemNacional getManifestoViagemNacional() {
        return this.manifestoViagemNacional;
    }

	public void setManifestoViagemNacional(
			com.mercurio.lms.expedicao.model.ManifestoViagemNacional manifestoViagemNacional) {
        this.manifestoViagemNacional = manifestoViagemNacional;
    }

    public com.mercurio.lms.expedicao.model.Conhecimento getConhecimento() {
        return this.conhecimento;
    }

	public void setConhecimento(
			com.mercurio.lms.expedicao.model.Conhecimento conhecimento) {
        this.conhecimento = conhecimento;
    }

    public Long getNrIntMovimentoGeracao() {
		return nrIntMovimentoGeracao;
	}

	public void setNrIntMovimentoGeracao(Long nrIntMovimentoGeracao) {
		this.nrIntMovimentoGeracao = nrIntMovimentoGeracao;
	}

    public String toString() {
		return new ToStringBuilder(this).append("idManifestoNacionalCto",
				getIdManifestoNacionalCto()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ManifestoNacionalCto))
			return false;
        ManifestoNacionalCto castOther = (ManifestoNacionalCto) other;
		return new EqualsBuilder().append(this.getIdManifestoNacionalCto(),
				castOther.getIdManifestoNacionalCto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdManifestoNacionalCto())
            .toHashCode();
    }

}
