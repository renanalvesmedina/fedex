package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ModeloMeioTranspAtributo implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idModeloMeioTranspAtributo;

    /** persistent field */
    private Boolean blOpcional;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.AtributoMeioTransporte atributoMeioTransporte;

    /** persistent field */
    private TipoMeioTransporte tipoMeioTransporte;
    
    /** persistent field */
    private List meioTranspConteudoAtribs;

    /** persistent field */
    private List conteudoAtributoModelos;

    public Long getIdModeloMeioTranspAtributo() {
        return this.idModeloMeioTranspAtributo;
    }

    public void setIdModeloMeioTranspAtributo(Long idModeloMeioTranspAtributo) {
        this.idModeloMeioTranspAtributo = idModeloMeioTranspAtributo;
    }

    public Boolean getBlOpcional() {
        return this.blOpcional;
    }

    public void setBlOpcional(Boolean blOpcional) {
        this.blOpcional = blOpcional;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.contratacaoveiculos.model.AtributoMeioTransporte getAtributoMeioTransporte() {
        return this.atributoMeioTransporte;
    }

	public void setAtributoMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.AtributoMeioTransporte atributoMeioTransporte) {
        this.atributoMeioTransporte = atributoMeioTransporte;
    }

    public com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte getTipoMeioTransporte() {
        return this.tipoMeioTransporte;
    }

	public void setTipoMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte tipoMeioTransporte) {
        this.tipoMeioTransporte = tipoMeioTransporte;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.MeioTranspConteudoAtrib.class)     
    public List getMeioTranspConteudoAtribs() {
        return this.meioTranspConteudoAtribs;
    }

    public void setMeioTranspConteudoAtribs(List meioTranspConteudoAtribs) {
        this.meioTranspConteudoAtribs = meioTranspConteudoAtribs;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.ConteudoAtributoModelo.class)     
    public List getConteudoAtributoModelos() {
        return this.conteudoAtributoModelos;
    }

    public void setConteudoAtributoModelos(List conteudoAtributoModelos) {
        this.conteudoAtributoModelos = conteudoAtributoModelos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idModeloMeioTranspAtributo",
				getIdModeloMeioTranspAtributo()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ModeloMeioTranspAtributo))
			return false;
        ModeloMeioTranspAtributo castOther = (ModeloMeioTranspAtributo) other;
		return new EqualsBuilder().append(this.getIdModeloMeioTranspAtributo(),
				castOther.getIdModeloMeioTranspAtributo()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdModeloMeioTranspAtributo())
            .toHashCode();
    }
}
