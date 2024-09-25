package com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoParcela;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCeCC;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntregaCC;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.vendas.model.Cliente;

public class NotaCreditoCalculoDoisStrategy extends NotaCreditoCalculoStrategy {

    public static final DomainValue TIPO_CALCULO = new DomainValue("C2");

    private boolean fretePesoCalculado = false;
    private List<TabelaColetaEntregaCC> tabelasColetaEntregaCC;
    protected List<ParcelaTabelaCeCC> parcelasPercentualValor = new ArrayList<ParcelaTabelaCeCC>();
    protected List<ParcelaTabelaCeCC> parcelasPercentualFrete = new ArrayList<ParcelaTabelaCeCC>();
    protected List<ParcelaTabelaCeCC> parcelasVolume = new ArrayList<ParcelaTabelaCeCC>();

    public void init() {
    }

    public void setup(NotaCredito notaCredito) {
        this.tabelasColetaEntregaCC = notaCredito.getControleCarga().getTabelasColetaEntregaCC();
        setFretePesoCalculado(false);
        super.setup(notaCredito);
    }

    @Override
    public DomainValue getTipoCalculo() {
        return TIPO_CALCULO;
    }

    @Override
    public BigDecimal executeCalculo() {
        BigDecimal total = BigDecimal.ZERO;

        for (NotaCreditoParcela parcela : notaCredito.getNotaCreditoParcelas()) {
            if (isParcelaPercentualSobreValor(parcela) || isParcelaPercentualSobreFrete(parcela)) {
            	total = total.add(getMaiorValor(getTotalParcela(parcela), parcela.getParcelaTabelaCe().getVlDefinido()));
            } else {
            	total = total.add(getTotalParcela(parcela));
            }
        }

        validateTotalNotaCredito(total);

        return getValorFinalNotaCredito(total, notaCredito);    	
    }

    @Override
    protected List<ParcelaTabelaCe> findParcelasTabelas() {
        List<ParcelaTabelaCe> parcelasTabela = new ArrayList<ParcelaTabelaCe>();

        for (TabelaColetaEntregaCC tabela : tabelasColetaEntregaCC) {
            parcelasTabela.addAll(tabela.getTabelaColetaEntrega().getParcelaTabelaCes());
        }

        return parcelasTabela;
    }

    @Override
    protected TabelaColetaEntrega getTabelaCliente(Cliente cliente) {
        for (TabelaColetaEntregaCC tabelaCC : tabelasColetaEntregaCC) {
            if (cliente.equals(tabelaCC.getTabelaColetaEntrega().getCliente())) {
                return tabelaCC.getTabelaColetaEntrega();
            }
        }

        for (TabelaColetaEntregaCC tabelaCC : tabelasColetaEntregaCC) {
            if (tabelaCC.getTabelaColetaEntrega().getCliente() == null) {
                return tabelaCC.getTabelaColetaEntrega();
            }
        }

        throw new BusinessException("LMS-25045");
    }    

	@Override
	protected void executeCalculoDesconto(BigDecimal pcDesconto) {
		BigDecimal vlParcelas = new BigDecimal(0);
		
		for (NotaCreditoParcela parcela : notaCredito.getNotaCreditoParcelas()) 			
			vlParcelas = vlParcelas.add(parcela.getQtNotaCreditoParcela().multiply(parcela.getVlNotaCreditoParcela()));	
		
		notaCredito.setVlDescUsoEquipamento(vlParcelas.subtract(BigDecimalUtils.desconto(vlParcelas, pcDesconto)));
	}

    private NotaCreditoParcela getParcelaDiaria(List<NotaCreditoParcela> parcelas) {
        NotaCreditoParcela candidate = null;

        if (CollectionUtils.isNotEmpty(parcelas)){
        	for (NotaCreditoParcela parcela : parcelas) {
                if (isParcelaDiaria(parcela) && (candidate == null || isParcelaMaior(parcela, candidate))) {
                    candidate = parcela;
                }
            }
        }

        return candidate;
    }

    private boolean isParcelaDiaria(NotaCreditoParcela parcela) {
        return TipoParcela.DIARIA.isEqualTo(parcela.getParcelaTabelaCe().getTpParcela());
    }

    protected void addParcelaTabelaPercentualValor(ParcelaTabelaCeCC parcelaTabela) {
        parcelasPercentualValor.add(parcelaTabela);
    }

    protected void addParcelaTabelaPercentualFrete(ParcelaTabelaCeCC parcelaTabela) {
        parcelasPercentualFrete.add(parcelaTabela);
    }

