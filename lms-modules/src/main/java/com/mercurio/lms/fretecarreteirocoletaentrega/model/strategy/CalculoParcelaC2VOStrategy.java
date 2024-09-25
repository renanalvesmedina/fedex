package com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy;

import java.math.BigDecimal;

import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoColeta;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoDocto;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoDocumentoFrete;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoParcela;

public class CalculoParcelaC2VOStrategy extends CalculoParcelaC2Strategy {

    @Override
    public void executeCalculo() {
        BigDecimal quantidade = calculaTotalVolumes();

        getCalculo().addParcelaTabelaVolume(createParcelaTabela(quantidade, parcelaTabela.getVlDefinido()));
        setParcela(new NotaCreditoParcela(), parcelaTabela, quantidade, parcelaTabela.getVlDefinido(), false);
    }

    private BigDecimal calculaTotalVolumes() {
        int totalVolumes = 0;

        for (DocumentoFrete<?> documentoFrete : getCalculo().documentosFrete) {
            if (isDocumentoFreteValido(documentoFrete.getDocumento())) {
                totalVolumes += getQuantidadeVolumesDocumento(documentoFrete.getDocumento());
            }
        }

        return new BigDecimal(totalVolumes);
    }

    private Integer getQuantidadeVolumesDocumento(NotaCreditoDocumentoFrete documento) {
        Integer quantidadeVolumes = 0;

        if (documento instanceof NotaCreditoDocto) {
            quantidadeVolumes = ((NotaCreditoDocto) documento).getDoctoServico().getQtVolumes();
        } else if (documento instanceof NotaCreditoColeta) {
            quantidadeVolumes = ((NotaCreditoColeta) documento).getPedidoColeta().getQtTotalVolumesVerificado();
        }

        if (quantidadeVolumes == null) {
            quantidadeVolumes = 0;
        }

        return quantidadeVolumes;
    }

}
