package com.mercurio.lms.fretecarreteirocoletaentrega.dto;

public class DadoNotaCreditoDto {

	private Long qtColetasExecutadas = 0L;
	private Long qtEntregasRealizadas = 0L;
	
	public Long getQtColetasExecutadas() {
		return qtColetasExecutadas;
	}
	public void setQtColetasExecutadas(Long qtColetasExecutadas) {
		this.qtColetasExecutadas = qtColetasExecutadas;
	}
	public Long getQtEntregasRealizadas() {
		return qtEntregasRealizadas;
	}
	public void setQtEntregasRealizadas(Long qtEntregasRealizadas) {
		this.qtEntregasRealizadas = qtEntregasRealizadas;
	}
	
}