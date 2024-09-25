package com.mercurio.lms.contasreceber.model.param;


public class LinhaSqlGroupByFaturamentoParam {
	
	private String tpCampo;
	
	private long idInformacaoDocServico;
	
	private long idInformacaoDoctoCliente;

	public long getIdInformacaoDocServico() {
		return idInformacaoDocServico;
	}

	public void setIdInformacaoDocServico(long idInformacaoDocServico) {
		this.idInformacaoDocServico = idInformacaoDocServico;
	}

	public long getIdInformacaoDoctoCliente() {
		return idInformacaoDoctoCliente;
	}

	public void setIdInformacaoDoctoCliente(long idInformacaoDoctoCliente) {
		this.idInformacaoDoctoCliente = idInformacaoDoctoCliente;
	}

	public String getTpCampo() {
		return tpCampo;
	}

	public void setTpCampo(String tpCampo) {
		this.tpCampo = tpCampo;
	}

}