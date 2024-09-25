package com.mercurio.lms.rest.tabeladeprecos;


public class ParcelaDTO {

	private Long idParcelaPreco;
	private String nmParcelaPreco;
	private char tpPrecificacao;
	
	public ParcelaDTO() {
		
	}
	
	public ParcelaDTO(Long idParcelaPreco, String nmParcelaPreco, char tpPrecificacao) {
		this.idParcelaPreco = idParcelaPreco;
		this.nmParcelaPreco = nmParcelaPreco;
		this.tpPrecificacao = tpPrecificacao;
	}

	public Long getIdParcelaPreco() {
		return idParcelaPreco;
	}

	public void setIdParcelaPreco(Long idParcelaPreco) {
		this.idParcelaPreco = idParcelaPreco;
	}

	public String getNmParcelaPreco() {
		return nmParcelaPreco;
	}

	public void setNmParcelaPreco(String nmParcelaPreco) {
		this.nmParcelaPreco = nmParcelaPreco;
	}

	public char getTpPrecificacao() {
		return tpPrecificacao;
	}

	public void setTpPrecificacao(char tpPrecificacao) {
		this.tpPrecificacao = tpPrecificacao;
	}

}
