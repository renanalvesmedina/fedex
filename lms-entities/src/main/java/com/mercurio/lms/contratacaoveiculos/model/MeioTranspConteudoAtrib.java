package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class MeioTranspConteudoAtrib implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMeioTranspConteudoAtrib;

    /** nullable persistent field */
    private String dsConteudo;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporte;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.ModeloMeioTranspAtributo modeloMeioTranspAtributo;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.ConteudoAtributoModelo conteudoAtributoModelo;

    public Long getIdMeioTranspConteudoAtrib() {
        return this.idMeioTranspConteudoAtrib;
    }

    public void setIdMeioTranspConteudoAtrib(Long idMeioTranspConteudoAtrib) {
        this.idMeioTranspConteudoAtrib = idMeioTranspConteudoAtrib;
    }

    public String getDsConteudo() {
        return this.dsConteudo;
    }

    public void setDsConteudo(String dsConteudo) {
        this.dsConteudo = dsConteudo;
    }

    public com.mercurio.lms.contratacaoveiculos.model.MeioTransporte getMeioTransporte() {
        return this.meioTransporte;
    }

	public void setMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporte) {
        this.meioTransporte = meioTransporte;
    }

    public com.mercurio.lms.contratacaoveiculos.model.ModeloMeioTranspAtributo getModeloMeioTranspAtributo() {
        return this.modeloMeioTranspAtributo;
    }

	public void setModeloMeioTranspAtributo(
			com.mercurio.lms.contratacaoveiculos.model.ModeloMeioTranspAtributo modeloMeioTranspAtributo) {
        this.modeloMeioTranspAtributo = modeloMeioTranspAtributo;
    }

    public com.mercurio.lms.contratacaoveiculos.model.ConteudoAtributoModelo getConteudoAtributoModelo() {
        return this.conteudoAtributoModelo;
    }

	public void setConteudoAtributoModelo(
			com.mercurio.lms.contratacaoveiculos.model.ConteudoAtributoModelo conteudoAtributoModelo) {
        this.conteudoAtributoModelo = conteudoAtributoModelo;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMeioTranspConteudoAtrib",
				getIdMeioTranspConteudoAtrib()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MeioTranspConteudoAtrib))
			return false;
        MeioTranspConteudoAtrib castOther = (MeioTranspConteudoAtrib) other;
		return new EqualsBuilder().append(this.getIdMeioTranspConteudoAtrib(),
				castOther.getIdMeioTranspConteudoAtrib()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMeioTranspConteudoAtrib())
            .toHashCode();
    }

}
