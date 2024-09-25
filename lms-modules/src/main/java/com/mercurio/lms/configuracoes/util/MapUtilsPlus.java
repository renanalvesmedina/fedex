package com.mercurio.lms.configuracoes.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.StringI18n;

/**
 * Classe que implementa os gettres de um mapa , porem com um melhor controle de null pointer
 * do que a solução do commons collections a MapUtils. E contendo varios metodos utilitarios.
 * @author Diego Pacheco, Rodrigo Dias, Alexandre Poletto - GT5 - LMS
 * @version 14.0
 */
public class MapUtilsPlus implements Serializable {
	private Map mapImpl = new HashMap();

	public MapUtilsPlus(Map map) {
		mapImpl.putAll(map);
	}

	/**
	 * Metodo que captura um Object apartir de uma chave no map.
	 * 
	 * @author Diego Pacheco - GT5 - LMS
	 * @version 2.0
	 * @param map
	 * @param key
	 * @param defaultValue
	 * @return Object com o valor ou o valor de defaultValue 
	 */
	public static Object getObject(Map<String, ?> map, String key, Object defaultValue) {
		if (map==null) return defaultValue;
		if (map.get(key)==null) return defaultValue;
		return map.get(key);
	}
	public static Object getObject(Map<String, ?> map, String key) {
		return getObject(map, key, null);
	}
	public Object getObject(String key) {
		return getObject(mapImpl, key);
	}

	/**
	 * Metodo que captura um Long apartir de uma key no map.
	 * 
	 * @author Diego Pacheco - GT5 - LMS
	 * @version 2.0
	 * @param map
	 * @param key
	 * @param defaultValue
	 * @return Long com o valor ou o valor de defaultValue 
	 */
	public static Long getLong(Map<String, ?> map,String key,Long defaultValue) {
		if (map==null) return defaultValue;
		if (map.get(key)==null) return defaultValue;

		if (map.get(key) instanceof Long) {
			return (Long) map.get(key);
		}
		if (map.get(key) instanceof java.math.BigDecimal) {
			return Long.valueOf(((java.math.BigDecimal)map.get(key)).longValue());
		}
		if (map.get(key) instanceof String) {
			if (map.get(key).toString().trim().equals("")) return defaultValue;
			return Long.valueOf(map.get(key).toString());
		}
		if (map.get(key) instanceof Integer) {
			return Long.valueOf((Integer)map.get(key));
		}
		
		return defaultValue;
	}
	public static Long getLong(Map<String, ?> map,String key) {
		return getLong(map, key, null);
	}
	public Long getLong(String key) {
		return getLong(mapImpl, key);
	}

	public static Long getLongCaseInsensitive(Map<String, ?> map,String key,Long defaultValue) {
		Object retorno = getLong(map,key,defaultValue);
		if (retorno==defaultValue) {
			retorno = getLong(map,key.toUpperCase(),defaultValue);
			if (retorno==defaultValue) {
				retorno = getLong(map,key.toLowerCase(),defaultValue);
			}
		}
		return (Long)retorno;
	}

	/**
	 * Metodo que captura uma String apartir de uma key no map.
	 * 
	 * @author Diego Pacheco - GT5 - LMS
	 * @version 1.0
	 * @param map
	 * @param key
	 * @param defaultValue
	 * @return String com o valor ou valor de defaultValue
	 */
	public static String getString(Map<String, ?> map,String key,String defaultValue) {
		if (map==null) return defaultValue;
		if (map.get(key)==null) return defaultValue;

		if (map.get(key) instanceof String) {
			if (map.get(key).toString().trim().equals("")) return defaultValue;
			return map.get(key).toString();
		}
		return defaultValue;
	}
	public static String getString(Map<String, ?> map,String key) {
		return getString(map, key, null);
	}
	public String getString(String key) {
		return getString(mapImpl, key);
	}

	public static String getStringCaseInsensitive(Map<String, ?> map,String key,String defaultValue) {
		Object retorno = getString(map,key,defaultValue);
		if (retorno==defaultValue) {
			retorno = getString(map,key.toUpperCase(),defaultValue);
			if (retorno==defaultValue) {
				retorno = getString(map,key.toLowerCase(),defaultValue);
			}
		}
		return (String)retorno;
	}

