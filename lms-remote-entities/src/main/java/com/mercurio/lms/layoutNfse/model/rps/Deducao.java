package com.mercurio.lms.layoutNfse.model.rps;

public class Deducao {
	
	private Integer deducaoPor;
	private Integer tipoDeducao;
	private String cpfCnpjReferencia;
	private String valorTotalReferencia;
	private String valorDeduzir;
	private String percentualDeduzir;
	private Integer numeroNfReferencia;
	
	
	public Integer getDeducaoPor() {
		return deducaoPor;
	}
	public void setDeducaoPor(Integer deducaoPor) {
		this.deducaoPor = deducaoPor;
	}
	public Integer getTipoDeducao() {
		return tipoDeducao;
	}
	public void setTipoDeducao(Integer tipoDeducao) {
		this.tipoDeducao = tipoDeducao;
	}
	public String getCpfCnpjReferencia() {
		return cpfCnpjReferencia;
	}
	public void setCpfCnpjReferencia(String cpfCnpjReferencia) {
		this.cpfCnpjReferencia = cpfCnpjReferencia;
	}
	public String getValorTotalReferencia() {
		return valorTotalReferencia;
	}
	public void setValorTotalReferencia(String valorTotalReferencia) {
		this.valorTotalReferencia = valorTotalReferencia;
	}
	public String getValorDeduzir() {
		return valorDeduzir;
	}
	public void setValorDeduzir(String valorDeduzir) {
		this.valorDeduzir = valorDeduzir;
	}
	public String getPercentualDeduzir() {
		return percentualDeduzir;
	}
	public void setPercentualDeduzir(String percentualDeduzir) {
		this.percentualDeduzir = percentualDeduzir;
	}
	public Integer getNumeroNfReferencia() {
		return numeroNfReferencia;
	}
	public void setNumeroNfReferencia(Integer numeroNfReferencia) {
		this.numeroNfReferencia = numeroNfReferencia;
	}

}
