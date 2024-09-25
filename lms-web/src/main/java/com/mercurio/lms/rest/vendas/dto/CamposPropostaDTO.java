package com.mercurio.lms.rest.vendas.dto;

import com.mercurio.adsm.rest.BaseDTO;


public class CamposPropostaDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;
	
	private boolean promotor = false;
	private boolean cliente = false;
	private boolean divisaoCliente = false;
	private boolean tabelaPreco = false;
	private boolean servico = false;
	private boolean tabelaPrecoFob = false;
	private boolean tpGeracaoProposta = false;
	private boolean nrFatorCubagem = false;
	private boolean nrFatorDensidade = false;
	private boolean removeById = false;
	
	private boolean dataValidadeProposta = false;
	private boolean dataInicioVigencia = false;
	private boolean dataAceitacaoCliente = false;
	private boolean observacao = false;
	
	public CamposPropostaDTO() {
	}
	
	public CamposPropostaDTO(boolean disableAll) {
		super();
		this.promotor = disableAll;
		this.cliente = disableAll;
		this.divisaoCliente = disableAll;
		this.tabelaPreco = disableAll;
		this.servico = disableAll;
		this.tabelaPrecoFob = disableAll;
		this.tpGeracaoProposta = disableAll;
		this.nrFatorCubagem = disableAll;
		this.nrFatorDensidade = disableAll;
		this.removeById = disableAll;
		this.dataValidadeProposta = disableAll;
		this.dataInicioVigencia = disableAll;
		this.dataAceitacaoCliente = disableAll;
		this.observacao = disableAll;
	}

	public boolean isPromotor() {
		return promotor;
	}

	public void setPromotor(boolean promotor) {
		this.promotor = promotor;
	}

	public boolean isCliente() {
		return cliente;
	}

	public void setCliente(boolean cliente) {
		this.cliente = cliente;
	}

	public boolean isDivisaoCliente() {
		return divisaoCliente;
	}

	public void setDivisaoCliente(boolean divisaoCliente) {
		this.divisaoCliente = divisaoCliente;
	}

	public boolean isTabelaPreco() {
		return tabelaPreco;
	}

	public void setTabelaPreco(boolean tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}

	public boolean isServico() {
		return servico;
	}

	public void setServico(boolean servico) {
		this.servico = servico;
	}

	public boolean isTabelaPrecoFob() {
		return tabelaPrecoFob;
	}

	public void setTabelaPrecoFob(boolean tabelaPrecoFob) {
		this.tabelaPrecoFob = tabelaPrecoFob;
	}

	public boolean isTpGeracaoProposta() {
		return tpGeracaoProposta;
	}

	public void setTpGeracaoProposta(boolean tpGeracaoProposta) {
		this.tpGeracaoProposta = tpGeracaoProposta;
	}

	public boolean isNrFatorCubagem() {
		return nrFatorCubagem;
	}

	public void setNrFatorCubagem(boolean nrFatorCubagem) {
		this.nrFatorCubagem = nrFatorCubagem;
	}

	public boolean isNrFatorDensidade() {
		return nrFatorDensidade;
	}

	public void setNrFatorDensidade(boolean nrFatorDensidade) {
		this.nrFatorDensidade = nrFatorDensidade;
	}

	public boolean isRemoveById() {
		return removeById;
	}

	public void setRemoveById(boolean removeById) {
		this.removeById = removeById;
	}

	public boolean isDataValidadeProposta() {
		return dataValidadeProposta;
	}

	public void setDataValidadeProposta(boolean dataValidadeProposta) {
		this.dataValidadeProposta = dataValidadeProposta;
	}

	public boolean isDataInicioVigencia() {
		return dataInicioVigencia;
	}

	public void setDataInicioVigencia(boolean dataInicioVigencia) {
		this.dataInicioVigencia = dataInicioVigencia;
	}

	public boolean isDataAceitacaoCliente() {
		return dataAceitacaoCliente;
	}

	public void setDataAceitacaoCliente(boolean dataAceitacaoCliente) {
		this.dataAceitacaoCliente = dataAceitacaoCliente;
	}

	public boolean isObservacao() {
		return observacao;
	}

	public void setObservacao(boolean observacao) {
		this.observacao = observacao;
	}

}
