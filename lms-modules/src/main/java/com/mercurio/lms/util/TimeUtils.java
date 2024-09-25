package com.mercurio.lms.util;


/**
 * 
 * Classe para funções utilitárias de manipulação de intervalos de vigências.
 * 
 * @author Felipe Ferreira
 * 
 */
public class TimeUtils {

	/**
	 * Recebe minutos e transforma em uma string no formato hh:MM.
	 * 
	 * @param minutes inteiro com os minutos. 
	 * @return horas e minutos - hh:MM.
	 */
	public static String minutesToHoursString(Integer minutes) {
		String str = null;
		if (minutes != null) {
			String hours = (Integer.valueOf(minutes.intValue() / 60)).toString();
			if (hours.length() < 2)
				hours = "0" + hours;
			
			String mins = (Integer.valueOf(minutes.intValue() % 60)).toString();
			if (mins.length() < 2)
				mins = "0" + mins;
			str = String.valueOf(hours + ":" + mins);	
		}
		return str;
	}

	/**
	 * Recebe minutos e transforma em horas.
	 * 
	 * @param minutes inteiro com os minutos. 
	 * @return horas.
	 */
	public static Integer minutesToHoursInteger(Integer minutes) {
		Integer hours = null;
		if (minutes != null)
			hours = Integer.valueOf(minutes.intValue() / 60);
		return hours;
	}
	
	/**
	 * Recebe horas e transforma em minutos.
	 * 
	 * @param hours inteiro com as horas. 
	 * @return minutos.
	 */
	public static Integer HoursToMinutesInteger(Integer hours) {
		Integer minutes = null;
		if (hours != null)
			minutes = Integer.valueOf(hours.intValue() * 60);
		return minutes;
	}
	
	public static Integer convertSecondToMiliSecond(Integer second) {
		Integer millisecond = null;
		
		if (second != null) {
			millisecond = Integer.valueOf((second.intValue() * 1000));
		}
		
		return millisecond;
	}
	
	public static String convertFractionToHourMinutes(Double valor) {
	    Integer totalMinutes = (int) (valor * 60);  
        
	    Integer hours = totalMinutes / 60;   
	    Integer minutes = totalMinutes % 60;  
	              
	    return String.format("%02d:%02d", hours, minutes);
	}
}
