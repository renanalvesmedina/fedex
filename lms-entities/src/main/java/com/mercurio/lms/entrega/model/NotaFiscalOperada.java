package com.mercurio.lms.entrega.model;

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
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;

@Entity
@Table(name = "NOTA_FISCAL_OPERADA")
@SequenceGenerator(name = "NOTA_FISCAL_OPERADA_SEQ", sequenceName = "NOTA_FISCAL_OPERADA_SQ", allocationSize = 1)
public class NotaFiscalOperada implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_NOTA_FISCAL_OPERADA", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTA_FISCAL_OPERADA_SEQ")
	private Long idNotaFiscalOperada;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_NOTA_FISCAL_CTO_ORIGINAL", nullable = false)
	private NotaFiscalConhecimento notaFiscalConhecimentoOriginal;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DOCTO_SERVICO", nullable = false)
	private DoctoServico doctoServico;
	
	@Column(name = "TP_SITUACAO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_SITUACAO_NOTA_FISCAL_OPERADA") })
	private DomainValue tpSituacao;
	
	public String toString() {
		return new ToStringBuilder(this).append("idNotaFiscalOperada", getIdNotaFiscalOperada()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof NotaFiscalOperada))
			return false;
		NotaFiscalOperada castOther = (NotaFiscalOperada) other;
		return new EqualsBuilder().append(this.getIdNotaFiscalOperada(), castOther.getIdNotaFiscalOperada()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdNotaFiscalOperada()).toHashCode();
	}

	public Long getIdNotaFiscalOperada() {
		return idNotaFiscalOperada;
	}

	public void setIdNotaFiscalOperada(Long idNotaFiscalOperada) {
		this.idNotaFiscalOperada = idNotaFiscalOperada;
	}

	public NotaFiscalConhecimento getNotaFiscalConhecimentoOriginal() {
		return notaFiscalConhecimentoOriginal;
	}

	public void setNotaFiscalConhecimentoOriginal(
			NotaFiscalConhecimento notaFiscalConhecimentoOriginal) {
		this.notaFiscalConhecimentoOriginal = notaFiscalConhecimentoOriginal;
	}

	public DoctoServico getDoctoServico() {
		return doctoServico;
	}

	public void setDoctoServico(DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}



}
