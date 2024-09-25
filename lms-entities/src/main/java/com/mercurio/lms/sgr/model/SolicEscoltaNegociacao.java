package com.mercurio.lms.sgr.model;

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
import com.mercurio.lms.configuracoes.model.UsuarioLMS;

/**
 * LMS-6885 - Entidade para gravar o historico da Negociação Escolta.
 * 
 * @author romulo.panassolo@cwi.com.br (Rômulo da Silva Panassolo)
 *
 */
@Entity
@Table(name = "SOLIC_ESCOLTA_NEGOCIACAO")
@SequenceGenerator(name = "SOLIC_ESCOLTA_NEGOCIACAO_SQ", sequenceName = "SOLIC_ESCOLTA_NEGOCIACAO_SQ")
public class SolicEscoltaNegociacao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_SOLIC_ESCOLTA_NEGOCIACAO", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SOLIC_ESCOLTA_NEGOCIACAO_SQ")
	private Long idSolicEscoltaNegociacao;

	@ManyToOne
	@JoinColumn(name = "ID_SOLICITACAO_ESCOLTA", nullable = false)
	private SolicitacaoEscolta solicitacaoEscolta;

	@ManyToOne
	@JoinColumn(name = "ID_FRANQUIA_FORNECEDOR_ESCOLTA", nullable = false)
	private FranquiaFornecedorEscolta franquiaFornecedorEscolta;

	@Column(name = "NR_ORDEM_RANKING", nullable = false)
	private Long nrOrdemRanking;

	@Column(name = "TP_SITUACAO_NEGOCIACAO", nullable = false)
	@Type(
			type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType",
			parameters = { @Parameter(name = "domainName", value = "DM_SITUACAO_ESCOLTA_NEGOCIACAO") }
	)
	private DomainValue tpSituacao;

	@Column(name = "TP_JUSTIFICATIVA")
	@Type(
			type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType",
			parameters = { @Parameter(name = "domainName", value = "DM_JUSTIFICATIVA_ESCOLTA_NEGOCIACAO") }
	)
	private DomainValue tpJustificativa;

	@Column(name = "DS_JUSTIFICATIVA", length = 400)
	private String dsJustificativa;

	@Columns(columns = {
			@Column(name = "DH_INCLUSAO", nullable = false),
			@Column(name = "DH_INCLUSAO_TZR ", nullable = false)
	})
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhInclusao;

	@Column(name = "NM_CONTATO_FORNECEDOR", length = 100, nullable = false)
	private String nmContatoFornecedor;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO", nullable = false)
	private UsuarioLMS usuario;

	public Long getIdSolicEscoltaNegociacao() {
		return idSolicEscoltaNegociacao;
	}

	public void setIdSolicEscoltaNegociacao(Long idSolicEscoltaNegociacao) {
		this.idSolicEscoltaNegociacao = idSolicEscoltaNegociacao;
	}

	public SolicitacaoEscolta getSolicitacaoEscolta() {
		return solicitacaoEscolta;
	}

	public void setSolicitacaoEscolta(SolicitacaoEscolta solicitacaoEscolta) {
		this.solicitacaoEscolta = solicitacaoEscolta;
	}

	public FranquiaFornecedorEscolta getFranquiaFornecedorEscolta() {
		return franquiaFornecedorEscolta;
	}

	public void setFranquiaFornecedorEscolta(FranquiaFornecedorEscolta franquiaFornecedorEscolta) {
		this.franquiaFornecedorEscolta = franquiaFornecedorEscolta;
	}

	public Long getNrOrdemRanking() {
		return nrOrdemRanking;
	}

	public void setNrOrdemRanking(Long nrOrdemRanking) {
		this.nrOrdemRanking = nrOrdemRanking;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public DomainValue getTpJustificativa() {
		return tpJustificativa;
	}

	public void setTpJustificativa(DomainValue tpJustificativa) {
		this.tpJustificativa = tpJustificativa;
	}

	public String getDsJustificativa() {
		return dsJustificativa;
	}

	public void setDsJustificativa(String dsJustificativa) {
		this.dsJustificativa = dsJustificativa;
	}

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public String getNmContatoFornecedor() {
		return nmContatoFornecedor;
	}

	public void setNmContatoFornecedor(String nmContatoFornecedor) {
		this.nmContatoFornecedor = nmContatoFornecedor;
	}

	public UsuarioLMS getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append(idSolicEscoltaNegociacao)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof SolicEscoltaNegociacao)) {
			return false;
		}
		SolicEscoltaNegociacao cast = (SolicEscoltaNegociacao) other;
		return new EqualsBuilder()
				.append(idSolicEscoltaNegociacao, cast.idSolicEscoltaNegociacao)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idSolicEscoltaNegociacao)
				.toHashCode();
	}

}
