package com.mercurio.lms.rest.vendas.dto;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;

public class ConhecimentosNaoComissionaveisFilterDTO extends BaseFilterDTO {

	private static final long serialVersionUID = -1589356556617984695L;

	private UsuarioDTO executivo;
	private YearMonthDay dtInicio;
	private YearMonthDay dtFim;
	private ClienteSuggestDTO cliente;

	public UsuarioDTO getExecutivo() {
		return executivo;
	}

	public void setExecutivo(UsuarioDTO executivo) {
		this.executivo = executivo;
	}

	public ClienteSuggestDTO getCliente() {
		return cliente;
	}
	
	public void setCliente(ClienteSuggestDTO cliente) {
		this.cliente = cliente;
	}

	public YearMonthDay getDtInicio() {
		return dtInicio;
	}

	public void setDtInicio(YearMonthDay dtInicio) {
		this.dtInicio = dtInicio;
	}

	public YearMonthDay getDtFim() {
		return dtFim;
	}

	public void setDtFim(YearMonthDay dtFim) {
		this.dtFim = dtFim;
	}

}
