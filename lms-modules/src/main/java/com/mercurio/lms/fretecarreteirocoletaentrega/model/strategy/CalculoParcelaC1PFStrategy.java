package com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy;


public class CalculoParcelaC1PFStrategy extends CalculoParcelaC1Strategy {

    @Override
    public void executeCalculo() {
    	executeCalculoPercentual(TipoParcela.PERCENTUAL_SOBRE_FRETE);
    }

}
