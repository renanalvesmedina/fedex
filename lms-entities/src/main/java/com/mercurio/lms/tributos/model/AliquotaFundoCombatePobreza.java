package com.mercurio.lms.tributos.model;

import java.math.BigDecimal;

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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.municipios.model.UnidadeFederativa;

@Entity
@Table(name = "ALIQUOTA_FUNDO_COMB_POBREZA")
@SequenceGenerator(name = "ALIQUOTA_FUNDO_COMB_POBREZA_SEQ", sequenceName = "ALIQUOTA_FUNDO_COMB_POBREZA_SQ", allocationSize = 1)
public class AliquotaFundoCombatePobreza implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_ALIQUOTA_FUNDO_COMB_POBREZA", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ALIQUOTA_FUNDO_COMB_POBREZA_SEQ")
	private Long idAliquotaFundoCombatePobreza;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_UNIDADE_FEDERATIVA", nullable = false)
	private UnidadeFederativa unidadeFederativa;
	
	@Column(name = "PC_ALIQUOTA", nullable = false)
	private BigDecimal pcAliquota;
	
	@Column(name = "DT_VIGENCIA_INCIAL", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaInicial;

	@Column(name = "DT_VIGENCIA_FINAL")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayNotNullUserType")
	private YearMonthDay dtVigenciaFinal;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO_INCLUSAO", nullable = false)
	private UsuarioLMS usuarioInclusao;
	
	@Columns(columns = { @Column(name = "DH_INCLUSAO"), @Column(name = "DH_INCLUSAO_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhInclusao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO_ALTERACAO", nullable = false)
	private UsuarioLMS usuarioAlteracao;
	
	@Columns(columns = { @Column(name = "DH_ALTERACAO"), @Column(name = "DH_ALTERACAO_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhAlteracao;
	
	public String toString() {
		return new ToStringBuilder(this).append("idAliquotaFundoCombatePobreza", getIdAliquotaFundoCombatePobreza()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AliquotaFundoCombatePobreza))
			return false;
		AliquotaFundoCombatePobreza castOther = (AliquotaFundoCombatePobreza) other;
		return new EqualsBuilder().append(this.getIdAliquotaFundoCombatePobreza(), castOther.getIdAliquotaFundoCombatePobreza()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdAliquotaFundoCombatePobreza()).toHashCode();
	}

	public Long getIdAliquotaFundoCombatePobreza() {
		return idAliquotaFundoCombatePobreza;
	}

	public void setIdAliquotaFundoCombatePobreza(Long idAliquotaFundoCombatePobreza) {
		this.idAliquotaFundoCombatePobreza = idAliquotaFundoCombatePobreza;
	}

	public UnidadeFederativa getUnidadeFederativa() {
		return unidadeFederativa;
	}

	public void setUnidadeFederativa(UnidadeFederativa unidadeFederativa) {
		this.unidadeFederativa = unidadeFederativa;
	}

	public BigDecimal getPcAliquota() {
		return pcAliquota;
	}

	public void setPcAliquota(BigDecimal pcAliquota) {
		this.pcAliquota = pcAliquota;
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

	public UsuarioLMS getUsuarioInclusao() {
		return usuarioInclusao;
	}

	public void setUsuarioInclusao(UsuarioLMS usuarioInclusao) {
		this.usuarioInclusao = usuarioInclusao;
	}

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public UsuarioLMS getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(UsuarioLMS usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public DateTime getDhAlteracao() {
		return dhAlteracao;
	}

	public void setDhAlteracao(DateTime dhAlteracao) {
		this.dhAlteracao = dhAlteracao;
	}
}
