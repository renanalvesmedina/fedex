package com.mercurio.lms.ppd.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.YearMonthDay;

public class PpdReciboGrm {
	private Logger log = LogManager.getLogger(this.getClass());
	private String filialOrigem;
	private String numeroCtrc;	
	private String filialDestino;
	private String dataEmissaoCtrc;
	private String dataEmissaoRecibo;	         
	private String dataPagamento;  	         
	private String valorIndenizacao;	         	
	private String tipoIndenizacao;
	private String filialRecibo;
	private String numeroRecibo;
	private String filialComp1;
	private String percentComp1;
	private String filialComp2;
	private String percentComp2;
	private String filialComp3;
	private String percentComp3;
	private String nomeFavorecido;
	private String cnpjFavorecido;	
	private String banco;
	private String agencia;
	private String digitoAgencia;
	private String contaCorrente;
	private String digitoContaCorrente;
	private String dataLiberacao;	 
	private String dataDevolucao;
	private String valorJuros;	
	private String situacao;
	private String flagCancelamento;
	private String dataProgramada;
	private String dataTransmissao;	
	private String flagEnvioBi;	
	private String flagEnvioJde;
	private String numeroSeguro;
	
	public PpdReciboGrm(String str) {
		str = str.replace("\"", "");
		String[] campos = str.split(",");    			
		this.setFilialOrigem(campos[0].split(" ")[0]);
		this.setNumeroCtrc(campos[0].split(" ")[1]);
		this.setFilialDestino(campos[1]);
		this.setDataEmissaoCtrc(campos[2]);
		this.setDataEmissaoRecibo(campos[3]);
		this.setDataPagamento(campos[4]);
		this.setValorIndenizacao(campos[5]);		
		this.setTipoIndenizacao(campos[6]);
		this.setFilialRecibo(campos[7].split(" ")[0]);
		this.setNumeroRecibo(campos[7].split(" ")[1]);	
		this.setFilialComp1(campos[8].substring(0,3));
		this.setPercentComp1(campos[8].substring(3,6));
		if(campos[8].trim().length() > 6) {
			this.setFilialComp2(campos[8].substring(6,9));
			this.setPercentComp2(campos[8].substring(9,12));
		}
		if(campos[8].trim().length() > 12) {
			this.setFilialComp3(campos[8].substring(12,15));
			this.setPercentComp3(campos[8].substring(15));
		}
		this.setNomeFavorecido(campos[9]);
		this.setCnpjFavorecido(campos[10]);
		this.setBanco(campos[11]);
		this.setAgencia(campos[12]);
		this.setDigitoAgencia(campos[13]);
		this.setContaCorrente(campos[14]);
		this.setDigitoContaCorrente(campos[15]);
		this.setDataLiberacao(campos[16]);
		this.setDataDevolucao(campos[17]);
		this.setValorJuros(campos[18]);
		this.setSituacao(campos[19]);
		this.setFlagCancelamento(campos[20]);
		this.setDataProgramada(campos[21]);
		this.setDataTransmissao(campos[22]);
		this.setFlagEnvioBi(campos[23]);
		this.setFlagEnvioJde(campos[24]);
		if (campos.length == 26) {
			this.setNumeroSeguro(campos[25]);
		}			
	}

	/**
	 * Método que transforma a data que vem do arquivo (AAAAMMDD) para o tipo
	 * Date
	 * 
	 * @param String
	 *            data
	 * @return Date
	 */
	public YearMonthDay stringToDate(String data) {		
		if (data == null || data.equals(""))  
			return null;  
		          
		Date date = null;  
		
		try {  
			DateFormat formatter = new SimpleDateFormat("yyyyMMdd");  
		    date = (java.util.Date)formatter.parse(data);  
		} catch (ParseException e) {              			
			log.error(e);  
		}  			
		
		return YearMonthDay.fromDateFields(date);			
	}	
	
	public String getFilialOrigem() {
		return filialOrigem;
	}

	public void setFilialOrigem(String filialOrigem) {
		this.filialOrigem = StringUtils.stripToNull(filialOrigem);
	}

	public String getNumeroCtrc() {
		return numeroCtrc;
	}

	public void setNumeroCtrc(String numeroCtrc) {
		this.numeroCtrc = StringUtils.stripToNull(numeroCtrc);
	}

	public String getFilialDestino() {
		return filialDestino;
	}

	public void setFilialDestino(String filialDestino) {
		this.filialDestino = StringUtils.stripToNull(filialDestino);
	}

	public String getDataEmissaoCtrc() {
		return dataEmissaoCtrc;
	}

	public void setDataEmissaoCtrc(String dataEmissaoCtrc) {
		this.dataEmissaoCtrc = StringUtils.stripToNull(dataEmissaoCtrc);
	}
	
	public String getDataEmissaoRecibo() {
		return dataEmissaoRecibo;
	}

	public void setDataEmissaoRecibo(String dataEmissaoRecibo) {
		this.dataEmissaoRecibo = StringUtils.stripToNull(dataEmissaoRecibo);
	}

	public String getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(String dataPagamento) {
		this.dataPagamento = StringUtils.stripToNull(dataPagamento);
	}

	public String getValorIndenizacao() {
		return valorIndenizacao;
	}

