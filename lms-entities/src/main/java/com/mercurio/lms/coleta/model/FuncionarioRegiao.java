package com.mercurio.lms.coleta.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class FuncionarioRegiao implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idFuncionarioRegiao;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.municipios.model.RegiaoColetaEntregaFil regiaoColetaEntregaFil;

    public Long getIdFuncionarioRegiao() {
        return this.idFuncionarioRegiao;
    }

    public void setIdFuncionarioRegiao(Long idFuncionarioRegiao) {
        this.idFuncionarioRegiao = idFuncionarioRegiao;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.municipios.model.RegiaoColetaEntregaFil getRegiaoColetaEntregaFil() {
        return this.regiaoColetaEntregaFil;
    }

	public void setRegiaoColetaEntregaFil(
			com.mercurio.lms.municipios.model.RegiaoColetaEntregaFil regiaoColetaEntregaFil) {
        this.regiaoColetaEntregaFil = regiaoColetaEntregaFil;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idFuncionarioRegiao",
				getIdFuncionarioRegiao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FuncionarioRegiao))
			return false;
        FuncionarioRegiao castOther = (FuncionarioRegiao) other;
		return new EqualsBuilder().append(this.getIdFuncionarioRegiao(),
				castOther.getIdFuncionarioRegiao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFuncionarioRegiao())
            .toHashCode();
    }

}
