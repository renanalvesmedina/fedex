package com.mercurio.lms.expedicao.model;

import com.mercurio.lms.municipios.model.Filial;

public class ConhecimentoBuilder {

	private Conhecimento conhecimento;
	
	private ConhecimentoBuilder() {
		conhecimento = new Conhecimento();
	}
	
	public static ConhecimentoBuilder novoConhecimento() {
		return new ConhecimentoBuilder();
	}
	
	public ConhecimentoBuilder id(Long idDoctoServico) {
		conhecimento.setIdDoctoServico(idDoctoServico);
		return this;
	}
	
	public ConhecimentoBuilder filialOrigem(Filial filial) {
		conhecimento.setFilialByIdFilialOrigem(filial);
		return this;
	}
	
	public Conhecimento build() {
		return conhecimento;
	}
	
}
