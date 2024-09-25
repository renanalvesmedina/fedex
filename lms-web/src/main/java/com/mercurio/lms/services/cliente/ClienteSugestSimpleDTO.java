package com.mercurio.lms.services.cliente;

import java.io.Serializable;

public class ClienteSugestSimpleDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nmPessoa;
	private String nrIdentificacao;
	private Long idFilial;
	private String sgFilial;
	private String nmFantasiaFilial;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public Long getIdFilial() {
		return idFilial;
	}
	public void setIdFilial(Long idFilial) {
		this.idFilial = idFilial;
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
