package com.mercurio.lms.expedicao.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * ...
 * @autor juliosce
 * @since 13/02/2007 18:41:47
 */
public final class StringUtils {
	private static final Pattern NUMBERS = Pattern.compile("\\d");//[0-9]
	private static final Pattern GROUP_NUMBERS = Pattern.compile("(\\d+)");//[0-9]+

	/**
	 * Retorna todos números inteiros de uma String.
	 * @param string
	 * @return
	 */
	public static Integer[] getIntegers(String string){
		String[] result = getGroups(string, GROUP_NUMBERS);
		List<Integer> integers = new ArrayList<Integer>(result.length);
		for(String integer : result){
			try{
				integers.add(Integer.valueOf(integer));
			} catch(NumberFormatException e) {
				continue;
			}
		}
		return integers.toArray(new Integer[integers.size()]);
	}
	
	/**
	 * Troca a <b>primeira</b> ocorrência de um número localizada pelo pattern passado pelo texto especificado.
	 * @param string
	 * @param what
	 * @return
	 */
	public static String replaceFirstNumber(String string, String what){
		return replaceFirst(string, GROUP_NUMBERS, what);
	}
	
	/**
	 * Retorna a primeira ocorrência de um Inteiro.
	 * @param string
	 * @return
	 */
	public static Integer getFirstInteger(String string){
		Matcher m = GROUP_NUMBERS.matcher(string);
		while(m.find()){
			try{
				return Integer.valueOf(m.group());
			} catch(NumberFormatException e) {
				continue;
			}
		}

		return null;
	}

	/**
	 * Retorna o primeiro número encontrado.
	 * @param string
	 * @return
	 */
	public static String getFirstNumber(String string){
		Matcher m = GROUP_NUMBERS.matcher(string);
		if (m.find()) return m.group();
		return "";
	}
	
	/**
	 * Troca <b>todos</b> os números de uma String por uma outra String. 
	 * @param string
	 * @param what
	 * @return
	 */
	public static String replaceAllNumbers(String string, String what){
		return replaceAll(string, GROUP_NUMBERS, what);
	}

	/**
	 * Retorna quantos números há em uma String.
	 * @param string
	 * @return
	 */
	public static int countNumbers(String string){
		if(string == null) return 0;
		return countGroup(string, GROUP_NUMBERS);
	}
	
	/**
	 * Conta quantos inteiros há na String.
	 * @param string
	 * @return
	 */
	public static int countIntegers(String string){
		Matcher m = GROUP_NUMBERS.matcher(string);
		int countIntegers = 0;

		while(m.find()){
			try{
				Integer.parseInt(m.group());
				++countIntegers;
			} catch(NumberFormatException e) {
				continue;
			}
		}
		return countIntegers;
	}

	/**
	 * Verifica se existe números em uma String;
	 * @param string
	 * @return
	 */
	public static boolean containsNumber(String string){
		if(string == null) return false;

		Matcher matcher = NUMBERS.matcher(string);
		return matcher.find();
	}

	/**
	 * Contabiliza o número de ocorrências de um grupo de caracteres.
	 * Exemplo:
	 * (\\d+): Contará quantos digitos numéricos há em uma String;
	 * (\\s+): Contará quantos espaços há em uma String;
	 * @param string
	 * @param group
	 * @return
	 */
	private static int countGroup(String string, Pattern group){
		Matcher matcher = group.matcher(string);
		int countOccurrences = 0;
		while(matcher.find()) ++countOccurrences;
		return countOccurrences;
	}
	
	/**
	 * Retorna um array de Strings de um determinado grupo de caracteres.
	 * @param string
	 * @param group
	 * @return
	 */
	private static String[] getGroups(String string, Pattern group){
		Matcher matcher = group.matcher(string);
		List<String> result = new ArrayList<String>();
		while(matcher.find()) result.add(matcher.group());

		return result.toArray(new String[result.size()]);
	}

	/**
	 * Troca a <b>primeira</b> ocorrência da String localizada pelo pattern passado por um texto especificado.
	 * @param string
	 * @param pattern
	 * @param what
	 * @return
	 */
	private static String replaceFirst(String string, Pattern pattern, String what){
		return pattern.matcher(string).replaceFirst(what);
	}

	/**
	 * Troca a <b>todas</b> ocorrências de uma String localizada pelo pattern passado por um texto especificado.
	 * @param string
	 * @param pattern
	 * @param what
	 * @return
	 */
	private static String replaceAll(String string, Pattern pattern, String what){
		return pattern.matcher(string).replaceAll(what);
	}

	/**
	 * Método retorna um conjunto de strings separados por vírgula e espaço.
	 * 
	 * @param tokens
	 * @return
	 */
	public static String fillCommaBetweenTokens(String...tokens){
		if(tokens == null || tokens.length == 0) return "";
		StringBuilder ret = new StringBuilder();

		for(int i = 0; i < tokens.length; i++)
			ret.append((tokens.length > 1 && i > 0 ? ", " : "")).append(tokens[i]);

		return ret.toString();
	}
	/**
	 * Método para remover os tab space de uma string.
	 * 
	 * @param string
	 * @return
	 */
	public static String removeTabSpace(String string){
		if (string == null)
			return "";
		return string.replaceAll("\t", "");
	}
	
	public static List<String> splitByNumeroCaracteres(String texto, int numeroCaracteres){
		
		List<String> listaRetorno = new ArrayList<String>();
		if(texto != null && texto.length() == 0){
			return listaRetorno;
		}
		if(numeroCaracteres == 0){
			listaRetorno.add( texto );
			return listaRetorno;
		}
		
		int inicioQuebra = 0;
		int fimQuebra = numeroCaracteres;
		int max = texto.length();
		
		while(inicioQuebra < max){
			if(fimQuebra > max){
				fimQuebra = max;
			}
			listaRetorno.add( texto.substring(inicioQuebra, fimQuebra) );
			inicioQuebra = fimQuebra;
			fimQuebra = fimQuebra+numeroCaracteres;
		};
		
		return listaRetorno;
	}
}