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
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;

@Entity
@Table(name = "COMISSAO_CONQUISTA")
@SequenceGenerator(name = "COMISSAO_CONQUISTA_SEQ", sequenceName = "COMISSAO_CONQUISTA_SQ", allocationSize = 1)
public class ComissaoConquista implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public ComissaoConquista() {
	}

	public ComissaoConquista(Long idComissaoConquista, ClienteTerritorio clienteTerritorio, UsuarioLMS usuarioInclusao, UsuarioLMS usuarioAlteracao,
			DateTime dhInclusao, DateTime dhAlteracao, YearMonthDay dtInicio, YearMonthDay dtFim, String nmSistema) {
		super();
		this.idComissaoConquista = idComissaoConquista;
		this.clienteTerritorio = clienteTerritorio;
		this.usuarioInclusao = usuarioInclusao;
		this.usuarioAlteracao = usuarioAlteracao;
		this.dhInclusao = dhInclusao;
		this.dhAlteracao = dhAlteracao;
		this.dtInicio = dtInicio;
		this.dtFim = dtFim;
		this.nmSistema = nmSistema;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMISSAO_CONQUISTA_SEQ")
	@Column(name = "ID_COMISSAO_CONQUISTA", nullable = false)
	private Long idComissaoConquista;

	@ManyToOne
	@JoinColumn(name = "ID_CLIENTE_TERRITORIO", nullable = true)
	private ClienteTerritorio clienteTerritorio;

	@Column(name = "TP_SITUACAO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_STATUS") })
	private DomainValue tpSituacao;
	
	@ManyToOne
	@JoinColumn(name = "ID_USUARIO_INCLUSAO")
	private UsuarioLMS usuarioInclusao;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO_ALTERACAO")
	private UsuarioLMS usuarioAlteracao;

	@Columns(columns = { @Column(name = "DH_INCLUSAO"), @Column(name = "DH_INCLUSAO_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhInclusao;

	@Columns(columns = { @Column(name = "DH_ALTERACAO"), @Column(name = "DH_ALTERACAO_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhAlteracao;

	@Column(name = "DT_INICIO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtInicio;

	@Column(name = "DT_FIM")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtFim;
	
	@Column(name = "NM_SISTEMA", nullable = false)
	private String nmSistema;

	public Long getIdComissaoConquista() {
		return idComissaoConquista;
	}

	public void setIdComissaoConquista(Long idComissaoConquista) {
		this.idComissaoConquista = idComissaoConquista;
	}

	public ClienteTerritorio getClienteTerritorio() {
		return clienteTerritorio;
	}

	public void setClienteTerritorio(ClienteTerritorio clienteTerritorio) {
		this.clienteTerritorio = clienteTerritorio;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
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

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public DateTime getDhAlteracao() {
		return dhAlteracao;
	}

	public void setDhAlteracao(DateTime dhAlteracao) {
		this.dhAlteracao = dhAlteracao;
	}

	public YearMonthDay getDtInicio() {
		return dtInicio;
	}

	public void setDtInicio(YearMonthDay dtInicio) {
		this.dtInicio = dtInicio;
	}

	public YearMonthDay getDtFim() {
		return dtFim;
	}

	public void setDtFim(YearMonthDay dtFim) {
		this.dtFim = dtFim;
	}
	
	public String getNmSistema() {
		return nmSistema;
	}

	public void setNmSistema(String nmSistema) {
		this.nmSistema = nmSistema;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idComissaoConquista == null) ? 0 : idComissaoConquista.hashCode());
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
		ComissaoConquista other = (ComissaoConquista) obj;
		if (idComissaoConquista == null) {
			if (other.idComissaoConquista != null)
				return false;
		} else if (!idComissaoConquista.equals(other.idComissaoConquista))
			return false;
		return true;
	}

}
