package com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy;

import java.math.BigDecimal;
import java.util.List;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoParcela;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega.DM_TP_CALCULO_TABELA_COLETA_ENTREGA;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.vendas.model.Cliente;

public class NotaCreditoCalculoUmStrategy extends NotaCreditoCalculoStrategy {

	public static final DomainValue TIPO_CALCULO = new DomainValue("C1");

	private TabelaColetaEntrega tabela;

	public void setup(NotaCredito notaCredito) {
        tabela = notaCredito.getControleCarga().getTabelaColetaEntrega();
		super.setup(notaCredito);
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
    public DomainValue getTipoCalculo() {
    	return TIPO_CALCULO;
    }

    @Override
    protected List<ParcelaTabelaCe> findParcelasTabelas() {
        return tabela.getParcelaTabelaCes();
    }

    @Override
    protected TabelaColetaEntrega getTabelaCliente(Cliente cliente) {
        return tabela;
    }

	@Override
	protected void executeCalculoDesconto(BigDecimal pcDesconto) {
		BigDecimal vlParcelas = new BigDecimal(0);
		
		for (NotaCreditoParcela parcela : notaCredito.getNotaCreditoParcelas()) 			
			vlParcelas = vlParcelas.add(parcela.getQtNotaCreditoParcela().multiply(parcela.getVlNotaCreditoParcela()));	
		
		notaCredito.setVlDescUsoEquipamento(vlParcelas.subtract(BigDecimalUtils.desconto(vlParcelas, pcDesconto)));
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
	
	

}
