package com.mercurio.lms.rest.tabeladeprecos;

import java.io.Serializable;
import java.math.BigDecimal;

public class ParcelaPrecoFreteDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long idParcela;
	private String nomeParcela;
	private BigDecimal valor;
	private Long idValorMarkupPrecoFrete;
	private Long idPrecoFrete;
	
	public ParcelaPrecoFreteDTO(){
	}
	
	public ParcelaPrecoFreteDTO(Long idValorMarkupPrecoFrete, Long idPrecoFrete, 
			Long idParcelaPreco, String nomeParcela, BigDecimal valorMarkup) {
		this.idValorMarkupPrecoFrete = idValorMarkupPrecoFrete;
		this.idPrecoFrete = idPrecoFrete;
		this.idParcela = idParcelaPreco;
		this.nomeParcela = nomeParcela;
		this.valor = valorMarkup;
	}

	public Long getIdValorMarkupPrecoFrete() {
		return idValorMarkupPrecoFrete;
	}
	public void setIdValorMarkupPrecoFrete(Long idValorMarkupPrecoFrete) {
		this.idValorMarkupPrecoFrete = idValorMarkupPrecoFrete;
	}
	public Long getIdPrecoFrete() {
		return idPrecoFrete;
	}
	public void setIdPrecoFrete(Long idPrecoFrete) {
		this.idPrecoFrete = idPrecoFrete;
	}
	public Long getIdParcela() {
		return idParcela;
	}

	public void setIdParcela(Long idParcela) {
		this.idParcela = idParcela;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getNomeParcela() {
		return nomeParcela;
	}

	public void setNomeParcela(String nomeParcela) {
		this.nomeParcela = nomeParcela;
	}
	
}
