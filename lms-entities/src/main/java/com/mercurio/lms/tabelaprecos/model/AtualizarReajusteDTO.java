package com.mercurio.lms.tabelaprecos.model;

import java.math.BigDecimal;

public class AtualizarReajusteDTO {
	private Long idTabelaDivisao;
	private Long idTabelaBase;
	private Long idTabelaNova;
	private BigDecimal pcReajusteGeral;
	private Long idTabelaFob;
	
	public Long getIdTabelaDivisao() {
		return idTabelaDivisao;
	}
	public void setIdTabelaDivisao(Long idTabelaDivisao) {
		this.idTabelaDivisao = idTabelaDivisao;
	}
	public Long getIdTabelaBase() {
		return idTabelaBase;
	}
	public void setIdTabelaBase(Long idTabelaBase) {
		this.idTabelaBase = idTabelaBase;
	}
	public Long getIdTabelaNova() {
		return idTabelaNova;
	}
	public void setIdTabelaNova(Long idTabelaNova) {
		this.idTabelaNova = idTabelaNova;
	}
	public BigDecimal getPcReajusteGeral() {
		return pcReajusteGeral;
	}
	public void setPcReajusteGeral(BigDecimal pcReajusteGeral) {
		this.pcReajusteGeral = pcReajusteGeral;
	}
	public Long getIdTabelaFob() {
		return idTabelaFob;
	}
	public void setIdTabelaFob(Long idTabelaFob) {
		this.idTabelaFob = idTabelaFob;
	}
	
	

}
