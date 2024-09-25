package com.mercurio.lms.layoutNfse.model.impressao;

public class Rps {
	
	private Long numero;
	private String serie;
	private Integer tipo;
	private String filaImpressao;
	
	public Long getNumero() {
		return numero;
	}
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public Integer getTipo() {
		return tipo;
	}
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}
	public String getFilaImpressao() {
		return filaImpressao;
	}
	public void setFilaImpressao(String filaImpressao) {
		this.filaImpressao = filaImpressao;
	}
}
