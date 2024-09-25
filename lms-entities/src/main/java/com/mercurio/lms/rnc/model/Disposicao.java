package com.mercurio.lms.rnc.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

/** @author LMS Custom Hibernate CodeGenerator */
public class Disposicao implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDisposicao;

    /** persistent field */
    private String dsDisposicao;

    /** persistent field */
    private DateTime dhDisposicao;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.rnc.model.MotivoDisposicao motivoDisposicao;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;
    
    /** persistent field */
    private com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade ocorrenciaNaoConformidade;

    public Long getIdDisposicao() {
        return this.idDisposicao;
    }

    public void setIdDisposicao(Long idDisposicao) {
        this.idDisposicao = idDisposicao;
    }

    public String getDsDisposicao() {
        return this.dsDisposicao;
    }

    public void setDsDisposicao(String dsDisposicao) {
        this.dsDisposicao = dsDisposicao;
    }

    public DateTime getDhDisposicao() {
        return this.dhDisposicao;
    }

    public void setDhDisposicao(DateTime dhDisposicao) {
        this.dhDisposicao = dhDisposicao;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.rnc.model.MotivoDisposicao getMotivoDisposicao() {
        return this.motivoDisposicao;
    }

	public void setMotivoDisposicao(
			com.mercurio.lms.rnc.model.MotivoDisposicao motivoDisposicao) {
        this.motivoDisposicao = motivoDisposicao;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade getOcorrenciaNaoConformidade() {
		return ocorrenciaNaoConformidade;
	}

	public void setOcorrenciaNaoConformidade(
			com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade ocorrenciaNaoConformidade) {
		this.ocorrenciaNaoConformidade = ocorrenciaNaoConformidade;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idDisposicao",
				getIdDisposicao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Disposicao))
			return false;
        Disposicao castOther = (Disposicao) other;
		return new EqualsBuilder().append(this.getIdDisposicao(),
				castOther.getIdDisposicao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDisposicao()).toHashCode();
    }

}
