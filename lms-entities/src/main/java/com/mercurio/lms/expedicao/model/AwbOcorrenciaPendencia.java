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

import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;

@Entity
@Table(name="AWB_OCORRENCIA_PENDENCIA")
@SequenceGenerator(name="AWB_OCORRENCIA_PENDENCIA_SEQ", sequenceName="AWB_OCORRENCIA_PENDENCIA_SQ", allocationSize=1)
public class AwbOcorrenciaPendencia implements Serializable{
	private static final long serialVersionUID = 1L;   	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AWB_OCORRENCIA_PENDENCIA_SEQ")
	@Column(name = "ID_AWB_OCORRENCIA_PENDENCIA", nullable = false)
	private Long idAwbOcorrenciaPendencia;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_AWB", nullable = false)
	private Awb awb;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_OCORRENCIA", nullable = false)
	private OcorrenciaPendencia ocorrenciaPendencia;
	
	@Columns(columns = { @Column(name = "DH_OCORRENCIA", nullable = false),
			@Column(name = "DH_OCORRENCIA_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")	
	private DateTime dhOcorrencia;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO", nullable = true)
	private UsuarioLMS usuarioLms;

	public Long getIdAwbOcorrenciaPendencia() {
		return idAwbOcorrenciaPendencia;
	}

	public void setIdAwbOcorrenciaPendencia(Long idAwbOcorrenciaPendencia) {
		this.idAwbOcorrenciaPendencia = idAwbOcorrenciaPendencia;
	}

	public Awb getAwb() {
		return awb;
	}

	public void setAwb(Awb awb) {
		this.awb = awb;
	}

	public OcorrenciaPendencia getOcorrenciaPendencia() {
		return ocorrenciaPendencia;
	}

	public void setOcorrenciaPendencia(OcorrenciaPendencia ocorrenciaPendencia) {
		this.ocorrenciaPendencia = ocorrenciaPendencia;
	}

	public DateTime getDhOcorrencia() {
		return dhOcorrencia;
	}

	public void setDhOcorrencia(DateTime dhOcorrencia) {
		this.dhOcorrencia = dhOcorrencia;
	}

	public UsuarioLMS getUsuarioLms() {
		return usuarioLms;
	}

	public void setUsuarioLms(UsuarioLMS usuarioLms) {
		this.usuarioLms = usuarioLms;
	}
	
}
