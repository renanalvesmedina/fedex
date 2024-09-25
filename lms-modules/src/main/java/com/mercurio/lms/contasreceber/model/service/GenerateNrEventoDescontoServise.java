package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * 
 * @author hectorj
 *
 */
public class GenerateNrEventoDescontoServise {

	public enum TpDocumento{
		FAT,
		CTRC
	}
	
	/**
	 * 
	 * @param percentualDesconto
	 * @param valorDesconto
	 * @param tpDocumento
	 * @return
	 */
	public Short getNrEventoDesconto(BigDecimal percentualDesconto, BigDecimal valorDesconto, TpDocumento tpDocumento) {
		Short nrEvento = null;
		
		if (percentualDesconto != null && valorDesconto != null) {
			if (tpDocumento.equals(TpDocumento.FAT)) {
				nrEvento = getNrEventoDescontoFatura(percentualDesconto,
						valorDesconto);
			} else if (tpDocumento.equals(TpDocumento.CTRC)) {
				nrEvento = getNrEventoDescontoCtrc(percentualDesconto,
						valorDesconto);
			}
		}
		
		return nrEvento;
	}

	/**
	 * 
	 * @param percentualDesconto
	 * @param valorDesconto
	 * @return
	 */
	private Short getNrEventoDescontoCtrc(BigDecimal percentualDesconto,
			BigDecimal valorDesconto) {
		Short nrEvento;
		if (CompareUtils.le(percentualDesconto,(new BigDecimal("30")).setScale(2, RoundingMode.HALF_UP)) &&
				CompareUtils.le(valorDesconto,(new BigDecimal("300")).setScale(2, RoundingMode.HALF_UP))){
			nrEvento = ConstantesWorkflow.NR3602_DESCONTO_ATE_30;
		} else if(CompareUtils.le(percentualDesconto,(new BigDecimal("40")).setScale(2, RoundingMode.HALF_UP)) &&
				CompareUtils.le(valorDesconto,(new BigDecimal("400")).setScale(2, RoundingMode.HALF_UP))){
			nrEvento = ConstantesWorkflow.NR3603_DESCONTO_DE_30_40;
		} else if(CompareUtils.le(percentualDesconto,(new BigDecimal("45")).setScale(2, RoundingMode.HALF_UP)) &&
				CompareUtils.le(valorDesconto,(new BigDecimal("800")).setScale(2, RoundingMode.HALF_UP))){
			nrEvento = ConstantesWorkflow.NR3604_DESCONTO_DE_40_45;
		} else {
		    nrEvento = ConstantesWorkflow.NR3605_DESCONTO_GERAL_100;
		}
		return nrEvento;
	}

	/**
	 * 
	 * @param percentualDesconto
	 * @param valorDesconto
	 * @return
	 */
	private Short getNrEventoDescontoFatura(BigDecimal percentualDesconto,
			BigDecimal valorDesconto) {
		Short nrEvento;
		if (CompareUtils.le(percentualDesconto,(new BigDecimal("30")).setScale(2, RoundingMode.HALF_UP)) &&
				CompareUtils.le(valorDesconto,(new BigDecimal("300")).setScale(2, RoundingMode.HALF_UP))){
			nrEvento = ConstantesWorkflow.NR3625_FAT_ATE_30;
		} else if(CompareUtils.le(percentualDesconto,(new BigDecimal("40")).setScale(2, RoundingMode.HALF_UP)) &&
				CompareUtils.le(valorDesconto,(new BigDecimal("400")).setScale(2, RoundingMode.HALF_UP))){
		    nrEvento = ConstantesWorkflow.NR3626_FAT_DE_30_40;
		} else if(CompareUtils.le(percentualDesconto,(new BigDecimal("45")).setScale(2, RoundingMode.HALF_UP)) &&
				CompareUtils.le(valorDesconto,(new BigDecimal("800")).setScale(2, RoundingMode.HALF_UP))){
		    nrEvento = ConstantesWorkflow.NR3627_FAT_DE_40_45;
		} else {
		    nrEvento = ConstantesWorkflow.NR3628_FAT_GERAL;
		}
		return nrEvento;
	}
	
}
