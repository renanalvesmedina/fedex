package com.mercurio.lms.portaria.model.service.utils;

import org.apache.commons.lang.StringUtils;

public class FrotaPlacaChegadaSaidaSuggestUtils {
	public static final int QUANTIDADE_CARACTERES_NR_FROTA = 6;
	
	/**
	 * Retorna o número da frota acrescido de quantos zeros a esquerda
	 * forem necessários para completar o número da frota.
	 * @param 	numeroFrota	O número da frota
	 * @return				O número da frota acrescido de zeros a esquerda									
	 */
	public static String completarNumeroFrotaComZeros(String numeroFrota) {
		if(!StringUtils.isNumeric(numeroFrota)) {
			throw new IllegalArgumentException("O parâmetro numeroFrota deve ser um número.");
		}
		if(numeroFrota != null && numeroFrota.length() > QUANTIDADE_CARACTERES_NR_FROTA) {
			throw new IllegalArgumentException("O parâmetro numeroFrota recebido é maior que a quantidade de caracteres de um número de frota");
		}
		return StringUtils.leftPad(numeroFrota, QUANTIDADE_CARACTERES_NR_FROTA, '0');
	}

	public static boolean isNumeroFrotaValido(String numeroFrota) {
		return numeroFrota != null && numeroFrota.length() <= QUANTIDADE_CARACTERES_NR_FROTA;
	}
}
