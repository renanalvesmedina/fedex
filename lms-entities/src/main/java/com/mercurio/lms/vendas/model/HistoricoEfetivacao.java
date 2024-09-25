package com.mercurio.lms.vendas.model;

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
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.lms.configuracoes.model.UsuarioLMS;

@Entity
@Table(name = "HISTORICO_EFETIVACAO")
@SequenceGenerator(name = "HISTORICO_EFETIVACAO_SEQ", sequenceName = "HISTORICO_EFETIVACAO_SQ", allocationSize = 1)
public class HistoricoEfetivacao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_HISTORICO_EFETIVACAO", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HISTORICO_EFETIVACAO_SEQ")
	private Long idHistoricoEfetivacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SIMULACAO", nullable = false)
	private Simulacao simulacao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO_SOLICITACAO", nullable = false)
	private UsuarioLMS usuarioSolicitacao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO_REPROVADOR", nullable = true)
	private UsuarioLMS usuarioReprovador;

	@Columns(columns = { @Column(name = "DH_SOLICITACAO", nullable = false), @Column(name = "DH_SOLICITACAO_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhSolicitacao;

	@Columns(columns = { @Column(name = "DH_REPROVACAO"), @Column(name = "DH_REPROVACAO_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhReprovacao;

	@Column(name = "DS_MOTIVO")
	private String dsMotivo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ID_MOTIVO_REPROVACAO")
	private MotivoReprovacao motivoReprovacao;

	public Long getIdHistoricoEfetivacao() {
		return idHistoricoEfetivacao;
	}

	public void setIdHistoricoEfetivacao(Long idHistoricoEfetivacao) {
		this.idHistoricoEfetivacao = idHistoricoEfetivacao;
	}

	public Simulacao getSimulacao() {
		return simulacao;
	}

	public void setSimulacao(Simulacao simulacao) {
		this.simulacao = simulacao;
	}

	public UsuarioLMS getUsuarioSolicitacao() {
		return usuarioSolicitacao;
	}

	public void setUsuarioSolicitacao(UsuarioLMS usuarioSolicitacao) {
		this.usuarioSolicitacao = usuarioSolicitacao;
	}

	public UsuarioLMS getUsuarioReprovador() {
		return usuarioReprovador;
	}

	public void setUsuarioReprovador(UsuarioLMS usuarioReprovador) {
		this.usuarioReprovador = usuarioReprovador;
	}

	public DateTime getDhSolicitacao() {
		return dhSolicitacao;
	}

	public void setDhSolicitacao(DateTime dhSolicitacao) {
		this.dhSolicitacao = dhSolicitacao;
	}

	public DateTime getDhReprovacao() {
		return dhReprovacao;
	}

	public void setDhReprovacao(DateTime dhReprovacao) {
		this.dhReprovacao = dhReprovacao;
	}

	public String getDsMotivo() {
		return dsMotivo;
	}

	public void setDsMotivo(String dsMotivo) {
		this.dsMotivo = dsMotivo;
	}

	public MotivoReprovacao getMotivoReprovacao() {
		return motivoReprovacao;
	}

	public void setMotivoReprovacao(MotivoReprovacao motivoReprovacao) {
		this.motivoReprovacao = motivoReprovacao;
	}
}
