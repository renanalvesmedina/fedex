package com.mercurio.lms.vendas.model;

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

@Entity
@Table(name = "REMETENTE_OTD")
@SequenceGenerator(name = "REMETENTE_OTD_SEQ", sequenceName = "REMETENTE_OTD_SQ", allocationSize=1)
public class RemetenteOTD implements Serializable {

	private static final long serialVersionUID = -3140116295355962292L;
	
	@Id
	@Column(name = "ID_REMETENTE_OTD", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REMETENTE_OTD_SEQ")
	private Long idRemetenteOtd;

	@OneToOne
	@JoinColumn(name = "ID_CLIENTE")
	private Cliente cliente;
	
	@OneToOne
	@JoinColumn(name = "ID_REMETENTE")
	private Cliente remetente;

	public RemetenteOTD() {
	}

	public RemetenteOTD(Cliente cliente, Cliente remetente) {
		this.cliente = cliente;
		this.remetente = remetente;
	}

	public Long getIdRemetenteOtd() {
		return idRemetenteOtd;
	}

	public void setIdRemetenteOtd(Long idRemetenteOtd) {
		this.idRemetenteOtd = idRemetenteOtd;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Cliente getRemetente() {
		return remetente;
	}

	public void setRemetente(Cliente remetente) {
		this.remetente = remetente;
	}

}
