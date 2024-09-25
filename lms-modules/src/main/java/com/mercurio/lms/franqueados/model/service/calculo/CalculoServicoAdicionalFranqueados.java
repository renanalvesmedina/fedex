package com.mercurio.lms.franqueados.model.service.calculo;

import java.math.BigDecimal;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.franqueados.ConstantesFranqueado;
import com.mercurio.lms.franqueados.model.ServicoAdicionalFranqueado;

public class CalculoServicoAdicionalFranqueados extends CalculoDocumentoFranqueados {

	@Override
	protected void calcularValorParticipacao() {
		getDoctoServicoFranqueado().setVlParticipacao( getDoctoServicoFranqueado().getVlBaseCalculo());
		calculoPcLimite();
		calcularParticipacaoDesconto();
	}

	@Override
	protected void setTipoFrete() {
		getDoctoServicoFranqueado().setTpFrete( new DomainValue(ConstantesFranqueado.SERVICO_ADICIONAL) );
	}

	@Override
	protected void setGris() {
		getDoctoServicoFranqueado().setVlGris(BigDecimal.ZERO);
	}

	@Override
	protected void setCustoCarreteiro() {
		getDoctoServicoFranqueado().setVlCustoCarreteiro(BigDecimal.ZERO);
	}

	@Override
	protected void setCustoAereo() {
		getDoctoServicoFranqueado().setVlCustoAereo(BigDecimal.ZERO);
	}

	@Override
	protected void setGeneralidade() {
		getDoctoServicoFranqueado().setVlGeneralidade(BigDecimal.ZERO);
	}
	
	@Override
	protected void setTipoOperacao() {
		getDoctoServicoFranqueado().setTpOperacao(new DomainValue(ConstantesFranqueado.TP_OPERACAO_FRETE_NACIONAL));
	}

	@Override
	protected void setMunicipio() {
		// Não necessita implementação para Serfiço adicional
		
	}

	@Override
	protected void calculoPcLimite() {
		ServicoAdicionalFranqueado servicoAdicionalFranqueado = getParametrosFranqueado().getServicoAdicionalFranqueado(getDTO().getIdServicoAdicional());
		if( servicoAdicionalFranqueado != null ){
			this.pcLimite = servicoAdicionalFranqueado.getPcServico();
		}
	}

	
}
