package com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy;

import java.math.BigDecimal;

import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoDocto;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoDocumentoFrete;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoParcela;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCeCC;

public abstract class CalculoParcelaC2Strategy extends CalculoParcelaStrategy {

    @Override
    protected NotaCreditoCalculoDoisStrategy getCalculo() {
        return (NotaCreditoCalculoDoisStrategy) calculo;
    }

    protected void executeCalculoPercentual(TipoParcela tipo) {
        if (isParcelaTabelaClienteEspecificoValida(parcelaTabela)) {
            BigDecimal valorParcela = BigDecimal.ZERO;

            for (DocumentoFrete<?> documentoFrete : getCalculo().documentosFrete) {
                if (isDocumentoServicoValido(documentoFrete.getDocumento())) {
                    NotaCreditoDocto documento = (NotaCreditoDocto) documentoFrete.getDocumento();
                    valorParcela = valorParcela.add(getCalculo().getMaiorValor(parcelaTabela.getVlDefinido(),
                            calculaPorcentagemSobreValor(getValorDocumento(tipo, documento.getDoctoServico()))));
                }
            }

            ParcelaTabelaCeCC tabela = createParcelaTabela(BigDecimal.ONE, valorParcela);

            if (TipoParcela.PERCENTUAL_SOBRE_FRETE.isEqualTo(tipo)) {
                getCalculo().addParcelaTabelaPercentualFrete(tabela);
            } else {
                getCalculo().addParcelaTabelaPercentualValor(tabela);
            }

            setParcela(new NotaCreditoParcela(), parcelaTabela, tabela.getQtParcela(), valorParcela, false);
        }
    }

    protected ParcelaTabelaCeCC createParcelaTabela(BigDecimal quantidadeParcelas, BigDecimal valorParcela) {
        ParcelaTabelaCeCC parcela = new ParcelaTabelaCeCC();
        parcela.setParcelaTabelaCe(parcelaTabela);
        parcela.setTabelaColetaEntregaCC(getCalculo().findTabelaColetaEntregaCC(parcelaTabela));
        parcela.setQtParcela(quantidadeParcelas);
        parcela.setVlParcela(valorParcela);

        return parcela;
    }

}
