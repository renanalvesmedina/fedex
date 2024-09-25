package com.mercurio.lms.workflow.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Bruno Zaccolo
 * 
 */

public class FilialEmailEventoUsuario implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idFilialEmailEventoUsuario;

	private com.mercurio.lms.municipios.model.Filial filial;

	private com.mercurio.lms.workflow.model.EmailEventoUsuario emailEventoUsuario;

	private Long idFilial;

	public com.mercurio.lms.workflow.model.EmailEventoUsuario getEmailEventoUsuario() {
		return emailEventoUsuario;
	}

	public void setEmailEventoUsuario(
			com.mercurio.lms.workflow.model.EmailEventoUsuario emailEventoUsuario) {
		this.emailEventoUsuario = emailEventoUsuario;
	}

	public com.mercurio.lms.municipios.model.Filial getFilial() {
		return filial;
	}

	public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
		this.filial = filial;
	}

	public Long getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(Long idFilial) {
		this.idFilial = idFilial;
	}

	public Long getIdFilialEmailEventoUsuario() {
		return idFilialEmailEventoUsuario;
	}

	public void setIdFilialEmailEventoUsuario(Long idFilialEmailEventoUsuario) {
		this.idFilialEmailEventoUsuario = idFilialEmailEventoUsuario;
	}
	
    public String toString() {
		return new ToStringBuilder(this).append("idFilialEmailEventoUsuario",
				getIdFilialEmailEventoUsuario()).toString();
    }
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Integrante))
			return false;
		Integrante castOther = (Integrante) other;
		return new EqualsBuilder().append(this.getIdFilialEmailEventoUsuario(),
				castOther.getIdIntegrante()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdFilialEmailEventoUsuario())
				.toHashCode();
	}
}
