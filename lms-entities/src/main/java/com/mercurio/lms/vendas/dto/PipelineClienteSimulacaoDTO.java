package com.mercurio.lms.vendas.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PipelineClienteSimulacaoDTO implements Serializable {

	private Long idPipelineClienteSimulacao;
	private Long idFilial;
	private String sgFilial;
	private String nmFantasiaFilial;
	private Long nrProposta;
	private Long idTabelaPreco;
	private String tabelaPrecoString;
	private String dsDescricaoTabelaPreco;
	
	public Long getIdPipelineClienteSimulacao() {
		return idPipelineClienteSimulacao;
	}
	public void setIdPipelineClienteSimulacao(Long idPipelineClienteSimulacao) {
		this.idPipelineClienteSimulacao = idPipelineClienteSimulacao;
	}
	public Long getIdFilial() {
		return idFilial;
	}
	public void setIdFilial(Long idFilial) {
		this.idFilial = idFilial;
	}
	public String getSgFilial() {
		return sgFilial;
	}
	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}
	public String getNmFantasiaFilial() {
		return nmFantasiaFilial;
	}
	public void setNmFantasiaFilial(String nmFantasiaFilial) {
		this.nmFantasiaFilial = nmFantasiaFilial;
	}
	public Long getNrProposta() {
		return nrProposta;
	}
	public void setNrProposta(Long nrProposta) {
		this.nrProposta = nrProposta;
	}
	public Long getIdTabelaPreco() {
		return idTabelaPreco;
	}
	public void setIdTabelaPreco(Long idTabelaPreco) {
		this.idTabelaPreco = idTabelaPreco;
	}
	public String getTabelaPrecoString() {
		return tabelaPrecoString;
	}
	public void setTabelaPrecoString(String tabelaPrecoString) {
		this.tabelaPrecoString = tabelaPrecoString;
	}
	public String getDsDescricaoTabelaPreco() {
		return dsDescricaoTabelaPreco;
	}
	public void setDsDescricaoTabelaPreco(String dsDescricaoTabelaPreco) {
		this.dsDescricaoTabelaPreco = dsDescricaoTabelaPreco;
	}

}
