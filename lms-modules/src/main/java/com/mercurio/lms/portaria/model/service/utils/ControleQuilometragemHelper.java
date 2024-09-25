package com.mercurio.lms.portaria.model.service.utils;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.mercurio.lms.util.BigDecimalUtils;

public class ControleQuilometragemHelper {

	public static String getNomeParametroGeralByTpCategoriaMeioTransporte(String tpCategoria) {
		String result = null;
		if("ME".equals(tpCategoria)){
			result = "VEL_MT_MEDIO";
		} else if("PE".equals(tpCategoria)){
			result = "VEL_MT_PESADO";
		} else if("LE".equals(tpCategoria)){
			result = "VEL_MT_LEVE";
		} else if("LL".equals(tpCategoria)){ 
			result = "VEL_MT_LEVE_LEVE";
		} else if("LM".equals(tpCategoria)){ 
			result = "VEL_MT_LEVE_MEDIO";
		}
		return result;
	}

	public static BigDecimal calculaDiferencaHoras(DateTime dhIni, DateTime dhFin) {
		if(dhIni == null || dhFin == null) return null;
		Duration calc = new Duration(dhIni, dhFin);
		BigDecimal minutos = BigDecimalUtils.divide(new BigDecimal(calc.getStandardSeconds()),new BigDecimal(60));
		return BigDecimalUtils.divide(minutos,new BigDecimal(60));
	}

	public static BigDecimal calculaKilometragemParametroXHoras(BigDecimal qtHoras, BigDecimal valorParametro) {
		BigDecimal result = new BigDecimal(0);
		result = eqValue(qtHoras).multiply(eqMultiplicador(valorParametro));
		return result;
	}

	private static BigDecimal eqValue(BigDecimal value) {
		if (value != null) {
			return value;
		}
		return BigDecimal.ZERO;
	}

	private static BigDecimal eqMultiplicador(BigDecimal valorParametro) {
		if (valorParametro == null || (valorParametro != null && valorParametro.equals(BigDecimal.ZERO))) {
			valorParametro = BigDecimal.valueOf(1);
		}
		return valorParametro;
	}
	
	public static Boolean isLess(BigDecimal value1, BigDecimal value2) {
		return Boolean.valueOf(value1.compareTo(value2) < 0);
	}
	
}
