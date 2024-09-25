package com.mercurio.lms.rest.carregamento.dto;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;

public class EmitirRelatorioCIOTFilterDTO extends BaseFilterDTO {
	private static final long serialVersionUID = 1L;

	private FilialSuggestDTO filial;
	private DomainValue tpSituacao;
	private DomainValue tpControleCarga;
	private DateTime periodoInicial;
	private DateTime periodoFinal;

	public FilialSuggestDTO getFilial() {
		return filial;
	}

	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public DomainValue getTpControleCarga() {
		return tpControleCarga;
	}

	public void setTpControleCarga(DomainValue tpControleCarga) {
		this.tpControleCarga = tpControleCarga;
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

}
