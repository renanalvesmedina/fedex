package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.RamoAtividade;
import com.mercurio.lms.municipios.model.Filial;

public class ClientePerdido implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long idClientePerdido;
	private Filial filial;
	private Cliente cliente;
	private SegmentoMercado segmentoMercado;
	private RamoAtividade ramoAtividade;
	private DomainValue tpAbrangencia;
	private DomainValue tpModal;
	private YearMonthDay dtPerda;
	private YearMonthDay dtFinalOperacao;
	private DomainValue tpPerda;
	private BigDecimal nrReceitaPerdida;
	private BigDecimal nrReceitaMedia;
	private Long nrPesoMedio;
	private Long nrMediaEnvio;
	private Long nrMediaCTRC;
	private DomainValue tpMotivoPerda;
	private Moeda moeda;
	
	public Moeda getMoeda() {
		return moeda;
	}

	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
	}

	public Long getIdClientePerdido() {
		return idClientePerdido;
	}

	public void setIdClientePerdido(Long idClientePerdido) {
		this.idClientePerdido = idClientePerdido;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public SegmentoMercado getSegmentoMercado() {
		return segmentoMercado;
	}

	public void setSegmentoMercado(SegmentoMercado segmentoMercado) {
		this.segmentoMercado = segmentoMercado;
	}

	public RamoAtividade getRamoAtividade() {
		return ramoAtividade;
	}

	public void setRamoAtividade(RamoAtividade ramoAtividade) {
		this.ramoAtividade = ramoAtividade;
	}

	public DomainValue getTpAbrangencia() {
		return tpAbrangencia;
	}

	public void setTpAbrangencia(DomainValue tpAbrangencia) {
		this.tpAbrangencia = tpAbrangencia;
	}

	public DomainValue getTpModal() {
		return tpModal;
	}

	public void setTpModal(DomainValue tpModal) {
		this.tpModal = tpModal;
	}
	
	public YearMonthDay getDtPerda() {
		return dtPerda;
	}

	public void setDtPerda(YearMonthDay dtPerda) {
		this.dtPerda = dtPerda;
	}

	public YearMonthDay getDtFinalOperacao() {
		return dtFinalOperacao;
	}

	public void setDtFinalOperacao(YearMonthDay dtFinalOperacao) {
		this.dtFinalOperacao = dtFinalOperacao;
	}

	public DomainValue getTpPerda() {
		return tpPerda;
	}

	public void setTpPerda(DomainValue tpPerda) {
		this.tpPerda = tpPerda;
	}
	
	public BigDecimal getNrReceitaPerdida() {
		return nrReceitaPerdida;
	}

	public void setNrReceitaPerdida(BigDecimal nrReceitaPerdida) {
		this.nrReceitaPerdida = nrReceitaPerdida;
	}

	public BigDecimal getNrReceitaMedia() {
		return nrReceitaMedia;
	}

	public void setNrReceitaMedia(BigDecimal nrReceitaMedia) {
		this.nrReceitaMedia = nrReceitaMedia;
	}

	public Long getNrPesoMedio() {
		return nrPesoMedio;
	}

	public void setNrPesoMedio(Long nrPesoMedio) {
		this.nrPesoMedio = nrPesoMedio;
	}

	public Long getNrMediaEnvio() {
		return nrMediaEnvio;
	}

	public void setNrMediaEnvio(Long nrMediaEnvio) {
		this.nrMediaEnvio = nrMediaEnvio;
	}

	public Long getNrMediaCTRC() {
		return nrMediaCTRC;
	}

	public void setNrMediaCTRC(Long nrMediaCTRC) {
		this.nrMediaCTRC = nrMediaCTRC;
	}

	public DomainValue getTpMotivoPerda() {
		return tpMotivoPerda;
	}

	public void setTpMotivoPerda(DomainValue tpMotivoPerda) {
		this.tpMotivoPerda = tpMotivoPerda;
	}
	
}
