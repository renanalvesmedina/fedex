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
 * LMS-6885 - Entidade para ter o historico de Solicitação da Escolta.
 * 
 * @author romulo.panassolo@cwi.com.br (Rômulo da Silva Panassolo)
 *
 */
@Entity
@Table(name = "SOLIC_ESCOLTA_HISTORICO")
@SequenceGenerator(name = "SOLIC_ESCOLTA_HISTORICO_SQ", sequenceName = "SOLIC_ESCOLTA_HISTORICO_SQ")
public class SolicEscoltaHistorico implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_SOLIC_ESCOLTA_HISTORICO", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SOLIC_ESCOLTA_HISTORICO_SQ")
	private Long idSolicEscoltaHistorico;

	@ManyToOne
	@JoinColumn(name = "ID_SOLICITACAO_ESCOLTA", nullable = false)
	private SolicitacaoEscolta solicitacaoEscolta;

	@Column(name = "TP_SITUACAO_SOLIC_ESCOLTA", nullable = false)
	@Type(
			type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType",
			parameters = { @Parameter(name = "domainName", value = "DM_SITUACAO_ESCOLTA_HISTORICO") }
	)
	private DomainValue tpSituacao;

	@Column(name = "DS_JUSTIFICATIVA", length = 400)
	private String dsJustificativa;

	@Columns(columns = {
			@Column(name = "DH_INCLUSAO", nullable = false),
			@Column(name = "DH_INCLUSAO_TZR ", nullable = false)
	})
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhInclusao;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO", nullable = false)
	private UsuarioLMS usuario;

	public Long getIdHistoricoSolicEscolta() {
		return idSolicEscoltaHistorico;
	}

	public void setIdSolicEscoltaHistorico(Long idSolicEscoltaHistorico) {
		this.idSolicEscoltaHistorico = idSolicEscoltaHistorico;
	}

	public SolicitacaoEscolta getSolicitacaoEscolta() {
		return solicitacaoEscolta;
	}

	public void setSolicitacaoEscolta(SolicitacaoEscolta solicitacaoEscolta) {
		this.solicitacaoEscolta = solicitacaoEscolta;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
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

	public UsuarioLMS getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append(idSolicEscoltaHistorico)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof SolicEscoltaHistorico)) {
			return false;
		}
		SolicEscoltaHistorico cast = (SolicEscoltaHistorico) other;
		return new EqualsBuilder()
				.append(idSolicEscoltaHistorico, cast.idSolicEscoltaHistorico)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idSolicEscoltaHistorico)
				.toHashCode();
	}

}
