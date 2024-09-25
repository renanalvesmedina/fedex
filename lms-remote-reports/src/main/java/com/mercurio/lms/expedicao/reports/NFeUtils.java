package com.mercurio.lms.expedicao.reports;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.expedicao.reports.CTeUtils;

/**
 * 
 * @author RafaelKF
 * 
 */
public class NFeUtils extends CTeUtils {

	public static String formatCep(String cep) {
		String retorno = "";
		if (cep != null && cep != "") {
			if (cep.length() == 8) {
				retorno += cep.substring(0, 5) + "-" + cep.substring(5, 8);
			} else {
				retorno = cep;
			}
		}
		return retorno;
	}

	public static String formatCpfCnpj(String nrIdentificacao) {
		String retorno = null;
		if(StringUtils.isNotBlank(nrIdentificacao)){
			if(nrIdentificacao.length() < 12){
				retorno = formatCPF(nrIdentificacao);
			}else{
				retorno = formatCNPJ(nrIdentificacao);
			}
		}
		return retorno;
	}
	
	public static String formatDateYearMonthDayToString(YearMonthDay date) {
		if(date != null) { 
			return date.toString("dd/MM/yyyy");
		} else {
			return null;
		}
	}
	
	public static BigDecimal formatValorToBigDecimal(String valor) {
		if(StringUtils.isNotBlank(valor)) {
			valor = valor.replace("." , "") ;
			valor = valor.replace("," , ".") ;
			return new BigDecimal(valor);
		} else {
			return null;
		}
	}
	
}
