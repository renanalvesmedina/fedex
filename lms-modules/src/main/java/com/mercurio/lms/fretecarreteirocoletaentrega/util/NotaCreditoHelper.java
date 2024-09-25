package com.mercurio.lms.fretecarreteirocoletaentrega.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy.TipoParcela;



public class NotaCreditoHelper {

	
	public Map<TipoParcela, BigDecimal> getDadosControleCarga(ControleCarga controleCarga,List<TipoParcela> identificadores) {
		if(controleCarga == null){
			throw new BusinessException("O controle de carga não pode ser nulo");//TODO criar LMS
		}
		
		Map<TipoParcela, BigDecimal> result = new HashMap<TipoParcela, BigDecimal>();
		
		for (TipoParcela tipoParcela : identificadores) {
			result.put(tipoParcela, getValorByTipoParcela(tipoParcela,controleCarga));
		}
		
		
		
		
		
		return result;
	}

	public BigDecimal getValorByTipoParcela(TipoParcela tipoParcela, ControleCarga controleCarga) {
		
		switch (tipoParcela) {
		case DIARIA:
			return getValorDiaria();
		case EVENTO:
			return getValorEvento(controleCarga);
		case FRETE_PESO:
			return getValorFretePeso(controleCarga);		
		case PERCENTUAL_SOBRE_FRETE:
			return getValorPercentualFrete(controleCarga);			
		case PERCENTUAL_SOBRE_VALOR:
			return getValorPercentualValor(controleCarga);
		case QUILOMETRAGEM:
			return getValorQuilometragem(controleCarga);
		case VOLUME:
			return getValorVolume(controleCarga);			
		default:
			break;
		}
		return BigDecimal.ZERO;
		
	}

	public BigDecimal getValorVolume(ControleCarga controleCarga) {
		// TODO Auto-generated method stub
		return null;
	}

	public BigDecimal getValorQuilometragem(ControleCarga controleCarga) {
		// TODO Auto-generated method stub
		return null;
	}

	public BigDecimal getValorPercentualValor(ControleCarga controleCarga) {
		// TODO Auto-generated method stub
		return null;
	}

	public BigDecimal getValorPercentualFrete(ControleCarga controleCarga) {
		// TODO Auto-generated method stub
		return null;
	}

	public BigDecimal getValorFretePeso(ControleCarga controleCarga) {
		// TODO Auto-generated method stub
		return null;
	}

	public BigDecimal getValorEvento(ControleCarga controleCarga) {
		// TODO Auto-generated method stub
		return null;
	}

	public BigDecimal getValorDiaria() {		
		return BigDecimal.ONE;
	}
	
}
