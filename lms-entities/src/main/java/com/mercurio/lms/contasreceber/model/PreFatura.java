package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

/** @author LMS Custom Hibernate CodeGenerator */
public class PreFatura implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPreFatura;

    /** persistent field */
    private Long nrPreFatura;

    /** persistent field */
    private BigDecimal vlFrete;

    /** persistent field */
    private BigDecimal vlBloqueio;

    /** persistent field */
    private BigDecimal vlDesbloqueio;

    /** persistent field */
    private YearMonthDay dtVencimento;

    /** persistent field */
    private YearMonthDay dtInicioFechamento;    
    
    /** persistent field */
    private YearMonthDay dtFinalFechamento;

    /** nullable persistent field */
    private DateTime dhTransmissao;

    /** nullable persistent field */
    private DateTime dhImportacao;

    /** nullable persistent field */
    private String nmCliente;

    /** nullable persistent field */
    private String cdDeposito;

    /** nullable persistent field */
    private String cdTransportadora;
    
    /** nullable persistent field */
    private String nrCnpjTransportadora;
    
    /** nullable persistent field */
    private String nrCnpjFornecedor;
    
    /** nullable persistent field */
    private String tpFrete;
    
    /** nullable persistent field */
    private String tpModalidadeFrete;    

    /** nullable persistent field */
    private String tpFreteUrbano;    
    
    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Fatura fatura;

	public String getCdDeposito() {
		return cdDeposito;
	}

	public void setCdDeposito(String cdDeposito) {
		this.cdDeposito = cdDeposito;
	}

	public String getCdTransportadora() {
		return cdTransportadora;
	}

	public void setCdTransportadora(String cdTransportadora) {
		this.cdTransportadora = cdTransportadora;
	}

	public DateTime getDhImportacao() {
		return dhImportacao;
	}

	public void setDhImportacao(DateTime dhImportacao) {
		this.dhImportacao = dhImportacao;
	}

	public DateTime getDhTransmissao() {
		return dhTransmissao;
	}

	public void setDhTransmissao(DateTime dhTransmissao) {
		this.dhTransmissao = dhTransmissao;
	}

	public YearMonthDay getDtFinalFechamento() {
		return dtFinalFechamento;
	}

	public void setDtFinalFechamento(YearMonthDay dtFinalFechamento) {
		this.dtFinalFechamento = dtFinalFechamento;
	}

	public YearMonthDay getDtInicioFechamento() {
		return dtInicioFechamento;
	}

	public void setDtInicioFechamento(YearMonthDay dtInicioFechamento) {
		this.dtInicioFechamento = dtInicioFechamento;
	}

	public YearMonthDay getDtVencimento() {
		return dtVencimento;
	}

	public void setDtVencimento(YearMonthDay dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public com.mercurio.lms.contasreceber.model.Fatura getFatura() {
		return fatura;
	}

	public void setFatura(com.mercurio.lms.contasreceber.model.Fatura fatura) {
		this.fatura = fatura;
	}

	public Long getIdPreFatura() {
		return idPreFatura;
	}

	public void setIdPreFatura(Long idPreFatura) {
		this.idPreFatura = idPreFatura;
	}

	public String getNmCliente() {
		return nmCliente;
	}

	public void setNmCliente(String nmCliente) {
		this.nmCliente = nmCliente;
	}

	public String getNrCnpjFornecedor() {
		return nrCnpjFornecedor;
	}

	public void setNrCnpjFornecedor(String nrCnpjFornecedor) {
		this.nrCnpjFornecedor = nrCnpjFornecedor;
	}

	public String getNrCnpjTransportadora() {
		return nrCnpjTransportadora;
	}

	public void setNrCnpjTransportadora(String nrCnpjTransportadora) {
		this.nrCnpjTransportadora = nrCnpjTransportadora;
	}

	public Long getNrPreFatura() {
		return nrPreFatura;
	}

	public void setNrPreFatura(Long nrPreFatura) {
		this.nrPreFatura = nrPreFatura;
	}

	public String getTpFrete() {
		return tpFrete;
	}

	public void setTpFrete(String tpFrete) {
		this.tpFrete = tpFrete;
	}

	public String getTpFreteUrbano() {
		return tpFreteUrbano;
	}

	public void setTpFreteUrbano(String tpFreteUrbano) {
		this.tpFreteUrbano = tpFreteUrbano;
	}

	public String getTpModalidadeFrete() {
		return tpModalidadeFrete;
	}

	public void setTpModalidadeFrete(String tpModalidadeFrete) {
		this.tpModalidadeFrete = tpModalidadeFrete;
	}

	public BigDecimal getVlBloqueio() {
		return vlBloqueio;
	}

	public void setVlBloqueio(BigDecimal vlBloqueio) {
		this.vlBloqueio = vlBloqueio;
	}

	public BigDecimal getVlDesbloqueio() {
		return vlDesbloqueio;
	}

	public void setVlDesbloqueio(BigDecimal vlDesbloqueio) {
		this.vlDesbloqueio = vlDesbloqueio;
	}

	public BigDecimal getVlFrete() {
		return vlFrete;
	}

	public void setVlFrete(BigDecimal vlFrete) {
		this.vlFrete = vlFrete;
	}

}
