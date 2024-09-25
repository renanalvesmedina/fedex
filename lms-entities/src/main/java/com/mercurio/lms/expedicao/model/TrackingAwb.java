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

/**
 * @author RuhanB
 *
 */
@Entity
@Table(name="TRACKING_AWB")
@SequenceGenerator(name = "TRACKING_AWB_SQ", sequenceName = "TRACKING_AWB_SQ", allocationSize=1)
public class TrackingAwb implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_TRACKING_AWB", nullable=false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRACKING_AWB_SQ")
	private Long idTrackingAwb;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_LOCALIZACAO_CIA_AEREA", nullable = false)
    private LocalizacaoAwbCiaAerea localizacaoAwbCiaAerea;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_AWB", nullable = false)
    private Awb awb;
	
	@Columns(columns = { @Column(name = "DH_EVENTO", nullable = true), @Column(name = "DH_EVENTO_TZR", nullable = true) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhEvento;

	public Long getIdTrackingAwb() {
		return idTrackingAwb;
	}

	public void setIdTrackingAwb(Long idTrackingAwb) {
		this.idTrackingAwb = idTrackingAwb;
	}

	public LocalizacaoAwbCiaAerea getLocalizacaoAwbCiaAerea() {
		return localizacaoAwbCiaAerea;
	}

	public void setLocalizacaoAwbCiaAerea(LocalizacaoAwbCiaAerea localizacaoAwbCiaAerea) {
		this.localizacaoAwbCiaAerea = localizacaoAwbCiaAerea;
	}

	public Awb getAwb() {
		return awb;
	}

	public void setAwb(Awb awb) {
		this.awb = awb;
	}

	public DateTime getDhEvento() {
		return dhEvento;
	}

	public void setDhEvento(DateTime dhEvento) {
		this.dhEvento = dhEvento;
	}

	
}
