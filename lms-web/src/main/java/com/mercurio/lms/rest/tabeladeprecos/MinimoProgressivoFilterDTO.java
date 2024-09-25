package com.mercurio.lms.rest.tabeladeprecos;

import com.mercurio.adsm.rest.BaseFilterDTO;

public class MinimoProgressivoFilterDTO extends BaseFilterDTO {
	
	private static final long serialVersionUID = 1L;
	
	private Long idTabela;
	
	public Long getIdTabela() {
		return idTabela;
	}
	
	public void setIdTabela(Long idTabela) {
		this.idTabela = idTabela;
	}

}
