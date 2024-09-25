package com.mercurio.lms.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.FieldPosition;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.lms.configuracoes.model.Moeda;

/**
 * @author Anibal Maffioletti de Deus
 * @since 13/Apr/2006
 */
public class SpellUtils {
	private static final int SINGULAR 	= 0;
	private static final int PLURAL 	= 1;
	private static final String SPACE	= " ";

	/**
	 * Transforma um valor numerico na sua representação textual, sem atribuir
	 * valores para a unidade e decimal.
	 * @param valor
	 * 			Valor numerico a ser convertido
	 * @param locale
	 * 			Locale em que as chaves para os valores devem ser obtidas.
	 * @return
	 * 			Valor expresso em textualmente, com um apontamento para unidade e decimal onde 
	 * 			{0} = decimal singular
	 * 			{1} = decimal plural
	 * 			{2} = unidade singular
	 * 			{3} = unidade plural
	 * 			
	 */
	public static String formataPorExtenso(Number valor, Locale locale) {
		return valor!=null ? spell(valor, locale) : spell(new Double(0), locale);
	}
	
	/**
	 * Transforma um valor numerico na sua representação textual, usando o locale 
	 * do usuário, sem atribuir valores para a unidade e decimal.
	 * @param valor
	 * 			Valor numerico a ser convertido
	 * @return
	 * 			Valor expresso em textualmente, com um apontamento para unidade e decimal onde 
	 * 			{0} = decimal singular
	 * 			{1} = decimal plural
	 * 			{2} = unidade singular
	 * 			{3} = unidade plural
	 * 			
	 */
	public static String formataPorExtenso(Number valor) {
		Locale locale = LocaleContextHolder.getLocale();
		return formataPorExtenso(valor, locale);
	}

	/**
	 * Transforma um valor numerico na sua representação textual, usando um array
	 * de string para qualificar a unidade e seus decimais
	 * @param valor
	 * 			Valor numerico a ser convertido
	 * @param qualificadores
	 * 			Array contendo as chaves para os qualificadores, onde
	 * 			0 = singular do decimal
	 * 			1 = plural do decimal
	 * 			2 = singular da unidade
	 * 			3 = plural da unidade
	 * @return
	 * 			Valor expresso textualmente.
	 */
	public static String formataPorExtenso(Number valor, String[] qualificadores, Locale locale) {
		StringBuffer saida = new StringBuffer();
		String spelledValue = formataPorExtenso(valor, locale);
		new MessageFormat(spelledValue).format(qualificadores, saida, new FieldPosition(0));
		return saida.toString();
	}
	
	/**
	 * Transforma um valor numerico na sua representação textual, usando um array
	 * de string para qualificar a unidade e seus decimais
	 * @param valor
	 * 			Valor numerico a ser convertido
	 * @param qualificadores
	 * 			Array contendo as chaves para os qualificadores, onde
	 * 			0 = singular do decimal
	 * 			1 = plural do decimal
	 * 			2 = singular da unidade
	 * 			3 = plural da unidade
	 * @param locale
	 * 			Locale em que as chaves para os valores devem ser obtidas.
	 * @return
	 * 			Valor expresso textualmente.
	 */
	public static String formataPorExtenso(Number valor, String[] qualificadores) {
		Locale locale = LocaleContextHolder.getLocale();
		return formataPorExtenso(valor, qualificadores, locale);
	}

	/**
	 * Função para gerar nomes de Qualificadores
	 * @param kind
	 * @return
	 */
	public static String[] getQualifiers(Moeda moeda, Locale locale) {
		String[] qualifiersMoeda = moeda.getDsValorExtenso().getValue(locale).split(";");
		if (qualifiersMoeda.length < 4) 
			throw new InfrastructureException("LMS-xxxxx: A definir!"); 
		return qualifiersMoeda;
	}
	
	/**
	 * Retorna valores para grandezas numericas
	 */
	private static String qualifiers(int scale, int plural, Locale locale) {
		//TODO: INTERNACIONALIZAR
		String QUALIFICADORES[][] = {
			{"{0}", "{1}"},
			{"{2}", "{3}"},
			{"mil", "mil"},
			{"milhão", "milhões"},
			{"bilhão", "bilhões"},
			{"trilhão", "trilhões"},
			{"quatrilhão", "quatrilhões"},
			{"quintilhão", "quintilhões"},
			{"sextilhão", "sextilhões"},
			{"septilhão", "septilhões"}
			};
		
		return QUALIFICADORES[scale][plural];
	}

	/**
	 * Retorna valores por extenso para numeros.
	 */
	private static String numbers(int x, int dezena, Locale locale) {
		//TODO: INTERNACIONALIZAR
		String NUMBERS[][] = {
				{"zero", "um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito", "nove", "dez",
				"onze", "doze", "treze", "quatorze", "quinze", "desesseis", "desessete", "dezoito", "desenove"},
				{"vinte", "trinta", "quarenta", "cinquenta", "sessenta", "setenta", "oitenta", "noventa"},
				{"cem", "cento", "duzentos", "trezentos", "quatrocentos", "quinhentos", "seiscentos",
				"setecentos", "oitocentos", "novecentos"}
				};
		return NUMBERS[x][dezena];
	}
	
