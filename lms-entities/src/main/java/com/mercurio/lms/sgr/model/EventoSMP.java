package com.mercurio.lms.sgr.model;

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
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * LMS-7906 - Entidade para eventos de Solicitação de Monitoramento Preventivo.
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 *
 */
@Entity
@Table(name = "EVENTO_SMP")
@SequenceGenerator(name = "EVENTO_SMP_SQ", sequenceName = "EVENTO_SMP_SQ")
public class EventoSMP implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_EVENTO_SMP", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EVENTO_SMP_SQ")
	private Long idEventoSMP;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_SOLIC_MONIT_PREVENTIVO", nullable = false)
	private SolicMonitPreventivo solicMonitPreventivo;

	@Column(name = "CD_EVENTO", nullable = false)
	private String cdEvento;

	@Column(name = "DS_EVENTO", nullable = false)
	private String dsEvento;

	@Columns(columns = {
			@Column(name = "DH_EVENTO", nullable = false),
			@Column(name = "DH_EVENTO_TZR", nullable = false)
	})
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhEvento;

	@Column(name = "DS_OBSERVACAO", nullable = true)
	private String dsObservacao;

	public Long getIdEventoSMP() {
		return idEventoSMP;
	}

	public void setIdEventoSMP(Long idEventoSMP) {
		this.idEventoSMP = idEventoSMP;
	}

	public SolicMonitPreventivo getSolicMonitPreventivo() {
		return solicMonitPreventivo;
	}

	public void setSolicMonitPreventivo(SolicMonitPreventivo solicMonitPreventivo) {
		this.solicMonitPreventivo = solicMonitPreventivo;
	}

	public String getCdEvento() {
		return cdEvento;
	}

	public void setCdEvento(String cdEvento) {
		this.cdEvento = cdEvento;
	}

	public String getDsEvento() {
		return dsEvento;
	}

	public void setDsEvento(String dsEvento) {
		this.dsEvento = dsEvento;
	}

	public DateTime getDhEvento() {
		return dhEvento;
	}

	public void setDhEvento(DateTime dhEvento) {
		this.dhEvento = dhEvento;
	}

	public String getDsObservacao() {
		return dsObservacao;
	}

	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}

	@Override
	public String toString() {
		ToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
		return new ToStringBuilder(this)
				.append(idEventoSMP)
				.toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(idEventoSMP)
				.toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof EventoSMP)) {
			return false;
		}
		EventoSMP cast = (EventoSMP) other;
		return new EqualsBuilder()
				.append(idEventoSMP, cast.idEventoSMP)
				.isEquals();
	}

}
