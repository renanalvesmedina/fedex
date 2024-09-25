package com.mercurio.lms.expedicao.model;

import java.util.List;

public class ListDoctosServicoManifesto {

	private List ctosInternacionais;
	private List mdas;
	private List conhecimentos;
	private List recibosReembolso;
	private List notasFiscaisServico;
	private List notasFiscaisTransporte;

	public List getConhecimentos() {
		return conhecimentos;
	}

	public void setConhecimentos(List conhecimentos) {
		this.conhecimentos = conhecimentos;
	}

	public List getCtosInternacionais() {
		return ctosInternacionais;
	}

	public void setCtosInternacionais(List ctosInternacionais) {
		this.ctosInternacionais = ctosInternacionais;
	}

	public List getMdas() {
		return mdas;
	}

	public void setMdas(List mdas) {
		this.mdas = mdas;
	}

	public List getRecibosReembolso() {
		return recibosReembolso;
	}

	public void setRecibosReembolso(List recibosReembolso) {
		this.recibosReembolso = recibosReembolso;
	}

	public List getNotasFiscaisServico() {
		return notasFiscaisServico;
	}

	public void setNotasFiscaisServico(List notasFiscaisServico) {
		this.notasFiscaisServico = notasFiscaisServico;
	}

	public List getNotasFiscaisTransporte() {
		return notasFiscaisTransporte;
	}

	public void setNotasFiscaisTransporte(List notasFiscaisTransporte) {
		this.notasFiscaisTransporte = notasFiscaisTransporte;
	}
	
}
