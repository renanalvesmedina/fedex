package com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoDocto;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoDocumentoFrete;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoParcela;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.ParcelaTabelaCeService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

public abstract class CalculoParcelaStrategy {

    protected static final Long HOUR_IN_MILLIS = 3600000L;
    protected static final Long DAY_IN_HOURS = 24L;
    protected static final BigDecimal PERCENTUAL = BigDecimal.valueOf(100);

    protected NotaCreditoCalculoStrategy calculo;
    protected ParcelaTabelaCe parcelaTabela;
    protected ControleCargaService controleCargaService;
    protected DoctoServicoService doctoServicoService;
    protected PedidoColetaService pedidoColetaService;
    protected ParcelaTabelaCeService parcelaTabelaCeService;
    protected ConfiguracoesFacade configuracoesFacade;

    protected List<DocumentoFrete<?>> documentosFreteAgrupadosPorEndereco = new ArrayList<DocumentoFrete<?>>();
    
    public abstract void executeCalculo();
    protected abstract NotaCreditoCalculoStrategy getCalculo();

    public void setup(NotaCreditoCalculoStrategy calculo, ParcelaTabelaCe parcelaTabela) {
        this.calculo = calculo;
        this.parcelaTabela = parcelaTabela;
    }

    protected BigDecimal calculaHorasPernoite(Double horasViagem, ParcelaTabelaCe parcelaTabela) {
        if (hasPercentualSobreValorParcela(parcelaTabela) && hasPernoiteNaViagem(horasViagem)) {
            BigDecimal percentualTabela = parcelaTabela.getPcSobreValor().divide(PERCENTUAL);
            BigDecimal total = percentualTabela.multiply(getQuantidadeDiarias(
                    getCalculo().getNotaCredito().getControleCarga()));

            if (BigDecimalUtils.gtZero(total)) {
                return total;
            }
        }

        return BigDecimal.ZERO;
    }

    protected boolean isParcelaDiaria(ParcelaTabelaCe parcela) {
        return TipoParcela.DIARIA.isEqualTo(parcela.getTpParcela());
    }

    protected boolean isParcelaTabelaClienteEspecificoValida(ParcelaTabelaCe parcelaTabelaCe) {
        return parcelaTabelaCeService.validateParcelaParaClienteEspecifico(
                getCalculo().getNotaCredito().getControleCarga(), parcelaTabelaCe);
    }

    protected boolean isDocumentoFreteValido(NotaCreditoDocumentoFrete documento) {
        return parcelaTabela.getTabelaColetaEntrega().equals(documento.getTabelaColetaEntrega());
    }

    protected boolean hasNotaCreditoDiariaInferior(NotaCredito nota) {
        return hasParcelaDiariaNotaCredito(nota)
                && ((getCalculo().getNotaCredito().getIdNotaCredito() == null && nota.getIdNotaCredito() != null)
                        || nota.getIdNotaCredito() < getCalculo().getNotaCredito().getIdNotaCredito());
    }

    protected boolean hasParcelaDiariaNotaCredito(NotaCredito nota) {
        if (nota.getNotaCreditoParcelas() != null && !nota.getNotaCreditoParcelas().isEmpty()) {
            for (NotaCreditoParcela parcela : nota.getNotaCreditoParcelas()) {
                if (isParcelaDiaria(parcela.getParcelaTabelaCe())) {
                    return true;
                }
            }
        }

        return false;
    }

    protected boolean hasNotaCreditoParcela(TipoParcela tipo) {
        for (NotaCreditoParcela parcela : getCalculo().parcelas) {
            if (tipo.isEqualTo(parcela.getParcelaTabelaCe().getTpParcela())) {
                return true;
            }
        }

        return false;
    }

    protected boolean hasPercentualSobreValorParcela(ParcelaTabelaCe parcelaTabela) {
        return parcelaTabela.getPcSobreValor() != null && BigDecimalUtils.gtZero(parcelaTabela.getPcSobreValor());
    }

    protected boolean hasPernoiteNaViagem(Double horasViagem) {
        return horasViagem >= DAY_IN_HOURS;
    }

    protected NotaCreditoParcela getNotaCreditoParcela(TipoParcela tipo) {
        for (NotaCreditoParcela parcela : getCalculo().parcelas) {
            if (parcela.getParcelaTabelaCe() != null && tipo.isEqualTo(parcela.getParcelaTabelaCe().getTpParcela())) {
                return parcela;
            }
        }

        return new NotaCreditoParcela();
    }

    protected List<ControleCarga> getControlesCargaProprietarioNoPeriodo() {
        return controleCargaService.findControlesCargaProprietarioNoPeriodo(
                getCalculo().getNotaCredito().getControleCarga());
    }

