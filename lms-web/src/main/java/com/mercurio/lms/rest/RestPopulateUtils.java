package com.mercurio.lms.rest;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.rest.json.YearMonthDayDeserializer;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe utilitária para auxiliar a criação de entidades a partir dos maps
 * retornados da tela.
 * 
 */
public class RestPopulateUtils {
	
	/**
	 * Default private Constructor.
	 */
	private RestPopulateUtils(){
		
	}
	
	/**
	 * Compõe um DomainValue para uma entidade, sendo o valor 
	 * uma String ou um JSON de um Domain.
	 * 
	 * @param values
	 * 
	 * @return DomainValue
	 */	
	public static DomainValue getDomainValue(Object values){		
		if(values == null){
			return null;
		}
		
		String value = null;		
		
		if(values instanceof java.util.Map<?,?>){
			@SuppressWarnings("unchecked")
			java.util.Map<String, Object> domainValues = (java.util.Map<String, Object>) values;
			value = (String) domainValues.get("value");
		} else {
			value = values.toString();
		}
		
		return (DomainValue) ReflectionUtils.toObject(value, DomainValue.class);    	
	}
	
	
	/**
	 * Converte uma string para o formato de data do YearMonthDay.
	 * 
	 * @param map
	 * @param key
	 * 
	 * @return YearMonthDay
	 */ 
	public static YearMonthDay getYearMonthDay(final java.util.Map<?, ?> map, String key){
		String value = MapUtils.getString(map, key);
		
		if(StringUtils.isBlank(value)){
			return null;
		}
		
		return JTDateTimeUtils.convertDataStringToYearMonthDay(value, "dd/MM/yyyy");
	}
	
	/**
	 * A partir da chave ("key"), é esperado um valor (em "map") do tipo String e no padrão ISO 8601. Exemplo: "2015-01-01T00:00:00.000-03:00".
	 *  
	 * @param map
	 * @param key
	 * 
	 * @return YearMonthDay
	 */
	public static YearMonthDay getYearMonthDayFromISO8601(final java.util.Map<?, ?> map, String key){
		String value = MapUtils.getString(map, key);
		return YearMonthDayDeserializer.parse(value);
	}

	/**
	 * A partir da chave ("key"), é esperado um valor (em "map") do tipo String e no padrão ISO 8601.
	 * Adiciona uma hora ao valor que será deserializado para evitar conflitos de horário de verão
	 *
	 * Exemplo: "1980-01-12T00:00:00.000-02:00" -> "1980-01-12T01:00:00.000-02:00".
	 *
	 * @param map Mapa contendo valores do json
	 * @param key Chave do json
	 *
	 * @return YearMonthDay
	 */
	public static YearMonthDay getYearMonthDayFromISO8601CorrecaoHorarioVerao(final java.util.Map<?, ?> map, String key){
		String value = MapUtils.getString(map, key);
		if(value != null){
			value = value.replace("00:00:00.000-02:00","01:00:00.000-02:00");
		}
		return YearMonthDayDeserializer.parse(value);
	}

	/**
	 * Cria um BigDecimal apartir de um valor, podendo o mesmo ser um object
	 * ou string.
	 * 
	 * @param map
	 * @param key
	 * 
	 * @return BigDecimal
	 */
	public static BigDecimal getBigDecimal(Map<String, Object> map, String key) {
		Object value = MapUtils.getObject(map, key);
		
		if(value == null){
			return null;
		}
		
		if(value.getClass().isAssignableFrom(BigDecimal.class)){
			return (BigDecimal) value;
		}
		
		if(value.getClass().isAssignableFrom(String.class)){
			return NumberUtils.createBigDecimal((String) value);
		}
		
		return null;
	}
}