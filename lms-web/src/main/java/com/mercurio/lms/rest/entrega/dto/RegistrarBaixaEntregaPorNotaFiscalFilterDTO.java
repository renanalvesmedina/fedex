package com.mercurio.lms.rest.entrega.dto;

import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.carregamento.dto.ControleCargaSuggestDTO;
import com.mercurio.lms.rest.expedicao.DoctoServicoSuggestDTO;
import com.mercurio.lms.rest.expedicao.dto.ManifestoViagemNacionalSuggestDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;

public class RegistrarBaixaEntregaPorNotaFiscalFilterDTO extends BaseFilterDTO {
	private static final long serialVersionUID = 1L;

	private Long idDoctoServico;
	private Long idManifesto;
	private FilialSuggestDTO filial;
	private ManifestoEntregaSuggestDTO manifestoEntrega;
	private DoctoServicoSuggestDTO doctoServico;
	private ManifestoViagemNacionalSuggestDTO manifestoViagemNacional;
	private ControleCargaSuggestDTO controleCarga;
	private String alteracao;
	
	public FilialSuggestDTO getFilial() {
		return filial;
	}
	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}
	public ManifestoEntregaSuggestDTO getManifestoEntrega() {
		return manifestoEntrega;
	}
	public void setManifestoEntrega(ManifestoEntregaSuggestDTO manifestoEntrega) {
		this.manifestoEntrega = manifestoEntrega;
	}
	public DoctoServicoSuggestDTO getDoctoServico() {
		return doctoServico;
	}
	public void setDoctoServico(DoctoServicoSuggestDTO doctoServico) {
		this.doctoServico = doctoServico;
	}
	public ControleCargaSuggestDTO getControleCarga() {
		return controleCarga;
	}
	public void setControleCarga(ControleCargaSuggestDTO controleCarga) {
		this.controleCarga = controleCarga;
	}
	public ManifestoViagemNacionalSuggestDTO getManifestoViagemNacional() {
		return manifestoViagemNacional;
	}
	public void setManifestoViagemNacional(
			ManifestoViagemNacionalSuggestDTO manifestoViagemNacional) {
		this.manifestoViagemNacional = manifestoViagemNacional;
	}
	public String getAlteracao() {
		return alteracao;
	}
	public void setAlteracao(String alteracao) {
		this.alteracao = alteracao;
	}
	public Long getIdManifesto() {
		return idManifesto;
	}
	public void setIdManifesto(Long idManifesto) {
		this.idManifesto = idManifesto;
	}
	public Long getIdDoctoServico() {
		return idDoctoServico;
	}
	public void setIdDoctoServico(Long idDoctoServico) {
		this.idDoctoServico = idDoctoServico;
	}
	
}
