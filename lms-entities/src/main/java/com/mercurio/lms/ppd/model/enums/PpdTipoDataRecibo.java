package com.mercurio.lms.ppd.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum PpdTipoDataRecibo {
	RECIBO("Emissão Recibo"), CTRC("Emissão CTRC"), RNC("Emissão RNC"), 
	RECEBIMENTO("Recebimento documentação"), LOTE("Adição ao lote"), 
	ENVIO("Envio JDE"), PAGAMENTO("Pagamento");

	private String description;

	private PpdTipoDataRecibo(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}
	
	public static List<Map<String, Object>> getList() {
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>();		
		for(int i=0; i<PpdTipoDataRecibo.values().length;i++) {
			Map<String, Object> mapa = new HashMap<String, Object>();
			mapa.put("sgTipoDataRecibo", PpdTipoDataRecibo.values()[i]);
			mapa.put("dsTipoDataRecibo", PpdTipoDataRecibo.values()[i].getDescription());
			retorno.add(mapa);
		}					
		return retorno;
	}
}
