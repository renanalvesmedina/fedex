package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.mercurio.adsm.framework.model.DomainValue;

/**
 * Entidade para atender a demanda LMS-2788
 * 
 * @author pfernandes@voiza.com.br
 * 
 */
@Entity
@Table(name = "REJEITO_MPC")
@SequenceGenerator(name = "REJEITO_MPC_SQ", sequenceName = "REJEITO_MPC_SQ")
public class RejeitoMpc implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REJEITO_MPC_SQ")
	@Column(name = "ID_REJEITO_MPC", nullable = false)
	private Long idRejeitoMPC;
	
	@Column(name = "DS_REJEITO_MPC", nullable = true)
	private String dsRejeitoMPC;
	
	@Column(name = "ST_REJEITO", nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
	parameters = { @Parameter(name = "domainName", value = "DM_STATUS_REJEITO_LIBERACAO") })
	private DomainValue stRejeito;
	
	@Column(name = "TP_ABRANGENCIA", nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
	parameters = { @Parameter(name = "domainName", value = "DM_TIPO_ABRANGENCIA") })
	private DomainValue tpAbrangencia;
	
	@Column(name = "TP_AUTORIZACAO", nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
	parameters = { @Parameter(name = "domainName", value = "DM_TIPO_AUTORIZACAO_REJEITO_LIBERACAO") })
	private DomainValue tpAutorizacao;
	
	@Column(name = "TP_REJEITO", nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
	parameters = { @Parameter(name = "domainName", value = "DM_TIPO_REJEITO_LIBERACAO") })
	private DomainValue tpRejeito;
	
	@Columns(columns = { @Column(name = "DH_CRIACAO", nullable = true),
			@Column(name = "DH_CRIACAO_TZR", nullable = true) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhCriacao;

	public Long getIdRejeitoMPC() {
		return idRejeitoMPC;
	}

	public void setIdRejeitoMPC(Long idRejeitoMPC) {
		this.idRejeitoMPC = idRejeitoMPC;
	}

	public String getDsRejeitoMPC() {
		return dsRejeitoMPC;
	}

	public void setDsRejeitoMPC(String dsRejeitoMPC) {
		this.dsRejeitoMPC = dsRejeitoMPC;
	}

	public DomainValue getStRejeito() {
		return stRejeito;
	}

	public void setStRejeito(DomainValue stRejeito) {
		this.stRejeito = stRejeito;
	}

	public DomainValue getTpAbrangencia() {
		return tpAbrangencia;
	}

	public void setTpAbrangencia(DomainValue tpAbrangencia) {
		this.tpAbrangencia = tpAbrangencia;
	}

	public DomainValue getTpAutorizacao() {
		return tpAutorizacao;
	}

	public void setTpAutorizacao(DomainValue tpAutorizacao) {
		this.tpAutorizacao = tpAutorizacao;
	}

	public DomainValue getTpRejeito() {
		return tpRejeito;
	}

	public void setTpRejeito(DomainValue tpRejeito) {
		this.tpRejeito = tpRejeito;
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
