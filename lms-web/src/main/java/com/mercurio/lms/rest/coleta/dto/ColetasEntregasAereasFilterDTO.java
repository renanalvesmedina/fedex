package com.mercurio.lms.rest.coleta.dto;

import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.carregamento.dto.ControleCargaSuggestDTO;
import com.mercurio.lms.rest.contratacaoveiculos.dto.MeioTransporteSuggestDTO;
import com.mercurio.lms.rest.entrega.dto.ManifestoEntregaSuggestDTO;
import com.mercurio.lms.rest.expedicao.DoctoServicoSuggestDTO;
import com.mercurio.lms.rest.expedicao.dto.AwbSuggestDTO;
 
public class ColetasEntregasAereasFilterDTO extends BaseFilterDTO { 
	private static final long serialVersionUID = 1L; 
 
	private ControleCargaSuggestDTO controleCarga;
	private MeioTransporteSuggestDTO meioTransporte;
	private DoctoServicoSuggestDTO doctoServico;
	private ManifestoColetaSuggestDTO manifestoColeta;
	private ManifestoEntregaSuggestDTO manifestoEntrega;
	private AwbSuggestDTO awb;
	
	public ControleCargaSuggestDTO getControleCarga() {
		return controleCarga;
	}
	public void setControleCarga(ControleCargaSuggestDTO controleCarga) {
		this.controleCarga = controleCarga;
	}
	public MeioTransporteSuggestDTO getMeioTransporte() {
		return meioTransporte;
	}
	public void setMeioTransporte(MeioTransporteSuggestDTO meioTransporte) {
		this.meioTransporte = meioTransporte;
	}
	public DoctoServicoSuggestDTO getDoctoServico() {
		return doctoServico;
	}
	public void setDoctoServico(DoctoServicoSuggestDTO doctoServico) {
		this.doctoServico = doctoServico;
	}
	public ManifestoColetaSuggestDTO getManifestoColeta() {
		return manifestoColeta;
	}
	public void setManifestoColeta(ManifestoColetaSuggestDTO manifestoColeta) {
		this.manifestoColeta = manifestoColeta;
	}
	public ManifestoEntregaSuggestDTO getManifestoEntrega() {
		return manifestoEntrega;
	}
	public void setManifestoEntrega(ManifestoEntregaSuggestDTO manifestoEntrega) {
		this.manifestoEntrega = manifestoEntrega;
	}
	public AwbSuggestDTO getAwb() {
		return awb;
	}
	public void setAwb(AwbSuggestDTO awb) {
		this.awb = awb;
	}
} 
