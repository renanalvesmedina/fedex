package com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy;


public class CalculoParcelaC1PVStrategy extends CalculoParcelaC1Strategy {

    @Override
    public void executeCalculo() {
    	executeCalculoPercentual(TipoParcela.PERCENTUAL_SOBRE_VALOR);
    }

}
