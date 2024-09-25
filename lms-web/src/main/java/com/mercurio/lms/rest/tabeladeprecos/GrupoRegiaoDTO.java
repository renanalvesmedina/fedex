package com.mercurio.lms.rest.tabeladeprecos;

import com.mercurio.adsm.rest.BaseDTO;


public class GrupoRegiaoDTO extends BaseDTO {
	
	private static final long serialVersionUID = 1L;
	
	private Long idGrupoRegiao;
	private String dsGrupoRegiao;
	
	public GrupoRegiaoDTO() {
		
	}

	public GrupoRegiaoDTO(Long idGrupoRegiao, String dsGrupoRegiao) {
		super();
		this.idGrupoRegiao = idGrupoRegiao;
		this.dsGrupoRegiao = dsGrupoRegiao;
	}

	public Long getIdGrupoRegiao() {
		return idGrupoRegiao;
	}

	public void setIdGrupoRegiao(Long idGrupoRegiao) {
		this.idGrupoRegiao = idGrupoRegiao;
	}

	public String getDsGrupoRegiao() {
		return dsGrupoRegiao;
	}

	public void setDsGrupoRegiao(String dsGrupoRegiao) {
		this.dsGrupoRegiao = dsGrupoRegiao;
	}

	
	

}
