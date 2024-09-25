package com.mercurio.lms.rest.fretecarreteirocoletaentrega.recibo.constants;

/**
 * Constantes para auxiliar o rest do rim.
 * 
 */
public enum ReciboConsultaRimRestConstants {
	
	TP_DOCUMENTO_SERVICO(0),
	NR_DOCTO_SERVICO(1),
	SG_DOCTO_SERVICO(2),
	DH_DOCTO_SERVICO(3),
	MOEDA_DOCTO_SERVICO(5),
	VL_DOCTO_SERVICO(6),
	RI_FILIAL(7),
	RI_NR(8),
	RI_DH(10),
	RI_MOEDA(12),
	RI_VL(13),
	RI_ID(14),
	RI_FILIAL_ID(15),
	RI_FILIAL_NOME(16);
	
	private Integer value;
	
	/**
	 * 
	 * @param value
	 */
	ReciboConsultaRimRestConstants(Integer value){
		this.value = value;
	}
	
	public Integer getValue(){
		return this.value;
	}
}