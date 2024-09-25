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
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;

@Entity
@Table(name = "PROPOSTA_FOB")
@SequenceGenerator(name = "PROPOSTA_FOB_SEQ", sequenceName = "PROPOSTA_FOB_SQ")
public class PropostaFOB implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROPOSTA_FOB_SEQ")
	@Column(name = "ID_PROPOSTA_FOB", nullable = false)
	private Long idPropostaFOB;
	
	@Column(name = "BL_EFETIVADA", length = 1,nullable=false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")	
	private Boolean blEfetivada;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "TBPR_ID_TABELA_PRECO",nullable=false)
	private TabelaPreco tabelaPreco;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "CLIE_ID_CLIENTE",nullable=false)
	private Cliente cliente;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "MUNI_ID_MUNICIPIO",nullable=false)	
	private Municipio municipio;
	
	@Transient
	private DivisaoCliente divisaoCliente;

	public Long getIdPropostaFOB() {
		return idPropostaFOB;
	}

	public void setIdPropostaFOB(Long idPropostaFOB) {
		this.idPropostaFOB = idPropostaFOB;
	}

	public Boolean getBlEfetivada() {
		return blEfetivada;
	}

	public void setBlEfetivada(Boolean blEfetivada) {
		this.blEfetivada = blEfetivada;
	}

	public TabelaPreco getTabelaPreco() {
		return tabelaPreco;
	}

	public void setTabelaPreco(TabelaPreco tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public DivisaoCliente getDivisaoCliente() {
		return divisaoCliente;
	}

	public void setDivisaoCliente(DivisaoCliente divisaoCliente) {
		this.divisaoCliente = divisaoCliente;
	}

}
