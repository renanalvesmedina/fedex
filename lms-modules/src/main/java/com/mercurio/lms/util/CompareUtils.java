package com.mercurio.lms.util;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class CompareUtils {

	/**
	 * Compara se os valores são iguais.
	 * 
	 * @param arg0
	 * @param arg1
	 * @return true se arg0 == arg1
	 * @throws IllegalArgumentException se arg0 ou arg1 is null
	 */
	public static boolean eq(Comparable arg0, Comparable arg1) {
		validateParams(new Comparable[]{arg0, arg1});
		return (arg0.compareTo(arg1) == 0);
	}

	/**
	 * Compara se os valores são iguais considerando valores nulos.
	 * Se os dois valores forem nulos retorna true.
	 * Se um dos valores for nulo retorna false.
	 * 
	 * @param arg0
	 * @param arg1
	 * @return true se arg0 == arg1
	 */
	public static boolean eqNull(Comparable arg0, Comparable arg1) {
		if (arg0 == null && arg1 == null) {
			return true;
		} else if (arg0 == null || arg1 == null) {
			return false;
		}
		return eq(arg0, arg1);
	}

	/**
	 * Compara se os valores são diferentes.
	 * @param arg0
	 * @param arg1
	 * @return true se arg0 != arg1
	 * @throws IllegalArgumentException se arg0 ou arg1 is null
	 */
	public static boolean ne(Comparable arg0, Comparable arg1) {
		return !eq(arg0, arg1);
	}

	/**
	 * Compara se os valores são diferentes, considerando valores nulos.
	 * @param arg0
	 * @param arg1
	 * @return true se arg0 != arg1
	 */
	public static boolean neNull(Comparable arg0, Comparable arg1) {
		return !eqNull(arg0, arg1);
	}

	/**
	 * Compara se arg0 é menor que arg1.
	 * @param arg0
	 * @param arg1
	 * @return true se arg0 < arg1
	 * @throws IllegalArgumentException se arg0 ou arg1 is null
	 */
	public static boolean lt(Comparable arg0, Comparable arg1) {
		validateParams(new Comparable[]{arg0, arg1});
		return (arg0.compareTo(arg1) == -1);
	}

	/**
	 * Compara se arg0 é menor ou igual que arg1.
	 * @param arg0
	 * @param arg1
	 * @return true se arg0 <= arg1
	 * @throws IllegalArgumentException se arg0 ou arg1 is null
	 */
	public static boolean le(Comparable arg0, Comparable arg1) {
		validateParams(new Comparable[]{arg0, arg1});
		return (arg0.compareTo(arg1) < 1);
	}

	/**
	 * Compara se arg0 é maior que arg1.
	 * @param arg0
	 * @param arg1
	 * @return true se arg0 > arg1
	 * @throws IllegalArgumentException se arg0 ou arg1 is null
	 */
	public static boolean gt(Comparable arg0, Comparable arg1) {
		return !le(arg0, arg1);
	}

	/**
	 * Compara se arg0 é maior ou igual que arg1.
	 * @param arg0
	 * @param arg1
	 * @return true se arg0 >= arg1
	 * @throws IllegalArgumentException se arg0 ou arg1 is null
	 */
	public static boolean ge(Comparable arg0, Comparable arg1) {
		return !lt(arg0, arg1);
	}

	/**
	 * Verifica se arg0 está entre arg1 e arg2 (considera o valor limite da faixa).
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @return true se ( (arg0 >= arg1) e (arg0 <= arg2) )
	 * @throws IllegalArgumentException se arg0 ou arg1 is null
	 */
	public static boolean between(Comparable arg0, Comparable arg1, Comparable arg2) {
		validateParams(new Comparable[]{arg0, arg1, arg2});
		return (arg0.compareTo(arg1) > -1) && (arg0.compareTo(arg2) < 1);
	}


	/**
	 * Retorna o maior valor entre os dois valores informados.
	 * @param arg0
	 * @param arg1
	 * @return o maior entre <code>arg0</code> e <code>arg1</code>.
	 * @throws IllegalArgumentException se arg0 ou arg1 is null
	 */
	public static BigDecimal max(BigDecimal arg0, BigDecimal arg1) {
		return (BigDecimal) maxComparable(arg0, arg1);
	}

	/**
	 * Retorna o maior valor entre os dois valores informados.
	 * @param arg0
	 * @param arg1
	 * @return o maior entre <code>arg0</code> e <code>arg1</code>.
	 * @throws IllegalArgumentException se arg0 ou arg1 is null
	 */
	public static YearMonthDay max(YearMonthDay arg0, YearMonthDay arg1) {
		return (YearMonthDay) maxComparable(arg0, arg1);
	}

	/**
	 * Retorna o maior valor entre os dois valores informados.
	 * @param arg0
	 * @param arg1
	 * @return o maior entre <code>arg0</code> e <code>arg1</code>.
	 * @throws IllegalArgumentException se arg0 ou arg1 is null
	 */
	public static DateTime max(DateTime arg0, DateTime arg1) {
		return (DateTime) maxComparable(arg0, arg1);
	}

	private static Comparable maxComparable(Comparable arg0, Comparable arg1) {
		return ge(arg0, arg1) ? arg0 : arg1;
	}

	/**
	 * Retorna o menor valor entre os dois valores informados.
	 * @param arg0
	 * @param arg1
	 * @return o menor entre <code>arg0</code> e <code>arg1</code>.
	 * @throws IllegalArgumentException se arg0 ou arg1 is null
	 */
	public static BigDecimal min(BigDecimal arg0, BigDecimal arg1) {
		return (BigDecimal) minComparable(arg0, arg1);
	}

	/**
	 * Retorna o menor valor entre os dois valores informados.
	 * @param arg0
	 * @param arg1
	 * @return o menor entre <code>arg0</code> e <code>arg1</code>.
	 * @throws IllegalArgumentException se arg0 ou arg1 is null
	 */
	public static YearMonthDay min(YearMonthDay arg0, YearMonthDay arg1) {
		return (YearMonthDay) minComparable(arg0, arg1);
	}

	/**
	 * Retorna o menor valor entre os dois valores informados.
	 * @param arg0
	 * @param arg1
	 * @return o menor entre <code>arg0</code> e <code>arg1</code>.
	 * @throws IllegalArgumentException se arg0 ou arg1 is null
	 */
	public static DateTime min(DateTime arg0, DateTime arg1) {
		return (DateTime) minComparable(arg0, arg1);
	}

	private static Comparable minComparable(Comparable arg0, Comparable arg1) {
		validateParams(new Comparable[]{arg0, arg1});
		return le(arg0, arg1) ? arg0 : arg1;
	}

	/**
	 * Valida os argumentos
	 * @throws IllegalArgumentException se args[] ou algum dos elementos do array for nulo.
	 */
	private static void validateParams(Comparable[] args) {
		if(args == null) {
			throw new IllegalArgumentException("args não pode ser null");
		}
		Class clazz = args[0].getClass();
		for (int i = 0; i < args.length; i++) {
			if(args[i] == null) {
				throw new IllegalArgumentException("param" + i + " não pode ser null");
			}
			if(!clazz.isAssignableFrom(args[i].getClass())) {
				throw new IllegalArgumentException("param" + i + " não possui mesmo tipo do param0");
			}
		}
	}

}