	/**
	 * Metodo que captura um BigDecimal apartir de uma key no map.
	 * 
	 * @author Diego Pacheco - GT5 - LMS
	 * @version 1.0
	 * @param map
	 * @param key
	 * @param defaultValue
	 * @return String com o valor ou valor de defaultValue
	 */
	public static BigDecimal getBigDecimal(Map<String, ?> map, String key, BigDecimal defaultValue) {
		if (map==null) return defaultValue;
		if (map.get(key)==null) return defaultValue;
		
		if (map.get(key) instanceof BigDecimal) {			
			return (BigDecimal)map.get(key);
		}
		if (map.get(key) instanceof String) {
			if (map.get(key).toString().trim().equals("")) return defaultValue;
			return new BigDecimal(map.get(key).toString());
		}
		return defaultValue;
	}
	public static BigDecimal getBigDecimal(Map<String, ?> map,String key) {
		return getBigDecimal(map, key, null);
	}
	public BigDecimal getBigDecimal(String key) {
		return getBigDecimal(mapImpl, key);
	}

	public static BigDecimal getBigDecimalCaseInsensitive(Map<String, ?> map,String key,BigDecimal defaultValue) {
		Object retorno = getBigDecimal(map,key,defaultValue);
		if (retorno==defaultValue) {
			retorno = getBigDecimal(map,key.toUpperCase(),defaultValue);
			if (retorno==defaultValue) {
				retorno = getBigDecimal(map,key.toLowerCase(),defaultValue);
			}
		}
		return (BigDecimal)retorno;
	}

	/**
	 * Gets a Boolean from a Map.
	 * 
	 * @param map the map to use
	 * @param key the key to look up
	 * @param defaultValue
	 * @return Boolean com o valor ou o valor de defaultValue
	 */
	public static Boolean getBoolean(Map<String, ?> map, String key, Boolean defaultValue) {
		if(map == null) {
			throw new IllegalArgumentException("The argument map cannot be null.");
		}

		Object value = map.get(key);
		if(value == null) {
			return defaultValue;
		} else if(value instanceof String) {
			String strValue = value.toString();
			if(StringUtils.isNotBlank(strValue)) {
				if(strValue.equalsIgnoreCase("S")
						|| strValue.equalsIgnoreCase("true")
						|| strValue.equalsIgnoreCase("Y")
						|| strValue.equalsIgnoreCase("sim")
						|| strValue.equalsIgnoreCase("yes")
				) {
					return Boolean.TRUE;
				}
				return Boolean.FALSE;
			}
			return defaultValue;
		}
		return (Boolean)value;
	}

	public static Boolean getBoolean(Map<String, ?> map, String key) {
		return getBoolean(map, key, null);
	}

	/**
	 * Metodo que captura uma Integer apartir de uma key no map.
	 * 
	 * @author Andre Valadas - GT4 - LMS
	 * @version 1.0
	 * @param map
	 * @param key
	 * @param defaultValue
	 * @return String com o valor ou valor de defaultValue
	 */
	public static Integer getInteger(Map<String, ?> map, String key, Integer defaultValue) {
		if (map == null || map.get(key) == null) {
			return defaultValue;
		}
		Object value = map.get(key);
		if (value instanceof Integer)
			return (Integer) value;
		return (Integer) ReflectionUtils.toObject(String.valueOf(value), Integer.class);
	}
	public static Integer getInteger(Map<String, ?> map, String key) {
		return getInteger(map, key, null);
	}
	public Integer getInteger(String key) {
		return getInteger(mapImpl, key);
	}

	/**
	 * Metodo que captura uma Double apartir de uma key no map.
	 * 
	 * @author Andre Valadas - GT4 - LMS
	 * @version 1.0
	 * @param map
	 * @param key
	 * @param defaultValue
	 * @return String com o valor ou valor de defaultValue
	 */
	public static Double getDouble(Map<String, ?> map, String key, Double defaultValue) {
		if (map == null || map.get(key) == null) {
			return defaultValue;
		}
		Object value = map.get(key);
		if (value instanceof Double)
			return (Double) value;
		return (Double) ReflectionUtils.toObject(String.valueOf(value), Double.class);
	}
	public static Double getDouble(Map<String, ?> map, String key) {
		return getDouble(map, key, null);
	}
	public Double getDouble(String key) {
		return getDouble(mapImpl, key);
	}

