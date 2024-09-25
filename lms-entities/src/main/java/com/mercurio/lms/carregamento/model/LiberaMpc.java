package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

/**
 * Entidade para atender a demanda LMS-2791
 * 
 * @author pfernandes@voiza.com.br
 * 
 */
@Entity
@Table(name = "LIBERA_MPC")
@SequenceGenerator(name = "LIBERA_MPC_SQ", sequenceName = "LIBERA_MPC_SQ")
public class LiberaMpc implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LIBERA_MPC_SQ")
	@Column(name = "ID_LIBERA_MPC", nullable = false)
	private Long idLiberaMPC;

	@Column(name = "DS_LIBERA_MPC", nullable = true)
	private String dsLiberaMPC;

	@Column(name = "ST_LIBERA", nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_STATUS_REJEITO_LIBERACAO") })
	private DomainValue stLibera;

	@Column(name = "TP_LIBERA", nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_REJEITO_LIBERACAO") })
	private DomainValue tpLibera;

	@Column(name = "TP_AUTORIZACAO", nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_AUTORIZACAO_REJEITO_LIBERACAO") })
	private DomainValue tpAutorizacao;

	@Columns(columns = { @Column(name = "DH_CRIACAO", nullable = true), @Column(name = "DH_CRIACAO_TZR", nullable = true) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhCriacao;

	public Long getIdLiberaMPC() {
		return idLiberaMPC;
	}

	public void setIdLiberaMPC(Long idLiberaMPC) {
		this.idLiberaMPC = idLiberaMPC;
	}

	public String getDsLiberaMPC() {
		return dsLiberaMPC;
	}

	public void setDsLiberaMPC(String dsLiberaMPC) {
		this.dsLiberaMPC = dsLiberaMPC;
	}

	public DomainValue getStLibera() {
		return stLibera;
	}

	public void setStLibera(DomainValue stLibera) {
		this.stLibera = stLibera;
	}

	public DomainValue getTpLibera() {
		return tpLibera;
	}

	public void setTpLibera(DomainValue tpLibera) {
		this.tpLibera = tpLibera;
	}

	public DomainValue getTpAutorizacao() {
		return tpAutorizacao;
	}

	public void setTpAutorizacao(DomainValue tpAutorizacao) {
		this.tpAutorizacao = tpAutorizacao;
	}

	public DateTime getDhCriacao() {
		return dhCriacao;
	}

	public void setDhCriacao(DateTime dhCriacao) {
		this.dhCriacao = dhCriacao;
	}

	public DateTime getDhHistoricoMPC() {
		return dhCriacao;
	}

	public void setDhHistoricoMPC(DateTime dhCriacao) {
		this.dhCriacao = dhCriacao;
	}

}