	public void setValorIndenizacao(String valorIndenizacao) {
		this.valorIndenizacao = StringUtils.stripToNull(valorIndenizacao);
	}

	public String getTipoIndenizacao() {
		return tipoIndenizacao;
	}

	public void setTipoIndenizacao(String tipoIndenizacao) {
		this.tipoIndenizacao = StringUtils.stripToNull(tipoIndenizacao);
	}
	
	public String getFilialRecibo() {
		return filialRecibo;
	}

	public void setFilialRecibo(String filialRecibo) {
		this.filialRecibo = StringUtils.stripToNull(filialRecibo);
	}

	public String getNumeroRecibo() {
		return numeroRecibo;
	}

	public void setNumeroRecibo(String numeroRecibo) {
		this.numeroRecibo = StringUtils.stripToNull(numeroRecibo);
	}

	public String getFilialComp1() {
		return filialComp1;
	}

	public void setFilialComp1(String filialComp1) {
		this.filialComp1 = StringUtils.stripToNull(filialComp1);
	}

	public String getPercentComp1() {
		return percentComp1;
	}

	public void setPercentComp1(String percentComp1) {
		this.percentComp1 = StringUtils.stripToNull(percentComp1);
	}

	public String getFilialComp2() {
		return filialComp2;
	}

	public void setFilialComp2(String filialComp2) {
		this.filialComp2 = StringUtils.stripToNull(filialComp2);
	}

	public String getPercentComp2() {
		return percentComp2;
	}

	public void setPercentComp2(String percentComp2) {
		this.percentComp2 = StringUtils.stripToNull(percentComp2);
	}

	public String getFilialComp3() {
		return filialComp3;
	}

	public void setFilialComp3(String filialComp3) {
		this.filialComp3 = StringUtils.stripToNull(filialComp3);
	}

	public String getPercentComp3() {
		return percentComp3;
	}

	public void setPercentComp3(String percentComp3) {
		this.percentComp3 = StringUtils.stripToNull(percentComp3);
	}

	public String getNomeFavorecido() {
		return nomeFavorecido;
	}

	public void setNomeFavorecido(String nomeFavorecido) {
		this.nomeFavorecido = StringUtils.stripToNull(nomeFavorecido);
	}

	public String getCnpjFavorecido() {
		return cnpjFavorecido;
	}

	public void setCnpjFavorecido(String cnpjFavorecido) {
		this.cnpjFavorecido = StringUtils.stripToNull(cnpjFavorecido);
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = StringUtils.stripToNull(banco);
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = StringUtils.stripToNull(agencia);
	}

	public String getDigitoAgencia() {
		return digitoAgencia;
	}

	public void setDigitoAgencia(String digitoAgencia) {
		this.digitoAgencia = StringUtils.stripToNull(digitoAgencia);
	}

	public String getContaCorrente() {
		return contaCorrente;
	}

	public void setContaCorrente(String contaCorrente) {
		this.contaCorrente = StringUtils.stripToNull(contaCorrente);
	}

	public String getDigitoContaCorrente() {
		return digitoContaCorrente;
	}

	public void setDigitoContaCorrente(String digitoContaCorrente) {
		this.digitoContaCorrente = StringUtils.stripToNull(digitoContaCorrente);
	}

	public String getDataLiberacao() {
		return dataLiberacao;
	}

	public void setDataLiberacao(String dataLiberacao) {
		this.dataLiberacao = StringUtils.stripToNull(dataLiberacao);
	}

	public String getDataDevolucao() {
		return dataDevolucao;
	}

	public void setDataDevolucao(String dataDevolucao) {
		this.dataDevolucao = StringUtils.stripToNull(dataDevolucao);
	}

	public String getValorJuros() {
		return valorJuros;
	}

	public void setValorJuros(String valorJuros) {
		this.valorJuros = StringUtils.stripToNull(valorJuros);
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = StringUtils.stripToNull(situacao);
	}

	public String getFlagCancelamento() {
		return flagCancelamento;
	}

	public void setFlagCancelamento(String flagCancelamento) {
		this.flagCancelamento = StringUtils.stripToNull(flagCancelamento);
	}

	public String getDataProgramada() {
		return dataProgramada;
	}

	public void setDataProgramada(String dataProgramada) {
		this.dataProgramada = StringUtils.stripToNull(dataProgramada);
	}

	public String getDataTransmissao() {
		return dataTransmissao;
	}

	public void setDataTransmissao(String dataTransmissao) {
		this.dataTransmissao = StringUtils.stripToNull(dataTransmissao);
	}

	public String getFlagEnvioBi() {
		return flagEnvioBi;
	}

	public void setFlagEnvioBi(String flagEnvioBi) {
		this.flagEnvioBi = StringUtils.stripToNull(flagEnvioBi);
	}

	public String getFlagEnvioJde() {
		return flagEnvioJde;
	}

	public void setFlagEnvioJde(String flagEnvioJde) {
		this.flagEnvioJde = StringUtils.stripToNull(flagEnvioJde);
	}

	public String getNumeroSeguro() {
		return numeroSeguro;
	}

	public void setNumeroSeguro(String numeroSeguro) {
		this.numeroSeguro = StringUtils.stripToNull(numeroSeguro);
	}	
}
