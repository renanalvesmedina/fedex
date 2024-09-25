package com.mercurio.lms.contasreceber.model.param;

import org.joda.time.DateTime;

public class LinhaSqlFaturamentoParam {
	
	private long idDevedorDocServFat;
	
	private long idCliente;
	
	private long idFilial;
	
	private long idDivisaoCliente;
	
	private char tpModal;
	
	private char tpAbrangencia;
	
	private String tpDocumentoServico;
	
	private long idMoeda;
	
	private char tpFrete;
	
	private long idServico;
	
	private DateTime dhEmissao;
	
	private String tpCobranca;
	
	private long idFilialCobranca;
	
	private long idCedente;
	
	private long nrQteDocRomaneio;
	
	private boolean blAgrupaFaturamentoMes;
	
	private Object[] lstAgrupamento;

	public Object[] getLstAgrupamento() {
		return lstAgrupamento;
	}

	public void setLstAgrupamento(Object[] lstAgrupamento) {
		this.lstAgrupamento = lstAgrupamento;
	}

	public void setBlAgrupaFaturamentoMes(boolean blAgrupaFaturamentoMes) {
		this.blAgrupaFaturamentoMes = blAgrupaFaturamentoMes;
	}

	public Boolean getBlAgrupaFaturamentoMes() {
		return blAgrupaFaturamentoMes;
	}

	public void setBlAgrupaFaturamentoMes(Boolean blAgrupaFaturamentoMes) {
		this.blAgrupaFaturamentoMes = blAgrupaFaturamentoMes;
	}

	public DateTime getDhEmissao() {
		return dhEmissao;
	}

	public void setDhEmissao(DateTime dhEmissao) {
		this.dhEmissao = dhEmissao;
	}

	public long getIdCedente() {
		return idCedente;
	}

	public void setIdCedente(long idCedente) {
		this.idCedente = idCedente;
	}

	public long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(long idCliente) {
		this.idCliente = idCliente;
	}

	public long getIdDevedorDocServFat() {
		return idDevedorDocServFat;
	}

	public void setIdDevedorDocServFat(long idDevedorDocServFat) {
		this.idDevedorDocServFat = idDevedorDocServFat;
	}

	public long getIdDivisaoCliente() {
		return idDivisaoCliente;
	}

	public void setIdDivisaoCliente(long idDivisaoCliente) {
		this.idDivisaoCliente = idDivisaoCliente;
	}

	public long getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(long idFilial) {
		this.idFilial = idFilial;
	}

	public long getIdFilialCobranca() {
		return idFilialCobranca;
	}

	public void setIdFilialCobranca(long idFilialCobranca) {
		this.idFilialCobranca = idFilialCobranca;
	}

	public long getIdMoeda() {
		return idMoeda;
	}

	public void setIdMoeda(long idMoeda) {
		this.idMoeda = idMoeda;
	}

	public long getIdServico() {
		return idServico;
	}

	public void setIdServico(long idServico) {
		this.idServico = idServico;
	}

	public long getNrQteDocRomaneio() {
		return nrQteDocRomaneio;
	}

	public void setNrQteDocRomaneio(long nrQteDocRomaneio) {
		this.nrQteDocRomaneio = nrQteDocRomaneio;
	}

	public char getTpAbrangencia() {
		return tpAbrangencia;
	}

	public void setTpAbrangencia(char tpAbrangencia) {
		this.tpAbrangencia = tpAbrangencia;
	}

	public String getTpCobranca() {
		return tpCobranca;
	}

	public void setTpCobranca(String tpCobranca) {
		this.tpCobranca = tpCobranca;
	}

	public String getTpDocumentoServico() {
		return tpDocumentoServico;
	}

	public void setTpDocumentoServico(String tpDocumentoServico) {
		this.tpDocumentoServico = tpDocumentoServico;
	}

	public char getTpFrete() {
		return tpFrete;
	}

	public void setTpFrete(char tpFrete) {
		this.tpFrete = tpFrete;
	}

	public char getTpModal() {
		return tpModal;
	}

	public void setTpModal(char tpModal) {
		this.tpModal = tpModal;
	}
	
	
}