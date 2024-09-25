package com.mercurio.lms.rnc.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class FotoOcorrencia implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idFotoOcorrencia;

    /** persistent field */
    private String dsFotoOcorrencia;

    /** persistent field */
    private com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade ocorrenciaNaoConformidade;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Foto foto;
    
    private DomainValue tpAnexo;

    public Long getIdFotoOcorrencia() {
        return this.idFotoOcorrencia;
    }

    public void setIdFotoOcorrencia(Long idFotoOcorrencia) {
        this.idFotoOcorrencia = idFotoOcorrencia;
    }

    public String getDsFotoOcorrencia() {
        return this.dsFotoOcorrencia;
    }

    public void setDsFotoOcorrencia(String dsFotoOcorrencia) {
        this.dsFotoOcorrencia = dsFotoOcorrencia;
    }

    public com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade getOcorrenciaNaoConformidade() {
        return this.ocorrenciaNaoConformidade;
    }

	public void setOcorrenciaNaoConformidade(
			com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade ocorrenciaNaoConformidade) {
        this.ocorrenciaNaoConformidade = ocorrenciaNaoConformidade;
    }

    public com.mercurio.lms.configuracoes.model.Foto getFoto() {
        return this.foto;
    }

    public void setFoto(com.mercurio.lms.configuracoes.model.Foto foto) {
        this.foto = foto;
    }
    
	public DomainValue getTpAnexo() {
		return tpAnexo;
	}

	public void setTpAnexo(DomainValue tpAnexo) {
		this.tpAnexo = tpAnexo;
	}

    public String toString() {
		return new ToStringBuilder(this).append("idFotoOcorrencia",
				getIdFotoOcorrencia()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FotoOcorrencia))
			return false;
        FotoOcorrencia castOther = (FotoOcorrencia) other;
		return new EqualsBuilder().append(this.getIdFotoOcorrencia(),
				castOther.getIdFotoOcorrencia()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFotoOcorrencia()).toHashCode();
    }

}
