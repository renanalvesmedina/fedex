package com.mercurio.lms.rest.contasareceber.retornodebanco.dto;
import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.rest.BaseDTO;
 
public class RetornoDeBancoDTO extends BaseDTO { 
	private static final long serialVersionUID = 1L; 
	
	private String numeroBoleto;
	private Long numeroBanco;
	private YearMonthDay dataDoMovimento;
	private String mensagemRetorno;
	private String ocorrencia;
	private String descricaoOcorrencia;
	private String motivo;
	private String descricaoMotivo;
	private BigDecimal valorTotal;
	private BigDecimal valorDesconto;
	private BigDecimal valorAbatimento;
	private BigDecimal valorJuros;
	private DateTime dataHoraInclusao;
	
	public String getNumeroBoleto() {
		return numeroBoleto;
	}
	public void setNumeroBoleto(String numeroBoleto) {
		this.numeroBoleto = numeroBoleto;
	}
	public Long getNumeroBanco() {
		return numeroBanco;
	}
	public void setNumeroBanco(Long numeroBanco) {
		this.numeroBanco = numeroBanco;
	}
	public YearMonthDay getDataDoMovimento() {
		return dataDoMovimento;
	}
	public void setDataDoMovimento(YearMonthDay dataDoMovimento) {
		this.dataDoMovimento = dataDoMovimento;
	}
	public String getMensagemRetorno() {
		return mensagemRetorno;
	}
	public void setMensagemRetorno(String mensagemRetorno) {
		this.mensagemRetorno = mensagemRetorno;
	}
	public String getOcorrencia() {
		return ocorrencia;
	}
	public void setOcorrencia(String ocorrencia) {
		this.ocorrencia = ocorrencia;
	}
	public String getDescricaoOcorrencia() {
		return descricaoOcorrencia;
	}
	public void setDescricaoOcorrencia(String descricaoOcorrencia) {
		this.descricaoOcorrencia = descricaoOcorrencia;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public String getDescricaoMotivo() {
		return descricaoMotivo;
	}
	public void setDescricaoMotivo(String descricaoMotivo) {
		this.descricaoMotivo = descricaoMotivo;
	}
	public BigDecimal getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}
	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}
	public BigDecimal getValorAbatimento() {
		return valorAbatimento;
	}
	public void setValorAbatimento(BigDecimal valorAbatimento) {
		this.valorAbatimento = valorAbatimento;
	}
	public BigDecimal getValorJuros() {
		return valorJuros;
	}
	public void setValorJuros(BigDecimal valorJuros) {
		this.valorJuros = valorJuros;
	}
	public DateTime getDataHoraInclusao() {
		return dataHoraInclusao;
	}
	public void setDataHoraInclusao(DateTime dataHoraInclusao) {
		this.dataHoraInclusao = dataHoraInclusao;
	}

} 
