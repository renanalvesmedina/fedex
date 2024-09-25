package com.mercurio.lms.rest.sgr.dto;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;

public class RankingFornecedorEscoltaFilterDTO extends BaseFilterDTO {

	private static final long serialVersionUID = 1L;

	private ClienteSuggestDTO cliente;
	private FilialSuggestDTO filial;
	private YearMonthDay dtVigenciaInicial;
	private YearMonthDay dtVigenciaFinal;

	public ClienteSuggestDTO getCliente() {
		return cliente;
	}

	public void setCliente(ClienteSuggestDTO cliente) {
		this.cliente = cliente;
	}

	public FilialSuggestDTO getFilial() {
		return filial;
	}

	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

}
