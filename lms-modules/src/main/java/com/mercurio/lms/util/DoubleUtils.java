package com.mercurio.lms.util;

import java.math.BigDecimal;

public class DoubleUtils {
	public static final Double ZERO = new Double("0");
	public static final Double ONE = new Double("1");
	public static final Double TEN = new Double("10");

	/**
	 * Converte um String para um Double.
	 * 
	 * @param val
	 * @return Double convertido
	 * @throws NumberFormatException se o valor não puder ser convertido
	 */
	public static Double getDouble(String val) {
		return new Double(val);
	}

	/**
	 * Converte um BigDecimal para um Double.
	 * 
	 * @param val
	 * @return Double convertido
	 * @throws NumberFormatException se o valor não puder ser convertido
	 */
	public static Double getDouble(BigDecimal val) {
		return getDouble(val.toString());
	}

	/**
	 * Converte um Long para um Double.
	 * 
	 * @param val
	 * @return Double convertido
	 * @throws NumberFormatException se o valor não puder ser convertido
	 */
	public static Double getDouble(Long val) {
		return getDouble(val.toString());
	}

	/**
	 * Soma o valor 1 com o valor 2.
	 * 
	 * @param val1
	 * @param val2
	 * @return val1 + val2
	 */
	public static Double add(Double val1, Double val2) {
		validateArguments(val1, val2);
		return new Double(val1.doubleValue() + val2.doubleValue());
	}

	/**
	 * Calcula o percentual de <code>amount</code> sobre <code>total</code>.
	 * 
	 * @param amount
	 *            valor a ser calculado o percentual
	 * @param total
	 *            valor que representa 100%
	 * @return (amount * 100) / total
	 */
	public static Double percent(Integer amount, Integer total) {
		return percent(new Double(amount.doubleValue()), new Double(total
				.doubleValue()));
	}

	/**
	 * Calcula o percentual de <code>amount</code> sobre <code>total</code>.
	 * 
	 * @param amount
	 *            valor a ser calculado o percentual
	 * @param total
	 *            valor que representa 100%
	 * @return (amount * 100) / total
	 */
	public static Double percent(BigDecimal amount, BigDecimal total) {
		return percent(new Double(amount.doubleValue()), new Double(total
				.doubleValue()));
	}

	/**
	 * Calcula o percentual de <code>amount</code> sobre <code>total</code>.
	 * 
	 * @param amount
	 *            valor a ser calculado o percentual
	 * @param total
	 *            valor que representa 100%
	 * @return (amount * 100) / total
	 */
	public static Double percent(Double amount, Double total) {		
		double retorno = (amount.doubleValue() * 100.0) / total.doubleValue();
		return new Double(retorno);
	}

	private static void validateArguments(Double val1, Double val2) {
		if (val1 == null) {
			throw new IllegalArgumentException("val1 não pode ser null");
		}
		if (val2 == null) {
			throw new IllegalArgumentException("val2 não pode ser null");
		}
	}
}
