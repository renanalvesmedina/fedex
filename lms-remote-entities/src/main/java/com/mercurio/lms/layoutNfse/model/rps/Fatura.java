package com.mercurio.lms.layoutNfse.model.rps;

public class Fatura {
	
	private Long NumeroFatura;
	private String Vencimento;
	private String Valor;
	
	public Long getNumeroFatura() {
		return NumeroFatura;
	}
	
	public void setNumeroFatura(Long numeroFatura) {
		NumeroFatura = numeroFatura;
	}
	
	public String getVencimento() {
		return Vencimento;
	}
	
	public void setVencimento(String vencimento) {
		Vencimento = vencimento;
	}
	
	public String getValor() {
		return Valor;
	}
	
	public void setValor(String valor) {
		Valor = valor;
	}

}
