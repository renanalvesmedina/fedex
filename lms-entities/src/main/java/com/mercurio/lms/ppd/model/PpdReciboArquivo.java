package com.mercurio.lms.ppd.model;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.YearMonthDay;

public class PpdReciboArquivo {
	private Logger log = LogManager.getLogger(this.getClass());
	private String ide;
	private String filialReceb;
	private String filialTransm;	
	private String filialOrigem;
	private String ctosNumero;
	private String ctosDigito;	
	private String filialDestino;
	private String zeros;
	private String dataEmissaoCtos;
	private String dataEmissaoRecibo;
	private String dataPagamento;
	private String valorIndenizacao;
	private String motivo;
	private String reciboFilial;
	private String reciboNumero;
	private String canc;
	private String banco;
	private String agenciaNumero;
	private String agenciaDigito;
	private String contaNumero;
	private String contaDigito;
	private String favorecidoCgc;
	private String favorecidoNome;		
	private String filialComp1;
	private String percentualFilialComp1;
	private String filialComp2;
	private String percentualFilialComp2;
	private String filialComp3;
	private String percentualFilialComp3;
	
	private String seguroNumero;
	private String qtdVolumesIndenizados;
	private String situacao;
	
	public String getIde() {
		return ide;
	}

	public void setIde(String ide) {
			this.ide = StringUtils.stripToNull(ide);
	}

	public String getFilialReceb() {
		return filialReceb;
	}

	public void setFilialReceb(String filialReceb) {
		this.filialReceb = StringUtils.stripToNull(filialReceb);
	}

	public String getFilialTransm() {
		return filialTransm;
	}

	public void setFilialTransm(String filialTransm) {
		this.filialTransm = StringUtils.stripToNull(filialTransm);
	}

	public String getFilialOrigem() {
		return filialOrigem;
	}

	public void setFilialOrigem(String filialOrigem) {
		this.filialOrigem = StringUtils.stripToNull(filialOrigem);
	}

	public String getCtosNumero() {
		return ctosNumero;
	}

	public void setCtosNumero(String ctosNumero) {
		this.ctosNumero = StringUtils.stripToNull(ctosNumero);
	}

	public String getCtosDigito() {
		return ctosDigito;
	}

	public void setCtosDigito(String ctosDigito) {
		this.ctosDigito = StringUtils.stripToNull(ctosDigito);
	}

	public String getFilialDestino() {
		return filialDestino;
	}

	public void setFilialDestino(String filialDestino) {
		this.filialDestino = StringUtils.stripToNull(filialDestino);
	}

	public String getZeros() {
		return zeros;
	}

	public void setZeros(String zeros) {
		this.zeros = StringUtils.stripToNull(zeros);
	}

	public String getDataEmissaoCtos() {
		return dataEmissaoCtos;
	}

	public YearMonthDay getDataEmissaoCtosDate() {
		return this.stringToDate(dataEmissaoCtos);
	}

	public void setDataEmissaoCtos(String dataEmissaoCtos) {
		this.dataEmissaoCtos = StringUtils.stripToNull(dataEmissaoCtos);
	}

	public String getDataEmissaoRecibo() {
		return dataEmissaoRecibo;
	}

	public YearMonthDay getDataEmissaoReciboDate() {
		return this.stringToDate(dataEmissaoRecibo);
	}

	public void setDataEmissaoRecibo(String dataEmissaoRecibo) {
		this.dataEmissaoRecibo = StringUtils.stripToNull(dataEmissaoRecibo);
	}

	public String getDataPagamento() {
		return dataPagamento;
	}

	public YearMonthDay getDataPagamentoDate() {
		return this.stringToDate(dataPagamento);
	}

	public void setDataPagamento(String dataPagamento) {
		this.dataPagamento = StringUtils.stripToNull(dataPagamento);
	}

	public String getValorIndenizacao() {
		return valorIndenizacao;
	}

	public BigDecimal getValorIndenizacaoBigDecimal() {		
		return stringToBigDecimal(valorIndenizacao);
	}

	public void setValorIndenizacao(String valorIndenizacao) {
		this.valorIndenizacao = StringUtils.stripToNull(valorIndenizacao);
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = StringUtils.stripToNull(motivo);
	}

	public String getReciboFilial() {
		return reciboFilial;
	}

	public void setReciboFilial(String reciboFilial) {
		this.reciboFilial = StringUtils.stripToNull(reciboFilial);
	}

	public String getReciboNumero() {		
		return reciboNumero;
	}

	public void setReciboNumero(String reciboNumero) {
		this.reciboNumero = StringUtils.stripToNull(reciboNumero);
	}

	public String getCanc() {
		return canc;
	}

	public void setCanc(String canc) {
		this.canc = StringUtils.stripToNull(canc);
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = StringUtils.stripToNull(banco);
	}

	public String getAgenciaNumero() {
		return agenciaNumero;
	}

	public void setAgenciaNumero(String agenciaNumero) {
		this.agenciaNumero = StringUtils.stripToNull(agenciaNumero);
	}

	public String getAgenciaDigito() {
		return agenciaDigito;
	}

	public void setAgenciaDigito(String agenciaDigito) {
		this.agenciaDigito = StringUtils.stripToNull(agenciaDigito);
	}

	public String getContaNumero() {
		return contaNumero;
	}

	public void setContaNumero(String contaNumero) {
		this.contaNumero = StringUtils.stripToNull(contaNumero);
	}

	public String getContaDigito() {
		return contaDigito;
	}

