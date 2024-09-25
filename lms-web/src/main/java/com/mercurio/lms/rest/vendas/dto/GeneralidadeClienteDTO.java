package com.mercurio.lms.rest.vendas.dto;

import java.math.BigDecimal;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;

public class GeneralidadeClienteDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idGeneralidadeCliente;
	private String nmParcelaPreco;
	private DomainValue tpIndicador; 
	private BigDecimal vlGeneralidade; //Valor em String por causa do retorno formatado de acordo com o indicador 
	private DomainValue tpIndicadorMinimo;
	private BigDecimal vlMinimo;//Valor em String por causa do retorno formatado de acordo com o indicador
	private Long idParcelaPreco;
	private Long idParametroCliente;
	
	
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
	public DomainValue getTpIndicador() {
		return tpIndicador;
	}
	public void setTpIndicador(DomainValue tpIndicador) {
		this.tpIndicador = tpIndicador;
	}
	public BigDecimal getVlGeneralidade() {
		return vlGeneralidade;
	}
	public void setVlGeneralidade(BigDecimal vlGeneralidade) {
		this.vlGeneralidade = vlGeneralidade;
	}
	public DomainValue getTpIndicadorMinimo() {
		return tpIndicadorMinimo;
	}
	public void setTpIndicadorMinimo(DomainValue tpIndicadorMinimo) {
		this.tpIndicadorMinimo = tpIndicadorMinimo;
	}
	public BigDecimal getVlMinimo() {
		return vlMinimo;
	}
	public void setVlMinimo(BigDecimal vlMinimo) {
		this.vlMinimo = vlMinimo;
	}
	public Long getIdParcelaPreco() {
		return idParcelaPreco;
	}
	public void setIdParcelaPreco(Long idParcelaPreco) {
		this.idParcelaPreco = idParcelaPreco;
	}
	public Long getIdParametroCliente() {
		return idParametroCliente;
	}
	public void setIdParametroCliente(Long idParametroCliente) {
		this.idParametroCliente = idParametroCliente;
	}
	
	
}
