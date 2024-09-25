package com.mercurio.lms.services.cliente.contato;

import java.io.Serializable;

public class ClienteSugestSimpleDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String nmPessoa;
	private String nrIdentificacao;
	private String sgFilial;
	private String nmFantasiaFilial;
	public String getNmPessoa() {
		return nmPessoa;
	}
	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}
	public String getNrIdentificacao() {
		return nrIdentificacao;
	}
	public void setNrIdentificacao(String nrIdentificacao) {
		this.nrIdentificacao = nrIdentificacao;
	}
	public String getSgFilial() {
		return sgFilial;
	}
	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}
	public String getNmFantasiaFilial() {
		return nmFantasiaFilial;
	}
	public void setNmFantasiaFilial(String nmFantasiaFilial) {
		this.nmFantasiaFilial = nmFantasiaFilial;
	}
	
	
	

}
