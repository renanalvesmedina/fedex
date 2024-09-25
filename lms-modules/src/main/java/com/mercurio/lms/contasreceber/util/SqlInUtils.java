package com.mercurio.lms.contasreceber.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SqlInUtils {

	/**
	 * Em caso de queries que utilizam IN o método divide os valores em 'n' segmentos de no máximo 998 valores
	 *  
	 * @param lista
	 * @return
	 */
	static public List<List<String>> splitEachThrousand(Object lista) {
		List<String> values = new ArrayList<String>();
		List<List<String>> idsProcessar = new ArrayList<List<String>>();

		if (lista instanceof String) {
			values = Arrays.asList(((String) lista).split(","));

		} else if (lista instanceof String[]) {
			values = Arrays.asList((String[]) lista);

		} else if (lista instanceof List) {
			values.addAll((Collection<? extends String>) lista);
			
		}

		if (values.size() < 1000) {
			idsProcessar.add(values);
			
			return idsProcessar;
			
		}
		
		float rounds = values.size() / 999;
		int resto = values.size() % 999;
		int init = 0;
		int end = values.size() > 999 ? 998 : values.size() - 1;
		int absRounds = (int) Math.abs(rounds);
		
		if (absRounds > 0 && resto != 0) {
			++absRounds;

		} else if (absRounds == 0 && resto > 0) {
			absRounds = 1;

		}

		for (int i = 1; i <= absRounds; i++) {
			if (i < absRounds && i > 1) {
				init = end;
				end = init + end;
				
			} else if (i == absRounds && absRounds > 1) {
				init = end;
				end = values.size();
				
			} else if (absRounds == 1) {
				end = 1;
				
			}
			idsProcessar.add(values.subList(init, end));

		}
		return idsProcessar;
		
	}
}
