package com.mercurio.lms.franqueados.model.service.calculo;

public class CalculoServicoAdicionalFranqueadoDTO extends ConhecimentoFranqueadoDTO {

	private static final long serialVersionUID = 1L;

	@Override
	public CalculoServicoAdicionalFranqueados createCalculo() {
		return new CalculoServicoAdicionalFranqueados();
	}

}
