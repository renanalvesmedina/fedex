package com.mercurio.lms.contasreceber.model.param;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.joda.time.YearMonthDay;
import com.mercurio.lms.util.JTDateTimeUtils;


public class LinhaArquivoRecebidoFaturaParam {

	private String linha;  
	
	public LinhaArquivoRecebidoFaturaParam(String linha) {
		super();
		this.linha = linha;
	}

	public String getCdDeposito() {
		return linha.substring(3, 8);
	}

	public String getCdTransportadora() {
		return linha.substring(22, 25);
	}

	public YearMonthDay getDtFinalFechamento() {
		return JTDateTimeUtils.convertDataStringToYearMonthDay(linha.substring(77, 85));
	}

	public YearMonthDay getDtInicioFechamento() {
		return JTDateTimeUtils.convertDataStringToYearMonthDay(linha.substring(69, 77));
	}

	public String getNmCliente() {
		return linha.substring(8, 22);
	}

	public String getNrCnpjFornecedor() {
		return linha.substring(85, 99);
	}

	public String getNrCnpjTransportadora() {
		return linha.substring(25, 39);
	}

	public Long getNrPreFatura() {
		return Long.valueOf(linha.substring(39, 46));
	}

	public String getTpFrete() {
		return linha.substring(46, 47);
	}

	public String getTpFreteUrbano() {
		return linha.substring(48, 49);
	}

	public String getTpModalidadeFrete() {
		return linha.substring(47, 48);
	}

	public BigDecimal getVlBloqueio() {
		return new BigDecimal(linha.substring(99, 111)).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
	}

	public BigDecimal getVlDesbloqueio() {
		return new BigDecimal(linha.substring(111, 123)).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
	}

	public BigDecimal getVlFrete() {
		return new BigDecimal(linha.substring(57, 69)).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
	}
	
	public YearMonthDay getDtVencimento() {
		return JTDateTimeUtils.convertDataStringToYearMonthDay(linha.substring(49, 57));
	}

}
