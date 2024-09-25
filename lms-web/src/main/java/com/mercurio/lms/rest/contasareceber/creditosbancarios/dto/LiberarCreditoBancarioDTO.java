package com.mercurio.lms.rest.contasareceber.creditosbancarios.dto;

import org.joda.time.YearMonthDay;

public class LiberarCreditoBancarioDTO {

	Long idCreditoBancario;
	YearMonthDay dataDeCorte;
	
	public Long getIdCreditoBancario() {
		return idCreditoBancario;
	}
	public void setIdCreditoBancario(Long idCreditoBancario) {
		this.idCreditoBancario = idCreditoBancario;
	}
	public YearMonthDay getDataDeCorte() {
		return dataDeCorte;
	}
	public void setDataDeCorte(YearMonthDay dataDeCorte) {
		this.dataDeCorte = dataDeCorte;
	}
	
	
}
