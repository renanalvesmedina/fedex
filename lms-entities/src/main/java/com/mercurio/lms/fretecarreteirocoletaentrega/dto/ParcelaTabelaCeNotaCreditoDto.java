package com.mercurio.lms.fretecarreteirocoletaentrega.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ParcelaTabelaCeNotaCreditoDto {
	private String tpParcela;
	private BigDecimal peSobreFrete = BigDecimal.ZERO;
	private BigDecimal peSobreValorMercadoria = BigDecimal.ZERO;
	private BigDecimal vlDiaria = BigDecimal.ZERO;
	private BigDecimal vlKm = BigDecimal.ZERO;
	private BigDecimal vlFracaoPeso = BigDecimal.ZERO;
	private BigDecimal vlVolume = BigDecimal.ZERO;
	private BigDecimal vlEvento = BigDecimal.ZERO;
	private BigDecimal vlCtrc = BigDecimal.ZERO;
	private BigDecimal qtFracaoPeso = BigDecimal.ZERO;
	private List<TabelaNotaCreditoParcelasCeDto> lstTabelaNotaCreditoParcelasCeDto;
	
	public String getTpParcela() {
		return tpParcela;
	}
	public void setTpParcela(String tpParcela) {
		this.tpParcela = tpParcela;
	}
	public BigDecimal getPeSobreFrete() {
		return peSobreFrete;
	}
	public void setPeSobreFrete(BigDecimal peSobreFrete) {
		this.peSobreFrete = peSobreFrete;
	}
	public BigDecimal getPeSobreValorMercadoria() {
		return peSobreValorMercadoria;
	}
	public void setPeSobreValorMercadoria(BigDecimal peSobreValorMercadoria) {
		this.peSobreValorMercadoria = peSobreValorMercadoria;
	}
	public BigDecimal getVlDiaria() {
		return vlDiaria;
	}
	public void setVlDiaria(BigDecimal vlDiaria) {
		this.vlDiaria = vlDiaria;
	}
	public BigDecimal getVlKm() {
		return vlKm;
	}
	public void setVlKm(BigDecimal vlKm) {
		this.vlKm = vlKm;
	}
	public BigDecimal getVlFracaoPeso() {
		return vlFracaoPeso;
	}
	public void setVlFracaoPeso(BigDecimal vlFracaoPeso) {
		this.vlFracaoPeso = vlFracaoPeso;
	}
	public BigDecimal getVlVolume() {
		return vlVolume;
	}
	public void setVlVolume(BigDecimal vlVolume) {
		this.vlVolume = vlVolume;
	}
	public BigDecimal getVlEvento() {
		return vlEvento;
	}
	public void setVlEvento(BigDecimal vlEvento) {
		this.vlEvento = vlEvento;
	}
	public BigDecimal getVlCtrc() {
		return vlCtrc;
	}
	public void setVlCtrc(BigDecimal vlCtrc) {
		this.vlCtrc = vlCtrc;
	}
	public BigDecimal getQtFracaoPeso() {
		return qtFracaoPeso;
	}
	public void setQtFracaoPeso(BigDecimal qtFracaoPeso) {
		this.qtFracaoPeso = qtFracaoPeso;
	}
	public List<TabelaNotaCreditoParcelasCeDto> getLstTabelaNotaCreditoParcelasCeDto() {
		return lstTabelaNotaCreditoParcelasCeDto;
	}
	public void setLstTabelaNotaCreditoParcelasCeDto(List<TabelaNotaCreditoParcelasCeDto> lstTabelaNotaCreditoParcelasCeDto) {
		this.lstTabelaNotaCreditoParcelasCeDto = lstTabelaNotaCreditoParcelasCeDto;
	}
	public void addParcelaCe(TabelaNotaCreditoParcelasCeDto tabelaNotaCreditoParcelasCeDto) {
		if (lstTabelaNotaCreditoParcelasCeDto == null) {
			lstTabelaNotaCreditoParcelasCeDto = new ArrayList<TabelaNotaCreditoParcelasCeDto>();
		}
		lstTabelaNotaCreditoParcelasCeDto.add(tabelaNotaCreditoParcelasCeDto);
	}
	
}