	/**
	 * Metodo que captura uma YearMonthDay apartir de uma key no map.
	 * 
	 * @author Andre Valadas - GT4 - LMS
	 * @version 1.0
	 * @param map
	 * @param key
	 * @param defaultValue
	 * @return String com o valor ou valor de defaultValue
	 */
	public static YearMonthDay getYearMonthDay(Map<String, ?> map, String key, YearMonthDay defaultValue) {
		if (map == null || map.get(key) == null) {
			return defaultValue;
		}
		Object value = map.get(key);
		if (value instanceof YearMonthDay)
			return (YearMonthDay) value;
		return (YearMonthDay) ReflectionUtils.toObject(String.valueOf(value), YearMonthDay.class);
	}
	public static YearMonthDay getYearMonthDay(Map<String, ?> map, String key) {
		return getYearMonthDay(map, key, null);
	}
	public YearMonthDay getYearMonthDay(String key) {
		return getYearMonthDay(mapImpl, key);
	}

	/**
	 * Metodo que captura uma DomainValue apartir de uma key no map.
	 * 
	 * @author Andre Valadas - GT4 - LMS
	 * @version 1.0
	 * @param map
	 * @param key
	 * @param defaultValue
	 * @return String com o valor ou valor de defaultValue
	 */
	public static DomainValue getDomainValue(Map<String, ?> map, String key, DomainValue defaultValue) {
		if (map == null || map.get(key) == null) {
			return defaultValue;
		}
		Object value = map.get(key);
		if (value instanceof DomainValue)
			return (DomainValue) value;
		return (DomainValue) ReflectionUtils.toObject(String.valueOf(value), DomainValue.class);
	}
	public static DomainValue getDomainValue(Map<String, ?> map, String key) {
		return getDomainValue(map, key, null);
	}
	public DomainValue getDomainValue(String key) {
		return getDomainValue(mapImpl, key);
	}

	/**
	 * Metodo que captura uma StringI18n apartir de uma key no map.
	 * 
	 * @author Andre Valadas - GT4 - LMS
	 * @version 1.0
	 * @param map
	 * @param key
	 * @param defaultValue
	 * @return String com o valor ou valor de defaultValue
	 */
	public static StringI18n getStringI18n(Map<String, ?> map, String key, StringI18n defaultValue) {
		if (map == null || map.get(key) == null) {
			return defaultValue;
		}
		Object value = map.get(key);
		if (value instanceof StringI18n)
			return (StringI18n) value;
		return (StringI18n) ReflectionUtils.toObject(String.valueOf(value), StringI18n.class);
	}
	public static StringI18n getStringI18n(Map<String, ?> map, String key) {
		return getStringI18n(map, key, null);
	}
	public StringI18n getStringI18n(String key) {
		return getStringI18n(mapImpl, key);
	}

	/**
	 * Metodo que captura um List apartir de uma key no map.
	 * 
	 * @author Diego Pacheco - GT5 - LMS
	 * @version 1.0
	 * @param map
	 * @param key
	 * @param defaultValue
	 * @return List com o valor ou o valor de defaultValue
	 */
	public static List getList(Map<String, ?> map,String key,List defaultValue) {
		if (map==null) return defaultValue;
		if (map.get(key)==null) return defaultValue;

		if (map.get(key) instanceof List) {
			if (((List) map.get(key)).size()==0) return defaultValue;
			return (List) map.get(key);
		}

		return defaultValue;
	}
	
	/**
	 * Metodo que captura um List apartir de uma key no map. Caso o map retorne um Object
	 * o metodo retorna uma lista contendo o Object
	 *
	 * @author Alexandre Poletto - GT5 - LMS
	 * @version 1.0
	 * @param map
	 * @param key
	 * @param defaultValue
	 * @return List com o valor ou o valor de defaultValue
	 */
	public static List getListEver(Map<String, ?> map,String key, List defaultValue) {
		if (map==null) return defaultValue;
		if (map.get(key)==null) return defaultValue;

		if (map.get(key) instanceof List) {
			if (((List) map.get(key)).size()==0) return defaultValue;
			return (List) map.get(key);
		} else {
			List retorno = new ArrayList();
			retorno.add(map.get(key));
			return retorno;
		}
	}