	/**
	 * Retorna valor do separador de unidades.
	 */
	private static String and(Locale locale) {
		//TODO: INTERNACIONALIZAR
		return " e ";
	}
	
	/**
	 * Retorna um mapa com os valores categorizado em grupos de milhares 
	 */
	private static ArrayList setNumber(BigDecimal dec) {
		ArrayList nro = new ArrayList();
		BigInteger num;
		// Converte para inteiro arredondando os centavos
		num = dec
			.setScale(2, BigDecimal.ROUND_HALF_UP)
			.multiply(BigDecimal.valueOf(100))
			.toBigInteger();

		// Adiciona valores
		nro.clear();
		if (num.equals(BigInteger.ZERO)) {
			// Centavos
			nro.add(Integer.valueOf(0));
			// Valor
			nro.add(Integer.valueOf(0));
		}
		else {
			// Adiciona centavos
			num = addRemainder(100, nro, num);
			
			// Adiciona grupos de 1000
			while (!num.equals(BigInteger.ZERO)) {
				num = addRemainder(1000, nro, num);
			}
		}
		return nro;
	}
	
	/**
	 * Função que transforma o valor numerico em valor por extenso 
	 */
	private static String spell(Number value, Locale locale) {
		return spell(new BigDecimal(value.doubleValue()), locale);
	}

	/**
	 * Função que transforma o valor numerico em valor por extenso 
	 */
	private static String spell(BigDecimal value, Locale locale) {
		StringBuffer buf = new StringBuffer();
		
		ArrayList nro = setNumber(value);
		int ct;

		for (ct = nro.size() - 1; ct > 0; ct--) {
			// Se ja existe texto e o atual não é zero
			if (buf.length() > 0 && ! isZeroGroup(ct, nro)) {
				buf.append(and(locale));
			}
			buf.append(numToString(((Integer) nro.get(ct)).intValue(), ct, locale));
		}
		if (buf.length() > 0) {
			if (isOnlyGroup(nro))
				buf.append(" de ");
			while (buf.toString().endsWith(SPACE))
				buf.setLength(buf.length()-1);
			if (((Integer) nro.get(0)).intValue() != 0) {
				buf.append(and(locale));
			}
		}
		if (((Integer) nro.get(0)).intValue() != 0) {
			buf.append(numToString(((Integer) nro.get(0)).intValue(), 0, locale));
		}
		return buf.toString();
	}

	private static BigInteger addRemainder(int divisor, ArrayList nro, BigInteger num) {
		// Encontra newNum[0] = num modulo divisor, newNum[1] = num dividido divisor
		BigInteger[] newNum = num.divideAndRemainder(BigInteger.valueOf(divisor));

		// Adiciona modulo
		nro.add(Integer.valueOf(newNum[1].intValue()));

		return newNum[0];
	}


	private static boolean isOnlyGroup(ArrayList nro) {
		if (nro.size() <= 3)
			return false;
		if (!isZeroGroup(1, nro) && !isZeroGroup(2, nro))
			return false;
		boolean hasOne = false;
		for(int i=3; i < nro.size(); i++) {
			if (((Integer)nro.get(i)).intValue() != 0) {
				if (hasOne)
					return false;
				hasOne = true;
			}
		}
		return true;
	}

	private static boolean isZeroGroup(int ps, ArrayList nro) {
		if (ps <= 0 || ps >= nro.size())
			return true;
		return ((Integer)nro.get(ps)).intValue() == 0;
	}
	
	private static String numToString(int numero, int escala, Locale locale) {
		int unidade = (numero % 10);
		int dezena = (numero % 100); //* nao pode dividir por 10 pois verifica de 0..19
		int centena = (numero / 100);
		StringBuffer buf = new StringBuffer();

		if (numero != 0) {
			if (centena != 0) {
				if (dezena == 0 && centena == 1) {
					buf.append(numbers(2, 0, locale));
				}
				else {
					buf.append(numbers(2, centena, locale));
				}
			}

			if ((buf.length() > 0) && (dezena != 0)) {
				buf.append(and(locale));
			}
			if (dezena > 19) {
				dezena /= 10;
				buf.append(numbers(1, dezena - 2, locale));
				if (unidade != 0) {
					buf.append(" e ");
					buf.append(numbers(0, unidade, locale));
				}
			}
			else if (centena == 0 || dezena != 0) {
				buf.append(numbers(0, dezena, locale));
			}

			buf.append(" ");
			if (numero == 1) {
				buf.append(qualifiers(escala, SINGULAR, locale));
			}
			else {
				buf.append(qualifiers(escala, PLURAL, locale));
			}
		}

		return buf.toString();
	}
}
