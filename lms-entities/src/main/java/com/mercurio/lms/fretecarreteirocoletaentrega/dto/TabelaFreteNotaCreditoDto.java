package com.mercurio.lms.fretecarreteirocoletaentrega.dto;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.YearMonthDay;

public class TabelaFreteNotaCreditoDto {

	private YearMonthDay dtVigenciaInicial;
	private YearMonthDay dtVigenciaFinal;
	private String dsCliente;
	private String tpCalculo;
	
	// Campos C1
	private BigDecimal vlDiaria = BigDecimal.ZERO;
	private BigDecimal pePernoite = BigDecimal.ZERO;
	private BigDecimal vlEvento = BigDecimal.ZERO;
	private BigDecimal qtKm = BigDecimal.ZERO;
	private BigDecimal vlFracaoPeso = BigDecimal.ZERO;
	private BigDecimal qtFracaoPeso = BigDecimal.ZERO;
	private String dsFracaoPeso = "";
	// Campos C2
	private BigDecimal qtVolume = BigDecimal.ZERO;
	private BigDecimal vlDefinidoPeSobreValorMercadoria = BigDecimal.ZERO;
	private BigDecimal peSobreValorMercadoria = BigDecimal.ZERO;
	private BigDecimal vlDefinidoSobreFrete = BigDecimal.ZERO;
	private BigDecimal peSobreFrete = BigDecimal.ZERO;

	private List<TabelaNotaCreditoParcelasCeDto> listParcelasCe;
	
	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}
	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}
	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}
	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}
	public String getDsCliente() {
		return dsCliente;
	}
	public void setDsCliente(String dsCliente) {
		this.dsCliente = dsCliente;
	}
	public String getTpCalculo() {
		return tpCalculo;
	}
	public void setTpCalculo(String tpCalculo) {
		this.tpCalculo = tpCalculo;
	}
	public BigDecimal getVlDiaria() {
		return vlDiaria;
	}
	public void setVlDiaria(BigDecimal vlDiaria) {
		this.vlDiaria = vlDiaria;
	}
	public BigDecimal getPePernoite() {
		return pePernoite;
	}
	public void setPePernoite(BigDecimal pePernoite) {
		this.pePernoite = pePernoite;
	}
	public BigDecimal getVlEvento() {
		return vlEvento;
	}
	public void setVlEvento(BigDecimal vlEvento) {
		this.vlEvento = vlEvento;
	}
	public BigDecimal getQtKm() {
		return qtKm;
	}
	public void setQtKm(BigDecimal qtKm) {
		this.qtKm = qtKm;
	}
	public BigDecimal getVlFracaoPeso() {
		return vlFracaoPeso;
	}
	public void setVlFracaoPeso(BigDecimal vlFracaoPeso) {
		this.vlFracaoPeso = vlFracaoPeso;
	}
	public BigDecimal getQtFracaoPeso() {
		return qtFracaoPeso;
	}
	public void setQtFracaoPeso(BigDecimal qtFracaoPeso) {
		this.qtFracaoPeso = qtFracaoPeso;
	}
	public BigDecimal getQtVolume() {
		return qtVolume;
	}
	public void setQtVolume(BigDecimal qtVolume) {
		this.qtVolume = qtVolume;
	}
	public BigDecimal getVlDefinidoPeSobreValorMercadoria() {
		return vlDefinidoPeSobreValorMercadoria;
	}
	public void setVlDefinidoPeSobreValorMercadoria(
			BigDecimal vlDefinidoPeSobreValorMercadoria) {
		this.vlDefinidoPeSobreValorMercadoria = vlDefinidoPeSobreValorMercadoria;
	}
	public BigDecimal getPeSobreValorMercadoria() {
		return peSobreValorMercadoria;
	}
	public void setPeSobreValorMercadoria(BigDecimal peSobreValorMercadoria) {
		this.peSobreValorMercadoria = peSobreValorMercadoria;
	}
	public BigDecimal getVlDefinidoSobreFrete() {
		return vlDefinidoSobreFrete;
	}
	public void setVlDefinidoSobreFrete(BigDecimal vlDefinidoSobreFrete) {
		this.vlDefinidoSobreFrete = vlDefinidoSobreFrete;
	}
	public BigDecimal getPeSobreFrete() {
		return peSobreFrete;
	}
	public void setPeSobreFrete(BigDecimal peSobreFrete) {
		this.peSobreFrete = peSobreFrete;
	}
	
	public String getDsFracaoPeso() {
		DecimalFormat decimalFmt = new DecimalFormat("#,##0.00;-#,##0.00");
		DecimalFormat integerFmt = new DecimalFormat("#,##0;-#,##0");
		dsFracaoPeso = decimalFmt.format(nvl(vlFracaoPeso)) + " / " + integerFmt.format(nvl(qtFracaoPeso)) + " Kg";
		return dsFracaoPeso;
	}
	
	private BigDecimal nvl(BigDecimal value) {
		if (value == null) {
			return BigDecimal.ZERO;
		} 
		return value;
	}
	
	public void addParcelaCe(TabelaNotaCreditoParcelasCeDto tabelaNotaCreditoParcelasCeDto) {
		if(CollectionUtils.isEmpty(listParcelasCe)) {
			listParcelasCe = new ArrayList<TabelaNotaCreditoParcelasCeDto>();
		}
		listParcelasCe.add(tabelaNotaCreditoParcelasCeDto);
	}
	
	public List<TabelaNotaCreditoParcelasCeDto> getListParcelasCe() {
		return listParcelasCe;
	}
	
	public void setListParcelasCe(List<TabelaNotaCreditoParcelasCeDto> listParcelasCe) {
		this.listParcelasCe = listParcelasCe;
	}
	public void setDsFracaoPeso(String dsFracaoPeso) {
		this.dsFracaoPeso = dsFracaoPeso;
	}	
}
