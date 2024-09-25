package com.mercurio.lms.rest.fretecarreteirocoletaentrega.recibo.constants;

/**
 * Constantes para auxiliar o helper do recibo.
 * 
 */
public enum ReciboHelperConstants {

	MATRIZ("matriz"),
	WORKFLOW("workflow"),
	STATUS_BLOQUEADO("BL"),
	STATUS_ASSINATURAS("AA"),
	STATUS_GERADO("GE"),
	STATUS_EMITIDO("EM"),
	STATUS_EM_APROVACAO("EA"),
	STATUS_REJEITADO("RE"),
	STATUS_ENVIADO_JDE("EJ"),
	STATUS_LIBERADO("LI"),
	STATUS_AGUARDANDO_ENVIO_JDE("AJ"),
	STATUS_PAGO("PA"),
	STATUS_CANCELADO("CA"),
	STATUS_EM_RELACAO_PAGAMENTO("ER"),
	EVENTO_2401("E_2401"),
	EVENTO_2402("E_2402");
	
	private String value;
	
	/**
	 * 
	 * @param value
	 */
	ReciboHelperConstants(String value){
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}
}