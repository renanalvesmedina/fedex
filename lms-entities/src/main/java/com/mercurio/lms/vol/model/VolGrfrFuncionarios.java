package com.mercurio.lms.vol.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.configuracoes.model.Usuario;

/** @author LMS Custom Hibernate CodeGenerator */
public class VolGrfrFuncionarios implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idGruFunc;

    /** persistent field */
    private com.mercurio.lms.vol.model.VolGruposFrotas volGruposFrota;

    /** persistent field */
    private Usuario usuario;

    public Long getIdGruFunc() {
        return this.idGruFunc;
    }

    public void setIdGruFunc(Long idGruFunc) {
        this.idGruFunc = idGruFunc;
    }

    public com.mercurio.lms.vol.model.VolGruposFrotas getVolGruposFrota() {
        return this.volGruposFrota;
    }

	public void setVolGruposFrota(
			com.mercurio.lms.vol.model.VolGruposFrotas volGruposFrota) {
        this.volGruposFrota = volGruposFrota;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idGruFunc", getIdGruFunc())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof VolGrfrFuncionarios))
			return false;
        VolGrfrFuncionarios castOther = (VolGrfrFuncionarios) other;
		return new EqualsBuilder().append(this.getIdGruFunc(),
				castOther.getIdGruFunc()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdGruFunc()).toHashCode();
    }

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
