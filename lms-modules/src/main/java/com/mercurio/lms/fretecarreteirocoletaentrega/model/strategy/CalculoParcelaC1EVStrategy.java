package com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy;

import java.math.BigDecimal;

import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoColeta;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoDocto;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoParcela;

public class CalculoParcelaC1EVStrategy extends CalculoParcelaC1Strategy {

    @Override
    public void executeCalculo() {
    	agrupaDocumentosFretePorEndereco();
    	
        Integer quantidadeColetas = 0;
        Integer quantidadeEntregas = 0;

        for(DocumentoFrete<?> df : documentosFreteAgrupadosPorEndereco ){
            if( df.getDocumento() instanceof NotaCreditoColeta ){
            	quantidadeColetas ++;
            }else if( df.getDocumento() instanceof NotaCreditoDocto ){
            	quantidadeEntregas ++;
            }
    	}
    	
        BigDecimal quantidadeParcelas = BigDecimal.valueOf(documentosFreteAgrupadosPorEndereco.size());

        if (quantidadeParcelas.compareTo(BigDecimal.ZERO) > 0) {
            NotaCreditoParcela parcela = getNotaCreditoParcela(TipoParcela.EVENTO);
            parcela.setQtColeta(quantidadeColetas);
            parcela.setQtEntrega(quantidadeEntregas);

            setParcela(parcela, parcelaTabela, quantidadeParcelas, parcelaTabela.getVlDefinido(),
                    hasNotaCreditoParcela(TipoParcela.EVENTO));
        }
    }

}
