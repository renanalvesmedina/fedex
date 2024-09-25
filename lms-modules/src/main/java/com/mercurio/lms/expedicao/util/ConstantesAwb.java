package com.mercurio.lms.expedicao.util;


public interface ConstantesAwb {

	int NR_AWB_END = 10;
	int NR_AWB_BEGIN = 4;
	int DS_SERIE_END = 4;
	int DS_SERIE_BEGIN = 0;
	int DV_AWB_END = 11;
	int DV_AWB_BEGIN = 10;
	int NR_CHAVE_END = 47;
	int NR_CHAVE_BEGIN = 3;
	String ID_MOEDA_REAL = "ID_MOEDA_REAL";
	String EXCESSAO_INSERCAO_FTP = "Excessao ao processar arquivo do FTP de inserção de awb por companhia";
	String LOCAL_XML_AWB = "LOCAL_XML_AWB";
	
	String EXTENSAO_XML= "xml";
	String EXTENSAO_PROCESSANDO = "proc";
	String EXTENSAO_OK = "ok";
	String EXTENSAO_NOK = "nok";
	String EXTENSAO_ERR = "err";
	String DIRETORIO_PROCESSADO = "Processado";
	
	String TP_STATUS_AWB = "E";
	String TP_STATUS_PRE_AWB = "P";
	String TP_STATUS_CANCELADO = "C";
	String TP_AWB_NORMAL = "NO";
	
	int MAX_FILES = 30;
	int TWO = 2;
	int THREE = 3;
	
	String AGUARDANDO_EMBARQUE = "AE";
	
	String TP_LOCALIZACAO_AWB_AGUARDANDO_ENTREGA_AEROPORTO = "AN";
}
