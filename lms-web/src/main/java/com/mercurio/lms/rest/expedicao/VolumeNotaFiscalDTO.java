package com.mercurio.lms.rest.expedicao;

import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.sim.LocalizacaoMercadoriaDTO;

public class VolumeNotaFiscalDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private String nrVolumeEmbarque;
	
	private Integer nrSequencia;
	
	private ConhecimentoDTO conhecimento;
	
	private FilialSuggestDTO filialLocalizacao;
	
	private LocalizacaoMercadoriaDTO localizacaoMercadoria;

	public String getNrVolumeEmbarque() {
		return nrVolumeEmbarque;
	}

	public void setNrVolumeEmbarque(String nrVolumeEmbarque) {
		this.nrVolumeEmbarque = nrVolumeEmbarque;
	}

	public Integer getNrSequencia() {
		return nrSequencia;
	}

	public void setNrSequencia(Integer nrSequencia) {
		this.nrSequencia = nrSequencia;
	}

	public ConhecimentoDTO getConhecimento() {
		return conhecimento;
	}

	public void setConhecimento(ConhecimentoDTO conhecimento) {
		this.conhecimento = conhecimento;
	}

	public FilialSuggestDTO getFilialLocalizacao() {
		return filialLocalizacao;
	}

	public void setFilialLocalizacao(FilialSuggestDTO filialLocalizacao) {
		this.filialLocalizacao = filialLocalizacao;
	}

	public LocalizacaoMercadoriaDTO getLocalizacaoMercadoria() {
		return localizacaoMercadoria;
	}

	public void setLocalizacaoMercadoria(
			LocalizacaoMercadoriaDTO localizacaoMercadoria) {
		this.localizacaoMercadoria = localizacaoMercadoria;
	}
}