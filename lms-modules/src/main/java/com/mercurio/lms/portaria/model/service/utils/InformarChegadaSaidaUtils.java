package com.mercurio.lms.portaria.model.service.utils;


public class InformarChegadaSaidaUtils {
	
	public static Long getIdControle(String idControleTemp) {
		String[] tokens = splitByPipe(idControleTemp);
		String idControleString = tokens[0];
		return Long.valueOf(idControleString);
	}
	
	public static String getTipo(String idControleTemp) {
		String[] tokens = splitByPipe(idControleTemp);
		String tp = tokens[1];
		return tp;
	}

	private static String[] splitByPipe(String idControleTemp) {
		String[] tokens = idControleTemp.split("\\|");
		return tokens;
	}

	public static String formatarRota(Integer nrRota, String dsRota) {
		if(nrRota != null) {
			return nrRota + " - " + dsRota;
		}
		return dsRota;
	}

}
