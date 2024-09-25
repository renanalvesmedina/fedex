package com.mercurio.lms.rest.carregamento.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class MonitoramentoCIOTListDTO extends BaseDTO {
	private static final long serialVersionUID = 1L;

	private Long nrControleCarga;
	private String sgFilialControleCarga;
	private String nrFrota;
	private String nrIdentificador;
	private Long nrCIOT;
	private String nrCodigoVerificador;
	private String tpSituacao;
	private String dsObservacao;

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

	public String getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(String tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public String getDsObservacao() {
		return dsObservacao;
	}

	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}
}
