package com.mercurio.lms.rest.carregamento.dto;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;

public class MonitoramentoCIOTFilterDTO extends BaseFilterDTO {
	private static final long serialVersionUID = 1L;

	private FilialSuggestDTO filial;
	private ControleCargaSuggestDTO controleCarga;
	private DateTime periodoInicial;
	private DateTime periodoFinal;
	private DomainValue tpSituacao;

	public FilialSuggestDTO getFilial() {
		return filial;
	}

	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}

	public ControleCargaSuggestDTO getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(ControleCargaSuggestDTO controleCarga) {
		this.controleCarga = controleCarga;
	}

	public DateTime getPeriodoInicial() {
		return periodoInicial;
	}

	public void setPeriodoInicial(DateTime periodoInicial) {
		this.periodoInicial = periodoInicial;
	}

	public DateTime getPeriodoFinal() {
		return periodoFinal;
	}

	public void setPeriodoFinal(DateTime periodoFinal) {
		this.periodoFinal = periodoFinal;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}
}
