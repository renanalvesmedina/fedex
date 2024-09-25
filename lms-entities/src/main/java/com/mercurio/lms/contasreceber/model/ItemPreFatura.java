package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.joda.time.YearMonthDay;

/** @author LMS Custom Hibernate CodeGenerator */
public class ItemPreFatura implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idItemPreFatura;

    /** persistent field */
    private Long nrNotaFiscal;

    /** persistent field */
    private Long nrRoteiro;
    
    /** persistent field */
    private Long nrProtocolo;
    
    /** persistent field */
    private BigDecimal vlNotaFiscal;

    /** persistent field */
    private BigDecimal psAforado;

    /** persistent field */
    private BigDecimal psMercadoria;

    /** persistent field */
    private YearMonthDay dtEmissaoNotaFiscal;
    
    /** nullable persistent field */
    private String tpFrete;
    
    /** nullable persistent field */
    private String tpModalidadeFrete;    

    /** nullable persistent field */
    private String tpFreteUrbano;        

    /** nullable persistent field */
    private String cdSerieNotaFiscal;

    /** nullable persistent field */
    private String nrCnpjClienteDestino;

    /** nullable persistent field */
    private String nmClienteDestino;
    
    /** nullable persistent field */
    private String nmCidadeClienteDestino;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.ItemFatura itemFatura;

	public String getCdSerieNotaFiscal() {
		return cdSerieNotaFiscal;
	}

	public void setCdSerieNotaFiscal(String cdSerieNotaFiscal) {
		this.cdSerieNotaFiscal = cdSerieNotaFiscal;
	}

	public YearMonthDay getDtEmissaoNotaFiscal() {
		return dtEmissaoNotaFiscal;
	}

	public void setDtEmissaoNotaFiscal(YearMonthDay dtEmissaoNotaFiscal) {
		this.dtEmissaoNotaFiscal = dtEmissaoNotaFiscal;
	}

	public Long getIdItemPreFatura() {
		return idItemPreFatura;
	}

	public void setIdItemPreFatura(Long idItemPreFatura) {
		this.idItemPreFatura = idItemPreFatura;
	}

	public com.mercurio.lms.contasreceber.model.ItemFatura getItemFatura() {
		return itemFatura;
	}

	public void setItemFatura(
			com.mercurio.lms.contasreceber.model.ItemFatura itemFatura) {
		this.itemFatura = itemFatura;
	}

	public String getNmCidadeClienteDestino() {
		return nmCidadeClienteDestino;
	}

	public void setNmCidadeClienteDestino(String nmCidadeClienteDestino) {
		this.nmCidadeClienteDestino = nmCidadeClienteDestino;
	}

	public String getNmClienteDestino() {
		return nmClienteDestino;
	}

	public void setNmClienteDestino(String nmClienteDestino) {
		this.nmClienteDestino = nmClienteDestino;
	}

	public String getNrCnpjClienteDestino() {
		return nrCnpjClienteDestino;
	}

	public void setNrCnpjClienteDestino(String nrCnpjClienteDestino) {
		this.nrCnpjClienteDestino = nrCnpjClienteDestino;
	}

	public Long getNrNotaFiscal() {
		return nrNotaFiscal;
	}

	public void setNrNotaFiscal(Long nrNotaFiscal) {
		this.nrNotaFiscal = nrNotaFiscal;
	}

	public Long getNrProtocolo() {
		return nrProtocolo;
	}

	public void setNrProtocolo(Long nrProtocolo) {
		this.nrProtocolo = nrProtocolo;
	}

	public Long getNrRoteiro() {
		return nrRoteiro;
	}

	public void setNrRoteiro(Long nrRoteiro) {
		this.nrRoteiro = nrRoteiro;
	}

	public BigDecimal getPsAforado() {
		return psAforado;
	}

	public void setPsAforado(BigDecimal psAforado) {
		this.psAforado = psAforado;
	}

	public BigDecimal getPsMercadoria() {
		return psMercadoria;
	}

	public void setPsMercadoria(BigDecimal psMercadoria) {
		this.psMercadoria = psMercadoria;
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

	public BigDecimal getVlNotaFiscal() {
		return vlNotaFiscal;
	}

	public void setVlNotaFiscal(BigDecimal vlNotaFiscal) {
		this.vlNotaFiscal = vlNotaFiscal;
	}
    
}
