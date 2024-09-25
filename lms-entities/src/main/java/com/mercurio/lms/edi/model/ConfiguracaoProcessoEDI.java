package com.mercurio.lms.edi.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="CONFIGURACAO_PROCESSO")
@SequenceGenerator(name="SEQUENCE",sequenceName="CONFIGURACAO_PROCESSO_SQ")
public class ConfiguracaoProcessoEDI implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long idConfiguracaoProcesso;
	private String chave;
	private String valor;
	private String descricao;
	
	@Id 
    @Column(name="ID_CONFIGURACAO_PROCESSO")
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQUENCE")  
	public Long getIdConfiguracaoProcesso() {
		return idConfiguracaoProcesso;
	}

	public void setIdConfiguracaoProcesso(Long idConfiguracaoProcesso) {
		this.idConfiguracaoProcesso = idConfiguracaoProcesso;
	}
	
	@Column(name="CHAVE")
	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}
	
	@Column(name="VALOR")
	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
	
	@Column(name="DESCRICAO")
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}