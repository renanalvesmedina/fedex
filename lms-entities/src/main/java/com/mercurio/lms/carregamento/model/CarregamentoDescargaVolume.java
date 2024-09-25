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
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;

@Entity
@Table(name = "CARREG_DESC_VOLUME")
@SequenceGenerator(name = "CARREG_DESC_VOLUME_SQ", sequenceName = "CARREG_DESC_VOLUME_SQ", allocationSize=1)
public class CarregamentoDescargaVolume implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CARREG_DESC_VOLUME_SQ")
	@Column(name = "ID_CARREG_DESC_VOLUME", nullable = false)
	private Long idCarregamentoDescargaVolume;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_CARREGAMENTO_DESCARGA", nullable = false)
	private CarregamentoDescarga carregamentoDescarga;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_VOLUME_NOTA_FISCAL", nullable = true)
	private VolumeNotaFiscal volumeNotaFiscal;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_DISPOSITIVO_UNITIZACAO", nullable = true)	
	private DispositivoUnitizacao dispositivoUnitizacao;
	
	@Columns(columns = { @Column(name = "DH_OPERACAO"), @Column(name = "DH_OPERACAO_TZR ") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhOperacao;
	
	@Column(name = "TP_SCAN", length = 2, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_SCAN") })
	private DomainValue tpScan;
	
	@Column(name = "QT_VOLUMES", nullable = true)
	private Integer qtVolumes;

	public Long getIdCarregamentoDescargaVolume() {
		return idCarregamentoDescargaVolume;
	}

	public void setIdCarregamentoDescargaVolume(Long idCarregamentoDescargaVolume) {
		this.idCarregamentoDescargaVolume = idCarregamentoDescargaVolume;
	}

	public CarregamentoDescarga getCarregamentoDescarga() {
		return carregamentoDescarga;
	}

	public void setCarregamentoDescarga(CarregamentoDescarga carregamentoDescarga) {
		this.carregamentoDescarga = carregamentoDescarga;
	}

	public VolumeNotaFiscal getVolumeNotaFiscal() {
		return volumeNotaFiscal;
	}

	public void setVolumeNotaFiscal(VolumeNotaFiscal volumeNotaFiscal) {
		this.volumeNotaFiscal = volumeNotaFiscal;
	}

	public DispositivoUnitizacao getDispositivoUnitizacao() {
		return dispositivoUnitizacao;
	}

	public void setDispositivoUnitizacao(DispositivoUnitizacao dispositivoUnitizacao) {
		this.dispositivoUnitizacao = dispositivoUnitizacao;
	}

	public DateTime getDhOperacao() {
		return dhOperacao;
	}

	public void setDhOperacao(DateTime dhOperacao) {
		this.dhOperacao = dhOperacao;
	}

	public DomainValue getTpScan() {
		return tpScan;
	}

	public void setTpScan(DomainValue tpScan) {
		this.tpScan = tpScan;
	}

	public Integer getQtVolumes() {
		return qtVolumes;
	}

	public void setQtVolumes(Integer qtVolumes) {
		this.qtVolumes = qtVolumes;
	}
}