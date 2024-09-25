package com.mercurio.lms.contasreceber.model.param;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.util.DateTimeUtils;




public class LinhaArquivoRecebidoItemFaturaParam {
	
	private String linha;  
	
	public LinhaArquivoRecebidoItemFaturaParam(String linha) {
		super();
		this.linha = linha;
	}

	public Long getNrNotaFiscal() {
		return Long.valueOf(linha.substring(13, 21));
	}

	public Long getNrRoteiro() {
		return Long.valueOf(linha.substring(56, 66));
	}

	public Long getNrProtocolo() {
		return Long.valueOf(linha.substring(66, 72));
	}
	
	public BigDecimal getVlNotaFiscal() {
		return new BigDecimal(linha.substring(43, 56)).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
	}
	
	public BigDecimal getPsAforado() {
		return new BigDecimal(linha.substring(86, 97)).divide(new BigDecimal("10000"), 2, RoundingMode.HALF_UP);
	}	
	
	public BigDecimal getPsMercadoria() {
		return new BigDecimal(linha.substring(32, 43)).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
	}	
	
	public YearMonthDay getDtEmissaoNotaFiscal() {
		// LMS-8817
		String dma = linha.substring(24, 32);
		dma = dma.substring(0, 2).concat("/".concat(dma.substring(2, 4).concat("/".concat(dma.substring(4)))));
		Calendar cal = DateTimeUtils.convertStringToCalendar(dma);

		return YearMonthDay.fromCalendarFields(cal);
	}

	public String getTpFrete() {
		return linha.substring(10, 11);
	}

	public String getTpFreteUrbano() {
		return linha.substring(12, 13);
	}

	public String getTpModalidadeFrete() {
		return linha.substring(11, 12);
	}	
	
	public String getCdSerieNotaFiscal() {
		return linha.substring(21, 24);
	}	
	
	public String getNrCnpjClienteDestino() {
		return linha.substring(72, 86);
	}	
	
	public String getNmClienteDestino() {
		return linha.substring(97, 127);
	}	
	
	public String getNmCidadeClienteDestino() {
		return linha.substring(127, 157);
	}	
	
}