	public void setContaDigito(String contaDigito) {
		this.contaDigito = StringUtils.stripToNull(contaDigito);
	}

	public String getFavorecidoCgc() {
		return favorecidoCgc;
	}

	public void setFavorecidoCgc(String favorecidoCgc) {
		this.favorecidoCgc = StringUtils.stripToNull(favorecidoCgc);
	}

	public String getFavorecidoNome() {
		return favorecidoNome;
	}

	public void setFavorecidoNome(String favorecidoNome) {
		this.favorecidoNome = StringUtils.stripToNull(favorecidoNome);
	}	

	public String getFilialComp1() {
		return filialComp1;
	}

	public void setFilialComp1(String filialComp1) {
		this.filialComp1 = StringUtils.stripToNull(filialComp1);
	}

	public String getPercentualFilialComp1() {
		return percentualFilialComp1;
	}

	public void setPercentualFilialComp1(String percentualFilialComp1) {
		this.percentualFilialComp1 = StringUtils
				.stripToNull(percentualFilialComp1);
	}

	public String getFilialComp2() {
		return filialComp2;
	}

	public void setFilialComp2(String filialComp2) {
		this.filialComp2 = StringUtils.stripToNull(filialComp2);
	}

	public String getPercentualFilialComp2() {
		return percentualFilialComp2;
	}

	public void setPercentualFilialComp2(String percentualFilialComp2) {
		this.percentualFilialComp2 = StringUtils
				.stripToNull(percentualFilialComp2);
	}

	public String getFilialComp3() {
		return filialComp3;
	}

	public void setFilialComp3(String filialComp3) {
		this.filialComp3 = StringUtils.stripToNull(filialComp3);
	}

	public String getPercentualFilialComp3() {
		return percentualFilialComp3;
	}

	public void setPercentualFilialComp3(String percentualFilialComp3) {
		this.percentualFilialComp3 = StringUtils
				.stripToNull(percentualFilialComp3);
	}

	public String getSeguroNumero() {
		return seguroNumero;
	}

	public void setSeguroNumero(String seguroNumero) {
		this.seguroNumero = StringUtils.stripToNull(seguroNumero);
	}

	public String getQtdVolumesIndenizados() {
		return qtdVolumesIndenizados;
	}

	public void setQtdVolumesIndenizados(String qtdVolumesIndenizados) {
		this.qtdVolumesIndenizados = StringUtils
				.stripToNull(qtdVolumesIndenizados);
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = StringUtils.stripToNull(situacao);
	}
	
	private BigDecimal stringToBigDecimal(String str) {		
		String intPart = str.substring(0, str.length() - 2);
		String decPart = str.substring(str.length() - 2);		
		BigDecimal retorno = new BigDecimal(intPart + "." + decPart);
		return retorno;		
	}
		
	/**
	 * Método que transforma a data que vem do arquivo (AAMMDD) para o tipo Date 
	 * 
	 * @param String
	 *            data
	 * @return Date
	 */
	private YearMonthDay stringToDate(String data) {		
		if (data == null || data.equals(""))  
			return null;  
		          
		Date date = null;  
		
		try {  
			DateFormat formatter = new SimpleDateFormat("yyMMdd");  
		    date = (java.util.Date)formatter.parse(data);  
		} catch (ParseException e) {              			
			log.error(e);  
		}  			
		
		return YearMonthDay.fromDateFields(date);			
	}
	
	public PpdReciboArquivo(String str) {
		this.setIde(str.substring(0, 2).trim());
		this.setFilialReceb(str.substring(2, 5).trim());
		this.setFilialTransm(str.substring(5, 8).trim());
		this.setFilialOrigem(str.substring(8, 11).trim());
		this.setCtosNumero(str.substring(11, 17).trim());
		this.setCtosDigito(str.substring(17, 18).trim());
		this.setFilialDestino(str.substring(18, 21).trim());
		this.setZeros(str.substring(21, 25).trim());
		this.setDataEmissaoCtos(str.substring(25, 31).trim());
		this.setDataEmissaoRecibo(str.substring(31, 37).trim());
		this.setDataPagamento(str.substring(37, 43).trim());
		this.setValorIndenizacao(str.substring(43, 56).trim());
		this.setMotivo(str.substring(56, 57).trim());
		this.setReciboFilial(str.substring(57, 60).trim());
		this.setReciboNumero(str.substring(60, 64).trim());
		this.setCanc(str.substring(64, 65).trim());
		this.setBanco(str.substring(65, 68).trim());
		this.setAgenciaNumero(str.substring(68, 72).trim());
		this.setAgenciaDigito(str.substring(72, 74).trim());
		this.setContaNumero(str.substring(74, 85).trim());
		this.setContaDigito(str.substring(85, 87).trim());
		this.setFavorecidoCgc(str.substring(87, 101).trim());
		this.setFavorecidoNome(str.substring(101, 131).trim());			        					        		
		this.setFilialComp1(str.substring(131,133).trim());
		this.setPercentualFilialComp1(str.substring(133,137).trim());
		this.setFilialComp2(str.substring(137,139).trim());
		this.setPercentualFilialComp2(str.substring(139,143).trim());
		this.setFilialComp3(str.substring(143,145).trim());
		this.setPercentualFilialComp3(str.substring(145,149).trim());			        		
		this.setSeguroNumero(str.substring(149, 164).trim());
		this.setQtdVolumesIndenizados(str.substring(164, 168).trim());
		this.setSituacao(str.substring(168, 169).trim());	
	}
}
