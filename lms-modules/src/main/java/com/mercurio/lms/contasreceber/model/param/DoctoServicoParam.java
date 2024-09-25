package com.mercurio.lms.contasreceber.model.param;

import java.util.List;

import org.joda.time.YearMonthDay;

public class DoctoServicoParam {
	private Long nrNotaFiscalInicial;
	
	private Long nrNotaFiscalFinal;
	
	private Long nrDoctoServicoInicial;
	
	private Long nrDoctoServicoFinal;	
	
	private Long idCliente;
	
	private Long idDoctoServico;
	
	private Long idFilialorigem;
	
	private Long idDivisaoCliente;
	
	private String tpDocumentoServico;
	
	private String tpFrete;	
	
	private String tpModal;
	
	private String tpAbrangencia;
	
	private Long idServico;
	
	private YearMonthDay dtEmissaoInicial;
	
	private YearMonthDay dtEmissaoFinal;
	
	private List idDevedores;

	public List getIdDevedores() {
		return idDevedores;
	}

	public void setIdDevedores(List idDevedores) {
		this.idDevedores = idDevedores;
	}

	public String getTpDocumentoServico() {
		return tpDocumentoServico;
	}

	public void setTpDocumentoServico(String tpDocumentoServico) {
		this.tpDocumentoServico = tpDocumentoServico;
	}

	public YearMonthDay getDtEmissaoFinal() {
		return dtEmissaoFinal;
	}

	public void setDtEmissaoFinal(YearMonthDay dtEmissaoFinal) {
		this.dtEmissaoFinal = dtEmissaoFinal;
	}

	public YearMonthDay getDtEmissaoInicial() {
		return dtEmissaoInicial;
	}

	public void setDtEmissaoInicial(YearMonthDay dtEmissaoInicial) {
		this.dtEmissaoInicial = dtEmissaoInicial;
	}

	public String getTpFrete() {
		return tpFrete;
	}

	public void setTpFrete(String tpFrete) {
		this.tpFrete = tpFrete;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public Long getIdDoctoServico() {
		return idDoctoServico;
	}

	public void setIdDoctoServico(Long idDoctoServico) {
		this.idDoctoServico = idDoctoServico;
	}

	public Long getIdFilialorigem() {
		return idFilialorigem;
	}

	public void setIdFilialorigem(Long idFilialorigem) {
		this.idFilialorigem = idFilialorigem;
	}

	public Long getNrNotaFiscalFinal() {
		return nrNotaFiscalFinal;
	}

	public void setNrNotaFiscalFinal(Long nrNotaFiscalFinal) {
		this.nrNotaFiscalFinal = nrNotaFiscalFinal;
	}

	public Long getNrNotaFiscalInicial() {
		return nrNotaFiscalInicial;
	}

	public void setNrNotaFiscalInicial(Long nrNotaFiscalInicial) {
		this.nrNotaFiscalInicial = nrNotaFiscalInicial;
	}

	public Long getIdServico() {
		return idServico;
	}

	public void setIdServico(Long idServico) {
		this.idServico = idServico;
	}

	public String getTpAbrangencia() {
		return tpAbrangencia;
	}

	public void setTpAbrangencia(String tpAbrangencia) {
		this.tpAbrangencia = tpAbrangencia;
	}

	public String getTpModal() {
		return tpModal;
	}

	public void setTpModal(String tpModal) {
		this.tpModal = tpModal;
	}

	public Long getNrDoctoServicoFinal() {
		return nrDoctoServicoFinal;
	}

	public void setNrDoctoServicoFinal(Long nrDoctoServicoFinal) {
		this.nrDoctoServicoFinal = nrDoctoServicoFinal;
	}

	public Long getNrDoctoServicoInicial() {
		return nrDoctoServicoInicial;
	}

	public void setNrDoctoServicoInicial(Long nrDoctoServicoInicial) {
		this.nrDoctoServicoInicial = nrDoctoServicoInicial;
	}

	public Long getIdDivisaoCliente() {
		return idDivisaoCliente;
	}

	public void setIdDivisaoCliente(Long idDivisaoCliente) {
		this.idDivisaoCliente = idDivisaoCliente;
	}
	
	
	
}
