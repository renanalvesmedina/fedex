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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.lms.configuracoes.model.UsuarioLMS;

@Entity
@Table(name = "DESTINATARIO_TDE_CLIENTE")
@SequenceGenerator(name = "DESTINATARIO_TDE_CLIENTE_SQ", sequenceName = "DESTINATARIO_TDE_CLIENTE_SQ", allocationSize=1)
public class DestinatarioTdeCliente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_DESTINATARIO_TDE_CLIENTE", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DESTINATARIO_TDE_CLIENTE_SQ")
	private Long idDestinatarioTdeCliente;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name= "ID_CLIENTE", nullable=false)
	private Cliente cliente;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_TDE_CLIENTE", nullable = false)
	private TdeCliente tdeCliente;

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

	public Long getIdDestinatarioTdeCliente() {
		return idDestinatarioTdeCliente;
	}

	public void setIdDestinatarioTdeCliente(Long idDestinatarioTdeCliente) {
		this.idDestinatarioTdeCliente = idDestinatarioTdeCliente;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public TdeCliente getTdeCliente() {
		return tdeCliente;
	}

	public void setTdeCliente(TdeCliente tdeCliente) {
		this.tdeCliente = tdeCliente;
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
	
	public String toString() {
		return new ToStringBuilder(this).append("idDestinatarioTdeCliente", getIdDestinatarioTdeCliente()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DestinatarioTdeCliente))
			return false;
		
		DestinatarioTdeCliente castOther = (DestinatarioTdeCliente) other;
		return new EqualsBuilder().append(this.getIdDestinatarioTdeCliente(), castOther.getIdDestinatarioTdeCliente()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdDestinatarioTdeCliente()).toHashCode();
	}
}
