package com.mercurio.lms.franqueados.model.service.calculo;

import java.math.BigDecimal;

import com.mercurio.lms.franqueados.model.DoctoServicoFranqueado;

public class RecalculoLocalFranqueados extends CalculoLocalFranqueados {

	@Override
	public void executarCalculo() {
		super.executarCalculo();
		DoctoServicoFranqueado doc = getDoctoServicoFranqueado();
		doc.setVlDiferencaParticipacao(calcularDiferencaParticipacao(doc, doc.getDoctoServicoFranqueadoOriginal()));
		if(getDoctoServicoFranqueado().getVlDiferencaParticipacao().compareTo(BigDecimal.ZERO) == 0){
			clearDoctoServicoFranqueado();
		}
	}

	private BigDecimal calcularDiferencaParticipacao(DoctoServicoFranqueado doctoServicoFranqueado,
			DoctoServicoFranqueado doctoServicoFranqueadoAnterior) {
		return doctoServicoFranqueadoAnterior.getVlParticipacao().subtract(doctoServicoFranqueado.getVlParticipacao());
	}
}
