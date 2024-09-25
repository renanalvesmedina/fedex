package com.mercurio.lms.fretecarreteirocoletaentrega.utils;

import java.math.BigDecimal;

public class NotaCreditoDtoUtils {

	public static BigDecimal nvl(BigDecimal value, BigDecimal replace) {
		if (value == null) {
			return replace;
		}
		return value;
	}

	public static BigDecimal nvl(BigDecimal value) {
		return nvl(value, new BigDecimal(0));
	}
	
}
