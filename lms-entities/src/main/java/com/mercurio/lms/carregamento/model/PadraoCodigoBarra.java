package com.mercurio.lms.carregamento.model;

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
@Table(name="PADRAO_CODIGO_BARRA")
@SequenceGenerator(name="PADRAO_CODIGO_BARRA_SEQ", sequenceName="PADRAO_CODIGO_BARRA_SQ", allocationSize=1)
public class PadraoCodigoBarra implements Serializable {
	private static final long serialVersionUID = 1L; 
	
	private Long idPadraoCodigoBarra;
	private String nmPadrao;
	private String dsConteudo;
	private Integer nrCaracter;
	private Integer nrInicio;
	private Long nrTamanho;
	private Cliente cliente;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PADRAO_CODIGO_BARRA_SEQ")
	@Column(name = "ID_PADRAO_CODIGO_BARRA", nullable = false)
	public Long getIdPadraoCodigoBarra() {
		return idPadraoCodigoBarra;
	}

	public void setIdPadraoCodigoBarra(Long idPadraoCodigoBarra) {
		this.idPadraoCodigoBarra = idPadraoCodigoBarra;
	}

	@Column(name = "NM_PADRAO", length=15)
	public String getNmPadrao() {
		return nmPadrao;
	}

	public void setNmPadrao(String nmPadrao) {
		this.nmPadrao = nmPadrao;
	}

	@Column(name = "DS_CONTEUDO", nullable=true, length=100)
	public String getDsConteudo() {
		return dsConteudo;
	}

	public void setDsConteudo(String dsConteudo) {
		this.dsConteudo = dsConteudo;
	}

	@Column(name="NR_CARACTER", length=5)
	public Integer getNrCaracter() {
		return nrCaracter;
	}

	public void setNrCaracter(Integer nrCaracter) {
		this.nrCaracter = nrCaracter;
	}

	@Column(name="NR_INICIO", length=5)
	public Integer getNrInicio() {
		return nrInicio;
	}

	public void setNrInicio(Integer nrInicio) {
		this.nrInicio = nrInicio;
	}

	@Column(name="NR_TAMANHO", length=10)
	public Long getNrTamanho() {
		return nrTamanho;
	}

	public void setNrTamanho(Long nrTamanho) {
		this.nrTamanho = nrTamanho;
	}

	@OneToOne
	@JoinColumn(name = "ID_CLIENTE", nullable = false)
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
