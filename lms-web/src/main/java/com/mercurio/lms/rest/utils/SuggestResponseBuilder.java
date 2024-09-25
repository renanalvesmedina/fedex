package com.mercurio.lms.rest.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

/**
 * Esta classe foi substituida pela abstração BaseSuggestRest
 * 
 * @see {@link com.mercurio.adsm.rest.BaseSuggestRest}
 */
@Deprecated
public class SuggestResponseBuilder {
	
	private final List<?> list;
	private final Integer limiteRegistros;
	

	public SuggestResponseBuilder(List<?> list, Integer limiteRegistros) {
		this.list = list;
		this.limiteRegistros = limiteRegistros;
	}
	
	public Response build() {
		
		Map<String, Object> toReturn = new HashMap<String, Object>();

		if (limiteRegistros == null || list.size() <= limiteRegistros) {
			toReturn.put("list", list);
		} else {
			toReturn.put("list", new ArrayList(list.subList(0, limiteRegistros)));
			toReturn.put("limiteRegistros", limiteRegistros);
			toReturn.put("limiteExcedido", true);
		}
		
		return Response.ok(toReturn).build();
		
	}

}
