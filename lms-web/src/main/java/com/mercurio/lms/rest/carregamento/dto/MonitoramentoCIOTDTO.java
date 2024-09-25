package com.mercurio.lms.rest.carregamento.dto;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;

public class MonitoramentoCIOTDTO extends BaseDTO {
	private static final long serialVersionUID = 1L;

	private Long idCiot;
	private Long idControleCarga;
	private Long nrControleCarga;
	
	private DomainValue tpStatusControleCarga;
	private String sgFilialControleCarga;
	private String dsProprietario;
	private String nrFrota;
	private String nrIdentificador;
	private Long nrCIOT;
	private DateTime dhGeracao;
	private String nrCodigoVerificador;
	private DomainValue tpSituacao;
	private String dsObservacao;
	private BigDecimal valor;

	public Long getNrControleCarga() {
		return nrControleCarga;
	}

	public void setNrControleCarga(Long nrControleCarga) {
		this.nrControleCarga = nrControleCarga;
	}

	public String getSgFilialControleCarga() {
		return sgFilialControleCarga;
	}

	public void setSgFilialControleCarga(String sgFilialControleCarga) {
		this.sgFilialControleCarga = sgFilialControleCarga;
	}

	public String getDsProprietario() {
		return dsProprietario;
	}

	public void setDsProprietario(String dsProprietario) {
		this.dsProprietario = dsProprietario;
	}

	public String getNrFrota() {
		return nrFrota;
	}

	public void setNrFrota(String nrFrota) {
		this.nrFrota = nrFrota;
	}

	public String getNrIdentificador() {
		return nrIdentificador;
	}

	public void setNrIdentificador(String nrIdentificador) {
		this.nrIdentificador = nrIdentificador;
	}

	public Long getNrCIOT() {
		return nrCIOT;
	}

	public void setNrCIOT(Long nrCIOT) {
		this.nrCIOT = nrCIOT;
	}

	public String getNrCodigoVerificador() {
		return nrCodigoVerificador;
	}

	public void setNrCodigoVerificador(String nrCodigoVerificador) {
		this.nrCodigoVerificador = nrCodigoVerificador;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public String getDsObservacao() {
		return dsObservacao;
	}

	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}

	public Long getIdControleCarga() {
		return idControleCarga;
	}

	public void setIdControleCarga(Long idControleCarga) {
		this.idControleCarga = idControleCarga;
	}

	public DateTime getDhGeracao() {
		return dhGeracao;
	}

	public void setDhGeracao(DateTime dhGeracao) {
		this.dhGeracao = dhGeracao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Long getIdCiot() {
		return idCiot;
	}

	public void setIdCiot(Long idCiot) {
		this.idCiot = idCiot;
	}

	public DomainValue getTpStatusControleCarga() {
		return tpStatusControleCarga;
	}

	public void setTpStatusControleCarga(DomainValue tpStatusControleCarga) {
		this.tpStatusControleCarga = tpStatusControleCarga;
	}
}
