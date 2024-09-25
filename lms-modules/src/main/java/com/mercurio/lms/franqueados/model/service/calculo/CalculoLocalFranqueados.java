package com.mercurio.lms.franqueados.model.service.calculo;

import java.math.BigDecimal;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.franqueados.ConstantesFranqueado;
import com.mercurio.lms.franqueados.util.FranqueadoUtils;

public class CalculoLocalFranqueados extends CalculoDocumentoFranqueados {

	@Override
	protected void calcularValorParticipacao() {
		getDoctoServicoFranqueado().setVlParticipacao( getDoctoServicoFranqueado().getVlBaseCalculo());
		calculoPcLimite();
		calcularParticipacaoDesconto();
	}

	@Override
	protected void setTipoFrete() {
		getDoctoServicoFranqueado().setTpFrete(new DomainValue(ConstantesFranqueado.FRETE_LOCAL));
		
	}

	@Override
	protected void setGris() {
		BigDecimal vlGris = BigDecimal.ZERO; 
		if( getParametrosFranqueado().getFreteLocalFranqueado() != null){
			vlGris = FranqueadoUtils.calcularPercentual(getDTO().getVlMercadoria(), getParametrosFranqueado().getFreteLocalFranqueado().getPcGris());
		}
		getDoctoServicoFranqueado().setVlGris(vlGris);
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
		// Não implementado para calculo LOCAL
	}

}
