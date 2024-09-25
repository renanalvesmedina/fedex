package com.mercurio.lms.rest.vendas.dto;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.tabeladeprecos.TabelaPrecoSuggestDTO;

public class PropostaClienteFiltroDTO extends BaseFilterDTO {

	private static final long serialVersionUID = 1L;

	private FilialSuggestDTO filial;
	private Long nrProposta;
	private ClienteSuggestDTO cliente;
	private Long divisaoCliente;
	private TabelaPrecoSuggestDTO tabelaPreco;
	private Long servico;
	private TabelaPrecoSuggestDTO tabelaFob;
	private FuncionarioPromotorSuggestDTO promotor;
	private DomainValue situacaoProposta;
	private DomainValue tpGeracaoProposta;
	
	public PropostaClienteFiltroDTO() {
	}
	
	public PropostaClienteFiltroDTO(FilialSuggestDTO filial, Long nrProposta,
			ClienteSuggestDTO cliente, Long divisaoCliente,
			TabelaPrecoSuggestDTO tabelaPreco, Long servico,
			TabelaPrecoSuggestDTO tabelaFob, FuncionarioPromotorSuggestDTO promotor,
			DomainValue situacao) {
		super();
		this.filial = filial;
		this.nrProposta = nrProposta;
		this.cliente = cliente;
		this.divisaoCliente = divisaoCliente;
		this.tabelaPreco = tabelaPreco;
		this.servico = servico;
		this.tabelaFob = tabelaFob;
		this.promotor = promotor;
		this.situacaoProposta = situacao;
	}
	
	public FilialSuggestDTO getFilial() {
		return filial;
	}
	
	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}
	
	public Long getIdFilial() {
		return this.filial == null? null : this.filial.getId();
	}
	
	public Long getNrProposta() {
		return nrProposta;
	}
	
	public void setNrProposta(Long nrProposta) {
		this.nrProposta = nrProposta;
	}
	
	public ClienteSuggestDTO getCliente() {
		return cliente;
	}
	
	public void setCliente(ClienteSuggestDTO cliente) {
		this.cliente = cliente;
	}
	
	public Long getIdCliente() {
		return this.cliente == null ? null : this.cliente.getIdCliente();
	}
	
	public Long getDivisaoCliente() {
		return divisaoCliente;
	}
	
	public void setDivisaoCliente(Long divisaoCliente) {
		this.divisaoCliente = divisaoCliente;
	}
	
	public TabelaPrecoSuggestDTO getTabelaPreco() {
		return tabelaPreco;
	}
	
	public void setTabelaPreco(TabelaPrecoSuggestDTO tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}
	
	public Long getIdTabelaPreco() {
		return this.tabelaPreco == null? null : this.tabelaPreco.getIdTabelaPreco();
	}
	
	public Long getServico() {
		return servico;
	}
	
	public void setServico(Long servico) {
		this.servico = servico;
	}
	
	public TabelaPrecoSuggestDTO getTabelaFob() {
		return tabelaFob;
	}
	
	public void setTabelaFob(TabelaPrecoSuggestDTO tabelaFob) {
		this.tabelaFob = tabelaFob;
	}
	
	public Long getIdTabelaFob() {
		return this.tabelaFob == null? null : this.tabelaFob.getIdTabelaPreco();
	}
	
	public FuncionarioPromotorSuggestDTO getPromotor() {
		return promotor;
	}
	
	public void setPromotor(FuncionarioPromotorSuggestDTO promotor) {
		this.promotor = promotor;
	}
	
	public Long getIdPromotor() {
		return this.promotor == null ? null : this.promotor.getIdUsuario();
	}
	
	public void setSituacaoProposta(DomainValue situacaoProposta) {
		this.situacaoProposta = situacaoProposta;
	}
	
	public DomainValue getSituacaoProposta() {
		return situacaoProposta;
	}
	
	public String getValorSituacao() {
		return this.situacaoProposta == null ? null : this.situacaoProposta.getValue(); 
	}

	public DomainValue getTpGeracaoProposta() {
		return tpGeracaoProposta;
	}

	public void setTpGeracaoProposta(DomainValue tpGeracaoProposta) {
		this.tpGeracaoProposta = tpGeracaoProposta;
	}
	
	public String getValorGeracaoProposta() {
		return this.tpGeracaoProposta == null ? null : this.tpGeracaoProposta.getValue(); 
	}
	
}
