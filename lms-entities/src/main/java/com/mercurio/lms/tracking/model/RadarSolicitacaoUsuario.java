package com.mercurio.lms.tracking.model;

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
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.pojo.UsuarioADSM;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.municipios.model.Municipio;

@Entity
@Table(name = "RAD_SOLICITACAO_USUARIO")
@SequenceGenerator(name = "RAD_SOLICITACAO_USUARIO_SEQ", sequenceName = "RAD_SOLICITACAO_USUARIO_SQ", allocationSize = 1)
public class RadarSolicitacaoUsuario implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_RAD_SOLICITACAO_USUARIO", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RAD_SOLICITACAO_USUARIO_SEQ")
	private Long idRadarSolicitacaoUsuario;

	@Column(name = "NM_EMPRESA", length = 60, nullable = false)
	private String nmEmpresa;

	@Column(name = "NM_CONTATO", length = 60, nullable = false)
	private String nmContato;
	
	@Column(name = "DS_EMAIL_CONTATO", length = 60, nullable = false)
	private String dsEmailContato;

	@Column(name = "NR_DDD", length = 5, nullable = false)
	private String nrDdd;

	@Column(name = "NR_TELEFONE", length = 10, nullable = false)
	private String nrTelefone;

	@Column(name = "TP_CONTATO", length = 2, nullable = false) // FIXME CLI-68, corrigir com o nome correto do domínio
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TP_CONTATO_RAD_SOLICITACAO_USUARIO") })
	private DomainValue tpContato;

	@Column(name = "TP_ORIGEM", length = 2, nullable = false) // FIXME CLI-68, corrigir com o nome correto do domínio
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TP_ORIGEM_RAD_SOLICITACAO_USUARIO") })
	private DomainValue tpOrigem;

	@Column(name = "TP_STATUS", length = 2, nullable = false) // FIXME CLI-68, corrigir com o nome correto do domínio
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TP_STATUS_RAD_SOLICITACAO_USUARIO") })
	private DomainValue tpStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_MUNICIPIO", nullable = false)
	private Municipio municipio;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SERVICO")
	private Servico servico;

	@Column(name = "DH_GRAVACAO", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType")
	private DateTime dhGravacao;

	@Column(name = "DH_ALTERACAO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType")
	private DateTime dhAlteracao;

	@Column(name = "NM_EXECUTIVO_VENDAS", length = 60)
	private String nmExecutivoVendas;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO")
	private UsuarioADSM usuarioAdsm;

	public String toString() {
		return new ToStringBuilder(this).append("idRadarSolicitacaoUsuario", getIdRadarSolicitacaoUsuario()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RadarSolicitacaoUsuario))
			return false;
		RadarSolicitacaoUsuario castOther = (RadarSolicitacaoUsuario) other;
		return new EqualsBuilder().append(this.getIdRadarSolicitacaoUsuario(), castOther.getIdRadarSolicitacaoUsuario()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdRadarSolicitacaoUsuario()).toHashCode();
	}

	public Long getIdRadarSolicitacaoUsuario() {
		return idRadarSolicitacaoUsuario;
	}

	public void setIdRadarSolicitacaoUsuario(Long idRadarSolicitacaoUsuario) {
		this.idRadarSolicitacaoUsuario = idRadarSolicitacaoUsuario;
	}

	public String getNmEmpresa() {
		return nmEmpresa;
	}

	public void setNmEmpresa(String nmEmpresa) {
		this.nmEmpresa = nmEmpresa;
	}

	public String getDsEmailContato() {
		return dsEmailContato;
	}

	public void setDsEmailContato(String dsEmailContato) {
		this.dsEmailContato = dsEmailContato;
	}

	public String getNrDdd() {
		return nrDdd;
	}

	public void setNrDdd(String nrDdd) {
		this.nrDdd = nrDdd;
	}

	public String getNrTelefone() {
		return nrTelefone;
	}

	public void setNrTelefone(String nrTelefone) {
		this.nrTelefone = nrTelefone;
	}

	public DomainValue getTpContato() {
		return tpContato;
	}

	public void setTpContato(DomainValue tpContato) {
		this.tpContato = tpContato;
	}

	public DomainValue getTpOrigem() {
		return tpOrigem;
	}

	public void setTpOrigem(DomainValue tpOrigem) {
		this.tpOrigem = tpOrigem;
	}

	public DomainValue getTpStatus() {
		return tpStatus;
	}

	public void setTpStatus(DomainValue tpStatus) {
		this.tpStatus = tpStatus;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	public String getNmExecutivoVendas() {
		return nmExecutivoVendas;
	}

	public void setNmExecutivoVendas(String nmExecutivoVendas) {
		this.nmExecutivoVendas = nmExecutivoVendas;
	}

	public UsuarioADSM getUsuarioAdsm() {
		return usuarioAdsm;
	}

	public void setUsuarioAdsm(UsuarioADSM usuarioAdsm) {
		this.usuarioAdsm = usuarioAdsm;
	}

	public DateTime getDhGravacao() {
		return dhGravacao;
	}

	public void setDhGravacao(DateTime dhGravacao) {
		this.dhGravacao = dhGravacao;
	}

	public DateTime getDhAlteracao() {
		return dhAlteracao;
	}

	public void setDhAlteracao(DateTime dhAlteracao) {
		this.dhAlteracao = dhAlteracao;
	}

	public String getNmContato() {
		return nmContato;
	}

	public void setNmContato(String nmContato) {
		this.nmContato = nmContato;
	}

}
