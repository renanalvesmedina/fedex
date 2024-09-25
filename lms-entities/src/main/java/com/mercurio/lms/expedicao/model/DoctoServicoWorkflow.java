package com.mercurio.lms.expedicao.model;

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

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.workflow.model.Pendencia;

@Entity
@Table(name = "DOCTO_SERVICO_WORKFLOW")
@SequenceGenerator(name = "DOCTO_SERVICO_WORKFLOW_SEQ", sequenceName = "DOCTO_SERVICO_WORKFLOW_SQ", allocationSize = 1)
public class DoctoServicoWorkflow implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_DOCTO_SERVICO_WORKFLOW", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCTO_SERVICO_WORKFLOW_SEQ")
	private Long idDoctoServicoWorkflow;

	@ManyToOne
	@JoinColumn(name = "ID_DOCTO_SERVICO", nullable = false)
	private DoctoServico doctoServico;

	@Column(name = "TP_SITUACAO_APROVACAO", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_STATUS_WORKFLOW") })
	private DomainValue tpSituacaoAprovacao;

	@Column(name = "DT_APROVACAO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtAprovacao;

	@ManyToOne
	@JoinColumn(name = "ID_PENDENCIA")
	private Pendencia pendencia;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO")
	private UsuarioLMS usuario;

	public Long getIdDoctoServicoWorkflow() {
		return idDoctoServicoWorkflow;
	}

	public void setIdDoctoServicoWorkflow(Long idDoctoServicoWorkflow) {
		this.idDoctoServicoWorkflow = idDoctoServicoWorkflow;
	}

	public DoctoServico getDoctoServico() {
		return doctoServico;
	}

	public void setDoctoServico(DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}

	public DomainValue getTpSituacaoAprovacao() {
		return tpSituacaoAprovacao;
	}

	public void setTpSituacaoAprovacao(DomainValue tpSituacaoAprovacao) {
		this.tpSituacaoAprovacao = tpSituacaoAprovacao;
	}

	public YearMonthDay getDtAprovacao() {
		return dtAprovacao;
	}

	public void setDtAprovacao(YearMonthDay dtAprovacao) {
		this.dtAprovacao = dtAprovacao;
	}

	public Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(Pendencia pendencia) {
		this.pendencia = pendencia;
	}

	public UsuarioLMS getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}
}
