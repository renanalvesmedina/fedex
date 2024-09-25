package com.mercurio.lms.rest.configuracoes;

import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.configuracoes.model.Usuario;

public class UsuarioDTO extends BaseDTO {

	private static final long serialVersionUID = 2L;

	private String nmUsuario;
	private String nrMatricula;
	private String login;
	private String nrCpf;

	public UsuarioDTO() {
		super();
	}

	public UsuarioDTO(Long idUsuario, String nmUsuario, String nrMatricula) {
		this();
		setId(idUsuario);
		this.nmUsuario = nmUsuario;
		this.nrMatricula = nrMatricula;
	}

	public UsuarioDTO(Long idUsuario, String nmUsuario, String nrMatricula, String login) {
		this(idUsuario, nmUsuario, nrMatricula);
		this.login = login;
	}

	public UsuarioDTO(Long idUsuario, String nmUsuario, String nrMatricula, String login, String nrCpf) {
		this(idUsuario, nmUsuario, nrMatricula, login);
		this.nrCpf = nrCpf;
	}

	public Long getIdUsuario() {
		return getId();
	}

	public void setIdUsuario(Long idUsuario) {
		setId(idUsuario);
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

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNrCpf() {
		return nrCpf;
	}

	public void setNrCpf(String nrCpf) {
		this.nrCpf = nrCpf;
	}

	public Usuario build() {
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(getIdUsuario());
		usuario.setNmUsuario(getNmUsuario());
		usuario.setNrMatricula(getNrMatricula());
		usuario.setLogin(getLogin());
		usuario.getVfuncionario().setNrCpf(getNrCpf());
		return usuario;
	}

}
