package com.mercurio.lms.rest.portaria.chegadasaida.dto;

import java.util.HashMap;
import java.util.Map;


public class FrotaPlacaChegadaSaidaSuggestFilterDTO {

	private String value;
	private Long idFilial;
	
	public FrotaPlacaChegadaSaidaSuggestFilterDTO() {
	}
	
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("value", getValue());
		map.put("idFilial", getIdFilial());
		return map;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(Long idFilial) {
		this.idFilial = idFilial;
	}

}
