package com.mercurio.lms.rest.vendas.dto;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.rest.BaseDTO;

public class ConhecimentosNaoComissionaveisGridDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long nrDoctoServico;
	private String tpDocumento;
	private Double vlValorFreteLiquido;
	private YearMonthDay dhEmissao;
	private YearMonthDay dtPagamento;
	private String tpFrete;
	private String tpModal;
	private String cnpjRemetente;
	private String nmRemetente;
	private String cnpjDestinatario;
	private String nmDestinatario;
	private String cnpjResponsavel;
	private String nmResponsavel;
	private String nmTerritorio;
	private Boolean naoComissionavel;
	private Long idExecutivo;
	
	public Long getNrDoctoServico() {
		return nrDoctoServico;
	}

	public void setNrDoctoServico(Long nrDoctoServico) {
		this.nrDoctoServico = nrDoctoServico;
	}

	public String getTpDocumento() {
		return tpDocumento;
	}

	public void setTpDocumento(String tpDocumento) {
		this.tpDocumento = tpDocumento;
	}

	public Double getVlValorFreteLiquido() {
		return vlValorFreteLiquido;
	}

	public void setVlValorFreteLiquido(Double vlValorFreteLiquido) {
		this.vlValorFreteLiquido = vlValorFreteLiquido;
	}

	public YearMonthDay getDhEmissao() {
		return dhEmissao;
	}
	
	public void setDhEmissao(YearMonthDay dhEmissao) {
		this.dhEmissao = dhEmissao;
	}
	
	public YearMonthDay getDtPagamento() {
		return dtPagamento;
	}
	
	public void setDtPagamento(YearMonthDay dtPagamento) {
		this.dtPagamento = dtPagamento;
	}
	
	public String getTpFrete() {
		return tpFrete;
	}
	
	public void setTpFrete(String tpFrete) {
		this.tpFrete = tpFrete;
	}
	
	public String getTpModal() {
		return tpModal;
	}
	
	public void setTpModal(String tpModal) {
		this.tpModal = tpModal;
	}
	
	public String getCnpjRemetente() {
		return cnpjRemetente;
	}
	
	public void setCnpjRemetente(String cnpjRemetente) {
		this.cnpjRemetente = cnpjRemetente;
	}
	
	public String getNmRemetente() {
		return nmRemetente;
	}
	
	public void setNmRemetente(String nmRemetente) {
		this.nmRemetente = nmRemetente;
	}
	
	public String getCnpjDestinatario() {
		return cnpjDestinatario;
	}
	
	public void setCnpjDestinatario(String cnpjDestinatario) {
		this.cnpjDestinatario = cnpjDestinatario;
	}
	
	public String getNmDestinatario() {
		return nmDestinatario;
	}
	
	public void setNmDestinatario(String nmDestinatario) {
		this.nmDestinatario = nmDestinatario;
	}
	
	public String getCnpjResponsavel() {
		return cnpjResponsavel;
	}
	
	public void setCnpjResponsavel(String cnpjResponsavel) {
		this.cnpjResponsavel = cnpjResponsavel;
	}
	
	public String getNmResponsavel() {
		return nmResponsavel;
	}
	
	public void setNmResponsavel(String nmResponsavel) {
		this.nmResponsavel = nmResponsavel;
	}
	
	public String getNmTerritorio() {
		return nmTerritorio;
	}

	public void setNmTerritorio(String nmTerritorio) {
		this.nmTerritorio = nmTerritorio;
	}

	public Boolean getNaoComissionavel() {
		return naoComissionavel;
	}

	public void setNaoComissionavel(Boolean naoComissionavel) {
		this.naoComissionavel = naoComissionavel;
	}

	public Long getIdExecutivo() {
		return idExecutivo;
	}

	public void setIdExecutivo(Long idExecutivo) {
		this.idExecutivo = idExecutivo;
	}
	
}
