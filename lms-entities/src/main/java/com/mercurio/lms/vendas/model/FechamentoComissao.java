package com.mercurio.lms.vendas.model;

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

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.workflow.model.Pendencia;

@Entity
@Table(name = "FECHAMENTO_COMISSAO")
@SequenceGenerator(name = "FECHAMENTO_COMISSAO_SEQ", sequenceName = "FECHAMENTO_COMISSAO_SQ", allocationSize=1)
public class FechamentoComissao implements Serializable {

	private static final long serialVersionUID = 7789039252003537294L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FECHAMENTO_COMISSAO_SEQ")
	@Column(name = "ID_FECHAMENTO_COMISSAO", nullable = false)
	private Long idFechamentoComissao;
	
	@Column(name = "TP_FECHAMENTO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_FECHAMENTO") })
	private DomainValue tpFechamento;
	
	@Columns(columns = { @Column(name = "DH_ALTERACAO", nullable = true), @Column(name = "DH_ALTERACAO_TZR", nullable = true) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhAlteracao;
	
	@Columns(columns = { @Column(name = "DH_INCLUSAO", nullable = true), @Column(name = "DH_INCLUSAO_TZR", nullable = true) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhInclusao;
	
	@ManyToOne
	@JoinColumn(name = "ID_USUARIO_INCLUSAO", nullable = false)
	private UsuarioLMS usuarioInclusao;
	
	@ManyToOne
	@JoinColumn(name = "ID_USUARIO_ALTERACAO", nullable = false)
	private UsuarioLMS usuarioAlteracao;
	
	@Column(name = "BL_RETORNO", nullable = true)
	private String blRetorno;

	@ManyToOne
	@JoinColumn(name = "ID_PENDENCIA_APROVACAO")
	private Pendencia pendenciaAprovacao;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO_APROVACAO")
	private UsuarioLMS usuarioAprovacao;

	@Column(name = "TP_SITUACAO_APROVACAO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_STATUS_WORKFLOW") })
	private DomainValue tpSituacaoAprovacao;
	
	public Long getIdFechamentoComissao() {
		return idFechamentoComissao;
	}

	public void setIdFechamentoComissao(Long idFechamentoComissao) {
		this.idFechamentoComissao = idFechamentoComissao;
	}

	public DomainValue getTpFechamento() {
		return tpFechamento;
	}

	public void setTpFechamento(DomainValue tpFechamento) {
		this.tpFechamento = tpFechamento;
	}

	public DateTime getDhAlteracao() {
		return dhAlteracao;
	}

	public void setDhAlteracao(DateTime dhAlteracao) {
		this.dhAlteracao = dhAlteracao;
	}

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public UsuarioLMS getUsuarioInclusao() {
		return usuarioInclusao;
	}

	public void setUsuarioInclusao(UsuarioLMS usuarioInclusao) {
		this.usuarioInclusao = usuarioInclusao;
	}

	public UsuarioLMS getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(UsuarioLMS usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public String getBlRetorno() {
		return blRetorno;
	}

	public void setBlRetorno(String blRetorno) {
		this.blRetorno = blRetorno;
	}

	public Pendencia getPendenciaAprovacao() {
		return pendenciaAprovacao;
	}

	public void setPendenciaAprovacao(Pendencia pendenciaAprovacao) {
		this.pendenciaAprovacao = pendenciaAprovacao;
	}

	public UsuarioLMS getUsuarioAprovacao() {
		return usuarioAprovacao;
	}

	public void setUsuarioAprovacao(UsuarioLMS usuarioAprovacao) {
		this.usuarioAprovacao = usuarioAprovacao;
	}

	public DomainValue getTpSituacaoAprovacao() {
		return tpSituacaoAprovacao;
	}

	public void setTpSituacaoAprovacao(DomainValue tpSituacaoAprovacao) {
		this.tpSituacaoAprovacao = tpSituacaoAprovacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idFechamentoComissao == null) ? 0 : idFechamentoComissao
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FechamentoComissao other = (FechamentoComissao) obj;
		if (idFechamentoComissao == null) {
			if (other.idFechamentoComissao != null)
				return false;
		} else if (!idFechamentoComissao.equals(other.idFechamentoComissao))
			return false;
		return true;
	}
	
	
	
	
	
}