    protected void addParcelaTabelaVolume(ParcelaTabelaCeCC parcelaTabela) {
        parcelasVolume.add(parcelaTabela);
    }

    protected ParcelaTabelaCe findMaiorParcelaTabelaCe(List<ParcelaTabelaCeCC> parcelas) {
        return findMaiorParcelaTabela(parcelas).getParcelaTabelaCe();
    }

    protected ParcelaTabelaCeCC findMaiorParcelaTabela(List<ParcelaTabelaCeCC> parcelas) {
        return parcelaTabelaCeCCService.findMaiorParcela(parcelas);
    }

    protected TabelaColetaEntregaCC findTabelaColetaEntregaCC(ParcelaTabelaCe parcelaTabela) {
        for (TabelaColetaEntregaCC tabelaColetaEntregaCC : tabelasColetaEntregaCC) {
            if (hasParcelaTabelaColetaEntrega(tabelaColetaEntregaCC, parcelaTabela)) {
                return tabelaColetaEntregaCC;
            }
        }

        throw new BusinessException("LMS-25066",
                new Object[]{ "Erro ao recuperar tabela para parcela " + parcelaTabela.getTpParcela().getValue() });
    }

    private boolean hasParcelaTabelaColetaEntrega(TabelaColetaEntregaCC tabela, ParcelaTabelaCe parcelaTabela) {
        for (ParcelaTabelaCe parcela : tabela.getTabelaColetaEntrega().getParcelaTabelaCes()) {
            if (parcela.equals(parcelaTabela)) {
                return true;
            }
        }

        return false;
    }

    protected boolean isFretePesoCalculado() {
        return fretePesoCalculado;
    }

    protected void setFretePesoCalculado(boolean fretePesoCalculado) {
        this.fretePesoCalculado = fretePesoCalculado;
    }

	@Override
	public BigDecimal findValorTotalNotaCredito(NotaCredito notaCredito) {		
			BigDecimal valor = BigDecimal.ZERO;
			BigDecimal vlAcrescimo = notaCredito.getVlAcrescimo() != null ? notaCredito.getVlAcrescimo() : BigDecimal.ZERO;
			BigDecimal vlDesconto = notaCredito.getVlDesconto() != null ? notaCredito.getVlDesconto() : BigDecimal.ZERO;
			BigDecimal vlDescUsoEquipamento = notaCredito.getVlDescUsoEquipamento() != null ? notaCredito.getVlDescUsoEquipamento() : BigDecimal.ZERO;
			List<NotaCreditoParcela> parcelas = notaCredito.getNotaCreditoParcelas();
			for(NotaCreditoParcela parcela : parcelas) {
				valor = valor.add(parcela.getQtNotaCreditoParcela().multiply(parcela.getVlNotaCreditoParcela()));
			}
			BigDecimal total = valor.add(vlAcrescimo).subtract(vlDesconto.add(vlDescUsoEquipamento));
			
			return BigDecimalUtils.round(total);			
	} 

	private BigDecimal getValorMaiorParcelaDHOuSomaParcelas(NotaCredito nc) {
		List<NotaCreditoParcela> notasCreditoParcela = nc.getNotaCreditoParcelas();
		BigDecimal vlMaiorParcelaDiariaDiaria = BigDecimal.ZERO;
		BigDecimal vlTotalNotasNaoDiarias = BigDecimal.ZERO;
		for (NotaCreditoParcela notaCreditoParcela : notasCreditoParcela) {
			ParcelaTabelaCe parcelaTabelaCe = notaCreditoParcela.getParcelaTabelaCe();
			if("DH".equals(parcelaTabelaCe.getTpParcela().getValue())){
				if(vlMaiorParcelaDiariaDiaria.compareTo(notaCreditoParcela.getVlNotaCreditoParcela()) < 0 ){
					vlMaiorParcelaDiariaDiaria = notaCreditoParcela.getVlNotaCreditoParcela().multiply(notaCreditoParcela.getQtNotaCreditoParcela());
				}
			} else {
				vlTotalNotasNaoDiarias = vlTotalNotasNaoDiarias.add(notaCreditoParcela.getVlNotaCreditoParcela().multiply(notaCreditoParcela.getQtNotaCreditoParcela()));
			}
		}
		BigDecimal valor = vlTotalNotasNaoDiarias.compareTo(vlMaiorParcelaDiariaDiaria) > 0 ?  vlTotalNotasNaoDiarias : vlMaiorParcelaDiariaDiaria;
		return valor;
	}
}
