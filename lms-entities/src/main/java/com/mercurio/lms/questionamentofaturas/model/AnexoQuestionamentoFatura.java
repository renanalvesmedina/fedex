package com.mercurio.lms.questionamentofaturas.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.lms.configuracoes.model.UsuarioLMS;

@Entity
@Table(name = "ANEXOS_QUESTIONAMENTOS_FATURAS")
@SequenceGenerator(name = "ANEXOS_QUESTIONAMENTOS_FATURAS_SEQ", sequenceName = "SQ_ANEXOS_QUESTS_FATURAS")
public class AnexoQuestionamentoFatura implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long idAnexoQuestionamentoFatura;
	private QuestionamentoFatura questionamentoFatura;
	private UsuarioLMS usuario;
	private String dsAnexo;
	private DateTime dhCriacao;
	private byte[] dcArquivo;
	private transient String dcArquivoTemp;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ANEXOS_QUESTIONAMENTOS_FATURAS_SEQ")
	@Column(name = "ID_ANEXOS_QUESTS_FATURAS", nullable = false)
	public Long getIdAnexoQuestionamentoFatura() {
		return this.idAnexoQuestionamentoFatura;
	}

	public void setIdAnexoQuestionamentoFatura(Long idAnexoQuestionamentoFatura) {
		this.idAnexoQuestionamentoFatura = idAnexoQuestionamentoFatura;
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

	@Column(name = "DS_ANEXO", length = 60, nullable = false)
	public String getDsAnexo() {
		return dsAnexo;
	}

	public void setDsAnexo(String dsAnexo) {
		this.dsAnexo = dsAnexo;
	}

	@Columns(columns = { @Column(name = "DH_CRIACAO", nullable = false),
			@Column(name = "DH_CRIACAO_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	public DateTime getDhCriacao() {
		return dhCriacao;
	}

	public void setDhCriacao(DateTime dhCriacao) {
		this.dhCriacao = dhCriacao;
	}

	@Lob
	@Column(name="DC_ARQUIVO", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.BinaryBlobUserType")
	public byte[] getDcArquivo() {
		return dcArquivo;
	}

	public void setDcArquivo(byte[] dcArquivo) {
		this.dcArquivo = dcArquivo;
	}

	@Transient
	public String getDcArquivoTemp() {
		return dcArquivoTemp;
	}

	public void setDcArquivoTemp(String dcArquivoTemp) {
		this.dcArquivoTemp = dcArquivoTemp;
	}
}