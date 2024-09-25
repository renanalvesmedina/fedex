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
@Table(name = "DIFERENCA_COMISSAO")
@SequenceGenerator(name = "DIFERENCA_COMISSAO_SEQ", sequenceName = "DIFERENCA_COMISSAO_SQ", allocationSize = 1)
public class DiferencaComissao implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public DiferencaComissao() {
	}

	public DiferencaComissao(Long idDiferencaComissao, BigDecimal vlComissao, DomainValue tpTeto, YearMonthDay dtCompetencia,
			ExecutivoTerritorio executivoTerritorio, String dsObservacao, DomainValue tpSituacao, UsuarioLMS usuarioInclusao, 
			UsuarioLMS usuarioAlteracao, DateTime dhInclusao, DateTime dhAlteracao) {
		super();
		this.idDiferencaComissao = idDiferencaComissao;
		this.vlComissao = vlComissao;
		this.tpTeto = tpTeto;
		this.dtCompetencia = dtCompetencia;
		this.executivoTerritorio = executivoTerritorio;
		this.dsObservacao = dsObservacao;
		this.tpSituacao = tpSituacao;
		this.usuarioInclusao = usuarioInclusao;
		this.usuarioAlteracao = usuarioAlteracao;
		this.dhInclusao = dhInclusao;
		this.dhAlteracao = dhAlteracao;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DIFERENCA_COMISSAO_SEQ")
	@Column(name = "ID_DIFERENCA_COMISSAO", nullable = false)
	private Long idDiferencaComissao;

	@Column(name = "VL_COMISSAO", nullable = false)
	private BigDecimal vlComissao;

	@Column(name = "TP_TETO", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_SIM_NAO") })
	private DomainValue tpTeto;

	@Column(name = "DT_COMPETENCIA", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtCompetencia;

	@Column(name = "DS_OBSERVACAO", nullable = false)
	private String dsObservacao;
	
	@ManyToOne
	@JoinColumn(name = "ID_EXECUTIVO_TERRITORIO", nullable = true)
	private ExecutivoTerritorio executivoTerritorio;

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

	public Long getIdDiferencaComissao() {
		return idDiferencaComissao;
	}

	public void setIdDiferencaComissao(Long idDiferencaComissao) {
		this.idDiferencaComissao = idDiferencaComissao;
	}

	public BigDecimal getVlComissao() {
		return vlComissao;
	}

	public void setVlComissao(BigDecimal vlComissao) {
		this.vlComissao = vlComissao;
	}

	public DomainValue getTpTeto() {
		return tpTeto;
	}

	public void setTpTeto(DomainValue tpTeto) {
		this.tpTeto = tpTeto;
	}

	public YearMonthDay getDtCompetencia() {
		return dtCompetencia;
	}

	public void setDtCompetencia(YearMonthDay dtCompetencia) {
		this.dtCompetencia = dtCompetencia;
	}

	public String getDsObservacao() {
		return dsObservacao;
	}

	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}

	public ExecutivoTerritorio getExecutivoTerritorio() {
		return executivoTerritorio;
	}

	public void setExecutivoTerritorio(ExecutivoTerritorio executivoTerritorio) {
		this.executivoTerritorio = executivoTerritorio;
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
	
	public void updateValues(Map<String, Object> map, ExecutivoTerritorio executivoTerritorio, UsuarioLMS usuarioAlteracao, DateTime dhAlteracao) {
		this.setVlComissao((BigDecimal) map.get("vlComissao"));
		this.setTpTeto((DomainValue) map.get("tpTeto"));
		this.setDtCompetencia((YearMonthDay) map.get("dtCompetencia"));
		this.setDsObservacao((String) map.get("dsObservacao"));
		this.setTpSituacao((DomainValue) map.get("tpSituacao"));
		this.setExecutivoTerritorio(executivoTerritorio);
		this.setUsuarioAlteracao(usuarioAlteracao);
		this.setDhAlteracao(dhAlteracao);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idDiferencaComissao == null) ? 0 : idDiferencaComissao.hashCode());
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
		DiferencaComissao other = (DiferencaComissao) obj;
		if (idDiferencaComissao == null) {
			if (other.idDiferencaComissao != null)
				return false;
		} else if (!idDiferencaComissao.equals(other.idDiferencaComissao))
			return false;
		return true;
	}

}
