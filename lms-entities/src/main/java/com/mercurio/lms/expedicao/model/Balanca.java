package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "BALANCA")
@SequenceGenerator(name = "BALANCA_SQ", sequenceName = "BALANCA_SQ")
public class Balanca implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BALANCA_SQ")
	@Column(name = "ID_BALANCA", nullable = false)
	private Long idBalanca;

	@Column(name = "DS_BALANCA", length = 30)
	private String dsBalanca;
	
	@Column(name = "DS_COMANDO_LEITURA", length = 10)
	private String dsComandoLeitura;

	@Column(name = "DS_VELOCIDADE", length = 10)
	private String dsVelocidade;

	@Column(name = "DS_BITDADOS", length = 10)
	private String dsBitdados;

	@Column(name = "DS_PARIDADE", length = 10)
	private String dsParidade;
	
	@Column(name = "DS_BITSTOP", length = 10)
	private String dsBitstop;
	
	@Column(name = "DS_CHAR_ESTABILIZADO ", length = 10)
	private String dsCharEstabilizado;
	
	@Column(name = "DS_EXPR_REGULAR ", length = 500)
	private String dsExpressaoRegular;

	public Long getIdBalanca() {
		return idBalanca;
	}

	public void setIdBalanca(Long idBalanca) {
		this.idBalanca = idBalanca;
	}

	public String getDsBalanca() {
		return dsBalanca;
	}

	public void setDsBalanca(String dsBalanca) {
		this.dsBalanca = dsBalanca;
	}

	public String getDsComandoLeitura() {
		return dsComandoLeitura;
	}

	public void setDsComandoLeitura(String dsComandoLeitura) {
		this.dsComandoLeitura = dsComandoLeitura;
	}

	public String getDsVelocidade() {
		return dsVelocidade;
	}

	public void setDsVelocidade(String dsVelocidade) {
		this.dsVelocidade = dsVelocidade;
	}

	public String getDsBitdados() {
		return dsBitdados;
	}

	public void setDsBitdados(String dsBitdados) {
		this.dsBitdados = dsBitdados;
	}

	public String getDsParidade() {
		return dsParidade;
	}

	public void setDsParidade(String dsParidade) {
		this.dsParidade = dsParidade;
	}

	public String getDsBitstop() {
		return dsBitstop;
	}

	public void setDsBitstop(String dsBitstop) {
		this.dsBitstop = dsBitstop;
	}

	public String getDsCharEstabilizado() {
		return dsCharEstabilizado;
	}

	public void setDsCharEstabilizado(String dsCharEstabilizado) {
		this.dsCharEstabilizado = dsCharEstabilizado;
	}
	public String getDsExpressaoRegular() {
		return dsExpressaoRegular;
	}

	public void setDsExpressaoRegular(String dsExpressaoRegular) {
		this.dsExpressaoRegular = dsExpressaoRegular;
	}
}