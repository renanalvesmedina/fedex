package com.mercurio.lms.franqueados.model.service.calculo;

import java.math.BigDecimal;

public class RecalculoServicoAdicionalFranqueados extends CalculoServicoAdicionalFranqueados{

	@Override
	public void executarCalculo() {
		super.executarCalculo();
		getDoctoServicoFranqueado().setVlDiferencaParticipacao(calcularDiferencaParticipacao(getDoctoServicoFranqueado().getDoctoServicoFranqueadoOriginal().getVlParticipacao(),getDoctoServicoFranqueado().getVlParticipacao()));
		
		if(getDoctoServicoFranqueado().getVlDiferencaParticipacao().compareTo(BigDecimal.ZERO) == 0){
			clearDoctoServicoFranqueado();
		}
		
	}

	private BigDecimal calcularDiferencaParticipacao(BigDecimal vlParticipacaoOriginal, BigDecimal vlParticipacao) {
		return vlParticipacao.subtract(vlParticipacao);
	}

}
