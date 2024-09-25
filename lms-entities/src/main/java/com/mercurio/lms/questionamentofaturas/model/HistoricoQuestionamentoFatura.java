package com.mercurio.lms.questionamentofaturas.model;

import java.io.Serializable;

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

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;

@Entity
@Table(name = "HISTS_QUESTIONAMENTOS_FATURAS")
@SequenceGenerator(name = "HISTS_QUESTIONAMENTOS_FATURAS_SEQ", sequenceName = "SQ_HISTS_QUESTS_FATURAS")
public class HistoricoQuestionamentoFatura implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long idHistoricoQuestionamentoFatura;
	private QuestionamentoFatura questionamentoFatura;
	private UsuarioLMS usuario;
	private DateTime dhHistorico;
	private DomainValue tpHistorico;
	private String obHistorico;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HISTS_QUESTIONAMENTOS_FATURAS_SEQ")
	@Column(name = "ID_HISTS_QUESTS_FATURAS", nullable = false)
	public Long getIdHistoricoQuestionamentoFatura() {
		return this.idHistoricoQuestionamentoFatura;
	}

	public void setIdHistoricoQuestionamentoFatura(
			Long idHistoricoQuestionamentoFatura) {
		this.idHistoricoQuestionamentoFatura = idHistoricoQuestionamentoFatura;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_QUESTIONAMENTOS_FATURAS", nullable = false)
	public QuestionamentoFatura getQuestionamentoFatura() {
		return questionamentoFatura;
	}

	public void setQuestionamentoFatura(
			QuestionamentoFatura questionamentoFatura) {
		this.questionamentoFatura = questionamentoFatura;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO", nullable = false)
	public UsuarioLMS getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}

	@Columns(columns = { @Column(name = "DH_HISTORICO", nullable = false),
			@Column(name = "DH_HISTORICO_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	public DateTime getDhHistorico() {
		return dhHistorico;
	}

	public void setDhHistorico(DateTime dhHistorico) {
		this.dhHistorico = dhHistorico;
	}

	@Column(name = "TP_HISTORICO", length = 3, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_HISTORICO") })
	public DomainValue getTpHistorico() {
		return tpHistorico;
	}

	public void setTpHistorico(DomainValue tpHistorico) {
		this.tpHistorico = tpHistorico;
	}

	@Column(name = "OB_HISTORICO", length = 200)
	public String getObHistorico() {
		return obHistorico;
	}

	public void setObHistorico(String obHistorico) {
		this.obHistorico = obHistorico;
	}
}