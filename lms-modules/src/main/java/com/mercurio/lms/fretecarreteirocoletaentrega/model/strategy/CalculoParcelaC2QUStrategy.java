package com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy;

import java.math.BigDecimal;

import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoDocto;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCeCC;

public class CalculoParcelaC2QUStrategy extends CalculoParcelaC2Strategy {

    @Override
    public void executeCalculo() {
        if (isParcelaTabelaClienteEspecificoValida(parcelaTabela)) {
        	for (DocumentoFrete<?> documentoFrete : getCalculo().documentosFrete){
        		if (documentoFrete.getDocumento() instanceof NotaCreditoDocto && documentoFrete.getDocumento().getTabelaColetaEntrega().equals(parcelaTabela.getTabelaColetaEntrega())){
		            ParcelaTabelaCeCC parcela = createParcelaTabela(calculaQuilometragemExcedente(),
		                    parcelaTabela.getVlDefinido());
		
		            getCalculo().addParcelaTabelaQuilometragem(parcela);
		            ParcelaTabelaCeCC tabela = getCalculo().findMaiorParcelaTabela(getCalculo().parcelasQuilometragem);
		
		            setParcela(getNotaCreditoParcela(TipoParcela.QUILOMETRAGEM),
		                    tabela.getParcelaTabelaCe(), tabela.getQtParcela(),
		                    tabela.getVlParcela(), hasNotaCreditoParcela(TipoParcela.QUILOMETRAGEM));
        		}
        	}
        }
    }

    private BigDecimal calculaQuilometragemExcedente() {
        ControleCarga controleCarga = getCalculo().getNotaCredito().getControleCarga();
        Long valorAcumulado = 0L;
        Long quilometragem = getQuilometragemControleCarga(controleCarga);
        Long quilometragemAcumulada = calculaQuilometragemNoPeriodo();

        if (quilometragemAcumulada >= controleCarga.getRotaColetaEntrega().getNrKm()) {
            valorAcumulado = quilometragem;
        } else {
            valorAcumulado = quilometragem + quilometragemAcumulada;
            valorAcumulado -= controleCarga.getRotaColetaEntrega().getNrKm();
        }

        if (valorAcumulado < 0) {
            valorAcumulado = 0L;
        }

        return new BigDecimal(valorAcumulado);
    }

}
