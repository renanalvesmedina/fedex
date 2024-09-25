package com.mercurio.lms.fretecarreteirocoletaentrega.dto;

import com.mercurio.lms.coleta.model.ManifestoColeta;

public class ManifestoColetaControleCargaNotaCreditoDto {
	
	private String sgFilial;
	private Integer nrManifestoColeta;
	private Boolean isManifestoForaNota = false;
	
	public ManifestoColetaControleCargaNotaCreditoDto(ManifestoColeta manifestoColeta) {
		sgFilial = manifestoColeta.getFilial().getSgFilial();
		nrManifestoColeta = manifestoColeta.getNrManifesto();
	}
	
	public String getSgFilial() {
		return sgFilial;
	}

	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}

	public Integer getNrManifestoColeta() {
		return nrManifestoColeta;
	}

	public void setNrManifestoColeta(Integer nrManifestoColeta) {
		this.nrManifestoColeta = nrManifestoColeta;
	}

	public String getFormattedManifesto() {
		return sgFilial + " " + nrManifestoColeta;
	}

	public Boolean getIsManifestoForaNota() {
		return isManifestoForaNota;
	}

	public void setIsManifestoForaNota(Boolean isManifestoForaNota) {
		this.isManifestoForaNota = isManifestoForaNota;
	}

}
