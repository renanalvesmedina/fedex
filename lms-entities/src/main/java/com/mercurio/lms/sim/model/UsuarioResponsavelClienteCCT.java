package com.mercurio.lms.sim.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.vendas.model.Cliente;

@Entity
@Table(name="USUARIO_RESP_CLIENTE_CCT")
@SequenceGenerator(name="SQ_USUARIO_RESP_CLIENTE_CCT", sequenceName="USUARIO_RESP_CLIENTE_CCT_SQ", allocationSize=1)
public class UsuarioResponsavelClienteCCT implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SQ_USUARIO_RESP_CLIENTE_CCT")
	@Column(name="ID_USUARIO_RESP_CLI_CCT", nullable=false)
	private Long idUsuarioResponsavelClienteCCT;
	
	@OneToOne
	@JoinColumn(name="ID_USUARIO", nullable=false)
	private UsuarioLMS usuario;
	
	@OneToOne
	@JoinColumn(name="ID_CLIENTE", nullable=false)
	private Cliente cliente;
	
	public Long getIdUsuarioResponsavelClienteCCT() {
		return idUsuarioResponsavelClienteCCT;
	}
	public void setIdUsuarioResponsavelClienteCCT(
			Long idUsuarioResponsavelClienteCCT) {
		this.idUsuarioResponsavelClienteCCT = idUsuarioResponsavelClienteCCT;
	}
	public UsuarioLMS getUsuario() {
		return usuario;
	}
	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
}
