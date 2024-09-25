package com.mercurio.lms.rest.vendas.dto;

import java.util.List;

import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.tabeladeprecos.TabelaPrecoSuggestDTO;

public class TaxaClienteFiltroDTO extends BaseFilterDTO {

	private static final long serialVersionUID = 1L;

	private Long idParametroCliente;
	private TabelaPrecoSuggestDTO tabelaPreco;

	private Long idSimulacao;
	private List<Long> ids;

	public Long getIdParametroCliente() {
		return idParametroCliente;
	}

	public void setIdParametroCliente(Long idParametroCliente) {
		this.idParametroCliente = idParametroCliente;
	}

	public TabelaPrecoSuggestDTO getTabelaPreco() {
		return tabelaPreco;
	}

	public void setTabelaPreco(TabelaPrecoSuggestDTO tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}

	public Long getIdSimulacao() {
		return idSimulacao;
	}

	public void setIdSimulacao(Long idSimulacao) {
		this.idSimulacao = idSimulacao;
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
}
