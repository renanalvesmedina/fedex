package com.mercurio.lms.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.lms.configuracoes.model.Moeda;

/**
 * Classe utilitária para manipulação de Moeda.
 */

public class MoedaUtils {
	/**
	 * Método que recebe uma lista de moeda e tranforma ela numa 
	 * lista de map com o campo descricao (moeda.getSgMoeda() + 
	 * " " + moeda.getDsSimbolo())
	 * 
	 * @param List lstMoeda
	 * @return List lista de map
	 * */
	public static List moedaToMap(List<Moeda> moedas){
		List lstMap = new ArrayList();
		Map map = null;
		for (Moeda moeda : moedas) {
			map = new HashMap();
			map.put("idMoeda", moeda.getIdMoeda());
			map.put("dsMoeda", moeda.getDsMoeda());
			map.put("sgMoeda", moeda.getSgMoeda());
			map.put("dsSimbolo", moeda.getDsSimbolo());
			map.put("tpSituacao", moeda.getTpSituacao());
			map.put("descricao", moeda.getSgMoeda() + " " + moeda.getDsSimbolo());
			lstMap.add(map);
		}
		return lstMap;
	}

	/**
	 * Converte um valor numerico e um objeto do tipo Moeda para sua representação textual
	 * conforme o Locale do usuário logado.
	 * @param valor
	 * 			Qualquer objeto de número (Byte, Double, Float, Integer)
	 * @param moeda
	 * 			Objeto do tipo Moeda do LMS
	 * @return
	 * 			Valor expresso textualmente
	 */
	public static String formataPorExtenso(Number valor, Moeda moeda) {
		Locale locale = LocaleContextHolder.getLocale();
		return formataPorExtenso(valor, moeda, locale);
	}

	/**
	 * Converte um valor numerico e um objeto do tipo Moeda para sua representação textual
	 * conforme o informado.
	 * @param valor
	 * 			Qualquer objeto de número (Byte, Double, Float, Integer)
	 * @param moeda
	 * 			Objeto do tipo Moeda do LMS
	 * @param locale
	 * 			Locale onde os dados devem ser formatados
	 * @return
	 * 			Valor expresso textualmente
	 */
	public static String formataPorExtenso(Number valor, Moeda moeda, Locale locale) {
		String result = SpellUtils.formataPorExtenso(valor, SpellUtils.getQualifiers(moeda, locale), locale);
		if(StringUtils.isNotBlank(result)) {
			return result.toLowerCase();
		}
		return result;
	}
}
