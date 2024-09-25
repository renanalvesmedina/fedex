package com.mercurio.lms.rest.sgr.dto;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;

public class FornecedorEscoltaImpedidoDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idFornecedorEscolta;
	private ClienteSuggestDTO cliente;
	private YearMonthDay dtVigenciaInicial;
	private YearMonthDay dtVigenciaFinal;

	public Long getIdFornecedorEscolta() {
		return idFornecedorEscolta;
	}

	public void setIdFornecedorEscolta(Long idFornecedorEscolta) {
		this.idFornecedorEscolta = idFornecedorEscolta;
	}

	public ClienteSuggestDTO getCliente() {
		return cliente;
	}

	public void setCliente(ClienteSuggestDTO clienteDTO) {
		this.cliente = clienteDTO;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dhVigenciaInicial) {
		this.dtVigenciaInicial = dhVigenciaInicial;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dhVigenciaFinal) {
		this.dtVigenciaFinal = dhVigenciaFinal;
	}

}
