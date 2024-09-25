package com.mercurio.lms.rest.vendas.dto;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.rest.BaseFilterDTO;

public class ReajusteTabelaClienteFilterDTO extends BaseFilterDTO {
	private static final long serialVersionUID = 1L;

	private Long nrIdentificacaoEmpregador;
	private Long idFilial;
	private YearMonthDay dataReajuste;
	
	public Long getNrIdentificacaoEmpregador() {
		return nrIdentificacaoEmpregador;
	}
	public void setNrIdentificacaoEmpregador(Long nrIdentificacaoEmpregador) {
		this.nrIdentificacaoEmpregador = nrIdentificacaoEmpregador;
	}
	public YearMonthDay getDataReajuste() {
		return dataReajuste;
	}
	public void setDataReajuste(YearMonthDay dataReajuste) {
		this.dataReajuste = dataReajuste;
	}
	public Long getIdFilial() {
		return idFilial;
	}
	public void setIdFilial(Long idFilial) {
		this.idFilial = idFilial;
	}
}
