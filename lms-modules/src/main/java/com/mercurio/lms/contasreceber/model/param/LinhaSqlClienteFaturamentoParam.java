package com.mercurio.lms.contasreceber.model.param;


public class LinhaSqlClienteFaturamentoParam {
	
	private long idCliente;
	
	private long idDivisaoCliente;
	
	private long idFormaAgrupamento;
	
	private long idAgrupamento;

	public long getIdAgrupamento() {
		return idAgrupamento;
	}

	public void setIdAgrupamento(long idAgrupamento) {
		this.idAgrupamento = idAgrupamento;
	}

	public long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(long idCliente) {
		this.idCliente = idCliente;
	}

	public long getIdDivisaoCliente() {
		return idDivisaoCliente;
	}

	public void setIdDivisaoCliente(long idDivisaoCliente) {
		this.idDivisaoCliente = idDivisaoCliente;
	}

	public long getIdFormaAgrupamento() {
		return idFormaAgrupamento;
	}

	public void setIdFormaAgrupamento(long idFormaAgrupamento) {
		this.idFormaAgrupamento = idFormaAgrupamento;
	}
	
	
}