package com.mercurio.lms.municipios.dto;

import java.io.Serializable;



public class ContatoDto  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String nmUsuario;
	private String dsEmail;
	private String nrDdd;
	private String nrFone;
	private String login;
	private Object idFilialAtendeComercial;
	
	public String getNmUsuario() {
		return nmUsuario;
	}
	public void setNmUsuario(String nmUsuario) {
		this.nmUsuario = nmUsuario;
	}
	public String getDsEmail() {
		return dsEmail;
	}
	public void setDsEmail(String dsEmail) {
		this.dsEmail = dsEmail;
	}
	public String getNrDdd() {
		return nrDdd;
	}
	public void setNrDdd(String nrDdd) {
		this.nrDdd = nrDdd;
	}
	public String getNrFone() {
		return nrFone;
	}
	public void setNrFone(String nrFone) {
		this.nrFone = nrFone;
	}
	public Object getIdFilialAtendeComercial() {
		return idFilialAtendeComercial;
	}
	public void setIdFilialAtendeComercial(Object idFilialAtendeComercial) {
		this.idFilialAtendeComercial = idFilialAtendeComercial;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	
	
}

