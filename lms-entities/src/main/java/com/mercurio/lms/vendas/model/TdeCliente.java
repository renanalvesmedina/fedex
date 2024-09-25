package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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

@Entity
@Table(name = "TDE_CLIENTE")
@SequenceGenerator(name = "TDE_CLIENTE_SQ", sequenceName = "TDE_CLIENTE_SQ", allocationSize=1)
public class TdeCliente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_TDE_CLIENTE", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TDE_CLIENTE_SQ")
	private Long idTdeCliente;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_DIVISAO_CLIENTE", nullable=false)
	private DivisaoCliente divisaoCliente;

	@Column(name = "DT_VIGENCIA_INICIAL")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaInicial;
	
	@Column(name = "DT_VIGENCIA_FINAL")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaFinal;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO_INCLUSAO", nullable = false)
	private UsuarioLMS usuarioInclusao;
	
	@Columns(columns = { @Column(name = "DH_INCLUSAO", nullable = true), @Column(name = "DH_INCLUSAO_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhInclusao;

	@Columns(columns = { @Column(name = "DH_ALTERACAO", nullable = true), @Column(name = "DH_ALTERACAO_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhAlteracao;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO_ALTERACAO")
	private UsuarioLMS usuarioAlteracao;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="tdeCliente")
	private List<DestinatarioTdeCliente> destinatarioTdeClientes;

	public Long getIdTdeCliente() {
		return idTdeCliente;
	}

	public void setIdTdeCliente(Long idTdeCliente) {
		this.idTdeCliente = idTdeCliente;
	}

	public DivisaoCliente getDivisaoCliente() {
		return divisaoCliente;
	}

	public void setDivisaoCliente(DivisaoCliente divisaoCliente) {
		this.divisaoCliente = divisaoCliente;
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

	public DateTime getDhAlteracao() {
		return dhAlteracao;
	}

	public void setDhAlteracao(DateTime dhAlteracao) {
		this.dhAlteracao = dhAlteracao;
	}

	public UsuarioLMS getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(UsuarioLMS usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}
	
	public List<DestinatarioTdeCliente> getDestinatarioTdeClientes() {
		return destinatarioTdeClientes;
	}

	public void setDestinatarioTdeClientes(List<DestinatarioTdeCliente> destinatarioTdeClientes) {
		this.destinatarioTdeClientes = destinatarioTdeClientes;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idTdeCliente", getIdTdeCliente()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TdeCliente))
			return false;
		
		TdeCliente castOther = (TdeCliente) other;
		return new EqualsBuilder().append(this.getIdTdeCliente(), castOther.getIdTdeCliente()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdTdeCliente()).toHashCode();
	}
	
}
