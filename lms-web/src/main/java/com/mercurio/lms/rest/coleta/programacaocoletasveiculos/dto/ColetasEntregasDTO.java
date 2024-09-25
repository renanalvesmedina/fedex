package com.mercurio.lms.rest.coleta.programacaocoletasveiculos.dto;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.rest.BaseDTO;

public class ColetasEntregasDTO extends BaseDTO{

	private static final long serialVersionUID = 1L;

	private String nrIdentificacaoFormatado;
	private String tpDocumentoServico;
	private String sgFilialOrigem;
	private String nrDoctoServico;
	private YearMonthDay dtPrevEntrega;
	private String nmPessoaDestinatario;
	private String dsEnderecoEntregaReal;
	private Integer qtVolumes;
	private BigDecimal psDoctoServico;
	private BigDecimal vlTotalDocServico;
	private String situacaoDoctoServico;
	private DateTime dhEvento;	
	private Long nrColeta;
	private String cliente;
	private String enderecoComComplemento;
	private BigDecimal psTotalVerificado;
	private BigDecimal vlTotalVerificado;
	private Integer qtTotalVolumesVerificado;
	private String sgFilial;
	private Long idControleCarga;
	private Long idPedidoColeta;
	private String strHorarioColeta;
	private String tpModoPedidoColeta;
	
	
	public String getTpModoPedidoColeta() {
		return tpModoPedidoColeta;
	}
	public void setTpModoPedidoColeta(String tpModoPedidoColeta) {
		this.tpModoPedidoColeta = tpModoPedidoColeta;
	}
	public String getTpDocumentoServico() {
		return tpDocumentoServico;
	}
	public void setTpDocumentoServico(String tpDocumentoServico) {
		this.tpDocumentoServico = tpDocumentoServico;
	}
	public String getSgFilialOrigem() {
		return sgFilialOrigem;
	}
	public void setSgFilialOrigem(String sgFilialOrigem) {
		this.sgFilialOrigem = sgFilialOrigem;
	}
	public String getNrDoctoServico() {
		return nrDoctoServico;
	}
	public void setNrDoctoServico(String nrDoctoServico) {
		this.nrDoctoServico = nrDoctoServico;
	}
	public YearMonthDay getDtPrevEntrega() {
		return dtPrevEntrega;
	}
	public void setDtPrevEntrega(YearMonthDay dtPrevEntrega) {
		this.dtPrevEntrega = dtPrevEntrega;
	}
	public String getNmPessoaDestinatario() {
		return nmPessoaDestinatario;
	}
	public void setNmPessoaDestinatario(String nmPessoaDestinatario) {
		this.nmPessoaDestinatario = nmPessoaDestinatario;
	}
	public String getDsEnderecoEntregaReal() {
		return dsEnderecoEntregaReal;
	}
	public void setDsEnderecoEntregaReal(String dsEnderecoEntregaReal) {
		this.dsEnderecoEntregaReal = dsEnderecoEntregaReal;
	}
	public Integer getQtVolumes() {
		return qtVolumes;
	}
	public void setQtVolumes(Integer qtVolumes) {
		this.qtVolumes = qtVolumes;
	}
	public BigDecimal getPsDoctoServico() {
		return psDoctoServico;
	}
	public void setPsDoctoServico(BigDecimal psDoctoServico) {
		this.psDoctoServico = psDoctoServico;
	}
	public BigDecimal getVlTotalDocServico() {
		return vlTotalDocServico;
	}
	public void setVlTotalDocServico(BigDecimal vlTotalDocServico) {
		this.vlTotalDocServico = vlTotalDocServico;
	}
	public String getSituacaoDoctoServico() {
		return situacaoDoctoServico;
	}
	public void setSituacaoDoctoServico(String situacaoDoctoServico) {
		this.situacaoDoctoServico = situacaoDoctoServico;
	}
	public DateTime getDhEvento() {
		return dhEvento;
	}
	public void setDhEvento(DateTime dhEvento) {
		this.dhEvento = dhEvento;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getNrIdentificacaoFormatado() {
		return nrIdentificacaoFormatado;
	}
	public void setNrIdentificacaoFormatado(String nrIdentificacaoFormatado) {
		this.nrIdentificacaoFormatado = nrIdentificacaoFormatado;
	}
	public Long getNrColeta() {
		return nrColeta;
	}
	public void setNrColeta(Long nrColeta) {
		this.nrColeta = nrColeta;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getEnderecoComComplemento() {
		return enderecoComComplemento;
	}
	public void setEnderecoComComplemento(String enderecoComComplemento) {
		this.enderecoComComplemento = enderecoComComplemento;
	}
	public BigDecimal getPsTotalVerificado() {
		return psTotalVerificado;
	}
	public void setPsTotalVerificado(BigDecimal psTotalVerificado) {
		this.psTotalVerificado = psTotalVerificado;
	}
	public Integer getQtTotalVolumesVerificado() {
		return qtTotalVolumesVerificado;
	}
	public void setQtTotalVolumesVerificado(Integer qtTotalVolumesVerificado) {
		this.qtTotalVolumesVerificado = qtTotalVolumesVerificado;
	}
	public String getSgFilial() {
		return sgFilial;
	}
	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}
	public BigDecimal getVlTotalVerificado() {
		return vlTotalVerificado;
	}
	public void setVlTotalVerificado(BigDecimal vlTotalVerificado) {
		this.vlTotalVerificado = vlTotalVerificado;
	}
	public Long getIdControleCarga() {
		return idControleCarga;
	}
	public void setIdControleCarga(Long idControleCarga) {
		this.idControleCarga = idControleCarga;
	}
	public Long getIdPedidoColeta() {
		return idPedidoColeta;
	}
	public void setIdPedidoColeta(Long idPedidoColeta) {
		this.idPedidoColeta = idPedidoColeta;
	}
	public String getStrHorarioColeta() {
		return strHorarioColeta;
	}
	public void setStrHorarioColeta(String strHorarioColeta) {
		this.strHorarioColeta = strHorarioColeta;
	}
	
	
	
}
