package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import com.mercurio.lms.expedicao.model.Conhecimento;

public class ControleCargaConhScan implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long idControleCargaConhScan;
	
	private ControleCarga controleCarga;
	
	private Conhecimento conhecimento;

	private CarregamentoDescarga carregamentoDescarga;

	public ControleCargaConhScan() {
	}

	public ControleCargaConhScan(Long idControleCargaConhScan, ControleCarga controleCarga, Conhecimento conhecimento, CarregamentoDescarga carregamentoDescarga) {
		this.idControleCargaConhScan = idControleCargaConhScan;
		this.controleCarga = controleCarga;
		this.conhecimento = conhecimento;
		this.carregamentoDescarga = carregamentoDescarga;
	}

	public Long getIdControleCargaConhScan() {
		return idControleCargaConhScan;
	}

	public void setIdControleCargaConhScan(Long idControleCargaConhScan) {
		this.idControleCargaConhScan = idControleCargaConhScan;
	}

	public ControleCarga getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(ControleCarga controleCarga) {
		this.controleCarga = controleCarga;
	}

	public Conhecimento getConhecimento() {
		return conhecimento;
	}

	public void setConhecimento(Conhecimento conhecimento) {
		this.conhecimento = conhecimento;
	}

	public void setCarregamentoDescarga(
			CarregamentoDescarga carregamentoDescarga) {
		this.carregamentoDescarga = carregamentoDescarga;
}

	public CarregamentoDescarga getCarregamentoDescarga() {
		return carregamentoDescarga;
	}
}
