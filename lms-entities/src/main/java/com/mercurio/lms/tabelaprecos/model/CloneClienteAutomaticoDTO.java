package com.mercurio.lms.tabelaprecos.model;

import org.joda.time.YearMonthDay;

public class CloneClienteAutomaticoDTO {
	private Long idTabelaDivisaoCliente;
	private YearMonthDay dataVigenciaInicial;
	private Long idReajusteTabelaPreco;
	private Long idTabelaNova;
	private String tipo;
	private Long idReajusteDivisaoCliente;
	
	public Long getIdTabelaDivisaoCliente() {
		return idTabelaDivisaoCliente;
	}
	public void setIdTabelaDivisaoCliente(Long idTabelaDivisaoCliente) {
		this.idTabelaDivisaoCliente = idTabelaDivisaoCliente;
	}
	public YearMonthDay getDataVigenciaInicial() {
		return dataVigenciaInicial;
	}
	public void setDataVigenciaInicial(YearMonthDay dataVigenciaInicial) {
		this.dataVigenciaInicial = dataVigenciaInicial;
	}
	public Long getIdReajusteTabelaPreco() {
		return idReajusteTabelaPreco;
	}
	public void setIdReajusteTabelaPreco(Long idReajusteTabelaPreco) {
		this.idReajusteTabelaPreco = idReajusteTabelaPreco;
	}
	public Long getIdTabelaNova() {
		return idTabelaNova;
	}
	public void setIdTabelaNova(Long idTabelaNova) {
		this.idTabelaNova = idTabelaNova;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Long getIdReajusteDivisaoCliente() {
		return idReajusteDivisaoCliente;
	}
	public void setIdReajusteDivisaoCliente(Long idReajusteDivisaoCliente) {
		this.idReajusteDivisaoCliente = idReajusteDivisaoCliente;
	}

}
