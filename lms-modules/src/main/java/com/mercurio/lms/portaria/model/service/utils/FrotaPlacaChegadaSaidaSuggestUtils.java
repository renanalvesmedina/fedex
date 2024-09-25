package com.mercurio.lms.portaria.model.service.utils;

import org.apache.commons.lang.StringUtils;

public class FrotaPlacaChegadaSaidaSuggestUtils {
	public static final int QUANTIDADE_CARACTERES_NR_FROTA = 6;
	
	/**
	 * Retorna o n�mero da frota acrescido de quantos zeros a esquerda
	 * forem necess�rios para completar o n�mero da frota.
	 * @param 	numeroFrota	O n�mero da frota
	 * @return				O n�mero da frota acrescido de zeros a esquerda									
	 */
	public static String completarNumeroFrotaComZeros(String numeroFrota) {
		if(!StringUtils.isNumeric(numeroFrota)) {
			throw new IllegalArgumentException("O par�metro numeroFrota deve ser um n�mero.");
		}
		if(numeroFrota != null && numeroFrota.length() > QUANTIDADE_CARACTERES_NR_FROTA) {
			throw new IllegalArgumentException("O par�metro numeroFrota recebido � maior que a quantidade de caracteres de um n�mero de frota");
		}
		return StringUtils.leftPad(numeroFrota, QUANTIDADE_CARACTERES_NR_FROTA, '0');
	}

	public static boolean isNumeroFrotaValido(String numeroFrota) {
		return numeroFrota != null && numeroFrota.length() <= QUANTIDADE_CARACTERES_NR_FROTA;
	}
}
