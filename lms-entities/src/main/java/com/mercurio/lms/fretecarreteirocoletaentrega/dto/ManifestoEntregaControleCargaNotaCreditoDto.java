package com.mercurio.lms.fretecarreteirocoletaentrega.dto;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.entrega.model.ManifestoEntrega;

public class ManifestoEntregaControleCargaNotaCreditoDto {
	
	private String sgFilial;
	private Integer nrManifestoEntrega;
	private String tpManifestoEntrega;
	private DomainValue tipoManifestoEntrega;
	private Boolean isManifestoForaNota = false;
	
	public ManifestoEntregaControleCargaNotaCreditoDto(ManifestoEntrega manifestoEntrega) {

		sgFilial = manifestoEntrega.getFilial().getSgFilial();
		nrManifestoEntrega = manifestoEntrega.getNrManifestoEntrega();
		tpManifestoEntrega = manifestoEntrega.getManifesto().getTpManifesto() == null ? "" : manifestoEntrega.getManifesto().getTpManifesto().getDescriptionAsString();
		tipoManifestoEntrega = manifestoEntrega.getManifesto().getTpManifestoEntrega();
	}
	
	public String getSgFilial() {
		return sgFilial;
	}

	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}

	public Integer getNrManifestoEntrega() {
		return nrManifestoEntrega;
	}

	public void setNrManifestoEntrega(Integer nrManifestoEntrega) {
		this.nrManifestoEntrega = nrManifestoEntrega;
	}

	public String getTpManifestoEntrega() {
		return tpManifestoEntrega;
	}

	public void setTpManifestoEntrega(String tpManifestoEntrega) {
		this.tpManifestoEntrega = tpManifestoEntrega;
	}
	
	public String getFormattedManifesto() {
		return  sgFilial + " " + nrManifestoEntrega;
	}

	public DomainValue getTipoManifestoEntrega() {
		return tipoManifestoEntrega;
	}

	public void setTipoManifestoEntrega(DomainValue tipoManifestoEntrega) {
		this.tipoManifestoEntrega = tipoManifestoEntrega;
	}

	public Boolean getIsManifestoForaNota() {
		return isManifestoForaNota;
	}

	public void setIsManifestoForaNota(Boolean isManifestoForaNota) {
		this.isManifestoForaNota = isManifestoForaNota;
	}

}
