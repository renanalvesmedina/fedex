package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="AGRUPADOR_CLIENTE")
@SequenceGenerator(name="SQ_AGRUPADOR_CLIENTE", sequenceName="AGRUPADOR_CLIENTE_SQ", allocationSize=1)
public class AgrupadorCliente  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SQ_AGRUPADOR_CLIENTE")
	@Column(name="ID_AGRUPADOR_CLIENTE", nullable=false)
	private Long idAgrupadorCliente;
	
	@ManyToOne
	@JoinColumn(name="ID_ENVIO_CTE_CLIENTE", nullable=false)
	private EnvioCteCliente envioCteCliente;
	
	@ManyToOne
	@JoinColumn(name="ID_CLIENTE", nullable=false)
	private Cliente cliente;

	public Long getIdAgrupadorCliente() {
		return idAgrupadorCliente;
	}

	public void setIdAgrupadorCliente(Long idAgrupadorCliente) {
		this.idAgrupadorCliente = idAgrupadorCliente;
	}

	public EnvioCteCliente getEnvioCteCliente() {
		return envioCteCliente;
	}

	public void setEnvioCteCliente(EnvioCteCliente envioCteCliente) {
		this.envioCteCliente = envioCteCliente;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
