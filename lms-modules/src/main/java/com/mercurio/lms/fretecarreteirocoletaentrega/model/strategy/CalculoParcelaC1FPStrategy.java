package com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CalculoParcelaC1FPStrategy extends CalculoParcelaC1Strategy {

    @Override
    public void executeCalculo() {

        agrupaDocumentosFrete();

        BigDecimal quantidade = calculaValorAcumulado();

        if (quantidade.compareTo(BigDecimal.ZERO) > 0) {
            setParcela(getNotaCreditoParcela(TipoParcela.FRETE_PESO), parcelaTabela, quantidade,
                    parcelaTabela.getVlDefinido(), hasNotaCreditoParcela(TipoParcela.FRETE_PESO));
        }
    }

    private void agrupaDocumentosFrete() {
    	agrupaDocumentosFretePorEndereco();
        for (DocumentoFrete<?> documento : getCalculo().documentosFrete) {
            for(DocumentoFrete<?> df : documentosFreteAgrupadosPorEndereco){
                if (df.comparaPorEndereco(documento)){
                    df.addPesoAcumulado(documento.getPeso());
                }
            }
        }
    }

    private BigDecimal calculaValorAcumulado() {
        BigDecimal valorAcumulado = BigDecimal.ZERO;
        BigDecimal capacidade = getCapacidadeMediaTransporte();

        for (DocumentoFrete<?> documento : documentosFreteAgrupadosPorEndereco) {
            BigDecimal valor = calculaPesoItem(documento.getPesoAcumulado(), capacidade);

            if (valor.compareTo(BigDecimal.ZERO) > 0) {
                valorAcumulado = valorAcumulado.add(new BigDecimal(Math.ceil(valor.doubleValue())));
            }
        }

        return valorAcumulado;
    }

    private BigDecimal calculaPesoItem(BigDecimal peso, BigDecimal capacidade) {
        return peso.divide(capacidade, BigDecimal.ROUND_UP).subtract(BigDecimal.ONE);
    }


    private BigDecimal getCapacidadeMediaTransporte() {
        return controleCargaService.findCapacidadeMediaMeioTransporteControleCarga(
                getCalculo().getNotaCredito().getControleCarga());
    }

}
