package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

@Entity
@Table(name="CONTROLE_VALIDACAO")
@SequenceGenerator(name = "CONTROLE_VALIDACAO_SQ", sequenceName = "CONTROLE_VALIDACAO_SQ")
public class ControleValidacao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONTROLE_VALIDACAO_SQ")
	@Column(name = "ID_CONTROLE_VALIDACAO", nullable = false)
	private Long idControleValidacao;
	
	@ManyToOne
	@JoinColumn(name = "ID_PESSOA", nullable = false)
	private Pessoa pessoa;
	
	@Column(name = "TP_ORGAO", length = 1, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_ORGAO_CONTROLE_VALIDACAO") })
	private DomainValue tpOrgao;
	
	@Columns(columns = { @Column(name = "DH_ENVIO_CONSULTA"), @Column(name = "DH_ENVIO_CONSULTA_TZR ") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhEnvioConsulta;
	
	@Columns(columns = { @Column(name = "DH_CONSULTA"), @Column(name = "DH_CONSULTA_TZR ") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhConsulta;

	public Long getIdControleValidacao() {
		return idControleValidacao;
	}

	public void setIdControleValidacao(Long idControleValidacao) {
		this.idControleValidacao = idControleValidacao;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public DomainValue getTpOrgao() {
		return tpOrgao;
	}

	public void setTpOrgao(DomainValue tpOrgao) {
		this.tpOrgao = tpOrgao;
	}

	public DateTime getDhEnvioConsulta() {
		return dhEnvioConsulta;
	}

	public void setDhEnvioConsulta(DateTime dhEnvioConsulta) {
		this.dhEnvioConsulta = dhEnvioConsulta;
	}

	public DateTime getDhConsulta() {
		return dhConsulta;
	}

	public void setDhConsulta(DateTime dhConsulta) {
		this.dhConsulta = dhConsulta;
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("idControleValidacao",
				getIdControleValidacao()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ControleValidacao))
			return false;
		ControleValidacao castOther = (ControleValidacao) other;
		return new EqualsBuilder().append(this.getIdControleValidacao(),
				castOther.getIdControleValidacao()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdControleValidacao()).toHashCode();
	}
	
}
