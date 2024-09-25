package com.mercurio.lms.franqueados.model.service.calculo;

import java.io.Serializable;

import com.mercurio.lms.franqueados.model.DoctoServicoFranqueado;
import com.mercurio.lms.franqueados.model.SimulacaoDoctoServicoFranqueado;
import com.mercurio.lms.municipios.model.Filial;

public class CalculoLocalFranqueadoSimulacao extends CalculoLocalFranqueados {

	@Override
	public Serializable getDocumentoFranqueado() {
		return converteDocumento(getDoctoServicoFranqueado());
	}

	private SimulacaoDoctoServicoFranqueado converteDocumento(DoctoServicoFranqueado doctoServicoFranqueado) {
		SimulacaoDoctoServicoFranqueado simulacao = new SimulacaoDoctoServicoFranqueado();
		simulacao.setConhecimento(doctoServicoFranqueado.getConhecimento());
		Filial filia = new Filial();
		filia.setIdFilial(doctoServicoFranqueado.getFranquia().getIdFranquia());
		simulacao.setFilial(filia);
		simulacao.setTpFrete(doctoServicoFranqueado.getTpFrete());
		simulacao.setDtCompetencia(doctoServicoFranqueado.getDtCompetencia());
		simulacao.setVlDoctoServico(doctoServicoFranqueado.getVlDoctoServico());
		simulacao.setVlMercadoria(doctoServicoFranqueado.getVlMercadoria());
		simulacao.setVlIcms(doctoServicoFranqueado.getVlIcms());
		simulacao.setVlPis(doctoServicoFranqueado.getVlPis());
		simulacao.setVlCofins(doctoServicoFranqueado.getVlCofins());
		simulacao.setVlDesconto(doctoServicoFranqueado.getVlDesconto());
		simulacao.setVlGris(doctoServicoFranqueado.getVlGris());
		simulacao.setVlCustoCarreteiro(doctoServicoFranqueado.getVlCustoCarreteiro());
		simulacao.setVlCustoAereo(doctoServicoFranqueado.getVlCustoAereo());
		simulacao.setVlGeneralidade(doctoServicoFranqueado.getVlGeneralidade());
		simulacao.setVlAjusteBaseNegativa(doctoServicoFranqueado.getVlAjusteBaseNegativa());
		simulacao.setNrKmTransferencia(doctoServicoFranqueado.getNrKmTransferencia());
		simulacao.setVlKmTransferencia(doctoServicoFranqueado.getVlKmTransferencia());
		simulacao.setNrKmColetaEntrega(doctoServicoFranqueado.getNrKmColetaEntrega());
		simulacao.setVlKmColetaEntrega(doctoServicoFranqueado.getVlKmColetaEntrega());
		simulacao.setVlFixoColetaEntrega(doctoServicoFranqueado.getVlFixoColetaEntrega());
		simulacao.setVlRepasseIcms(doctoServicoFranqueado.getVlRepasseIcms());
		simulacao.setVlRepassePis(doctoServicoFranqueado.getVlRepassePis());
		simulacao.setVlRepasseCofins(doctoServicoFranqueado.getVlRepasseCofins());
		simulacao.setVlDescontoLimitador(doctoServicoFranqueado.getVlDescontoLimitador());
		simulacao.setVlRepasseGeneralidade(doctoServicoFranqueado.getVlRepasseGeneralidade());
		simulacao.setTpOperacao(doctoServicoFranqueado.getTpOperacao());
		simulacao.setVlBaseCalculo(doctoServicoFranqueado.getVlBaseCalculo());
		simulacao.setMunicipio(doctoServicoFranqueado.getMunicipio());
		simulacao.setVlParticipacao(doctoServicoFranqueado.getVlParticipacao());
		simulacao.setVlDiferencaParticipacao(doctoServicoFranqueado.getVlDiferencaParticipacao());
		clearDoctoServicoFranqueado();
		return simulacao;
	}


}
