package com.mercurio.lms.util;

import java.math.BigDecimal;

/**
 * Classe utilitária para manipulação de valores BigDecimal.
 * 
 * @author Claiton Grings, Felipe Cuozzo
 *
 */
public class BigDecimalUtils {
	
	public static final BigDecimal ZERO    = new BigDecimal("0");
	public static final BigDecimal ONE     = new BigDecimal("1");
	public static final BigDecimal TWO     = new BigDecimal("2");
	public static final BigDecimal TREE    = new BigDecimal("3");
	public static final BigDecimal TEN     = new BigDecimal("10");
	public static final BigDecimal HUNDRED = new BigDecimal("100");
	public static final BigDecimal MILLION = new BigDecimal("1000000");

	/**
	 * Arredondamento padrão. @see BigDecimal#ROUND_HALF_UP.
	 */
	public static final String ROUND_DEFAULT = "P";

	/**
	 * Arredondamento para baixo. @see BigDecimal#ROUND_DOWN.
	 */
	public static final String ROUND_DOWN = "B";

	/**
	 * Arredondamento para cima. @see BigDecimal#ROUND_UP.
	 */
	public static final String ROUND_UP = "C";

	/**
	 * Compara se o valor é diferente de <code>null</code> e de <code>zero</code>.
	 * 
	 * @param val
	 * @return true se val não nulo e diferente de zero.
	 */
	public static boolean hasValue(BigDecimal val) {
		return (val == null) ? false : (val.signum() != 0);
	}

	/**
	 * Verifica se o valor é igual a <code>zero</code>.
	 * 
	 * @param value
	 * @return true se value igual a zero.
	 */
	public static boolean isZero(BigDecimal value) {
		validateArgument(value);
		return CompareUtils.eq(value, ZERO);
	}

	/**
	 * Verifica se o valor é menor do que <code>zero</code>.
	 * 
	 * @param value
	 * @return true se value menor do que zero.
	 */
	public static boolean ltZero(BigDecimal value) {
		validateArgument(value);
		return CompareUtils.lt(value, ZERO);
	}

	/**
	 * Verifica se o valor é maior do que <code>zero</code>.
	 * 
	 * @param value
	 * @return true se value maior do que zero.
	 */
	public static boolean gtZero(BigDecimal value) {
		validateArgument(value);
		return CompareUtils.gt(value, ZERO);
	}

	/**
	 * Converte um Object para um BigDecimal.
	 * 
	 * @param value
	 * @return
	 */
	public static BigDecimal getBigDecimal(Object val) {
		if(val instanceof BigDecimal)
			return (BigDecimal) val;
		return new BigDecimal(val.toString());
	}

	/**
	 * Retorna Valor Adicionado.
	 * 
	 * @param valor
	 * @param valorAdicional
	 * @return (valor + valorAdicional)
	 */
	public static BigDecimal add(BigDecimal valor, BigDecimal valorAdicional) {
		if (valor == null) {
			valor = ZERO;
		}
		if (valorAdicional != null) {
			return valor.add(valorAdicional);
		}
		return valor;
	}

	/**
	 * Divide o valor informado por <code>100</code>. Realiza o arredondamento
	 * no método <code>BigDcimento.ROUND_HALF_UP</code> com precisão de quatro (4) casas decimais.
	 * 
	 * @param valor
	 * @return valor / 100
	 */
	public static BigDecimal percent(BigDecimal valor) {
		if (valor == null) {
			throw new IllegalArgumentException("valor não pode ser null");
		}
		return percent(valor, 4);
	}

