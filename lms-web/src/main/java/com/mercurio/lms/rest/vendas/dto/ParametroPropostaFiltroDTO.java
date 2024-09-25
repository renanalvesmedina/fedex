package com.mercurio.lms.rest.vendas.dto;

import java.util.List;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.tabeladeprecos.ServicoSuggestDTO;
import com.mercurio.lms.rest.tabeladeprecos.TabelaPrecoSuggestDTO;

public class ParametroPropostaFiltroDTO extends BaseFilterDTO {

	private static final long serialVersionUID = 1L;

	private List<Long> ids;
	private Long idSimulacao;
	private DomainValue tpGeracaoProposta;

	private ClienteSuggestDTO cliente;
	private DivisaoClienteSuggestDTO divisaoCliente;
	private TabelaPrecoSuggestDTO tabelaPreco;
	private ServicoSuggestDTO servico;

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public Long getIdSimulacao() {
		return idSimulacao;
	}

	public void setIdSimulacao(Long idSimulacao) {
		this.idSimulacao = idSimulacao;
	}

	public DomainValue getTpGeracaoProposta() {
		return tpGeracaoProposta;
	}

	public void setTpGeracaoProposta(DomainValue tpGeracaoProposta) {
		this.tpGeracaoProposta = tpGeracaoProposta;
	}

	public ClienteSuggestDTO getCliente() {
		return cliente;
	}

	public void setCliente(ClienteSuggestDTO cliente) {
		this.cliente = cliente;
	}

	public DivisaoClienteSuggestDTO getDivisaoCliente() {
		return divisaoCliente;
	}

	public void setDivisaoCliente(DivisaoClienteSuggestDTO divisaoCliente) {
		this.divisaoCliente = divisaoCliente;
	}

	public TabelaPrecoSuggestDTO getTabelaPreco() {
		return tabelaPreco;
	}

	public void setTabelaPreco(TabelaPrecoSuggestDTO tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}

	public ServicoSuggestDTO getServico() {
		return servico;
	}

	public void setServico(ServicoSuggestDTO servico) {
		this.servico = servico;
	}
}
