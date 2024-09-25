package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class FilialUsuario implements Serializable {

	private static final long serialVersionUID = 1L;
	
    /** identifier field */
    private Long idFilialUsuario;

    /** persistent field */    
    private Boolean blAprovaWorkflow;    

    /** persistent field */
    private EmpresaUsuario empresaUsuario;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    public Long getIdFilialUsuario() {
        return this.idFilialUsuario;
    }

    public void setIdFilialUsuario(Long idFilialUsuario) {
        this.idFilialUsuario = idFilialUsuario;
    }

    public EmpresaUsuario getEmpresaUsuario() {
		return empresaUsuario;
	}
    
    public void setEmpresaUsuario(EmpresaUsuario empresaUsuario) {
		this.empresaUsuario = empresaUsuario;
	}
    
    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idFilialUsuario",
				getIdFilialUsuario()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FilialUsuario))
			return false;
        FilialUsuario castOther = (FilialUsuario) other;
		return new EqualsBuilder().append(this.getIdFilialUsuario(),
				castOther.getIdFilialUsuario()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFilialUsuario()).toHashCode();
    }

	public Boolean getBlAprovaWorkflow() {
		return blAprovaWorkflow;
	}

	public void setBlAprovaWorkflow(Boolean blAprovaWorkflow) {
		this.blAprovaWorkflow = blAprovaWorkflow;
	}

}
