package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class ConteudoAtributoModelo implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idConteudoAtributoModelo;

    /** persistent field */
    private VarcharI18n dsConteudoAtributoModelo;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.ModeloMeioTranspAtributo modeloMeioTranspAtributo;

    /** persistent field */
    private List meioTranspConteudoAtribs;

    public Long getIdConteudoAtributoModelo() {
        return this.idConteudoAtributoModelo;
    }

    public void setIdConteudoAtributoModelo(Long idConteudoAtributoModelo) {
        this.idConteudoAtributoModelo = idConteudoAtributoModelo;
    }

    public VarcharI18n getDsConteudoAtributoModelo() {
		return dsConteudoAtributoModelo;
    }

	public void setDsConteudoAtributoModelo(VarcharI18n dsConteudoAtributoModelo) {
        this.dsConteudoAtributoModelo = dsConteudoAtributoModelo;
    }

    public com.mercurio.lms.contratacaoveiculos.model.ModeloMeioTranspAtributo getModeloMeioTranspAtributo() {
        return this.modeloMeioTranspAtributo;
    }

	public void setModeloMeioTranspAtributo(
			com.mercurio.lms.contratacaoveiculos.model.ModeloMeioTranspAtributo modeloMeioTranspAtributo) {
        this.modeloMeioTranspAtributo = modeloMeioTranspAtributo;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.MeioTranspConteudoAtrib.class)     
    public List getMeioTranspConteudoAtribs() {
        return this.meioTranspConteudoAtribs;
    }

    public void setMeioTranspConteudoAtribs(List meioTranspConteudoAtribs) {
        this.meioTranspConteudoAtribs = meioTranspConteudoAtribs;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idConteudoAtributoModelo",
				getIdConteudoAtributoModelo()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ConteudoAtributoModelo))
			return false;
        ConteudoAtributoModelo castOther = (ConteudoAtributoModelo) other;
		return new EqualsBuilder().append(this.getIdConteudoAtributoModelo(),
				castOther.getIdConteudoAtributoModelo()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdConteudoAtributoModelo())
            .toHashCode();
    }

}
