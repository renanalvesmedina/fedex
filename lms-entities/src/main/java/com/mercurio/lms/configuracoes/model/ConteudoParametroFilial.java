package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ConteudoParametroFilial implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idConteudoParametroFilial;

    /** persistent field */
    private String vlConteudoParametroFilial;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.ParametroFilial parametroFilial;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    public Long getIdConteudoParametroFilial() {
        return this.idConteudoParametroFilial;
    }

    public void setIdConteudoParametroFilial(Long idConteudoParametroFilial) {
        this.idConteudoParametroFilial = idConteudoParametroFilial;
    }    

    public String getVlConteudoParametroFilial() {
        return this.vlConteudoParametroFilial;
    }

    public void setVlConteudoParametroFilial(String vlConteudoParametroFilial) {
        this.vlConteudoParametroFilial = vlConteudoParametroFilial;
    }

    public com.mercurio.lms.configuracoes.model.ParametroFilial getParametroFilial() {
        return this.parametroFilial;
    }

	public void setParametroFilial(
			com.mercurio.lms.configuracoes.model.ParametroFilial parametroFilial) {
        this.parametroFilial = parametroFilial;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idConteudoParametroFilial",
				getIdConteudoParametroFilial()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ConteudoParametroFilial))
			return false;
        ConteudoParametroFilial castOther = (ConteudoParametroFilial) other;
		return new EqualsBuilder().append(this.getIdConteudoParametroFilial(),
				castOther.getIdConteudoParametroFilial()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdConteudoParametroFilial())
            .toHashCode();
    }

}
