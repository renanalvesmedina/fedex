package com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy;

import java.math.BigDecimal;
import java.util.List;

import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoColeta;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoDocto;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoParcela;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCeCC;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.ParcelaTabelaCeCCService;
import com.mercurio.lms.util.BigDecimalUtils;

public class CalculoParcelaC2DHStrategy extends CalculoParcelaC2Strategy {

    @Override
    public void executeCalculo() {
        if (isParcelaTabelaClienteEspecificoValida(parcelaTabela)) {
        	for (DocumentoFrete<?> documentoFrete : getCalculo().documentosFrete){
        		if (documentoFrete.getDocumento().getTabelaColetaEntrega().equals(parcelaTabela.getTabelaColetaEntrega())){
		            ParcelaTabelaCeCC parcelaDiaria = createParcelaTabela(getQuantidadeDiarias(
		                    getCalculo().getNotaCredito().getControleCarga()), getValorDiaria(
		                            getTempoViagemEmHoras(getCalculo().getNotaCredito().getControleCarga())));
		            
		            
		            if (getCalculo().parcelasDiaria.isEmpty()){
		            	getCalculo().addParcelaTabelaDiaria(parcelaDiaria);
		            	ParcelaTabelaCe tabela = parcelaDiaria.getParcelaTabelaCe();
			            setParcela(new NotaCreditoParcela(), tabela, getQuantidadeDiarias(
			                    getCalculo().getNotaCredito().getControleCarga()),
			                    calculaValorNotaCredito(tabela), false);
		            }
		            else{
		            	ParcelaTabelaCeCC parcelaDiariaExistente = getCalculo().parcelasDiaria.get(0);	 
		            	BigDecimal valorParcelaDiariaExistente = parcelaDiariaExistente.getQtParcela().multiply(parcelaDiariaExistente.getVlParcela());
		            	BigDecimal valorParcelaDiaria = parcelaDiaria.getQtParcela().multiply(parcelaDiaria.getVlParcela());
		            	if (valorParcelaDiaria.compareTo(valorParcelaDiariaExistente) > 0){
		            		for (NotaCreditoParcela notaCreditoParcela : getCalculo().getNotaCreditoParcela()){          		
		            			if (TipoParcela.DIARIA.isEqualTo(notaCreditoParcela.getParcelaTabelaCe().getTpParcela())){
		            				getCalculo().parcelasDiaria.remove(0);
		            				getCalculo().addParcelaTabelaDiaria(parcelaDiaria);
		            				ParcelaTabelaCe tabela = parcelaDiaria.getParcelaTabelaCe();
		            				 setParcela(notaCreditoParcela, tabela, getQuantidadeDiarias(
		     			                    getCalculo().getNotaCredito().getControleCarga()),
		     			                    calculaValorNotaCredito(tabela), true);
		            			}
		            		}
		            	}
		            }		           
        		}
        	}
        }
    }

    private BigDecimal calculaValorNotaCredito(ParcelaTabelaCe parcelaTabela) {
        BigDecimal totalDiarias = BigDecimal.ZERO;
        List<ControleCarga> controlesCarga = getControlesCargaProprietarioNoPeriodo();

        for (ControleCarga controleCarga : controlesCarga) {
            for (NotaCredito nota : getCalculo().findNotasCreditoControleCarga(controleCarga.getIdControleCarga())) {
                if (hasNotaCreditoDiariaInferior(nota)) {
                    totalDiarias = totalDiarias.add(calculaDiariasNotaCredito(nota));
                }
            }
        }

        if (BigDecimalUtils.gtZero(totalDiarias)) {
            BigDecimal valorParcela = parcelaTabela.getVlDefinido().subtract(totalDiarias);

            if (BigDecimalUtils.gtZero(valorParcela)) {
                
                if (parcelaTabela.getPcSobreValor() != null && parcelaTabela.getPcSobreValor().compareTo(BigDecimal.ZERO) > 0)
                	return BigDecimalUtils.acrescimo(valorParcela, parcelaTabela.getPcSobreValor());
                else
                	return valorParcela;
            }

            return BigDecimal.ZERO;
        }
        
        if (parcelaTabela.getPcSobreValor() != null && parcelaTabela.getPcSobreValor().compareTo(BigDecimal.ZERO) > 0 
        		&& hasPernoiteNaViagem(getTempoViagemEmHoras(getCalculo().getNotaCredito().getControleCarga())))
        	return BigDecimalUtils.acrescimo(parcelaTabela.getVlDefinido(), parcelaTabela.getPcSobreValor());
        else
        	return parcelaTabela.getVlDefinido();
    }

    private BigDecimal calculaDiariasNotaCredito(NotaCredito nota) {
        BigDecimal totalDiarias = BigDecimal.ZERO;

        for (NotaCreditoParcela parcela : nota.getNotaCreditoParcelas()) {
            if (isParcelaDiaria(parcela.getParcelaTabelaCe())) {
                totalDiarias = totalDiarias.add(parcela.getVlNotaCreditoParcela().multiply(parcela.getQtNotaCreditoParcela()));
            }
        }

        return totalDiarias;
    }

}
