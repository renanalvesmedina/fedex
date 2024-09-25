package com.mercurio.lms.rest.contasareceber.lotecobranca.dto;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.contasreceber.model.ItemLoteCobrancaTerceira;
import com.mercurio.lms.rest.contasareceber.excecaonegativacao.dto.FaturaDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
 
public class ItemLoteCobrancaDTO extends BaseDTO { 
	
	private static final long serialVersionUID = 1L;
	private Long idLoteCobranca;
	private FaturaDTO fatura;
	private FilialSuggestDTO filial;
	private String motivo;
	private String nrProcesso;
	private YearMonthDay dtPagamento;
	private YearMonthDay dtDevolucao;
	private Double vlPagamento;
	private Double vlProtesto;
	private Double vlCredito;
	private Double vlJuros;
	private Double vlMulta;
	private Double vlContrato;
	private String observacao;
	private String historico;
	
	public Long getIdLoteCobranca() {
		return idLoteCobranca;
	}
	public void setIdLoteCobranca(Long idLoteCobranca) {
		this.idLoteCobranca = idLoteCobranca;
	}
	public FaturaDTO getFatura() {
		return fatura;
	}
	public void setFatura(FaturaDTO fatura) {
		this.fatura = fatura;
	}
	public FilialSuggestDTO getFilial() {
		return filial;
	}
	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public String getNrProcesso() {
		return nrProcesso;
	}
	public void setNrProcesso(String nrProcesso) {
		this.nrProcesso = nrProcesso;
	}
	public YearMonthDay getDtPagamento() {
		return dtPagamento;
	}
	public void setDtPagamento(YearMonthDay dtPagamento) {
		this.dtPagamento = dtPagamento;
	}
	public YearMonthDay getDtDevolucao() {
		return dtDevolucao;
	}
	public void setDtDevolucao(YearMonthDay dtDevolucao) {
		this.dtDevolucao = dtDevolucao;
	}
	public Double getVlPagamento() {
		return vlPagamento;
	}
	public void setVlPagamento(Double vlPagamento) {
		this.vlPagamento = vlPagamento;
	}
	public Double getVlProtesto() {
		return vlProtesto;
	}
	public void setVlProtesto(Double vlProtesto) {
		this.vlProtesto = vlProtesto;
	}
	public Double getVlCredito() {
		return vlCredito;
	}
	public void setVlCredito(Double vlCredito) {
		this.vlCredito = vlCredito;
	}
	public Double getVlJuros() {
		return vlJuros;
	}
	public void setVlJuros(Double vlJuros) {
		this.vlJuros = vlJuros;
	}
	public Double getVlMulta() {
		return vlMulta;
	}
	public void setVlMulta(Double vlMulta) {
		this.vlMulta = vlMulta;
	}
	public Double getVlContrato() {
		return vlContrato;
	}
	public void setVlContrato(Double vlContrato) {
		this.vlContrato = vlContrato;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public String getHistorico() {
		return historico;
	}
	public void setHistorico(String historico) {
		this.historico = historico;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ItemLoteCobrancaTerceira build(ItemLoteCobrancaTerceira current){
		ItemLoteCobrancaTerceira itemLoteCobrancaTerceira = current;
		itemLoteCobrancaTerceira.setDsMotivo(getMotivo());
		itemLoteCobrancaTerceira.setDsHistorico(getHistorico());
		itemLoteCobrancaTerceira.setDsObservacao(getObservacao());
		itemLoteCobrancaTerceira.setNrProcesso(getNrProcesso());
		itemLoteCobrancaTerceira.setDtPagamento(getDtPagamento());
		itemLoteCobrancaTerceira.setDtDevolucao(getDtDevolucao());
		itemLoteCobrancaTerceira.setVlPagamento(getVlPagamento());
		itemLoteCobrancaTerceira.setVlProtesto(getVlProtesto());
		itemLoteCobrancaTerceira.setVlCredito(getVlCredito());
		itemLoteCobrancaTerceira.setVlJuros(getVlJuros());
		itemLoteCobrancaTerceira.setVlMulta(getVlMulta());
		itemLoteCobrancaTerceira.setVlContrato(getVlContrato());
		
		return itemLoteCobrancaTerceira;
	}
	
} 
