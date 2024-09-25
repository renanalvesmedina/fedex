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

/**
 * @author vagnerh
 *
 */
@Entity
@Table(name = "CLIENTE_OPERADOR_LOGISTICO")
@SequenceGenerator(name = "CLIENTE_OPERADOR_LOGISTICO_SEQ", sequenceName = "CLIENTE_OPERADOR_LOGISTICO_SQ")
public class ClienteOperadorLogistico implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long idClienteOperadorLogistico;
	private Cliente clienteOperador;
	private Cliente clienteOperado;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLIENTE_OPERADOR_LOGISTICO_SEQ")
	@Column(name = "ID_CLIENTE_OPERADOR_LOGISTICO", nullable = false)
	public Long getIdClienteOperadorLogistico() {
    	return idClienteOperadorLogistico;
    }
	public void setIdClienteOperadorLogistico(Long idClienteOperadorLogistico) {
    	this.idClienteOperadorLogistico = idClienteOperadorLogistico;
    }
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_CLIENTE_OPERADOR", nullable = false)
	public Cliente getClienteOperador() {
    	return clienteOperador;
    }
	public void setClienteOperador(Cliente clienteOperador) {
    	this.clienteOperador = clienteOperador;
    }
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_CLIENTE_OPERADO", nullable = false)
	public Cliente getClienteOperado() {
    	return clienteOperado;
    }
	public void setClienteOperado(Cliente clienteOperado) {
    	this.clienteOperado = clienteOperado;
    }
}
