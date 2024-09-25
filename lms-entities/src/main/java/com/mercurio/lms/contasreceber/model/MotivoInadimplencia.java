package com.mercurio.lms.contasreceber.model;

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

/**
 * The persistent class for the MOTIVO_INADIMPLENCIA database table.
 * 
 */
@Entity
@Table(name="MOTIVO_INADIMPLENCIA")
public class MotivoInadimplencia  implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_MOTIVO_INADIMPLENCIA")
    @SequenceGenerator(name = "MOTIVO_INADIMPLENCIA_SEQ", sequenceName = "MOTIVO_INADIMPLENCIA_SQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MOTIVO_INADIMPLENCIA_SEQ")
	private Long idMotivoInadimplencia;
	
	@Column(name = "TP_SITUACAO", length = 1, nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
		  parameters = { @Parameter(name = "domainName", value = "DM_STATUS") })
	private DomainValue tpSituacao;
	
	@Column(name = "DS_MOTIVO_INADIMPLENCIA", nullable = false)
	private String descricao;

	public Long getIdMotivoInadimplencia() {
		return idMotivoInadimplencia;
	}
	public void setIdMotivoInadimplencia(Long idMotivoInadimplencia) {
		this.idMotivoInadimplencia = idMotivoInadimplencia;
	}
	public DomainValue getTpSituacao() {
		return tpSituacao;
	}
	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}