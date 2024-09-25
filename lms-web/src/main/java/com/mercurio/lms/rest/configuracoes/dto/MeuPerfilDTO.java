package com.mercurio.lms.rest.configuracoes.dto;
 
public class MeuPerfilDTO { 
	private static final long serialVersionUID = 1L; 
	
	private Long idEmpresa;
	private Long idFilial;
	
	public Long getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	public Long getIdFilial() {
		return idFilial;
	}
	public void setIdFilial(Long idFilial) {
		this.idFilial = idFilial;
	}
} 
