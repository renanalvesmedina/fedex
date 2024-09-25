package com.mercurio.lms.franqueados.model.service.calculo;

import java.math.BigDecimal;

import com.mercurio.lms.franqueados.model.DoctoServicoFranqueado;
import com.mercurio.lms.franqueados.model.FixoFranqueado;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.vendas.model.Cliente;

public class RecalculoNacionalFranqueados extends CalculoNacionalFranqueados {

	@Override
	public void executarCalculo() {
		super.executarCalculo();
		DoctoServicoFranqueado doc = getDoctoServicoFranqueado();
		doc.setVlDiferencaParticipacao(calcularDiferencaParticipacao(doc,doc.getDoctoServicoFranqueadoOriginal()));
		if(getDoctoServicoFranqueado().getVlDiferencaParticipacao().compareTo(BigDecimal.ZERO) == 0){
			clearDoctoServicoFranqueado();
		}
	}

	private BigDecimal calcularDiferencaParticipacao(DoctoServicoFranqueado doctoServicoFranqueado, DoctoServicoFranqueado doctoServicoFranqueadoAnterior) {
		return doctoServicoFranqueadoAnterior.getVlParticipacao().subtract(doctoServicoFranqueado.getVlParticipacao());
	}

	protected FixoFranqueado getFixoFranqueadoVigente(Cliente cliente, Municipio municipio){ 
		return getParametrosFranqueado().findFixoFranqueado(cliente.getIdCliente(),
			municipio.getIdMunicipio(), ((RecalculoNacionalFranqueadoDTO)getDTO()).getDtCompetenciaRecalculo());
	}

}
