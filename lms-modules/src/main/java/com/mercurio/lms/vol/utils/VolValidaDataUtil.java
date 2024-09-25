package com.mercurio.lms.vol.utils;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class VolValidaDataUtil {
	
	/**
	 * Calcula a diferença entre a data/hora do celular e a data/hora do servidor. 
	 *  
	 * @param dataTimeCelular
	 * @param dataTimeServidor
	 * @param toleranciaDataHora
	 * @return false - se a diferença é maior que o parametro toleranciaHorario
	 * 		   true - se a diferença é menor ou igual que o parametro toleranciaHorario
	 */
	public static Boolean isDateTimeCelularOk(DateTime dataTimeCelular, DateTime dataTimeServidor, Long toleranciaDataHora){	
		Duration diferencaEntreDatas = new Duration(dataTimeCelular.getMillis(), dataTimeServidor.getMillis());
			
		if(Math.abs(diferencaEntreDatas.getMillis()) > (toleranciaDataHora * 60 * 1000) ){
			return false;
		}
		
		return true;		
	}

}
