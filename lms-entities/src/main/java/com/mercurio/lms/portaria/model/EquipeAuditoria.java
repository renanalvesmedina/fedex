package com.mercurio.lms.portaria.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.configuracoes.model.Usuario;

/** @author LMS Custom Hibernate CodeGenerator */
public class EquipeAuditoria implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEquipeAuditoria;

    /** persistent field */
    private Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.portaria.model.RegistroAuditoria registroAuditoria;

    public Long getIdEquipeAuditoria() {
        return this.idEquipeAuditoria;
    }

    public void setIdEquipeAuditoria(Long idEquipeAuditoria) {
        this.idEquipeAuditoria = idEquipeAuditoria;
    }

    public com.mercurio.lms.portaria.model.RegistroAuditoria getRegistroAuditoria() {
        return this.registroAuditoria;
    }

	public void setRegistroAuditoria(
			com.mercurio.lms.portaria.model.RegistroAuditoria registroAuditoria) {
        this.registroAuditoria = registroAuditoria;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idEquipeAuditoria",
				getIdEquipeAuditoria()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EquipeAuditoria))
			return false;
        EquipeAuditoria castOther = (EquipeAuditoria) other;
		return new EqualsBuilder().append(this.getIdEquipeAuditoria(),
				castOther.getIdEquipeAuditoria()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEquipeAuditoria())
            .toHashCode();
    }

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
