package com.mercurio.lms.vol.utils;

import java.util.NoSuchElementException;
import java.util.Scanner;

import org.joda.time.DateTime;

import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTDateTimeUtils;

public class VolFomatterUtil {
	
	/**
	 * Converte o conteudo do parametro geral "VOL_TIMER" em segundos
	 * @param timerValue
	 * @return
	 */
	public static Integer formatTimerInSeconds(String timerValue){
		
		Integer timer;
		Integer minutos;
		Integer segundos;
		
		try{
			Scanner scanner = new Scanner(timerValue).useDelimiter(":");			
			minutos = Integer.valueOf(scanner.next());
			
			segundos  = Integer.valueOf(scanner.next());
			segundos = (segundos == null ? 0 : segundos) ;
				
		}catch (NoSuchElementException e) {
			return timer = (Integer.valueOf(timerValue) * 60);
		}catch (Exception e) {
			return timer = (Integer.valueOf(5) * 60);
		}
		
		timer = (minutos * 60) + segundos;
		
		return timer;
		
	}
	
	/**
	 * reformata o número da frota sem os zeros à esquerda caso a frota retorne null
	 * @param nrFrota
	 * @return
	 */
	public static String reformataNroFrota (String nrFrota) {
		for( int i = 0; i < nrFrota.length(); i++ ){
			char caracter = nrFrota.charAt(i);
			if( caracter != '0' ){
				nrFrota = nrFrota.substring( i );
				return nrFrota;
			}
		}
		return nrFrota;
	}
	
	/**
	 * Remove os separadores da String e retorna um DateTime a partir dessa String
	 * caso tenha Filial setada na sessão retorna o DateTime com o timezone
	 * @param dateString
	 * @return DateTime
	 */
	public static DateTime formatStringToDateTime(String dateString){
		return  JTDateTimeUtils.formatStringToDateTimeWithTimeZone( dateString.replace("_", " ").replace("D", "/").replace("H", ":") );	
						
	}
	
	public static DateTime formatStringToDateTime(String dateString, Filial filial){
		return  JTDateTimeUtils.formatStringToDateTimeWithTimeZone( dateString.replace("_", " ").replace("D", "/").replace("H", ":"), filial);	

}
	
}
