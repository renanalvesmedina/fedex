package com.mercurio.lms.configuracoes.model.param;


public class ConsultarUsuarioLMSParam {
	
	private Long idUsuario;
	
	private String nrMatricula;
	
	private String login;
	
	private String nmUsuario;
	
	private String tpCategoriaUsuario;

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
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

	public String getTpCategoriaUsuario() {
		return tpCategoriaUsuario;
	}

	public void setTpCategoriaUsuario(String tpCategoriaUsuario) {
		this.tpCategoriaUsuario = tpCategoriaUsuario;
	}
	
}
