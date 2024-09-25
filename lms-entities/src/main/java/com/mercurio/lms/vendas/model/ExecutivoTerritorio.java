package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
@Table(name = "EXECUTIVO_TERRITORIO")
@SequenceGenerator(name = "EXECUTIVO_TERRITORIO_SEQ", sequenceName = "EXECUTIVO_TERRITORIO_SQ", allocationSize = 1)
public class ExecutivoTerritorio implements Serializable {

	private static final long serialVersionUID = 1L;

	public ExecutivoTerritorio() {
	}

	public ExecutivoTerritorio(Long idExecutivoTerritorio, Territorio territorio, UsuarioLMS usuario, UsuarioLMS usuarioInclusao, UsuarioLMS usuarioAlteracao,
			DomainValue tpExecutivo, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal, DateTime dhAlteracao, DateTime dhInclusao) {
		super();
		this.idExecutivoTerritorio = idExecutivoTerritorio;
		this.territorio = territorio;
		this.usuario = usuario;
		this.usuarioInclusao = usuarioInclusao;
		this.usuarioAlteracao = usuarioAlteracao;
		this.tpExecutivo = tpExecutivo;
		this.dtVigenciaInicial = dtVigenciaInicial;
		this.dtVigenciaFinal = dtVigenciaFinal;
		this.dhAlteracao = dhAlteracao;
		this.dhInclusao = dhInclusao;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EXECUTIVO_TERRITORIO_SEQ")
	@Column(name = "ID_EXECUTIVO_TERRITORIO", nullable = false)
	private Long idExecutivoTerritorio;

	@OneToOne
	@JoinColumn(name = "ID_TERRITORIO", nullable = true)
	private Territorio territorio;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO")
	private UsuarioLMS usuario;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO_INCLUSAO")
	private UsuarioLMS usuarioInclusao;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO_ALTERACAO")
	private UsuarioLMS usuarioAlteracao;

	@Column(name = "TP_EXECUTIVO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_EXECUTIVO") })
	private DomainValue tpExecutivo;
	
	@Column(name = "TP_SITUACAO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_STATUS") })
	private DomainValue tpSituacao;

	@Column(name = "DT_INICIO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaInicial;

	@Column(name = "DT_FIM")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaFinal;

	@Columns(columns = { @Column(name = "DH_ALTERACAO", nullable = true), @Column(name = "DH_ALTERACAO_TZR", nullable = true) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhAlteracao;

	@Columns(columns = { @Column(name = "DH_INCLUSAO", nullable = true), @Column(name = "DH_INCLUSAO_TZR", nullable = true) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhInclusao;

	public Long getIdExecutivoTerritorio() {
		return idExecutivoTerritorio;
	}

	public void setIdExecutivoTerritorio(Long idExecutivoTerritorio) {
		this.idExecutivoTerritorio = idExecutivoTerritorio;
	}

	public Territorio getTerritorio() {
		return territorio;
	}

	public void setTerritorio(Territorio territorio) {
		this.territorio = territorio;
	}

	public UsuarioLMS getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
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

	public DomainValue getTpExecutivo() {
		return tpExecutivo;
	}

	public void setTpExecutivo(DomainValue tpExecutivo) {
		this.tpExecutivo = tpExecutivo;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idExecutivoTerritorio == null) ? 0 : idExecutivoTerritorio.hashCode());
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
		ExecutivoTerritorio other = (ExecutivoTerritorio) obj;
		if (idExecutivoTerritorio == null) {
			if (other.idExecutivoTerritorio != null)
				return false;
		} else if (!idExecutivoTerritorio.equals(other.idExecutivoTerritorio))
			return false;
		return true;
	}

	public void updateValues(Map<String, Object> map, DateTime dhAlteracao, UsuarioLMS usuarioAlteracao, Territorio territorio, UsuarioLMS usuario) {
		this.setDtVigenciaInicial((YearMonthDay) map.get("vigenciaInicial"));
		this.setDtVigenciaFinal((YearMonthDay) map.get("vigenciaFinal"));
		this.setTpExecutivo((DomainValue) map.get("tpExecutivo"));
		this.setDhAlteracao(dhAlteracao);
		this.setUsuarioAlteracao(usuarioAlteracao);
		this.setTerritorio(territorio);
		this.setUsuario(usuario);
	}

}
