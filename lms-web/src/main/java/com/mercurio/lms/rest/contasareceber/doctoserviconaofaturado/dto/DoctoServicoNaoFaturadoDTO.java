package com.mercurio.lms.rest.contasareceber.doctoserviconaofaturado.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class DoctoServicoNaoFaturadoDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private String vlDevido;
	private String filialCobranca;
	private String filialResp;
	private String devedor;
	private String dtEntrega;
	private String tpConhecimento;
	private String tpFrete;
	private String dtEmissao;
	private String tpDocumento;
	private String doctoServico;
	private String chaveCTE;

	public String getVlDevido() {
		return vlDevido;
	}

	public void setVlDevido(String vlDevido) {
		this.vlDevido = vlDevido;
	}

	public String getFilialCobranca() {
		return filialCobranca;
	}

	public void setFilialCobranca(String filialCobranca) {
		this.filialCobranca = filialCobranca;
	}

	public String getFilialResp() {
		return filialResp;
	}

	public void setFilialResp(String filialResp) {
		this.filialResp = filialResp;
	}

	public String getDevedor() {
		return devedor;
	}

	public void setDevedor(String devedor) {
		this.devedor = devedor;
	}

	public String getDtEntrega() {
		return dtEntrega;
	}

	public void setDtEntrega(String dtEntrega) {
		this.dtEntrega = dtEntrega;
	}

	public String getTpConhecimento() {
		return tpConhecimento;
	}

	public void setTpConhecimento(String tpConhecimento) {
		this.tpConhecimento = tpConhecimento;
	}

	public String getTpFrete() {
		return tpFrete;
	}

	public void setTpFrete(String tpFrete) {
		this.tpFrete = tpFrete;
	}

	public String getDtEmissao() {
		return dtEmissao;
	}

	public void setDtEmissao(String dtEmissao) {
		this.dtEmissao = dtEmissao;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setTpDocumento(String tpDocumento) {
		this.tpDocumento = tpDocumento;
	}

	public String getTpDocumento() {
		return tpDocumento;
	}
	
	public void setDoctoServico(String doctoServico) {
		this.doctoServico = doctoServico;
	}
	
	public String getDoctoServico() {
		return doctoServico;
	}

	public String getChaveCTE() {
		return chaveCTE;
	}

	public void setChaveCTE(String chaveCTE) {
		this.chaveCTE = chaveCTE;
	}

}
