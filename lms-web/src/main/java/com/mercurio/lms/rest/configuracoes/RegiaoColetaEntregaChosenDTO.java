package com.mercurio.lms.rest.configuracoes;

import com.mercurio.adsm.rest.BaseDTO;

public class RegiaoColetaEntregaChosenDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idRegiaoColetaEntregaFil;
	private String dsRegiaoColetaEntregaFil;
	
	public RegiaoColetaEntregaChosenDTO() {
		super();
	}

	public RegiaoColetaEntregaChosenDTO(Long idRegiaoColetaEntregaFil,
			String dsRegiaoColetaEntregaFil) {
		super();
		this.idRegiaoColetaEntregaFil = idRegiaoColetaEntregaFil;
		this.dsRegiaoColetaEntregaFil = dsRegiaoColetaEntregaFil;
	}

	public Long getIdRegiaoColetaEntregaFil() {
		return idRegiaoColetaEntregaFil;
	}

	public void setIdRegiaoColetaEntregaFil(Long idRegiaoColetaEntregaFil) {
		this.idRegiaoColetaEntregaFil = idRegiaoColetaEntregaFil;
	}

	public String getDsRegiaoColetaEntregaFil() {
		return dsRegiaoColetaEntregaFil;
	}

	public void setDsRegiaoColetaEntregaFil(String dsRegiaoColetaEntregaFil) {
		this.dsRegiaoColetaEntregaFil = dsRegiaoColetaEntregaFil;
	}

}
