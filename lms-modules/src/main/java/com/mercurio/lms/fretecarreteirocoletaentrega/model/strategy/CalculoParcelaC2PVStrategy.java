package com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy;


public class CalculoParcelaC2PVStrategy extends CalculoParcelaC2Strategy {

    @Override
    public void executeCalculo() {
        executeCalculoPercentual(TipoParcela.PERCENTUAL_SOBRE_VALOR);
    }

}
