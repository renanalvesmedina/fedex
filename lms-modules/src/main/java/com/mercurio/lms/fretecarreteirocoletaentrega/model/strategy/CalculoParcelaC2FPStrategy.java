package com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.mercurio.lms.fretecarreteirocoletaentrega.model.FaixaPesoParcelaTabelaCE;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoParcela;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCeCC;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega;

public class CalculoParcelaC2FPStrategy extends CalculoParcelaC2Strategy {

    private static final String DOCUMENTO = "Documento";
    private static final String PESO = "KG";

    private List<FaixaPeso> faixasPeso;
    private int quantidadeFaixas;

    @Override
    public void executeCalculo() {
        for (DocumentoFrete<?> documentoFrete : getCalculo().documentosFrete) {
            if (hasParcelaFretePeso(documentoFrete.getDocumento().getTabelaColetaEntrega())) {
                calculaFaixasPeso(documentoFrete);
            }
        }
    }

    private void calculaFaixasPeso(DocumentoFrete<?> documentoFrete) {
        faixasPeso = new ArrayList<FaixaPeso>();
        quantidadeFaixas = 0;
        calculaFaixaPesoNotaFrete(documentoFrete);

        for (FaixaPeso faixaPeso : faixasPeso) {
        	ParcelaTabelaCeCC tabela = getParcelaTabelaCeCCByFaixaPeso(faixaPeso);
        	if( tabela == null ){
        		tabela = createParcelaTabela(BigDecimal.valueOf(faixaPeso.getQuantidade()),
        				faixaPeso.getFaixa().getVlValor());
        		tabela.setFaixaPesoParcelaTabelaCE(faixaPeso.getFaixa());
        		
        		getCalculo().addParcelaTabelaFretePeso(tabela);
        	}else{
        		tabela.getQtParcela().add(BigDecimal.valueOf(faixaPeso.getQuantidade()));
        		tabela.setVlParcela(faixaPeso.getFaixa().getVlValor());
        	}
            
            NotaCreditoParcela parcela = getParcelaByFaixaPeso(faixaPeso);
            BigDecimal qtNotaCredito = BigDecimal.ZERO;
            BigDecimal vlNotaCredito = BigDecimal.ZERO;
            boolean atualiza = false;
            if( parcela == null ){
            	parcela = new NotaCreditoParcela();
                parcela.setFaixaPesoParcelaTabelaCE(faixaPeso.getFaixa());
                qtNotaCredito = BigDecimal.valueOf(faixaPeso.getQuantidade());
                vlNotaCredito = faixaPeso.getFaixa().getVlValor();
            }else{
                qtNotaCredito = parcela.getQtNotaCreditoParcela().add(BigDecimal.valueOf(faixaPeso.getQuantidade()));
                vlNotaCredito = faixaPeso.getFaixa().getVlValor();
            	atualiza = true;
            }
            setParcela(parcela, parcelaTabela,qtNotaCredito,vlNotaCredito, atualiza);
        }
    }

    private NotaCreditoParcela getParcelaByFaixaPeso(FaixaPeso faixaPeso){
    	for( NotaCreditoParcela parcela : getCalculo().getNotaCreditoParcela() ){
    		if( parcela.getFaixaPesoParcelaTabelaCE() != null && 
    				parcela.getFaixaPesoParcelaTabelaCE().equals(faixaPeso.getFaixa())){
    			return parcela;
    		}
    	}
    	return null;
    }
    
    private ParcelaTabelaCeCC getParcelaTabelaCeCCByFaixaPeso(FaixaPeso faixaPeso){
    	for( ParcelaTabelaCeCC parcelaTabelaCeCc : getCalculo().getParcelaTabelaCeCC()){
    		if( parcelaTabelaCeCc.getFaixaPesoParcelaTabelaCE() != null && 
    				parcelaTabelaCeCc.getFaixaPesoParcelaTabelaCE().equals(faixaPeso.getFaixa())){
    			return parcelaTabelaCeCc;
    		}
    	}
    	return null;
    }
    
    private void calculaFaixaPesoNotaFrete(DocumentoFrete<?> documentoFrete) {
        TabelaColetaEntrega tabela = documentoFrete.getDocumento().getTabelaColetaEntrega();

        for (FaixaPesoParcelaTabelaCE faixa : tabela.getFaixasPesoParcelaTabelaCE()) {
            calculaFaixaPeso(faixa, documentoFrete.getPeso().setScale(0, RoundingMode.UP));
        }
    }

    private void calculaFaixaPeso(FaixaPesoParcelaTabelaCE faixa, BigDecimal pesoCarga) {
        FaixaPeso faixaPeso = new FaixaPeso(faixa);
        BigDecimal pesoCreditado = BigDecimal.ZERO;

        if (!faixasPeso.contains(faixaPeso)) {
            if (DOCUMENTO.equals(faixa.getTpFator())) {
                pesoCreditado = faixa.getVlValor();
                quantidadeFaixas = 1;
            } else if (PESO.equals(faixa.getTpFator())) {
                pesoCreditado = calculaPesoCredito(pesoCarga, faixa.getPsInicial(), faixa.getPsFinal());
                quantidadeFaixas = pesoCreditado.intValue();
            }

            if (pesoCreditado.compareTo(BigDecimal.ZERO) > 0) {
                faixaPeso.setQuantidade(quantidadeFaixas);
                faixasPeso.add(faixaPeso);
            }
        }
    }

    private BigDecimal calculaPesoCredito(BigDecimal pesoCarga, BigDecimal faixaInicial, BigDecimal faixaFinal) {
        BigDecimal pesoCreditado = BigDecimal.ZERO;

        if (pesoCarga.compareTo(faixaInicial) >= 0) {
            if (pesoCarga.compareTo(faixaFinal) >= 0) {
                pesoCreditado = faixaFinal.subtract(faixaInicial);
            } else if (pesoCarga.compareTo(faixaFinal) == -1 && pesoCarga.compareTo(faixaInicial) >= 0) {
                pesoCreditado = pesoCarga.subtract(faixaInicial);
            }
        }

        return pesoCreditado.setScale(0, RoundingMode.CEILING);
    }

    private boolean hasParcelaFretePeso(TabelaColetaEntrega tabela) {
        if (tabela.getParcelaTabelaCes() != null && tabela.getFaixasPesoParcelaTabelaCE() != null) {
            for (ParcelaTabelaCe parcela : tabela.getParcelaTabelaCes()) {
                if (TipoParcela.FRETE_PESO.isEqualTo(parcela.getTpParcela()) && parcela.equals(parcelaTabela) ) {
                    return true;
                }
            }
        }

        return false;
    }

}
