package com.mercurio.lms.fretecarreteirocoletaentrega.model;

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

import com.mercurio.lms.vendas.model.Cliente;

@Entity
@Table(name = "FATOR_DIFICULDADE_COL_ENT")
@SequenceGenerator(name = "FATOR_DIFICULDADE_COL_ENT_SQ", sequenceName = "FATOR_DIFICULDADE_COL_ENT_SQ", allocationSize = 1)
public class DificuldadeColetaEntrega implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FATOR_DIFICULDADE_COL_ENT_SQ")
	@Column(name = "ID_FATOR_DIFICULDADE_COL_ENT", nullable = false)
	private Long idDificuldadeColetaEntrega;

	@OneToOne
	@JoinColumn(name = "ID_CLIENTE")
	private Cliente cliente;

	@Column(name = "NR_FATOR_DIFICULDADE_COL")
	private Integer nrFatorColeta;
	
	@Column(name = "NR_FATOR_DIFICULDADE_ENT")
	private Integer nrFatorEntrega;


	public Long getIdDificuldadeColetaEntrega() {
		return idDificuldadeColetaEntrega;
	}


	public void setIdDificuldadeColetaEntrega(Long idDificuldadeColetaEntrega) {
		this.idDificuldadeColetaEntrega = idDificuldadeColetaEntrega;
	}


	public Cliente getCliente() {
		return cliente;
	}


	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}


	public Integer getNrFatorColeta() {
		return nrFatorColeta;
	}


	public void setNrFatorColeta(Integer nrFatorColeta) {
		this.nrFatorColeta = nrFatorColeta;
	}


	public Integer getNrFatorEntrega() {
		return nrFatorEntrega;
	}


	public void setNrFatorEntrega(Integer nrFatorEntrega) {
		this.nrFatorEntrega = nrFatorEntrega;
	}
	
	
}