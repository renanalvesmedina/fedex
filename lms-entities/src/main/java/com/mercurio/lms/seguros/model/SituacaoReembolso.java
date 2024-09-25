package com.mercurio.lms.seguros.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.mercurio.adsm.framework.model.DomainValue;

@Entity
@Table(name = "SITUACAO_REEMBOLSO")
@SequenceGenerator(name = "SITUACAO_REEMBOLSO_SQ", sequenceName = "SITUACAO_REEMBOLSO_SQ")
public class SituacaoReembolso implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long idSituacaoReembolso;
	private String dsSituacaoReembolso;
	private DomainValue blStatus;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SITUACAO_REEMBOLSO_SQ")
	@Column(name = "ID_SITUACAO_REEMBOLSO", nullable = false)
	public Long getIdSituacaoReembolso() {
		return idSituacaoReembolso;
	}

	public void setIdSituacaoReembolso(Long idSituacaoReembolso) {
		this.idSituacaoReembolso = idSituacaoReembolso;
	}

	@Column(name = "DS_SITUACAO_REEMBOLSO", length = 20, nullable = false)
	public String getDsSituacaoReembolso() {
		return dsSituacaoReembolso;
	}

	public void setDsSituacaoReembolso(String dsSituacaoReembolso) {
		this.dsSituacaoReembolso = dsSituacaoReembolso;
	}

	@Column(name = "BL_STATUS", length = 1, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
		  parameters = { @Parameter(name = "domainName", value = "DM_STATUS") })
	public DomainValue getBlStatus() {
		return blStatus;
	}

	public void setBlStatus(DomainValue blStatus) {
		this.blStatus = blStatus;
	}

}
