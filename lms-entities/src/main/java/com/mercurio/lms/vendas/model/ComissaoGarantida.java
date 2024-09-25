package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.configuracoes.model.UsuarioLMS;

@Entity
@Table(name = "COMISSAO_GARANTIDA")
@SequenceGenerator(name = "COMISSAO_GARANTIDA_SEQ", sequenceName = "COMISSAO_GARANTIDA_SQ", allocationSize = 1)
public class ComissaoGarantida implements Serializable {

	private static final long serialVersionUID = 1L;

	public ComissaoGarantida() {
	}

	public ComissaoGarantida(Long idComissaoGarantida, BigDecimal vlComissao, YearMonthDay dtInicio, YearMonthDay dtFim,
			ExecutivoTerritorio executivoTerritorio, UsuarioLMS usuarioInclusao, UsuarioLMS usuarioAlteracao, DateTime dhInclusao, DateTime dhAlteracao) {
		super();
		this.idComissaoGarantida = idComissaoGarantida;
		this.vlComissao = vlComissao;
		this.dtInicio = dtInicio;
		this.dtFim = dtFim;
		this.executivoTerritorio = executivoTerritorio;
		this.usuarioInclusao = usuarioInclusao;
		this.usuarioAlteracao = usuarioAlteracao;
		this.dhInclusao = dhInclusao;
		this.dhAlteracao = dhAlteracao;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMISSAO_GARANTIDA_SEQ")
	@Column(name = "ID_COMISSAO_GARANTIDA", nullable = false)
	private Long idComissaoGarantida;

	@Column(name = "VL_COMISSAO", nullable = false, length = 12, scale = 2)
	private BigDecimal vlComissao;

	@Column(name = "DT_INICIO", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtInicio;

	@Column(name = "DT_FIM", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtFim;

	@OneToOne
	@JoinColumn(name = "ID_EXECUTIVO_TERRITORIO", nullable = false)
	private ExecutivoTerritorio executivoTerritorio;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO_INCLUSAO", nullable = false)
	private UsuarioLMS usuarioInclusao;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO_ALTERACAO", nullable = false)
	private UsuarioLMS usuarioAlteracao;

	@Columns(columns = { @Column(name = "DH_INCLUSAO", nullable = false), @Column(name = "DH_INCLUSAO_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhInclusao;

	@Columns(columns = { @Column(name = "DH_ALTERACAO", nullable = false), @Column(name = "DH_ALTERACAO_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhAlteracao;

	public Long getIdComissaoGarantida() {
		return idComissaoGarantida;
	}

	public void setIdComissaoGarantida(Long idComissaoGarantida) {
		this.idComissaoGarantida = idComissaoGarantida;
	}

	public BigDecimal getVlComissao() {
		return vlComissao;
	}

	public void setVlComissao(BigDecimal vlComissao) {
		this.vlComissao = vlComissao;
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

	public ExecutivoTerritorio getExecutivoTerritorio() {
		return executivoTerritorio;
	}

	public void setExecutivoTerritorio(ExecutivoTerritorio executivoTerritorio) {
		this.executivoTerritorio = executivoTerritorio;
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
		result = prime * result + ((idComissaoGarantida == null) ? 0 : idComissaoGarantida.hashCode());
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
		ComissaoGarantida other = (ComissaoGarantida) obj;
		if (idComissaoGarantida == null) {
			if (other.idComissaoGarantida != null)
				return false;
		} else if (!idComissaoGarantida.equals(other.idComissaoGarantida))
			return false;
		return true;
	}

	public void updateValues(Map<String, Object> map, ExecutivoTerritorio executivoTerritorio, UsuarioLMS usuarioAlteracao, DateTime dhAlteracao) {
		this.setVlComissao((BigDecimal) map.get("vlComissao"));
		this.setDtInicio((YearMonthDay) map.get("dtInicio"));
		this.setDtFim((YearMonthDay) map.get("dtFim"));
		this.setExecutivoTerritorio(executivoTerritorio);
		this.setUsuarioAlteracao(usuarioAlteracao);
		this.setDhAlteracao(dhAlteracao);
	}

}
