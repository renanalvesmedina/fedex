package com.mercurio.lms.contasreceber.model;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

public class Email implements Serializable {

	private static final long serialVersionUID = 1L;
	private String pAssunto;
	private String pCorpo;
	private Map<String, File> pAnexos;

	public String getpAssunto() {
		return pAssunto;
	}
	public void setpAssunto(String pAssunto) {
		this.pAssunto = pAssunto;
	}
	public String getpCorpo() {
		return pCorpo;
	}
	public void setpCorpo(String pCorpo) {
		this.pCorpo = pCorpo;
	}
	public Map<String, File> getpAnexos() {
		return pAnexos;
	}
	public void setpAnexos(Map<String, File> pAnexos) {
		this.pAnexos = pAnexos;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
