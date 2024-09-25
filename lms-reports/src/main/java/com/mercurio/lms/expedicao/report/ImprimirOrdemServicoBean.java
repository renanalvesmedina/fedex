package com.mercurio.lms.expedicao.report;

import java.io.Serializable;

public class ImprimirOrdemServicoBean implements Serializable, Comparable<ImprimirOrdemServicoBean> {

	private static final long serialVersionUID = 1L;
	
	private String dsParcelaPreco;
	private String dtExecucao;
	private String vlTabela;
	private String vlNegociado;
	private String vlCusto;
	private String nrKmRodado;
	private String qtHomem;
	private String qtVolume;
	private String qtPalete;
	private String tpModeloPalete;
	private String blRetornaPalete;
	private String tpEscolta;
	private String dhPeriodoInicial;
	private String dhPeriodoFinal;
	private String dsServico;

	public ImprimirOrdemServicoBean()  {
	}

	public ImprimirOrdemServicoBean(String dsParcelaPreco) {
		super();
		this.dsParcelaPreco = dsParcelaPreco;
	}

	public String getDsParcelaPreco() {
		return dsParcelaPreco;
	}

	public void setDsParcelaPreco(String dsParcelaPreco) {
		this.dsParcelaPreco = dsParcelaPreco;
	}

	public String getDtExecucao() {
		return dtExecucao;
	}

	public void setDtExecucao(String dtExecucao) {
		this.dtExecucao = dtExecucao;
	}

	public String getVlTabela() {
		return vlTabela;
	}

	public void setVlTabela(String vlTabela) {
		this.vlTabela = vlTabela;
	}

	public String getVlNegociado() {
		return vlNegociado;
	}

	public void setVlNegociado(String vlNegociado) {
		this.vlNegociado = vlNegociado;
	}

	public String getVlCusto() {
		return vlCusto;
	}

	public void setVlCusto(String vlCusto) {
		this.vlCusto = vlCusto;
	}

	public String getNrKmRodado() {
		return nrKmRodado;
	}

	public void setNrKmRodado(String nrKmRodado) {
		this.nrKmRodado = nrKmRodado;
	}

	public String getQtHomem() {
		return qtHomem;
	}

	public void setQtHomem(String qtHomem) {
		this.qtHomem = qtHomem;
	}

	public String getQtVolume() {
		return qtVolume;
	}

	public void setQtVolume(String qtVolume) {
		this.qtVolume = qtVolume;
	}

	public String getQtPalete() {
		return qtPalete;
	}

	public void setQtPalete(String qtPalete) {
		this.qtPalete = qtPalete;
	}

	public String getTpModeloPalete() {
		return tpModeloPalete;
	}

	public void setTpModeloPalete(String tpModeloPalete) {
		this.tpModeloPalete = tpModeloPalete;
	}

	public String getBlRetornaPalete() {
		return blRetornaPalete;
	}

	public void setBlRetornaPalete(String blRetornaPalete) {
		this.blRetornaPalete = blRetornaPalete;
	}

	public String getTpEscolta() {
		return tpEscolta;
	}

	public void setTpEscolta(String tpEscolta) {
		this.tpEscolta = tpEscolta;
	}

	public String getDhPeriodoInicial() {
		return dhPeriodoInicial;
	}

	public void setDhPeriodoInicial(String dhPeriodoInicial) {
		this.dhPeriodoInicial = dhPeriodoInicial;
	}

	public String getDhPeriodoFinal() {
		return dhPeriodoFinal;
	}

	public void setDhPeriodoFinal(String dhPeriodoFinal) {
		this.dhPeriodoFinal = dhPeriodoFinal;
	}

	public String getDsServico() {
		return dsServico;
	}

	public void setDsServico(String dsServico) {
		this.dsServico = dsServico;
	}

	@Override
	public int compareTo(ImprimirOrdemServicoBean that) {
		return this.getDsParcelaPreco().compareTo(that.getDsParcelaPreco());
	}

}
