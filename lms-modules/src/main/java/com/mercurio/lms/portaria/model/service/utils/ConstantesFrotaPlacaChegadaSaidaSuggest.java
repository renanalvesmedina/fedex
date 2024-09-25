package com.mercurio.lms.portaria.model.service.utils;

public class ConstantesFrotaPlacaChegadaSaidaSuggest {
	
	private static final String SCAPE = "'";
	
	public static final String TIPO_SAIDA_SQL_STRING = SCAPE + ConstantesPortaria.TIPO_SAIDA + SCAPE;
	public static final String TIPO_SAIDA_LABEL_SQL_STRING = "'Saída'";
	
	public static final String TIPO_CHEGADA_SQL_STRING = SCAPE + ConstantesPortaria.TIPO_CHEGADA + SCAPE;
	public static final String TIPO_CHEGADA_LABEL_SQL_STRING = "'Chegada'";
	
	public static final String SUBTIPO_VIAGEM_SQL_STRING = SCAPE + ConstantesPortaria.SUBTIPO_VIAGEM + SCAPE;	
	public static final String SUBTIPO_VIAGEM_LABEL_SQL_STRING = "'Viagem'";
	
	public static final String SUBTIPO_COLETA_ENTREGA_SQL_STRING = SCAPE + ConstantesPortaria.SUBTIPO_COLETA_ENTREGA + SCAPE;	
	public static final String SUBTIPO_COLETA_ENTREGA_LABEL_SQL_STRING = "'Coleta/entrega'";
	
	public static final String SUBTIPO_ORDEM_SAIDA_SQL_STRING = SCAPE + ConstantesPortaria.SUBTIPO_ORDEM_SAIDA + SCAPE;
	public static final String SUBTIPO_ORDEM_SAIDA_LABEL_SQL_STRING = "'Ordem de Saída'";
	
	private ConstantesFrotaPlacaChegadaSaidaSuggest() {
	}

}
