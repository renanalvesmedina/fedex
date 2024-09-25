package com.mercurio.lms.franqueados.model.service.calculo;

public class CalculoReembarqueFranqueadoSimulacaoDTO extends CalculoReembarqueFranqueadoDTO {

	private static final long serialVersionUID = 1L;

	@Override
	public CalculoReembarqueFranqueados createCalculo() {
		return new CalculoReembarqueFranqueadoSimulacao();
	}
}