    protected BigDecimal getValorDiaria(Double horasViagem) {
        return parcelaTabela.getVlDefinido();
    }

    protected Double getTempoViagemEmHoras(ControleCarga controleCarga) {
        return (getTempoViagem(controleCarga) / HOUR_IN_MILLIS);
    }

    protected Double getTempoViagem(ControleCarga controleCarga) {
    	DateTime chegada = JTDateTimeUtils.getDataHoraAtual();

        if (controleCarga.getDhChegadaColetaEntrega() != null) {
            chegada = controleCarga.getDhChegadaColetaEntrega();
        }

        return Double.valueOf(chegada.getMillis() - controleCarga.getDhSaidaColetaEntrega().getMillis());
    }

    protected BigDecimal getQuantidadeDiarias(ControleCarga controleCarga) {
       return BigDecimal.ONE;
    }

    protected void setParcela(NotaCreditoParcela parcela, ParcelaTabelaCe tabela, BigDecimal quantidade,
            BigDecimal valor, boolean atualizaExistente) {
        parcela.setNotaCredito(getCalculo().getNotaCredito());
        parcela.setParcelaTabelaCe(tabela);
        parcela.setQtNotaCreditoParcela(quantidade);
        parcela.setVlNotaCreditoParcela(valor);

        if (!atualizaExistente) {
            getCalculo().addNotaCreditoParcela(parcela);
        }
    }
    
    protected Long calculaQuilometragemNoPeriodo() {
        Long quilometragem = 0L;

        
        
        for (ControleCarga controleCarga : getControlesCargaProprietarioNoPeriodo()) {
            if (hasNotaCreditoNoControleCarga(controleCarga)) {
                quilometragem += getQuilometragemControleCarga(controleCarga);
            }
        }

        return quilometragem;
    }

    protected Long getQuilometragemControleCarga(ControleCarga controleCarga) {
        Long quilometragem = controleCargaService.findQuilometrosPercorridosControleCarga(controleCarga);

        if (quilometragem == null || quilometragem < 0) {
            return 0L;
        }

        return quilometragem;
    }

    private boolean hasNotaCreditoNoControleCarga(ControleCarga controleCarga) {
        if (controleCarga != null && controleCarga.getNotasCredito() != null) {
            for (NotaCredito notaCredito : controleCarga.getNotasCredito()) {
                if (notaCredito.getIdNotaCredito() < getCalculo().getNotaCredito().getIdNotaCredito()) {
                    return true;
                }
            }
        }

        return false;
    }

    protected void agrupaDocumentosFretePorEndereco() {
        for (DocumentoFrete<?> documento : getCalculo().documentosFrete) {
        	boolean add = true; 
            for (DocumentoFrete<?> df : documentosFreteAgrupadosPorEndereco) {
                if (df.comparaPorEndereco(documento)) {
                	add = false;
                	break;
                }
            }
            if( add ){
                documentosFreteAgrupadosPorEndereco.add(documento);
            }
        }
    }
    
    protected boolean isDocumentoServicoValido(NotaCreditoDocumentoFrete documento) {
        return documento instanceof NotaCreditoDocto && isDocumentoFreteValido(documento);
    }

    protected BigDecimal getValorDocumento(TipoParcela tipo, DoctoServico documento) {
        if (TipoParcela.PERCENTUAL_SOBRE_FRETE.isEqualTo(tipo)) {
            return documento.getVlTotalParcelas();
        }

        return documento.getVlMercadoria();
    }
    
    protected BigDecimal calculaPorcentagemSobreValor(BigDecimal valor) {
        if (valor != null) {
            return valor.multiply(parcelaTabela.getPcSobreValor()).divide(BigDecimal.valueOf(100));
        }

        return BigDecimal.ZERO;
    }
    
    private boolean hasParcelaQuilometragemControleCarga(ControleCarga controleCarga) {
        return getCalculo().notaCreditoService.hasTipoParcelaNoControleCarga(
                controleCarga.getIdControleCarga(), TipoParcela.QUILOMETRAGEM.value());
    }

    public void setControleCargaService(ControleCargaService controleCargaService) {
        this.controleCargaService = controleCargaService;
    }

    public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
        this.doctoServicoService = doctoServicoService;
    }

    public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
        this.pedidoColetaService = pedidoColetaService;
    }

    public void setParcelaTabelaCeService(ParcelaTabelaCeService parcelaTabelaCeService) {
        this.parcelaTabelaCeService = parcelaTabelaCeService;
    }

    public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
        this.configuracoesFacade = configuracoesFacade;
    }

}