	/**
	 * Meotod que voce passa um Map e uma key e ele retorna o valor primitivo long do registro.
	 * 
	 * @author Diego Pacheco - GT5 - LMS
	 * @version 1.0
	 * @param dados eh um Map com os dados.
	 * @param key eh uma String q eh a key do map q vc devejah pegar o valor.
	 * @return long com o valor.
	 */
	public static long longByMap(Map<String, ?> map, String key) {
		if (map==null || map.size()==0) return 0;
		return Long.valueOf(map.get(key).toString()).longValue();
	}

	/**
	 * Meotod que voce passa um Map e uma key e ele retorna o valor primitivo int do registro.
	 * 
	 * @author Diego Pacheco - GT5 - LMS
	 * @version 1.0
	 * @param dados eh um Map com os dados.
	 * @param key eh uma String q eh a key do map q vc devejah pegar o valor.
	 * @return int com o valor.
	 */
	public static int intByMap(Map<String, ?> map, String key) {
		if (map==null || map.size()==0) return 0;
		return Long.valueOf(map.get(key).toString()).intValue();
	}

	/**
	 * Meotod que voce passa um Map e uma key e ele retorna o valor primitivo int do registro.
	 * Exploder significa que se o valor for assim por ex: 1.500,25
	 * ele vai ser explodido retornando um valor da seguinte forma: 150025
	 * 
	 * @author Diego Pacheco - GT5 - LMS
	 * @version 1.0
	 * @param dados eh um Map com os dados.
	 * @param key eh uma String q eh a key do map q vc devejah pegar o valor.
	 * @return int com o valor.
	 */
	public static int intByMapExploder(Map<String, ?> map, String key) {		
		if ( (map==null) || (map.size()==0) || (map.get(key)==null) ) return 0;
		return Long.valueOf(map.get(key).toString().replaceAll("\\,","").replaceAll("\\.","").trim()).intValue();
	}

	/**
	 * Metodo que pega um Elemento dentro de um Map da seguinte forma:
	 * eh recebido uma lista de opcoes[array de String] assim por ex:
	 * {"id1","id2","id3"} e ele tentara pegar todos os valores desde 
	 * id1 a id3 e retorna o primeiro que naum for null ou NULL caso 
	 * nao tenha em nenhum dos tres.
	 * 
	 * @author Diego Pacheco - GT5 - LMS
	 * @version 2.0
	 * @param map eh um Map com os dados.
	 * @param key eh uma String q eh a key do map q vc quer o valor.
	 * @param opcoes eh um array de String contendo as opcoes de procura.
	 * @return Object contido no map ou null caso nao ache o dado.
	 */
	public static Object getObjectOnList(Map<String, ?> map,String key,String[] opcoes) {
		if (map==null || map.size()==0) return null;

		for(int i=0;i<opcoes.length;i++) {
			if (map.get(key + opcoes[i])!=null)
				return map.get(key + opcoes[i]);
		}
		return null;
	}

	/**
	 * Metodo que pega um Elemento dentro de um Map da seguinte forma:
	 * eh recebido uma lista de opcoes[array de String] assim por ex:
	 * {"id1","id2","id3"} e ele tentara pegar todos os valores desde 
	 * id1 a id3 e retorna o primeiro que naum for null ou 0 caso 
	 * nao tenha em nenhum dos tres.
	 * 
	 * @author Diego Pacheco - GT5 - LMS
	 * @version 2.0
	 * @param map eh um Map com os dados.
	 * @param key eh uma String q eh a key do map q vc quer o valor.
	 * @param opcoes eh um array de String contendo as opcoes de procura.
	 * @return int contido no map ou 0 caso nao ache o dado.
	 */
	public static int getIntOnList(Map<String, ?> map, String key,String[] opcoes) {
		if (map==null || map.size()==0) return 0;

		for(int i=0;i<opcoes.length;i++) {
			if (map.get(key + opcoes[i])!=null)
				return intByMap(map,key + opcoes[i]);
		}
		return 0;
	}

	/**
	 * Metodo que pega um Elemento dentro de um Map da seguinte forma:
	 * eh recebido uma lista de opcoes[array de String] assim por ex:
	 * {"id1","id2","id3"} e ele tentara pegar todos os valores desde 
	 * id1 a id3 e retorna o primeiro que naum for null ou 0 caso 
	 * nao tenha em nenhum dos tres. UTILIZA: intByMapExploder
	 * 
	 * @author Diego Pacheco - GT5 - LMS
	 * @version 2.0
	 * @param map eh um Map com os dados.
	 * @param key eh uma String q eh a key do map q vc quer o valor.
	 * @param opcoes eh um array de String contendo as opcoes de procura.
	 * @return int contido no map ou 0 caso nao ache o dado.
	 */
	public static int getIntOnListExploder(Map<String, ?> map,String key,String[] opcoes) {
		if (map==null || map.size()==0) return 0;

		for(int i=0;i<opcoes.length;i++) {
			if (map.get(key + opcoes[i])!=null)
				return intByMap(map,key + opcoes[i]);
		}
		return 0;
	}

