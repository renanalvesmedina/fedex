package com.mercurio.lms.edi.model;

import java.util.List;

public class EDI945File {
	private Long id;
	//arquivo
	private String arquivo;
	//ISA
	private String interchange;
	//GS
	private String functional;
	//dnns
	private List<Edi945dnn> dnns; 
	//Todos os Danfes Recebidos
	private boolean todosDanfesRecebidos;
	
	private String path; 
	
	private boolean generated997;
	
	private Long nrTransacao;
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setInterchange(String interchange) {
		this.interchange = interchange;
	}

	public String getInterchange() {
		return interchange;
	}

	public void setFunctional(String functional) {
		this.functional = functional;
	}

	public String getFunctional() {
		return functional;
	}

	public void setArquivo(String arquivo) {
		this.arquivo = arquivo;
	}

	public String getArquivo() {
		return arquivo;
	}

	public void setDnns(List<Edi945dnn> dnns) {
		this.dnns = dnns;
	}

	public List<Edi945dnn> getDnns() {
		return dnns;
	}

	public void setTodosDanfesRecebidos(boolean todosDanfesRecebidos) {
		this.todosDanfesRecebidos = todosDanfesRecebidos;
	}

	public boolean isTodosDanfesRecebidos() {
		return todosDanfesRecebidos;
	}

	public void setGenerate997(boolean generated997) {
		this.generated997 = generated997;
	}

	public boolean isGenerated997() {
		return generated997;
	}

	public void setNrTransacao(Long nrTransacao) {
		this.nrTransacao = nrTransacao;
	}

	public Long getNrTransacao() {
		return nrTransacao;
	}

}
