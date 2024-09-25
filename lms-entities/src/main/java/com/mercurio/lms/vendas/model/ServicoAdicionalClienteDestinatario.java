package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "DESTINARIO_PAGA_SERVICO")
@SequenceGenerator(name = "DESTINARIO_PAGA_SERVICO_SEQ", sequenceName = "DESTINARIO_PAGA_SERVICO_SQ", allocationSize=1)
public class ServicoAdicionalClienteDestinatario implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DESTINARIO_PAGA_SERVICO_SEQ")
	@Column(name = "ID_DESTINATARIO_PAGA_SERVICO", nullable = false)
	private Long idServicoAdicionalClienteDestinatario;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_CLIENTE_DESTINATARIO", nullable = false)
	private Cliente clienteDestinatario;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_SERVICO_ADICIONAL_CLIENTE", nullable = false)
	private ServicoAdicionalCliente servicoAdicionalCliente;
	
	public Long getIdServicoAdicionalClienteDestinatario() {
		return idServicoAdicionalClienteDestinatario;
	}
	public void setIdServicoAdicionalClienteDestinatario(
			Long idServicoAdicionalClienteDestinatario) {
		this.idServicoAdicionalClienteDestinatario = idServicoAdicionalClienteDestinatario;
	}
	
	public Cliente getClienteDestinatario() {
		return clienteDestinatario;
	}
	public void setClienteDestinatario(Cliente clienteDestinatario) {
		this.clienteDestinatario = clienteDestinatario;
	}
	
	public ServicoAdicionalCliente getServicoAdicionalCliente() {
		return servicoAdicionalCliente;
	}
	public void setServicoAdicionalCliente(
			ServicoAdicionalCliente servicoAdicionalCliente) {
		this.servicoAdicionalCliente = servicoAdicionalCliente;
	}	
}
