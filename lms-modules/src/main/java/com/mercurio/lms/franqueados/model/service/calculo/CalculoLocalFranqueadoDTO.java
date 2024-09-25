package com.mercurio.lms.franqueados.model.service.calculo;

public class CalculoLocalFranqueadoDTO extends ConhecimentoFranqueadoDTO{

	private static final long serialVersionUID = 1L;

	@Override
	public CalculoLocalFranqueados createCalculo() {
		return new CalculoLocalFranqueados();
	}

	
}
