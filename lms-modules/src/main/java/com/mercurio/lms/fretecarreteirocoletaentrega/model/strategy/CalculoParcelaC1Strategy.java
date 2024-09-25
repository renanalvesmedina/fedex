package com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy;

import java.math.BigDecimal;

import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoDocto;


public abstract class CalculoParcelaC1Strategy extends CalculoParcelaStrategy {

    @Override
    protected NotaCreditoCalculoUmStrategy getCalculo() {
        return (NotaCreditoCalculoUmStrategy) calculo;
    }

    
	protected void executeCalculoPercentual(TipoParcela tipo) {
		BigDecimal valorParcela = BigDecimal.ZERO;

		for (DocumentoFrete<?> documentoFrete : getCalculo().documentosFrete) {
			if (isDocumentoServicoValido(documentoFrete.getDocumento())) {
				NotaCreditoDocto documento = (NotaCreditoDocto) documentoFrete.getDocumento();
				valorParcela = valorParcela.add(getCalculo().getMaiorValor(parcelaTabela.getVlDefinido(),
						calculaPorcentagemSobreValor(getValorDocumento(tipo, documento.getDoctoServico()))));
			}
		}

		setParcela(getNotaCreditoParcela(tipo), parcelaTabela, BigDecimal.ONE, valorParcela, hasNotaCreditoParcela(tipo));
	}
}
