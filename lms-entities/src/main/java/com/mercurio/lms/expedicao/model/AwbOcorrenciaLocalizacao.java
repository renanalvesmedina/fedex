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
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

@Entity
@Table(name = "AWB_OCORRENCIA")
@SequenceGenerator(name = "AWOC_SEQ", sequenceName = "AWOC_SEQ", allocationSize = 1)
public class AwbOcorrenciaLocalizacao implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_AWB_OCORRENCIA", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AWOC_SEQ")
	private Long idAwbOcorrencia;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_AWB", nullable = false)
	private Awb awb;
	
	@Column(name = "TP_LOCALIZACAO", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_LOCALIZACAO_AWB") })
	private DomainValue tpLocalizacao;
	
	@Columns(columns = { @Column(name = "DH_OCORRENCIA"), @Column(name = "DH_OCORRENCIA_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhOcorrencia;

	public AwbOcorrenciaLocalizacao() {
		super();
	}

	public Long getIdAwbOcorrencia() {
		return idAwbOcorrencia;
	}

	public void setIdAwbOcorrencia(Long idAwbOcorrencia) {
		this.idAwbOcorrencia = idAwbOcorrencia;
	}

	public Awb getAwb() {
		return awb;
	}

	public void setAwb(Awb awb) {
		this.awb = awb;
	}

	public DomainValue getTpLocalizacao() {
		return tpLocalizacao;
	}

	public void setTpLocalizacao(DomainValue tpLocalizacao) {
		this.tpLocalizacao = tpLocalizacao;
	}

	public DateTime getDhOcorrencia() {
		return dhOcorrencia;
	}

	public void setDhOcorrencia(DateTime dhOcorrencia) {
		this.dhOcorrencia = dhOcorrencia;
	}

}
