package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.municipios.model.Regional;

public class RegionalUsuario implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long idRegionalUsuario;
	private Regional regional;
	private EmpresaUsuario empresaUsuario;
    private Boolean blAprovaWorkflow;
	
    public Long getIdRegionalUsuario() {
		return idRegionalUsuario;
	}

	public void setIdRegionalUsuario(Long idRegionalUsuario) {
		this.idRegionalUsuario = idRegionalUsuario;
	}

	public Regional getRegional() {
		return regional;
	}

	public void setRegional(Regional regional) {
		this.regional = regional;
	}

	public EmpresaUsuario getEmpresaUsuario() {
		return empresaUsuario;
	}
	
	public void setEmpresaUsuario(EmpresaUsuario empresaUsuario) {
		this.empresaUsuario = empresaUsuario;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idRegionalUsuario",
				getIdRegionalUsuario()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RegionalUsuario))
			return false;
        RegionalUsuario castOther = (RegionalUsuario) other;
		return new EqualsBuilder().append(this.getIdRegionalUsuario(),
				castOther.getIdRegionalUsuario()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRegionalUsuario())
            .toHashCode();
    }

    public Boolean getBlAprovaWorkflow() {
		return blAprovaWorkflow;
	}
    
    public void setBlAprovaWorkflow(Boolean blAprovaWorkflow) {
		this.blAprovaWorkflow = blAprovaWorkflow;
	}
}