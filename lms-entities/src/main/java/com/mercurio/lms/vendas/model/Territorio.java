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
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Regional;

@Entity
@Table(name = "TERRITORIO")
@SequenceGenerator(name = "TERRITORIO_SEQ", sequenceName = "TERRITORIO_SQ", allocationSize = 1)
public class Territorio implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TERRITORIO_SEQ")
	@Column(name = "ID_TERRITORIO", nullable = false)
	private Long idTerritorio;

	@ManyToOne
	@JoinColumn(name = "ID_REGIONAL", nullable = true)
	private Regional regional;

	@ManyToOne
	@JoinColumn(name = "ID_FILIAL", nullable = true)
	private Filial filial;

	@Column(name = "NM_TERRITORIO", nullable = false, unique = true)
	private String nmTerritorio;

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

	public Long getIdTerritorio() {
		return idTerritorio;
	}

	public void setIdTerritorio(Long idTerritorio) {
		this.idTerritorio = idTerritorio;
	}

	public Regional getRegional() {
		return regional;
	}

	public void setRegional(Regional regional) {
		this.regional = regional;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public String getNmTerritorio() {
		return nmTerritorio;
	}

	public void setNmTerritorio(String territorio) {
		this.nmTerritorio = territorio;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idTerritorio == null) ? 0 : idTerritorio.hashCode());
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
		Territorio other = (Territorio) obj;
		if (idTerritorio == null) {
			if (other.idTerritorio != null)
				return false;
		} else if (!idTerritorio.equals(other.idTerritorio))
			return false;
		return true;
	}

}