	/**
	 * Metodo que pega um Elemento dentro de um Map da seguinte forma:
	 * eh recebido uma lista de opcoes[array de String] assim por ex:
	 * {"id1","id2","id3"} e ele tentara pegar todos os valores desde 
	 * id1 a id3 e retorna o primeiro que naum for null ou 0 caso 
	 * nao tenha em nenhum dos tres.
	 * 
	 * @author Diego Pacheco - GT5 - LMS
	 * @version 2.0
	 * @param map eh um Map com os dados.
	 * @param key eh uma String q eh a key do map q vc quer o valor.
	 * @param opcoes eh um array de String contendo as opcoes de procura.
	 * @return long contido no map ou 0 caso nao ache o dado.
	 */
	public static long getLongOnList(Map<String, ?> map, String key,String[] opcoes) {
		if (map==null || map.size()==0) return 0;

		for(int i=0; i < opcoes.length; i++) {
			if (map.get(key + opcoes[i])!=null)
				return longByMap(map,key + opcoes[i]);
		}
		return 0;
	}

	/**
	 * Metodo que pega um Elemento dentro de um Map da seguinte forma:
	 * eh recebido uma lista de opcoes[array de String] assim por ex:
	 * {"id1","id2","id3"} e ele tentara pegar todos os valores desde 
	 * id1 a id3 e retorna o primeiro que naum for null ou 0 caso 
	 * nao tenha em nenhum dos tres.
	 * 
	 * @author Diego Pacheco - GT5 - LMS
	 * @version 2.0
	 * @param map eh um Map com os dados.
	 * @param key eh uma String q eh a key do map q vc quer o valor.
	 * @param opcoes eh um array de String contendo as opcoes de procura.
	 * @return long contido no map ou 0 caso nao ache o dado.
	 */
	public static double getDoubleOnList(Map<String, ?> map, String key,String[] opcoes) {
		if (map==null || map.size()==0) return 0;

		for(int i=0;i<opcoes.length;i++) {
			if (map.get(key + opcoes[i])!=null)
				return Double.valueOf(map.get(key + opcoes[i]).toString());
		}
		return 0;
	}

	/**
	 * Metodo que informa se um Map eh vazio.
	 * 
	 * @author Diego Pacheco - GT5 - LMS
	 * @version 1.0 
	 * @param map a ser testado.
	 * @return true se for vazio e false se nao for vazio.
	 */
	public static boolean isEmpityMap(Map<String, ?> map) {
		return (map==null || map.size()==0) ? true : false;
	}

	/**
	 * Metodo que informa se um List dentro de um Map eh null ou nao.
	 * 
	 * @author Diego Pacheco - GT5 - LMS
	 * @version 1.0
	 * @param map que contem os dados.
	 * @param key eh a key do map que contem a List.
	 * @return true caso a List sejah null, false se ela for <> null.
	 */
	public static boolean isEmpityListOnMap(Map<String, ?> map, String key) {
		return ( ( (getList(map,key,null)==null) || (getList(map,key,new ArrayList(0)).size()==0) ) ? true : false );
	}

	/**
	 * Metodo que pega um Elemento de uma List que está dentro de um Map.
	 *
 	 * @author Diego Pacheco - GT5 - LMS
	 * @version 1.0
	 * @param map eh um Map contendo os dados.
	 * @param keyList eh uma String contendo a key do Map que contem uma List.
	 * @param index eh o index da List que vc quer recuperar.
	 * @param defaultValue eh o retorno caso o metodo naum ache a List ou o valor do index.
	 * @return Long com o dado ou o defaultValue.
	 */
	public static Long getLongOnListItem(Map<String, ?> map, String keyList, int index, Long defaultValue) {
		List ret = getList(map,keyList,null);
		if (ret==null) return defaultValue;

		if (ret.size() >= index) {
			Object aux = ret.get(index);
			if (aux!=null) return (Long) aux;
		}
		return defaultValue;
	}

