package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class FuncionarioVisita implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idFuncionarioVisita;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Visita visita;

    public Long getIdFuncionarioVisita() {
        return this.idFuncionarioVisita;
    }

    public void setIdFuncionarioVisita(Long idFuncionarioVisita) {
        this.idFuncionarioVisita = idFuncionarioVisita;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.vendas.model.Visita getVisita() {
        return this.visita;
    }

    public void setVisita(com.mercurio.lms.vendas.model.Visita visita) {
        this.visita = visita;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idFuncionarioVisita",
				getIdFuncionarioVisita()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FuncionarioVisita))
			return false;
        FuncionarioVisita castOther = (FuncionarioVisita) other;
		return new EqualsBuilder().append(this.getIdFuncionarioVisita(),
				castOther.getIdFuncionarioVisita()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFuncionarioVisita())
            .toHashCode();
    }

}
