package com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy;

import java.math.BigDecimal;

import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoColeta;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoDocto;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoParcela;

public class CalculoParcelaC2EVStrategy extends CalculoParcelaC2Strategy {

    private Integer quantidadeColetas;
    private Integer quantidadeEntregas;
    private BigDecimal quantidadeParcelas;

    @Override
    public void executeCalculo() {
        if (isParcelaTabelaClienteEspecificoValida(parcelaTabela)) {
            calculaQuantidades();

            if (quantidadeParcelas.compareTo(BigDecimal.ZERO) > 0) {
                getCalculo().addParcelaTabelaEvento(
                        createParcelaTabela(quantidadeParcelas, parcelaTabela.getVlDefinido()));

                NotaCreditoParcela parcela = new NotaCreditoParcela();
                parcela.setQtColeta(quantidadeColetas);
                parcela.setQtEntrega(quantidadeEntregas);

                setParcela(parcela, parcelaTabela, quantidadeParcelas, parcelaTabela.getVlDefinido(), false);
            }
        }
    }

    private void calculaQuantidades() {
        quantidadeColetas = 0;
        quantidadeEntregas = 0;

        for (DocumentoFrete<?> documentoFrete : getCalculo().documentosFrete) {
            if (documentoFrete.getDocumento().getTabelaColetaEntrega().equals(parcelaTabela.getTabelaColetaEntrega())) {
                if (documentoFrete.getDocumento() instanceof NotaCreditoDocto) {
                    quantidadeEntregas++;
                } else if (documentoFrete.getDocumento() instanceof NotaCreditoColeta) {
                    quantidadeColetas++;
                }
            }
        }

        quantidadeParcelas = new BigDecimal(quantidadeColetas + quantidadeEntregas);
    }

}
