package com.mercurio.lms.rest.configuracoes.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class TipoServicoDTO extends BaseDTO{
	private static final long serialVersionUID = 7107956235457178669L;

	private Long idTipoServico;	
    private String dsTipoServico;

    public Long getIdTipoServico() {
		return idTipoServico;
	}
	public void setIdTipoServico(Long idTipoServico) {
		this.idTipoServico = idTipoServico;
	}
	public String getDsTipoServico() {
		return dsTipoServico;
	}
	public void setDsTipoServico(String dsTipoServico) {
		this.dsTipoServico = dsTipoServico;
	}
}
