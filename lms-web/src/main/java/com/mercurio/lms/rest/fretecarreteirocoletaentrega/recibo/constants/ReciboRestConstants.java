package com.mercurio.lms.rest.fretecarreteirocoletaentrega.recibo.constants;

/**
 * Constantes para auxiliar o rest do recibo.
 * 
 */
public enum ReciboRestConstants {

	ID_RECIBO_FRETE_CARRETEIRO("idReciboFreteCarreteiro"),
	TP_RECIBO_FRETE_CARRETEIRO_VALUE("tpReciboFreteCarreteiroValue"),
	DT_PROGRAMADA_PAGTO("dtProgramadaPagto"),
	DD_MM_YYYY("dd/MM/yyyy"),
	YYYY_MM_DD_TZ("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
	VL_DT_SUGERIDA_PAGTO("VL_DT_SUGERIDA_PAGTO"),
	NOME_REPORT("recibo-coleta-entrega"),
	VL_LIMITE_REGISTROS_CSV("VL_LIMITE_REGISTROS_CSV"), 
	CONFIRMED("confirmed"), 
	MOEDAS("moedas"), 
	ANEXOS("anexos"),
	TP_SITUACAO_RECIBO_VALUE("tpSituacaoReciboValue"),
	TP_RECIBO_FRETE_CARRETEIRO("tpReciboFreteCarreteiro"),
	SG_FILIAL("sgFilial"),
	LMS_24043("LMS-24043"),
	LMS_24044("LMS-24044"),
	MTZ("MTZ"),
	COLETA_ENTREGA("Coleta/Entrega"),
	LMS_24046("LMS-24046"),
	LMS_24045("LMS-24045"),
	COMPLEMENTAR("complementar");
	
	private String value;
	
	/**
	 * 
	 * @param value
	 */
	ReciboRestConstants(String value){
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}
}