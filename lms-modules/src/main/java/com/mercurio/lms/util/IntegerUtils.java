package com.mercurio.lms.util;

import java.math.BigDecimal;

import com.mercurio.adsm.core.util.ReflectionUtils;

/**
 * Classe utilitária para manipulação de valores Integer.
 * @author Claiton Grings
 */
public class IntegerUtils {
	public static final Integer ZERO = Integer.valueOf("0");
	public static final Integer ONE = Integer.valueOf("1");
	public static final Integer TEN = Integer.valueOf("10");

	/**
	 * Converte um String para um Integer.
	 * 
	 * @param val
	 * @return Integer convertido
	 * @throws NumberFormatException se o valor não puder ser convertido
	 */
	public static Integer getInteger(String val) {
		return Integer.valueOf(val);
	}

	/**
	 * Converte um Long para um Integer.
	 * 
	 * @param val
	 * @return Integer convertido
	 * @throws NumberFormatException se o valor não puder ser convertido
	 */
	public static Integer getInteger(Long val) {
		return getInteger(val.toString());
	}

	/**
	 * Converte um BigDecimal para um Integer.
	 * 
	 * @param val
	 * @return Integer convertido
	 * @throws NumberFormatException se o valor não puder ser convertido
	 */
	public static Integer getInteger(BigDecimal val) {
		return getInteger(val.toString());
	}

	/**
	 * Converte um Object para um Integer.
	 * 
	 * @param value
	 * @return Integer convertido
	 * @throws NumberFormatException se o valor não puder ser convertido
	 */
	public static Integer getInteger(Object value) {
		if (value instanceof Integer)
			return (Integer) value;
		return (Integer) ReflectionUtils.toObject(String.valueOf(value), Integer.class);
	}

	/**
	 * Verifica se o valor é igual a <code>zero</code>.
	 * 
	 * @param value
	 * @return true se value igual a zero.
	 */
	public static boolean isZero(Integer value) {
		validateParams(new Integer[]{value});
		return CompareUtils.eq(value, ZERO);
	}

	/**
	 * Verifica se o valor é menor do que <code>zero</code>.
	 * 
	 * @param value
	 * @return true se value menor do que zero.
	 */
	public static boolean ltZero(Integer value) {
		validateParams(new Integer[]{value});
		return CompareUtils.lt(value, ZERO);
	}

	/**
	 * Verifica se o valor é maior do que <code>zero</code>.
	 * 
	 * @param value
	 * @return true se value maior do que zero.
	 */
	public static boolean gtZero(Integer value) {
		validateParams(new Integer[]{value});
		return CompareUtils.gt(value, ZERO);
	}

	/**
	 * Retorna o valor do Integer valido, ou ZERO em caso de null.
	 * 
	 * @param val
	 * @return Integer
	 */
	public static Integer defaultInteger(Integer val) {
        return val == null ? ZERO : val;
    }

	/**
	 * Verifica se o valor é diferente de <code>null</code>, <code>zero</code> ou <code>string vazia</code>.
	 * 
	 * @param val
	 * @return true se val não nulo e diferente de zero.
	 */
	public static boolean hasValue(Integer val) {
		if((val == null))
			return false;
		if((CompareUtils.gt(val, ZERO)))
			return true;
		return false;
	}

	/**
	 * Soma o valor de arg0 com o valor arg1.
	 * 
	 * @param arg0
	 * @param arg1
	 * @return arg0 + arg1
	 */
	public static Integer add(Integer arg0, Integer arg1) {
		validateParams(new Integer[]{arg0, arg1});
		return Integer.valueOf(arg0.intValue() + arg1.intValue());
	}

	/**
	 * Soma o valor de arg0 com o valor arg1 considerando valores nulos.
	 * 
	 * @param arg0
	 * @param arg1
	 * @return arg0 + arg1
	 */
	public static Integer addNull(Integer arg0, Integer arg1) {
		if (arg0 == null) {
			arg0 = ZERO;
		}
		if (arg1 != null) {
			return Integer.valueOf(arg0.intValue() + arg1.intValue());
		}
		return arg0;
	}

	/**
	 * Subtrai do valor 1 o valor 2.
	 * 
	 * @param arg0
	 * @param arg1
	 * @return arg0 - arg1
	 */
	public static Integer subtract(Integer arg0, Integer arg1) {
		validateParams(new Integer[]{arg0, arg1});
		return Integer.valueOf(arg0.intValue() - arg1.intValue());
	}

	/**
	 * Multiplica o valor 1 pelo valor 2.
	 * 
	 * @param arg0
	 * @param arg1
	 * @return arg0 * arg1
	 */
	public static Integer multiply(Integer arg0, Integer arg1) {
		validateParams(new Integer[]{arg0, arg1});
		return Integer.valueOf(arg0.intValue() * arg1.intValue());
	}

	/**
	 * Divide o valor 1 pelo valor 2.
	 * 
	 * @param arg0
	 * @param arg1
	 * @return arg0 / arg1
	 */
	public static Integer divide(Integer arg0, Integer arg1) {
		validateParams(new Integer[]{arg0, arg1});
		return Integer.valueOf(arg0.intValue() / arg1.intValue());
	}

	/**
	 * Incrementa o valor em 1(um)
	 * 
	 * @param val
	 * @return val + 1
	 */
	public static Integer incrementValue(Integer val) {
		validateParams(new Integer[]{val});
		int i = val.intValue();
		return Integer.valueOf(++i);
	}

	/**
	 * Decrementa o valor em 1(um)
	 * 
	 * @param val
	 * @return val - 1
	 */
	public static Integer decrementValue(Integer val) {
		validateParams(new Integer[]{val});
		int i = val.intValue();
		return Integer.valueOf(--i);
	}

	/**
	 * Valida os argumentos
	 * @throws IllegalArgumentException se args[] ou algum dos elementos for nulo.
	 */
	private static void validateParams(Integer[] args) {
		if(args == null) {
			throw new IllegalArgumentException("args não pode ser null");
		}
		for (int i = 0; i < args.length; i++) {
			if(args[i] == null) {
				throw new IllegalArgumentException("param" + i + " não pode ser null");
			}
		}
	}

}
