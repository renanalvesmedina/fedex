package com.mercurio.lms.util;

import java.math.BigDecimal;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.util.SqlTemplate;

/**
 * Classe utilitária para manipulação de valores Long.
 * @author Claiton Grings
 */
public class LongUtils {
	public static final Long ZERO = Long.valueOf("0");
	public static final Long ONE = Long.valueOf("1");
	public static final Long TEN = Long.valueOf("10");
    
    public static Long getOrDefault(Long value, Long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        
        return value;
    }
    
    public static Long getOrZero(Long value) {
        return getOrDefault(value, ZERO);
    }

	/**
	 * Compara se o valor é diferente de <code>null</code> e de <code>zero</code>.
	 * 
	 * @param val
	 * @return true se val não nulo e diferente de zero.
	 */
	public static boolean hasValue(Long val) {
		return (val == null) ? false : (val.longValue() != 0);
	}

	/**
	 * Converte um String para um Long.
	 * 
	 * @param val
	 * @return Long convertido
	 * @throws NumberFormatException se o valor não puder ser convertido
	 */
	public static Long getLong(String val) {
		return Long.valueOf(val);
	}

	/**
	 * Converte um Integer para um Long.
	 * 
	 * @param val
	 * @return Long convertido
	 * @throws NumberFormatException se o valor não puder ser convertido
	 */
	public static Long getLong(Integer val) {
		return Long.valueOf(val.longValue());
	}

	/**
	 * Converte um BigDecimal para um Long.
	 * 
	 * @param val
	 * @return Long convertido
	 * @throws NumberFormatException se o valor não puder ser convertido
	 */
	public static Long getLong(BigDecimal val) {
		return Long.valueOf(val.longValue());
	}

	/**
	 * Converte um Object para um Long.
	 * 
	 * @param value
	 * @return Long convertido
	 * @throws NumberFormatException se o valor não puder ser convertido
	 */
	public static Long getLong(Object value) {
		if (value instanceof Long)
			return (Long) value;
		return (Long) ReflectionUtils.toObject(String.valueOf(value), Long.class);
	}
    
    public static Long safeAdd(Long x, Long y) {
        return getOrZero(x) + getOrZero(y);
    }

	/**
	 * Soma o valor 1 com o valor 2.
	 * 
	 * @param arg0
	 * @param arg1
	 * @return arg0 + arg1
	 */
	public static Long add(Long arg0, Long arg1) {
		validateParams(new Long[]{arg0, arg1});
		return Long.valueOf(arg0.longValue() + arg1.longValue());
	}

	/**
	 * Subtrai do valor 1 o valor 2.
	 * 
	 * @param arg0
	 * @param arg1
	 * @return arg0 - arg1
	 */
	public static Long subtract(Long arg0, Long arg1) {
		validateParams(new Long[]{arg0, arg1});
		return Long.valueOf(arg0.longValue() - arg1.longValue());
	}

	/**
	 * Multiplica o valor 1 pelo valor 2.
	 * 
	 * @param arg0
	 * @param arg1
	 * @return arg0 * arg1
	 */
	public static Long multiply(Long arg0, Long arg1) {
		validateParams(new Long[]{arg0, arg1});
		return Long.valueOf(arg0.longValue() * arg1.longValue());
	}

	/**
	 * Divide o valor 1 pelo valor 2.
	 * 
	 * @param arg0
	 * @param arg1
	 * @return arg0 / arg1
	 */
	public static Long divide(Long arg0, Long arg1) {
		validateParams(new Long[]{arg0, arg1});
		return Long.valueOf(arg0.longValue() / arg1.longValue());
	}

	/**
	 * Resto da divisão de valor 1 pelo valor 2.
	 * 
	 * @param arg0
	 * @param arg1
	 * @return resto da divisão de arg0 / arg1
	 */
	public static Long mod(Long arg0, Long arg1) {
		validateParams(new Long[]{arg0, arg1});
		return Long.valueOf(arg0.longValue() % arg1.longValue());
	}	

	/**
	 * Incrementa o valor em 1(um)
	 * 
	 * @param val
	 * @return val + 1
	 */
	public static Long incrementValue(Long val) {
		validateParams(new Long[]{val});
		long l = val.longValue();
		return Long.valueOf(++l);
	}

	/**
	 * Decrementa o valor em 1(um)
	 * 
	 * @param val
	 * @return val - 1
	 */
	public static Long decrementValue(Long val) {
		validateParams(new Long[]{val});
		long l = val.longValue();
		return Long.valueOf(--l);
	}

	/**
	 * Valida os argumentos
	 * @throws IllegalArgumentException se args[] ou algum dos elementos do array for nulo.
	 */
	private static void validateParams(Long[] args) {
		if(args == null) {
			throw new IllegalArgumentException("args não pode ser null");
		}
		for (int i = 0; i < args.length; i++) {
			if(args[i] == null) {
				throw new IllegalArgumentException("param" + i + " não pode ser null");
			}
		}
	}

	/**
	 * 
	 * Adiciona ao sqlTemplate passado como parâmetro o critério de validação 
	 * de intervalos
	 * 
	 * @author José Rodrigo Moraes
	 * @since  26/01/2007
	 *  
	 * @param hql SqlTemplate da consulta
	 * @param campoIni Nome do campo inicial do intervalo
	 * @param campoFin Nome do campo final do intervalo
	 * @param valIni Valor inicial
	 * @param valFin Valor final
	 * @return SqlTemplate com o critério de validação de intervalos
	 */
	public static SqlTemplate getHqlValidaIntervalo(SqlTemplate hql, 
			                                        String campoIni,
			                                        String campoFin, 
			                                        Long valIni, 
			                                        Long valFin) {
		
		String sQuery = new String();
		
		sQuery = "( (? between " + campoIni + " and " + campoFin + ") or (? between " + campoIni + " and " + campoFin + ") " +
				 "or ( ? <= " + campoIni + " and ? >= " + campoFin + " ))";
		
		hql.addCustomCriteria(sQuery);
		
		hql.addCriteriaValue(valIni);		
		hql.addCriteriaValue(valFin);
		hql.addCriteriaValue(valIni);
		hql.addCriteriaValue(valFin);
		
		return hql;
		
	}

}
