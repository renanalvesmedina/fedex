package com.mercurio.lms.rest.municipios.dto;


public class FilialSuggestFilterDTO {
	private static final long serialVersionUID = 1L;

	private String value;
	private Long idEmpresa;
	private Boolean somenteEmpresaUsuario = Boolean.TRUE;
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	
	public Boolean getSomenteEmpresaUsuario() {
		return somenteEmpresaUsuario;
	}

	public void setSomenteEmpresaUsuario(Boolean somenteEmpresaUsuario) {
		this.somenteEmpresaUsuario = somenteEmpresaUsuario;
	}
}
