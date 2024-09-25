package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.municipios.model.Filial;

public class EmpresaUsuario implements Serializable {
		
	private static final long serialVersionUID = 1L;

	    /** identifier field */
	    private Long idEmpresaUsuario;

	    /** persistent field */
	    private Usuario usuario;

	    /** persistent field */
	    private com.mercurio.lms.municipios.model.Empresa empresa;
	    
	    private Filial filialPadrao;
	    
	    private Boolean blIrrestritoFilial;

		private List filiaisUsuario;

		private List regionalUsuario;

	    public Long getIdEmpresaUsuario() {
	        return this.idEmpresaUsuario;
	    }

	    public void setIdEmpresaUsuario(Long idEmpresaUsuario) {
	        this.idEmpresaUsuario = idEmpresaUsuario;
	    }

	    public Usuario getUsuario() {
	        return this.usuario;
	    }

	    public void setUsuario(Usuario usuario) {
	        this.usuario = usuario;
	    }

	    public com.mercurio.lms.municipios.model.Empresa getEmpresa() {
	        return this.empresa;
	    }

	    public void setEmpresa(com.mercurio.lms.municipios.model.Empresa empresa) {
	        this.empresa = empresa;
	    }

	    public String toString() {
		return new ToStringBuilder(this).append("idEmpresaUsuario",
				getIdEmpresaUsuario()).toString();
	    }

	    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EmpresaUsuario))
			return false;
	        EmpresaUsuario castOther = (EmpresaUsuario) other;
		return new EqualsBuilder().append(this.getIdEmpresaUsuario(),
				castOther.getIdEmpresaUsuario()).isEquals();
	    }

	    public int hashCode() {
		return new HashCodeBuilder().append(getIdEmpresaUsuario()).toHashCode();
	    }
	    
	    public Boolean getBlIrrestritoFilial() {
			return blIrrestritoFilial;
		}
	    
	    public void setBlIrrestritoFilial(Boolean blIrrestritoFilial) {
			this.blIrrestritoFilial = blIrrestritoFilial;
		}
	    
	    public Filial getFilialPadrao() {
			return filialPadrao;
		}
	    
	    public void setFilialPadrao(Filial filialPadrao) {
			this.filialPadrao = filialPadrao;
		}
	    
	    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.FilialUsuario.class)     
		public List getFiliaisUsuario() {
			return filiaisUsuario;
		}

		public void setFiliaisUsuario(List filiaisUsuario) {
			this.filiaisUsuario = filiaisUsuario;
		}

		@ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.RegionalUsuario.class)     
		public List getRegionalUsuario() {
			return regionalUsuario;
		}

		public void setRegionalUsuario(List regionalUsuario) {
			this.regionalUsuario = regionalUsuario;
		}
}