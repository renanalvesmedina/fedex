package com.mercurio.lms.indenizacoes.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

/** @author LMS Custom Hibernate CodeGenerator */
public class FilialDebitada implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idFilialDebitada;

    /** persistent field */
    private BigDecimal pcDebitado;

    /** nullable persistent field */
    private YearMonthDay dtDadoReembolso;

    /** nullable persistent field */
    private YearMonthDay dtReembolso;

    /** nullable persistent field */
    private BigDecimal vlReembolso;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /** persistent field */
    private com.mercurio.lms.indenizacoes.model.ReciboIndenizacao reciboIndenizacao;

    /** persistent field */
    private com.mercurio.lms.indenizacoes.model.DoctoServicoIndenizacao doctoServicoIndenizacao;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialReembolso;

    public com.mercurio.lms.municipios.model.Filial getFilialReembolso() {
		return filialReembolso;
	}

	public void setFilialReembolso(
			com.mercurio.lms.municipios.model.Filial filialReembolso) {
		this.filialReembolso = filialReembolso;
	}

	public Long getIdFilialDebitada() {
        return this.idFilialDebitada;
    }

    public void setIdFilialDebitada(Long idFilialDebitada) {
        this.idFilialDebitada = idFilialDebitada;
    }

    public BigDecimal getPcDebitado() {
        return this.pcDebitado;
    }

    public void setPcDebitado(BigDecimal pcDebitado) {
        this.pcDebitado = pcDebitado;
    }

    public YearMonthDay getDtDadoReembolso() {
        return this.dtDadoReembolso;
    }

    public void setDtDadoReembolso(YearMonthDay dtDadoReembolso) {
        this.dtDadoReembolso = dtDadoReembolso;
    }

    public YearMonthDay getDtReembolso() {
        return this.dtReembolso;
    }

    public void setDtReembolso(YearMonthDay dtReembolso) {
        this.dtReembolso = dtReembolso;
    }

    public BigDecimal getVlReembolso() {
        return this.vlReembolso;
    }

    public void setVlReembolso(BigDecimal vlReembolso) {
        this.vlReembolso = vlReembolso;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public com.mercurio.lms.indenizacoes.model.ReciboIndenizacao getReciboIndenizacao() {
        return this.reciboIndenizacao;
    }

	public void setReciboIndenizacao(
			com.mercurio.lms.indenizacoes.model.ReciboIndenizacao reciboIndenizacao) {
        this.reciboIndenizacao = reciboIndenizacao;
    }

    public com.mercurio.lms.indenizacoes.model.DoctoServicoIndenizacao getDoctoServicoIndenizacao() {
		return doctoServicoIndenizacao;
	}

	public void setDoctoServicoIndenizacao(
			com.mercurio.lms.indenizacoes.model.DoctoServicoIndenizacao doctoServicoIndenizacao) {
		this.doctoServicoIndenizacao = doctoServicoIndenizacao;
	}

	public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idFilialDebitada",
				getIdFilialDebitada()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FilialDebitada))
			return false;
        FilialDebitada castOther = (FilialDebitada) other;
		return new EqualsBuilder().append(this.getIdFilialDebitada(),
				castOther.getIdFilialDebitada()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFilialDebitada()).toHashCode();
    }

}