	/**
	 * Metodo que pega um Elemento de uma List que está dentro de um Map.
	 *
 	 * @author Alexandre Poletto - GT5 - LMS
	 * @version 1.0
	 * @param map eh um Map contendo os dados.
	 * @param keyList eh uma String contendo a key do Map que contem uma List.
	 * @param index eh o index da List que vc quer recuperar.
	 * @param defaultValue eh o retorno caso o metodo naum ache a List ou o valor do index.
	 * @return String com o dado ou o defaultValue.
	 */
	public static String getStringOnListItem(Map<String, ?> map, String keyList,int index,String defaultValue) {
		List ret = getList(map,keyList,null);
		if (ret==null) return defaultValue;
		
		if (ret.size() >= index) {
			Object aux = ret.get(index);
			if (aux!=null) return (String) aux;
		}
		return defaultValue;
	}

	/**
	 * Metodo que pega todas as keys de uma metodo apartir de um valor
	 * OBS: para fazer isso ele utiliza o metodo equals.
	 *
 	 * @author Diego Pacheco - GT5 - LMS
	 * @version 1.0
	 * @param Map dados eh o map com os dados.
	 * @param Object value eh o valor a ser procurado denrto do Map.
	 * @param List defaultValue eh o retorno caso nao ache nada.
	 * @return List contendo as keys do Map com o dado.
	 */
	public static List getKeysFromValue(Map<String, ?> map, Object value, List defaultValue) {
		if (map==null) return defaultValue;
		if (map.entrySet().size()==0) return defaultValue;

		List newList = new ArrayList();

		Iterator it = map.keySet().iterator();
		while(it.hasNext()) {
			Object aux = it.next();
			if (aux!=null) {
				if(map.get(aux).equals(value)) {
					newList.add(aux);
				}
			}
		}
		return ((newList.size()==0) ? defaultValue: newList);
	}

	/**
	 * Metodo que traz um Map de dentro de um Map.
	 *
 	 * @author Diego Pacheco - GT5 - LMS
	 * @version 1.0
	 * @param Map dados eh o map com os dados.
	 * @param String key eh a chave do Map que tem um map.
	 * @param Map retornoFalha eh o retorno caso nao tenha dados.
	 * @return retorna um Map .
	 */
	public static Map<?, ?> getMap(Map<String, ?> map, String key, Map<?, ?> retornoFalha) {
		if (map==null) return retornoFalha;
		if (map.get(key)==null) return retornoFalha;

		if (map.get(key) instanceof Map) {			
			return (Map<?, ?>)map.get(key);
		}
		return retornoFalha;
	}

	/**
	 * Metodo que traz uma String de dentro de um Map que esta dentro de um Map.
	 *
 	 * @author Diego Pacheco - GT5 - LMS
	 * @version 1.0
	 * @param Map dados eh o map com os dados.
	 * @param String key eh a chave do Map que tem um map.
	 * @param String subKey eh a chave do Map que estah dentro do map principal.
	 * @param Map retornoFalha eh o retorno caso nao tenha dados.
	 * @return retorna uma String ou o retornoFalha.
	 */
	public static String getStringOnMap(Map<String, ?> map, String key, String subKey, String retornoFalha) {
		Map mapaAux = getMap(map,key,null);
		if (mapaAux==null || mapaAux.size()==0 ) return retornoFalha;
		return getString(mapaAux,subKey,retornoFalha);
	}

	/**
	 * Metodo que traz um Long de dentro de um Map que esta dentro de um Map.
	 *
 	 * @author Diego Pacheco - GT5 - LMS
	 * @version 1.0
	 * @param Map dados eh o map com os dados.
	 * @param String key eh a chave do Map que tem um map.
	 * @param String subKey eh a chave do Map que estah dentro do map principal.
	 * @param Map retornoFalha eh o retorno caso nao tenha dados.
	 * @return retorna um Long ou o retornoFalha.
	 */
	public static Long getLongOnMap(Map<String, ?> map, String key, String subKey, Long retornoFalha) {
		Map mapaAux = getMap(map,key,null);
		if (mapaAux==null || mapaAux.size()==0) return retornoFalha;
		return getLong(mapaAux,subKey,retornoFalha);
	}

	public static Long getLongOnMap(Map<String, ?> map, String key, String subKey)	{
		return getLongOnMap(map, key, subKey, null);
	}
}