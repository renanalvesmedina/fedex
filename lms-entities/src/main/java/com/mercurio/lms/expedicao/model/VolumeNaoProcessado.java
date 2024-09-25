package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.joda.time.YearMonthDay;

public class VolumeNaoProcessado implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long idVolumeNaoProcessado;
	private String nrLote;
	private Long qtTotalVolumesLote;
	private String nrCodigoBarras;
	private YearMonthDay dtProcessamento;
	private String hrProcessamento;
	private String dsObservacao;

	/**
	 * @return the idVolumeNaoProcessado
	 */
	public Long getIdVolumeNaoProcessado() {
		return idVolumeNaoProcessado;
	}
	/**
	 * @return the nrLote
	 */
	public String getNrLote() {
		return nrLote;
	}
	/**
	 * @return the qtTotalVolumesLote
	 */
	public Long getQtTotalVolumesLote() {
		return qtTotalVolumesLote;
	}
	/**
	 * @return the nrCodigoBarras
	 */
	public String getNrCodigoBarras() {
		return nrCodigoBarras;
	}
	/**
	 * @return the dtProcessamento
	 */
	public YearMonthDay getDtProcessamento() {
		return dtProcessamento;
	}
	/**
	 * @return the hrProcessamento
	 */
	public String getHrProcessamento() {
		return hrProcessamento;
	}
	/**
	 * @return the dsObservacao
	 */
	public String getDsObservacao() {
		return dsObservacao;
	}
	/**
	 * @param idVolumeNaoProcessado the idVolumeNaoProcessado to set
	 */
	public void setIdVolumeNaoProcessado(Long idVolumeNaoProcessado) {
		this.idVolumeNaoProcessado = idVolumeNaoProcessado;
	}
	/**
	 * @param nrLote the nrLote to set
	 */
	public void setNrLote(String nrLote) {
		this.nrLote = nrLote;
	}
	/**
	 * @param qtTotalVolumesLote the qtTotalVolumesLote to set
	 */
	public void setQtTotalVolumesLote(Long qtTotalVolumesLote) {
		this.qtTotalVolumesLote = qtTotalVolumesLote;
	}
	/**
	 * @param nrCodigoBarras the nrCodigoBarras to set
	 */
	public void setNrCodigoBarras(String nrCodigoBarras) {
		this.nrCodigoBarras = nrCodigoBarras;
	}
	/**
	 * @param dtProcessamento the dtProcessamento to set
	 */
	public void setDtProcessamento(YearMonthDay dtProcessamento) {
		this.dtProcessamento = dtProcessamento;
	}
	/**
	 * @param hrProcessamento the hrProcessamento to set
	 */
	public void setHrProcessamento(String hrProcessamento) {
		this.hrProcessamento = hrProcessamento;
	}
	/**
	 * @param dsObservacao the dsObservacao to set
	 */
	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}
	
}
