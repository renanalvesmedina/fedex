package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

public class Frete implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private CalculoFrete calculoFrete;
	private Conhecimento conhecimento;
	private RecalculoFrete recalculoFrete;
	
	private CalculoNFServico  calculoNFServico;
	private NotaFiscalServico notaFiscalServico;
	
	private String tpFrete;
	
	public CalculoFrete getCalculoFrete() {
		return calculoFrete;
	}

	public void setCalculoFrete(CalculoFrete calculoFrete) {
		this.calculoFrete = calculoFrete;
	}

	public Conhecimento getConhecimento() {
		return conhecimento;
	}

	public void setConhecimento(Conhecimento conhecimento) {
		this.conhecimento = conhecimento;
	}

	public CalculoNFServico getCalculoNFServico() {
		return calculoNFServico;
	}

	public void setCalculoNFServico(CalculoNFServico calculoNFServico) {
		this.calculoNFServico = calculoNFServico;
	}

	public NotaFiscalServico getNotaFiscalServico() {
		return notaFiscalServico;
	}

	public void setNotaFiscalServico(NotaFiscalServico notaFiscalServico) {
		this.notaFiscalServico = notaFiscalServico;
	}

	public String getTpFrete() {
		return tpFrete;
	}

	public void setTpFrete(String tpFrete) {
		this.tpFrete = tpFrete;
	}

	public RecalculoFrete getRecalculoFrete() {
		return recalculoFrete;
	}

	public void setRecalculoFrete(RecalculoFrete recalculoFrete) {
		this.recalculoFrete = recalculoFrete;
	}
}