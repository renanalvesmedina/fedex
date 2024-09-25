package com.mercurio.lms.expedicao.model;

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

import com.mercurio.lms.vendas.model.Cliente;

@Entity
@Table(name = "CLIENTE_PROCESSAR")
@SequenceGenerator(name = "CLIENTE_PROCESSAR_SEQ", sequenceName = "CLIENTE_PROCESSAR_SQ")
public class ClienteProcessar implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLIENTE_PROCESSAR_SEQ")	
	@Column(name = "ID_CLIENTE_PROCESSAR", nullable = false)
	private Long idClienteProcessar;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_RECALCULO_FRETE")	
	private RecalculoFrete recalculoFrete;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_CLIENTE")	
	private Cliente cliente;

	public Long getIdClienteProcessar() {
		return idClienteProcessar;
	}

	public void setIdClienteProcessar(Long idClienteProcessar) {
		this.idClienteProcessar = idClienteProcessar;
	}

	public RecalculoFrete getRecalculoFrete() {
		return recalculoFrete;
	}

	public void setRecalculoFrete(RecalculoFrete recalculoFrete) {
		this.recalculoFrete = recalculoFrete;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
}
