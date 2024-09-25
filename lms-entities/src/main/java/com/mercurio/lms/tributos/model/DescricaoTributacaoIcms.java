package com.mercurio.lms.tributos.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class DescricaoTributacaoIcms implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDescricaoTributacaoIcms;
    
    private Integer versao;

    /** persistent field */
    private com.mercurio.lms.tributos.model.TipoTributacaoIcms tipoTributacaoIcms;

    /** persistent field */
    private com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativa;
    
    private List observacoesICMS;

    public Long getIdDescricaoTributacaoIcms() {
        return this.idDescricaoTributacaoIcms;
    }

    public void setIdDescricaoTributacaoIcms(Long idDescricaoTributacaoIcms) {
        this.idDescricaoTributacaoIcms = idDescricaoTributacaoIcms;
    }

     public com.mercurio.lms.tributos.model.TipoTributacaoIcms getTipoTributacaoIcms() {
        return this.tipoTributacaoIcms;
    }

	public void setTipoTributacaoIcms(
			com.mercurio.lms.tributos.model.TipoTributacaoIcms tipoTributacaoIcms) {
        this.tipoTributacaoIcms = tipoTributacaoIcms;
    }

    public com.mercurio.lms.municipios.model.UnidadeFederativa getUnidadeFederativa() {
        return this.unidadeFederativa;
    }

	public void setUnidadeFederativa(
			com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativa) {
        this.unidadeFederativa = unidadeFederativa;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDescricaoTributacaoIcms",
				getIdDescricaoTributacaoIcms()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DescricaoTributacaoIcms))
			return false;
        DescricaoTributacaoIcms castOther = (DescricaoTributacaoIcms) other;
		return new EqualsBuilder().append(this.getIdDescricaoTributacaoIcms(),
				castOther.getIdDescricaoTributacaoIcms()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDescricaoTributacaoIcms())
            .toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public List getObservacoesICMS() {
		return observacoesICMS;
	}

	public void setObservacoesICMS(List observacoesICMS) {
		this.observacoesICMS = observacoesICMS;
	}

}
