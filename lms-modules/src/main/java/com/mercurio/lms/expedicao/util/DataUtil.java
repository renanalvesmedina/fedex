package com.mercurio.lms.expedicao.util;

import java.text.SimpleDateFormat;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class DataUtil {
	
	public static String formatarData(DateTime dataConsulta){		
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(dataConsulta.toDate());		
		return dataFormatada;
	}
	
	public static Integer subtraiDataDataAtual (DateTime dataInicial ) {		
		Duration dur = new Duration(dataInicial,new DateTime());
		
		return dur.toStandardSeconds().getSeconds();	
	}
}
