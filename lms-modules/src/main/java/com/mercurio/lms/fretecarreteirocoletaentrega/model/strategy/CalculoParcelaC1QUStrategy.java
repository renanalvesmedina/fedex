package com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy;

import java.math.BigDecimal;

import com.mercurio.lms.carregamento.model.ControleCarga;

public class CalculoParcelaC1QUStrategy extends CalculoParcelaC1Strategy {

    @Override
    public void executeCalculo() {
        setParcela(getNotaCreditoParcela(TipoParcela.QUILOMETRAGEM), parcelaTabela, calculaQuilometragem(),
                parcelaTabela.getVlDefinido(), hasNotaCreditoParcela(TipoParcela.QUILOMETRAGEM));
    }

    private BigDecimal calculaQuilometragem() {
        ControleCarga controleCarga = getCalculo().getNotaCredito().getControleCarga();
        Long quilometragemAcumulada = 0L;
        Long quilometragemPercorrida = getQuilometragemControleCarga(controleCarga);
        Long quilometragemPeriodo = calculaQuilometragemNoPeriodo();

        if (quilometragemPeriodo >= controleCarga.getFilialByIdFilialOrigem().getNrFranquiaKm()) {
        	quilometragemAcumulada = quilometragemPercorrida;
        }else{
        	quilometragemAcumulada = quilometragemPeriodo + quilometragemPercorrida;
        	quilometragemAcumulada = quilometragemAcumulada - controleCarga.getFilialByIdFilialOrigem().getNrFranquiaKm();
        }

        if (quilometragemAcumulada < 0) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(quilometragemAcumulada);
    }

}
