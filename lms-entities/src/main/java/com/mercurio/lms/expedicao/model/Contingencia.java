package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.municipios.model.Filial;


@Entity
@Table(name="CONTINGENCIA")
@SequenceGenerator(name="SQ_CONTINGENCIA", sequenceName="CONTINGENCIA_SQ", allocationSize=1)


public class Contingencia implements Serializable{


	private static final long serialVersionUID = 1L;
	
	private Long idContingencia;
	private Filial filial;
	private DateTime dhSolicitacao;
	private DateTime dhAprovacao;
	private DateTime dhFinalizacao;
	private int qtEmissoes;
	private String dsContingencia;
	private DomainValue tpContingencia;
	private DomainValue tpSituacao;
	private UsuarioLMS usuarioSolicitante;
	private UsuarioLMS usuarioAprovador;
	private UsuarioLMS usuarioFinalizador;
	private List<MonitoramentoDocEletronico> monitoramentoDocEletronico;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SQ_CONTINGENCIA")
	@Column(name="ID_CONTINGENCIA", nullable=false)
	public Long getIdContingencia() {
		return idContingencia;
	}
	public void setIdContingencia(Long idContingencia) {
		this.idContingencia = idContingencia;
	}
	
	@OneToOne
	@JoinColumn(name="ID_FILIAL", nullable=false)
	public Filial getFilial() {
		return filial;
	}
	public void setFilial(Filial filial) {
		this.filial = filial;
	}
	
	@Columns(columns = { @Column(name = "DH_SOLICITACAO", nullable = false),
			@Column(name = "DH_SOLICITACAO_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	public DateTime getDhSolicitacao() {
		return dhSolicitacao;
	}
	public void setDhSolicitacao(DateTime dhSolicitacao) {
		this.dhSolicitacao = dhSolicitacao;
	}
	
	@Columns(columns = { @Column(name = "DH_APROVACAO"),
			@Column(name = "DH_APROVACAO_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	public DateTime getDhAprovacao() {
		return dhAprovacao;
	}
	public void setDhAprovacao(DateTime dhAprovacao) {
		this.dhAprovacao = dhAprovacao;
	}
	
	@Columns(columns = { @Column(name = "DH_FINALIZACAO"),
			@Column(name = "DH_FINALIZACAO_TZR ") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	public DateTime getDhFinalizacao() {
		return dhFinalizacao;
	}
	public void setDhFinalizacao(DateTime dhFinalizacao) {
		this.dhFinalizacao = dhFinalizacao;
	}
	
	@Column(name="QT_EMISSOES", nullable=false, length=10)
	public int getQtEmissoes() {
		return qtEmissoes;
	}
	public void setQtEmissoes(int qtEmissoes) {
		this.qtEmissoes = qtEmissoes;
	}
	
	@Column(name = "TP_SITUACAO", length = 1, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_SITUACAO_CONTINGENCIA") })
	public DomainValue getTpSituacao() {
		return tpSituacao;
	}
	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}
	
	@OneToOne
	@JoinColumn(name="ID_USUARIO_SOLICITANTE",nullable=false)
	public UsuarioLMS getUsuarioSolicitante() {
		return usuarioSolicitante;
	}
	public void setUsuarioSolicitante(UsuarioLMS usuarioSolicitante) {
		this.usuarioSolicitante = usuarioSolicitante;
	}
	
	@OneToOne
	@JoinColumn(name="ID_USUARIO_APROVADOR")
	public UsuarioLMS getUsuarioAprovador() {
		return usuarioAprovador;
	}
	public void setUsuarioAprovador(UsuarioLMS usuarioAprovador) {
		this.usuarioAprovador = usuarioAprovador;
	}
	
	@OneToOne
	@JoinColumn(name="ID_USUARIO_FINALIZADOR")
	public UsuarioLMS getUsuarioFinalizador() {
		return usuarioFinalizador;
	}
	public void setUsuarioFinalizador(UsuarioLMS usuarioFinalizador) {
		this.usuarioFinalizador = usuarioFinalizador;
	}
	
	@OneToMany(mappedBy="contingencia")
	public List<MonitoramentoDocEletronico> getMonitoramentoDocEletronico() {
		return monitoramentoDocEletronico;
	}
	public void setMonitoramentoDocEletronico(
			List<MonitoramentoDocEletronico> monitoramentoDocEletronico) {
		this.monitoramentoDocEletronico = monitoramentoDocEletronico;
	}
	
	@Column(name="DS_CONTINGENCIA", nullable=false, length=50)
	public String getDsContingencia() {
		return dsContingencia;
	}
	public void setDsContingencia(String dsContingencia) {
		this.dsContingencia = dsContingencia;
	}
	
	@Column(name = "TP_CONTINGENCIA", length = 1, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_CONTINGENCIA") })
	public DomainValue getTpContingencia() {
		return tpContingencia;
	}
	public void setTpContingencia(DomainValue tpContingencia) {
		this.tpContingencia = tpContingencia;
	}
	

}
