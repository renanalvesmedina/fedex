package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.pojo.Autoridade;
import com.mercurio.adsm.framework.model.pojo.UsuarioSupport;
import com.mercurio.lms.contasreceber.model.BloqueioFaturamento;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.UsuarioClienteResponsavel;

/** @author LMS Custom Hibernate CodeGenerator */
public class Usuario extends UsuarioSupport implements Serializable {
    
	private static final long serialVersionUID = 1L;
	private Boolean blAdminFilial;
	private Boolean blIrrestritoCliente;
	
	private Filial filial;
    
    /** persistent field */
    private Boolean blTermoComp;

    /** persistent field */
    private List clienteUsuario;

    /** persistent field */
    private List empresaUsuario;

    /** persistent field */
    private Funcionario vfuncionario;

    /** persistent field */
    private Autoridade autoridade;

    private Empresa empresaPadrao;

	private Boolean blAdministrador;
	
	private boolean blAdminCliente;
	
	private Cliente cliente;
	
	private List<BloqueioFaturamento> bloqueioFaturamentos;

	private List<UsuarioClienteResponsavel> usuarioClienteResponsaveis;

	private String loginIdFedex;

	public Usuario() {
	}

	public Usuario(Boolean blAdminFilial, Boolean blIrrestritoCliente, Filial filial, Boolean blTermoComp, List clienteUsuario, List empresaUsuario, Funcionario vfuncionario, Autoridade autoridade, Empresa empresaPadrao, Boolean blAdministrador, boolean blAdminCliente, Cliente cliente, List<BloqueioFaturamento> bloqueioFaturamentos, List<UsuarioClienteResponsavel> usuarioClienteResponsaveis) {
		this.blAdminFilial = blAdminFilial;
		this.blIrrestritoCliente = blIrrestritoCliente;
		this.filial = filial;
		this.blTermoComp = blTermoComp;
		this.clienteUsuario = clienteUsuario;
		this.empresaUsuario = empresaUsuario;
		this.vfuncionario = vfuncionario;
		this.autoridade = autoridade;
		this.empresaPadrao = empresaPadrao;
		this.blAdministrador = blAdministrador;
		this.blAdminCliente = blAdminCliente;
		this.cliente = cliente;
		this.bloqueioFaturamentos = bloqueioFaturamentos;
		this.usuarioClienteResponsaveis = usuarioClienteResponsaveis;
	}

	/**
	 * @return the usuarioClienteResponsaveis
	 */
	public List<UsuarioClienteResponsavel> getUsuarioClienteResponsaveis() {
		return usuarioClienteResponsaveis;
	}

	/**
	 * @param usuarioClienteResponsaveis the usuarioClienteResponsaveis to set
	 */
	public void setUsuarioClienteResponsaveis(
			List<UsuarioClienteResponsavel> usuarioClienteResponsaveis) {
		this.usuarioClienteResponsaveis = usuarioClienteResponsaveis;
	}

	public List<BloqueioFaturamento> getBloqueioFaturamentos() {
		return bloqueioFaturamentos;
	}

	public void setBloqueioFaturamentos(
			List<BloqueioFaturamento> bloqueioFaturamentos) {
		this.bloqueioFaturamentos = bloqueioFaturamentos;
	}

	public Boolean getBlAdminFilial() {
		return blAdminFilial;
	}

	public Boolean getBlIrrestritoCliente() {
		return blIrrestritoCliente;
	}

	public void setBlAdminFilial(Boolean blAdminFilial) {
		this.blAdminFilial = blAdminFilial;
	}

	public void setBlIrrestritoCliente(Boolean blIrrestritoCliente) {
		this.blIrrestritoCliente = blIrrestritoCliente;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idUsuario", getIdUsuario())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Usuario))
			return false;
        Usuario castOther = (Usuario) other;
		return new EqualsBuilder().append(this.getIdUsuario(),
				castOther.getIdUsuario()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdUsuario()).toHashCode();
    }

	public Funcionario getVfuncionario() {
		return vfuncionario;
	}

	public void setVfuncionario(Funcionario funcionario) {
		vfuncionario = funcionario;
	}

	public Autoridade getAutoridade() {
		return autoridade;
	}

	public void setAutoridade(Autoridade autoridade) {
		this.autoridade = autoridade;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.ClienteUsuario.class)     
	public List getClienteUsuario() {
		return clienteUsuario;
	}

	public void setClienteUsuario(List clienteUsuario) {
		this.clienteUsuario = clienteUsuario;
	}
	
	public List getEmpresaUsuario() {
		return empresaUsuario;
	}

	public void setEmpresaUsuario(List empresaUsuario) {
		this.empresaUsuario = empresaUsuario;
	}

	public Boolean getBlAdministrador() {
		return blAdministrador;
	}

	public void setBlAdministrador(Boolean blAdministrador) {
		this.blAdministrador = blAdministrador;
	}

	public Empresa getEmpresaPadrao() {
		return empresaPadrao;
	}

	public void setEmpresaPadrao(Empresa empresaPadrao) {
		this.empresaPadrao = empresaPadrao;
	}
	
	public Boolean getBlTermoComp() {
		return blTermoComp;
	}

	public void setBlTermoComp(Boolean blTermoComp) {
		this.blTermoComp = blTermoComp;
	}

	/**
	 * @return Returns the filial.
	 */
	public Filial getFilial() {
		return filial;
	}

	/**
	 * @param filial
	 *            The filial to set.
	 */
	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public Cliente getCliente() {
		
		return cliente;
	}
	
	public void setCliente(Cliente cliente) {
		
		this.cliente = cliente;
	}
	
	public boolean isBlAdminCliente() {
		
		return blAdminCliente;
	}
	
	public void setBlAdminCliente(boolean blAdminCliente) {
		
		this.blAdminCliente = blAdminCliente;
	}

	public String getLoginIdFedex() {
		return loginIdFedex;
	}

	public void setLoginIdFedex(String loginIdFedex) {
		this.loginIdFedex = loginIdFedex;
	}
}