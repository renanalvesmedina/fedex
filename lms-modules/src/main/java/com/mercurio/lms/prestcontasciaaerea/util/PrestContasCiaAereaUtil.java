package com.mercurio.lms.prestcontasciaaerea.util;

import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;

public class PrestContasCiaAereaUtil {

	/**
	 * Define a Data Final do Periodo de vendas, conforme data inicial.<BR>
	 * @param map Critérios de pesquisa : dtInicial, diaPeriodoVendas
	 * @return String da data final formatada dd/MM/yyyy
	 */
	public static String findDataFinal(YearMonthDay dtInicial, String diaPeriodoVendas){
		
		if (dtInicial == null || diaPeriodoVendas == null){
			return null;
		}
		
		YearMonthDay dtFinal = null; 
		
		if ("01".equals(diaPeriodoVendas)){
			dtFinal = JTDateTimeUtils.setDay(dtInicial, 10);
		}else if ("11".equals(diaPeriodoVendas)){
			dtFinal = JTDateTimeUtils.setDay(dtInicial, 20);
		}else if ("21".equals(diaPeriodoVendas)){
			dtFinal = JTDateTimeUtils.getLastDayOfYearMonthDay(dtInicial);
		}
		
		return JTFormatUtils.format(dtFinal, JTFormatUtils.MEDIUM);
	}
	
	
	
}