	/**
	 * Divide o valor informado por <code>100</code>. Realiza o arredondamento
	 * no método <code>BigDcimento.ROUND_HALF_UP</code>.
	 * 
	 * @param valor
	 * @param scala
	 * @return valor / 100
	 */
	public static BigDecimal percent(BigDecimal valor, int escala) {
		if(valor == null) {
			throw new IllegalArgumentException("valor não pode ser null");
		}
		if(escala < 0) {
			throw new IllegalArgumentException("escala não pode ser menor que zero");
		}
		return valor.divide(BigDecimal.valueOf(100), escala, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * Calcula o valor referente a aplicação de um percentual.
	 * 
	 * @param value
	 * @param percent
	 * @param scale
	 * @return value*(percent/100)
	 */
	public static BigDecimal percentValue(BigDecimal value, BigDecimal percent, int scale) {
		if(value == null) {
			throw new IllegalArgumentException("valor não pode ser null");
		}
		if(percent == null) {
			throw new IllegalArgumentException("percentual não pode ser null");
		}
		return round(value.multiply(percent(percent, scale)), scale);
	}

	/**
	 * Calcula o desconto sobre o valor.
	 * 
	 * @param valor
	 * @param valorDesconto
	 * @return valor - (valor * (valorDesconto/100))
	 */
	public static BigDecimal desconto(BigDecimal valor, BigDecimal valorDesconto) {
		if (valor == null) {
			throw new IllegalArgumentException("valor não pode ser null");
		}
		if (valorDesconto == null) {
			throw new IllegalArgumentException("valorDesconto não pode ser null");
		}
		return valor.subtract(valor.multiply(percent(valorDesconto)));
	}

	/**
	 * Calcula o acréscimo sobre o valor.
	 * 
	 * @param valor
	 * @param valorAcrescimo
	 * @return valor + (valor * (valorAcrescimo/100))
	 */
	public static BigDecimal acrescimo(BigDecimal valor, BigDecimal valorAcrescimo) {
		if (valor == null) {
			throw new IllegalArgumentException("valor não pode ser null");
		}
		if (valorAcrescimo == null) {
			throw new IllegalArgumentException("valorAcrescimo não pode ser null");
		}
		return valor.add(valor.multiply(percent(valorAcrescimo)));
	}

	/**
	 * Divide val1 por val2 utilizando arredondamento ROUND_HALF_UP e 4 casas decimais.
	 * 
	 * @param val1
	 * @param val2
	 * @return val1 / val2
	 */
	public static BigDecimal divide(BigDecimal val1, BigDecimal val2) {
		validateArguments(val1, val2);
		return val1.divide(val2, 4, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * Divide val1 por val2 e arredonda o retorno de acordo com o parâmetro round
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 24/07/2006
	 *
	 * @param val1
	 * @param val1 
	 * @param roundingMode
	 *
	 * @return val1 / val2 
	 *
	 */
	public static BigDecimal divide(BigDecimal val1, BigDecimal val2, int roundingMode) {
		BigDecimal val = ZERO;
		//Caso o primeiro BigDecimal seja null, retorna o valor 0
		if(hasValue(val1)) {
			//Caso o segundo BigDecimal não seja null e não seja 0, é feita a divisão
			if(hasValue(val2)) {
				val = val1.divide(val2, roundingMode);
			}
		}
		return val;
	}

	/**
	 * Retorna o valor com duas (2) casas decimais e arredondamento padrão. 
	 * 
	 * @return valor arredondado com a precisão de duas (2) casas.
	 * @see BigDecimal#ROUND_HALF_UP
	 */
	public static BigDecimal round(BigDecimal value) {
		return round(value, 2);
	}

	/**
	 * Retorna o valor com casas decimais e arredondamento padrão. 
	 * 
	 * @return valor arredondado com a precisão do parametro escala.
	 * @see BigDecimal#ROUND_HALF_UP
	 */
	public static BigDecimal round(BigDecimal value, int scale) {
		validateArgument(value);
		if(scale < 0) {
			throw new IllegalArgumentException("escala não pode ser menor que zero");
		}
		return value.setScale(scale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * Retorna o valor formatado conforme número de casas decimais e tipo de arredondamento.
	 * 
	 * @param valor Valor com a precisão informada. 
	 * @param escala escala de precisão em casas decimais. 
	 * @param modoArredondamento tipo de arredondamento a ser usado, ver constantes na classe.
	 * @return val arredondado
	 */
	public static BigDecimal round(BigDecimal value, int escala, String modoArredondamento) {
		validateArgument(value);
		int roundingMode = 0;
		if(ROUND_DEFAULT.equals(modoArredondamento)) {
			roundingMode = BigDecimal.ROUND_HALF_UP;
		} else if(ROUND_DOWN.equals(modoArredondamento)) {
			roundingMode = BigDecimal.ROUND_DOWN;
		} else if(ROUND_UP.equals(modoArredondamento)) {
			roundingMode = BigDecimal.ROUND_UP;
		} else {
			throw new IllegalArgumentException("Invalid rounding mode");
		}
		
		return value.setScale(escala, roundingMode);
	}

	/**
	 * Retorna defaultValue caso value seja nulo.
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static BigDecimal defaultBigDecimal(BigDecimal value, BigDecimal defaultValue) {
        return value == null ? defaultValue : value;
    }
	
	/**
	 * Retorna ZERO caso value seja nulo.
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static BigDecimal defaultBigDecimal(BigDecimal value) {
        return defaultBigDecimal(value, ZERO);
    }
	
	/**
	 * Retorna a fração de 100 do valor de entrada
	 * @param val
	 * @return int(val / 100) 
	 */
	public static BigDecimal hundredFraction(BigDecimal val) {
		return val.add(BigDecimalUtils.getBigDecimal("99.999")).divide(HUNDRED, 0, BigDecimal.ROUND_DOWN);
	}

	// valida os argumentos
	private static void validateArgument(BigDecimal value) {
		if (value == null) {
			throw new IllegalArgumentException("value não pode ser null");
		}
	}

	// valida os argumentos
	private static void validateArguments(BigDecimal val1, BigDecimal val2) {
		if (val1 == null) {
			throw new IllegalArgumentException("val1 não pode ser null");
		}
		if (val2 == null) {
			throw new IllegalArgumentException("val2 não pode ser null");
		}
	}

}
