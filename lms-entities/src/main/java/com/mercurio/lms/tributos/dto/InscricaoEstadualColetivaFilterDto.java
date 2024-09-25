package com.mercurio.lms.tributos.dto;

import org.joda.time.YearMonthDay;

public class InscricaoEstadualColetivaFilterDto {

	private Long idRemetente;
	private YearMonthDay dtEmissao;
	private Long idUfDestino;
	private Long idPais;
	
	public Long getIdRemetente() {
		return idRemetente;
	}
	public void setIdRemetente(Long idRemetente) {
		this.idRemetente = idRemetente;
	}
	public YearMonthDay getDtEmissao() {
		return dtEmissao;
	}
	public void setDtEmissao(YearMonthDay dtEmissao) {
		this.dtEmissao = dtEmissao;
	}
	public Long getIdUfDestino() {
		return idUfDestino;
	}
	public void setIdUfDestino(Long idUfDestino) {
		this.idUfDestino = idUfDestino;
	}
	public Long getIdPais() {
		return idPais;
	}
	public void setIdPais(Long idPais) {
		this.idPais = idPais;
	}
	
}
