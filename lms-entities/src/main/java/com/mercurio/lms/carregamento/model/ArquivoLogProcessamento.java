package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * Entidade para atender a demanda LMS-1236
 * 
 * @author mxavier@voiza.com.br
 * 
 */

@Entity
@Table(name = "ARQUIVO_LOG_PROCESSAMENTO")
@SequenceGenerator(name = "ARQUIVO_LOG_PROCESSAMENTO_SQ", sequenceName = "ARQUIVO_LOG_PROCESSAMENTO_SQ")
public class ArquivoLogProcessamento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ARQUIVO_LOG_PROCESSAMENTO_SQ")
	@Column(name = "ID_ARQUIVO_LOG_PROCESSAMENTO", nullable = false)
	private Long idArquivoLogProcessamento;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ARQUIVO_LOG_DESCOMPACTADO", nullable = false)
	private ArquivoLogDescompactado arquivoLogDescompactado;

	@Columns(columns = { @Column(name = "DH_INICIO_PROCESSAMENTO"), @Column(name = "DH_INICIO_PROCESSAMENTO_TZR ") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhInicioProcessamento;

	@Columns(columns = { @Column(name = "DH_FIM_PROCESSAMENTO"), @Column(name = "DH_FIM_PROCESSAMENTO_TZR ") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhFimProcessamento;

	@Column(name = "BL_LOG_SUCESSO", nullable = true)
	private String blLogSucesso;

	@Column(name = "BL_LOG_CARRIER", nullable = true)
	private String blLogCarrier;

	@Column(name = "BL_LOG_DATA", nullable = true)
	private String blLogData;

	@Column(name = "BL_LOG_WAVE", nullable = true)
	private String blLogWave;

	@Column(name = "BL_LOG_DOCA", nullable = true)
	private String blLogDoca;

	@Column(name = "BL_LOG_DESTINO", nullable = true)
	private String blLogDestino;

	@Column(name = "BL_LOG_VOLUME", nullable = true)
	private String blLogVolume;

	@Column(name = "BL_LOG_ROTA", nullable = true)
	private String blLogRota;

	@Column(name = "BL_LOG_ITEM", nullable = true)
	private String blLogItem;

	@Column(name = "BL_LOG_CUBAGEM", nullable = true)
	private String blLogCubagem;

	@Column(name = "BL_LOG_PESO", nullable = true)
	private String blLogPeso;

	@Column(name = "BL_LOG_TOTAL_VOLM", nullable = true)
	private String blLogTotalVolume;

	@Column(name = "BL_LOG_TOTAL_PESO", nullable = true)
	private String blLogTotalPeso;

	@Column(name = "BL_LOG_TOTAL_CUBAGEM", nullable = true)
	private String blLogTotalCubagem;

	@Column(name = "DS_ERRO_ARQUIVO", nullable = true)
	private String dsErroArquivo;

	@Column(name = "NR_MPC", nullable = true)
	private Long nrMpc;

	public Long getIdArquivoLogProcessamento() {
		return idArquivoLogProcessamento;
	}

	public void setIdArquivoLogProcessamento(Long idArquivoLogProcessamento) {
		this.idArquivoLogProcessamento = idArquivoLogProcessamento;
	}

	public ArquivoLogDescompactado getArquivoLogDescompactado() {
		return arquivoLogDescompactado;
	}

	public void setArquivoLogDescompactado(ArquivoLogDescompactado arquivoLogDescompactado) {
		this.arquivoLogDescompactado = arquivoLogDescompactado;
	}

	public DateTime getDhInicioProcessamento() {
		return dhInicioProcessamento;
	}

	public void setDhInicioProcessamento(DateTime dhInicioProcessamento) {
		this.dhInicioProcessamento = dhInicioProcessamento;
	}

	public DateTime getDhFimProcessamento() {
		return dhFimProcessamento;
	}

	public void setDhFimProcessamento(DateTime dhFimProcessamento) {
		this.dhFimProcessamento = dhFimProcessamento;
	}

	public String getBlLogSucesso() {
		return blLogSucesso;
	}

	public void setBlLogSucesso(String blLogSucesso) {
		this.blLogSucesso = blLogSucesso;
	}

	public String getBlLogCarrier() {
		return blLogCarrier;
	}

	public void setBlLogCarrier(String blLogCarrier) {
		this.blLogCarrier = blLogCarrier;
	}

	public String getBlLogData() {
		return blLogData;
	}

	public void setBlLogData(String blLogData) {
		this.blLogData = blLogData;
	}

	public String getBlLogWave() {
		return blLogWave;
	}

	public void setBlLogWave(String blLogWave) {
		this.blLogWave = blLogWave;
	}

	public String getBlLogDoca() {
		return blLogDoca;
	}

	public void setBlLogDoca(String blLogDoca) {
		this.blLogDoca = blLogDoca;
	}

	public String getBlLogDestino() {
		return blLogDestino;
	}

	public void setBlLogDestino(String blLogDestino) {
		this.blLogDestino = blLogDestino;
	}

	public String getBlLogVolume() {
		return blLogVolume;
	}

	public void setBlLogVolume(String blLogVolume) {
		this.blLogVolume = blLogVolume;
	}

	public String getBlLogRota() {
		return blLogRota;
	}

	public void setBlLogRota(String blLogRota) {
		this.blLogRota = blLogRota;
	}

	public String getBlLogItem() {
		return blLogItem;
	}

	public void setBlLogItem(String blLogItem) {
		this.blLogItem = blLogItem;
	}

	public String getBlLogCubagem() {
		return blLogCubagem;
	}

	public void setBlLogCubagem(String blLogCubagem) {
		this.blLogCubagem = blLogCubagem;
	}

	public String getBlLogPeso() {
		return blLogPeso;
	}

	public void setBlLogPeso(String blLogPeso) {
		this.blLogPeso = blLogPeso;
	}

	public String getBlLogTotalVolume() {
		return blLogTotalVolume;
	}

	public void setBlLogTotalVolume(String blLogTotalVolume) {
		this.blLogTotalVolume = blLogTotalVolume;
	}

	public String getBlLogTotalPeso() {
		return blLogTotalPeso;
	}

	public void setBlLogTotalPeso(String blLogTotalPeso) {
		this.blLogTotalPeso = blLogTotalPeso;
	}

	public String getBlLogTotalCubagem() {
		return blLogTotalCubagem;
	}

	public void setBlLogTotalCubagem(String blLogTotalCubagem) {
		this.blLogTotalCubagem = blLogTotalCubagem;
	}

	public String getDsErroArquivo() {
		return dsErroArquivo;
	}

	public void setDsErroArquivo(String dsErroArquivo) {
		this.dsErroArquivo = dsErroArquivo;
	}

	public Long getNrMpc() {
		return nrMpc;
	}

	public void setNrMpc(Long nrMpc) {
		this.nrMpc = nrMpc;
	}
}