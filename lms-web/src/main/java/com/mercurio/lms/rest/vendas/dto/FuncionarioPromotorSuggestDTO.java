package com.mercurio.lms.rest.vendas.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class FuncionarioPromotorSuggestDTO extends BaseDTO {


	private static final long serialVersionUID = 8814542679778782493L;

	private Long idUsuario;
	private String nmUsuario;
	private String nrMatricula;
	private String nrCpf;

	public FuncionarioPromotorSuggestDTO(){
	}
	
	public FuncionarioPromotorSuggestDTO(Long idUsuario, String nmUsuario,	String nrMatricula) {
		this.idUsuario = idUsuario;
		this.nmUsuario = nmUsuario;
		this.nrMatricula = nrMatricula;
	}

	public FuncionarioPromotorSuggestDTO(Long idUsuario, String nmUsuario,	String nrMatricula, String nrCpf) {
		this.idUsuario = idUsuario;
		this.nmUsuario = nmUsuario;
		this.nrMatricula = nrMatricula;
		this.nrCpf = nrCpf;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNmUsuario() {
		return nmUsuario;
	}

	public void setNmUsuario(String nmUsuario) {
		this.nmUsuario = nmUsuario;
	}

	public String getNrMatricula() {
		return nrMatricula;
	}

	public void setNrMatricula(String nrMatricula) {
		this.nrMatricula = nrMatricula;
	}

	public String getNrCpf() {
		return nrCpf;
	}

	public void setNrCpf(String nrCpf) {
		this.nrCpf = nrCpf;
	}

}
