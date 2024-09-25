package com.mercurio.lms.rest.expedicao;

public class ConhecimentoDTO extends DoctoServicoSuggestDTO {

	private static final long serialVersionUID = 1L;
	
	private Long nrConhecimento;
	
	private Integer dvConhecimento;
	
	public Long getNrConhecimento() {
		return nrConhecimento;
	}

	public void setNrConhecimento(Long nrConhecimento) {
		this.nrConhecimento = nrConhecimento;
	}

	public Integer getDvConhecimento() {
		return dvConhecimento;
	}

	public void setDvConhecimento(Integer dvConhecimento) {
		this.dvConhecimento = dvConhecimento;
	}
	
}