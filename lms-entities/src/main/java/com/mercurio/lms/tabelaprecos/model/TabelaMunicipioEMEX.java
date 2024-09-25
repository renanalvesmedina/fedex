package com.mercurio.lms.tabelaprecos.model;

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

import org.apache.commons.lang.builder.EqualsBuilder;

import com.mercurio.lms.municipios.model.Municipio;


@Entity
@Table(name = "TABELA_MUNICIPIO_EMEX")
@SequenceGenerator(name = "TABELA_MUNICIPIO_EMEX_SEQ", sequenceName = "TABELA_MUNICIPIO_EMEX_SQ")
public class TabelaMunicipioEMEX implements Serializable{

	@Id	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TABELA_MUNICIPIO_EMEX_SEQ")
	@Column(name = "ID_TABELA_MUNICIPIO_EMEX", nullable = false)
	private Long idTabelaMunicipioEMEX;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_TABELA_PRECO", nullable = false)
	private TabelaPreco tabelaPreco;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_MUNICIPIO", nullable = false)
	private Municipio municipio;
	
	public TabelaMunicipioEMEX() {

	}
	
	

	public TabelaMunicipioEMEX(TabelaPreco tabelaPreco, Municipio municipio) {
		super();
		this.tabelaPreco = tabelaPreco;
		this.municipio = municipio;
	}


	public Long getIdTabelaMunicipioEMEX() {
		return idTabelaMunicipioEMEX;
	}

	public void setIdTabelaMunicipioEMEX(Long idTabelaMunicipioEMEX) {
		this.idTabelaMunicipioEMEX = idTabelaMunicipioEMEX;
	}

	public TabelaPreco getTabelaPreco() {
		return tabelaPreco;
	}

	public void setTabelaPreco(TabelaPreco tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}
	
}
