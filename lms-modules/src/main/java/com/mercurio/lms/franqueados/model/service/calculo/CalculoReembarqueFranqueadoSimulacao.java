package com.mercurio.lms.franqueados.model.service.calculo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.franqueados.model.SimulacaoReembarqueDoctoServicoFranqueado;
import com.mercurio.lms.franqueados.util.FranqueadoUtils;
import com.mercurio.lms.municipios.model.Filial;

public class CalculoReembarqueFranqueadoSimulacao extends CalculoReembarqueFranqueados {
	
	private SimulacaoReembarqueDoctoServicoFranqueado simulacaoReembarqueDoctoServicoFranqueado;
	
	public CalculoReembarqueFranqueadoSimulacao() {
		super();
		simulacaoReembarqueDoctoServicoFranqueado = new SimulacaoReembarqueDoctoServicoFranqueado();
	}

	@Override
	public void executarCalculo() {

		BigDecimal vlToneladaCalculado = BigDecimal.ZERO;
		BigDecimal pesoAferido = BigDecimal.ZERO;
		
		if (getReembarqueFranqueadoDTO().getPsReal() != null){
			 pesoAferido = getReembarqueFranqueadoDTO().getPsReal();
			 vlToneladaCalculado = FranqueadoUtils.calcularTonelada(getParametrosFranqueado().getReembarqueFranqueado().getPcTonelada(), pesoAferido);
		}
		
		Filial filial = new Filial();
		filial.setIdFilial(getIdFranquia());
		simulacaoReembarqueDoctoServicoFranqueado.setFilial(filial);
		
		DoctoServico doctoServico = new DoctoServico();
		doctoServico.setIdDoctoServico( getReembarqueFranqueadoDTO().getIdDoctoServico() );
		simulacaoReembarqueDoctoServicoFranqueado.setConhecimento(doctoServico);
		
		Manifesto manifesto = new Manifesto();
		manifesto.setIdManifesto( getReembarqueFranqueadoDTO().getIdManifesto() );
		simulacaoReembarqueDoctoServicoFranqueado.setManifesto( manifesto );
		
		simulacaoReembarqueDoctoServicoFranqueado.setDtCompetencia( getParametrosFranqueado().getCompetencia().getInicio() );
		simulacaoReembarqueDoctoServicoFranqueado.setPsMercadoria(pesoAferido);
		simulacaoReembarqueDoctoServicoFranqueado.setVlCte(getParametrosFranqueado().getReembarqueFranqueado().getVlCte());
		simulacaoReembarqueDoctoServicoFranqueado.setVlTonelada(vlToneladaCalculado);

	}

	@Override
	public Serializable getDocumentoFranqueado() {
		return simulacaoReembarqueDoctoServicoFranqueado;
	}

}
