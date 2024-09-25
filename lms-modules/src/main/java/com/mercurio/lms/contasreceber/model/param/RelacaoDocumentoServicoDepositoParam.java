package com.mercurio.lms.contasreceber.model.param;

import java.util.List;

import org.joda.time.YearMonthDay;



public class RelacaoDocumentoServicoDepositoParam {

	private YearMonthDay dtEmissaoInicial;
	
	private YearMonthDay dtEmissaoFinal;
	
	private Long nrDoctoServicoInicial;
	
	private Long nrDoctoServicoFinal;
	
	private Long idCliente;
	
	private String tpDoctoServico;
	
	private List lstTpSituacaoDocumento;
	
	private List lstDevedores;

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

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
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

	public List getLstDevedores() {
		return lstDevedores;
	}

	public void setLstDevedores(List lstDevedores) {
		this.lstDevedores = lstDevedores;
	}

	public List getLstTpSituacaoDocumento() {
		return lstTpSituacaoDocumento;
	}

	public void setLstTpSituacaoDocumento(List lstTpSituacaoDocumento) {
		this.lstTpSituacaoDocumento = lstTpSituacaoDocumento;
	}

	public String getTpDoctoServico() {
		return tpDoctoServico;
	}

	public void setTpDoctoServico(String tpDoctoServico) {
		this.tpDoctoServico = tpDoctoServico;
	} 
}
