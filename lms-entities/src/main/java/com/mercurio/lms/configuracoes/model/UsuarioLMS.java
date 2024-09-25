package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.pojo.UsuarioADSM;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.vendas.model.Cliente;

public class UsuarioLMS implements Serializable {

	private static final long serialVersionUID = 1L;
	private UsuarioADSM usuarioADSM;
	private Long idUsuario;
	private Boolean blIrrestritoCliente;
	private Boolean blAdminCliente;
	private Boolean blAdminFilial;
	private Boolean blTermoComp;
	private Empresa empresaPadrao;
	private Filial filial;
	private Cliente cliente;
	private List<EmpresaUsuario> empresasUsuario;

	public UsuarioADSM getUsuarioADSM() {
		return usuarioADSM;
	}

	public void setUsuarioADSM(UsuarioADSM usuarioADSM) {
		this.usuarioADSM = usuarioADSM;
	}

	public Boolean getBlIrrestritoCliente() {
		return blIrrestritoCliente;
	}

	public void setBlIrrestritoCliente(Boolean blIrrestritoCliente) {
		this.blIrrestritoCliente = blIrrestritoCliente;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
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

	public Boolean getBlAdminCliente() {
		return blAdminCliente;
	}

	public void setBlAdminCliente(Boolean blAdminCliente) {
		this.blAdminCliente = blAdminCliente;
	}

	public Boolean getBlAdminFilial() {
		return blAdminFilial;
	}

	public void setBlAdminFilial(Boolean blAdminFilial) {
		this.blAdminFilial = blAdminFilial;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@ParametrizedAttribute(type = EmpresaUsuario.class) 
	public List<EmpresaUsuario> getEmpresasUsuario() {
		return empresasUsuario;
	}

	public void setEmpresasUsuario(List<EmpresaUsuario> empresasUsuario) {
		this.empresasUsuario = empresasUsuario;
	}

}