/**
 * 
 */
package com.mercurio.lms.expedicao.model;

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

import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.sim.model.EventoVolume;

/**
 * @author vagnerh
 *
 */
@Entity
@Table(name="VOLUME_SOBRA_FILIAL")
@SequenceGenerator(name="SQ_VOLUME_SOBRA_FILIAL", sequenceName="VOLUME_SOBRA_FILIAL_SQ", allocationSize=1)
public class VolumeSobraFilial implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private Long idVolumeSobraFilial;
	
	private Filial filial;
	
	private VolumeNotaFiscal volumeNotaFiscal;
	
	private EventoVolume eventoVolume;
	
	private DateTime dhCriacao;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_VOLUME_SOBRA_FILIAL")
	@Column(name = "ID_VOLUME_SOBRA_FILIAL", nullable = false)
	public Long getIdVolumeSobraFilial() {
		return idVolumeSobraFilial;
	}

	public void setIdVolumeSobraFilial(Long idVolumeSobraFilial) {
		this.idVolumeSobraFilial = idVolumeSobraFilial;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_FILIAL", nullable = true)
	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_VOLUME_NOTA_FISCAL",nullable = false)
	public VolumeNotaFiscal getVolumeNotaFiscal() {
		return volumeNotaFiscal;
	}

	public void setVolumeNotaFiscal(VolumeNotaFiscal volumeNotaFiscal) {
		this.volumeNotaFiscal = volumeNotaFiscal;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_EVENTO_VOLUME", nullable = false)
	public EventoVolume getEventoVolume() {
		return eventoVolume;
	}

	public void setEventoVolume(EventoVolume eventoVolume) {
		this.eventoVolume = eventoVolume;
	}

	@Columns(columns = { @Column(name = "DH_CRIACAO", nullable = false),
			@Column(name = "DH_CRIACAO_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	public DateTime getDhCriacao() {
		return dhCriacao;
	}

	public void setDhCriacao(DateTime dhCriacao) {
		this.dhCriacao = dhCriacao;
	}
}
