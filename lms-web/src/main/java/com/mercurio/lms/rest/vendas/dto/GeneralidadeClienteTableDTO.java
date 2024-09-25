package com.mercurio.lms.rest.vendas.dto;

import java.math.BigDecimal;

import com.mercurio.adsm.rest.BaseDTO;

public class GeneralidadeClienteTableDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idGeneralidadeCliente;
	private String nmParcelaPreco;
	private String tpIndicador; 
	private String vlGeneralidade; //Valor em String por causa do retorno formatado de acordo com o indicador 
	private String tpIndicadorMinimo;
	private String vlMinimo;//Valor em String por causa do retorno formatado de acordo com o indicador
	
	public GeneralidadeClienteTableDTO(){
		
	}
	
	public GeneralidadeClienteTableDTO(Long idGeneralidadeCliente,
			String nmParcelaPreco, String tpIndicador,
			String vlGeneralidade, String tpIndicadorMinimo,
			String vlMinimo) {
		super();
		this.idGeneralidadeCliente = idGeneralidadeCliente;
		this.nmParcelaPreco = nmParcelaPreco;
		this.tpIndicador = tpIndicador;
		this.vlGeneralidade = vlGeneralidade;
		this.tpIndicadorMinimo = tpIndicadorMinimo;
		this.vlMinimo = vlMinimo;
	}
	public Long getIdGeneralidadeCliente() {
		return idGeneralidadeCliente;
	}
	public void setIdGeneralidadeCliente(Long idGeneralidadeCliente) {
		this.idGeneralidadeCliente = idGeneralidadeCliente;
	}
	public String getNmParcelaPreco() {
		return nmParcelaPreco;
		
	}
	public void setNmParcelaPreco(String nmParcelaPreco) {
		this.nmParcelaPreco = nmParcelaPreco;
	}
	public String getTpIndicador() {
		return tpIndicador;
	}
	public void setTpIndicador(String tpIndicador) {
		this.tpIndicador = tpIndicador;
	}
	public String getVlGeneralidade() {
		return vlGeneralidade;
	}
	public void setVlGeneralidade(String vlGeneralidade) {
		this.vlGeneralidade = vlGeneralidade;
	}
	public String getTpIndicadorMinimo() {
		return tpIndicadorMinimo;
	}
	public void setTpIndicadorMinimo(String tpIndicadorMinimo) {
		this.tpIndicadorMinimo = tpIndicadorMinimo;
	}
	public String getVlMinimo() {
		return vlMinimo;
	}
	public void setVlMinimo(String vlMinimo) {
		this.vlMinimo = vlMinimo;
	}

}
