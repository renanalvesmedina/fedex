package com.mercurio.lms.rest.sgr.dto;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;

public class RankingFornecedorEscoltaDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private ClienteSuggestDTO cliente;
	private FilialSuggestDTO Filial;
	private YearMonthDay dtVigenciaInicial;
	private YearMonthDay dtVigenciaFinal;
	private List<FornecedorEscoltaSuggestDTO> fornecedoresEscolta;

	public ClienteSuggestDTO getCliente() {
		return cliente;
	}

	public void setCliente(ClienteSuggestDTO cliente) {
		this.cliente = cliente;
	}

	public FilialSuggestDTO getFilial() {
		return Filial;
	}

	public void setFilial(FilialSuggestDTO filial) {
		Filial = filial;
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

	public List<FornecedorEscoltaSuggestDTO> getFornecedoresEscolta() {
		return fornecedoresEscolta;
	}

	public void setFornecedoresEscolta(List<FornecedorEscoltaSuggestDTO> fornecedoresEscolta) {
		this.fornecedoresEscolta = fornecedoresEscolta;
	}

}
