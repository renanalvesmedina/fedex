package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

@Entity
@Table(name="BLACK_LIST")
@SequenceGenerator(name = "BLACK_LIST_SQ", sequenceName = "BLACK_LIST_SQ")
public class BlackList implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_BLACK_LIST")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BLACK_LIST_SQ")
	private Long idBlackList;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_CONTATO", nullable=false)
	private Contato contato;

	@Column(name="DT_INCLUSAO", nullable=false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtInclusao;


	public Long getIdBlackList() {
		return idBlackList;
	}

	public void setIdBlackList(Long idBlackList) {
		this.idBlackList = idBlackList;
	}

	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	public YearMonthDay getDtInclusao() {
		return dtInclusao;
	}

	public void setDtInclusao(YearMonthDay dtInclusao) {
		this.dtInclusao = dtInclusao;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("idBlackList", getIdBlackList()).toString();
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof BlackList))
			return false;
		BlackList castOther = (BlackList) other;
		return new EqualsBuilder().append(this.getIdBlackList(),
				castOther.getIdBlackList()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getIdBlackList()).toHashCode();
	}

}
