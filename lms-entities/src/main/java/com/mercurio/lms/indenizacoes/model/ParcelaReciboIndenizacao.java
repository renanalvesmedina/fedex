package com.mercurio.lms.indenizacoes.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ParcelaReciboIndenizacao implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idParcelaReciboIndenizacao;

    /** persistent field */
    private YearMonthDay dtVencimento;

    /** persistent field */
    private String nrBoleto;

    /** persistent field */
    private BigDecimal vlPagamento;

    /** persistent field */
    private DomainValue tpStatusParcelaIndenizacao;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /** persistent field */
    private com.mercurio.lms.indenizacoes.model.ReciboIndenizacao reciboIndenizacao;
    
    /** persistent field */
    private Integer versao;

    public Long getIdParcelaReciboIndenizacao() {
        return this.idParcelaReciboIndenizacao;
    }

    public void setIdParcelaReciboIndenizacao(Long idParcelaReciboIndenizacao) {
        this.idParcelaReciboIndenizacao = idParcelaReciboIndenizacao;
    }

    public YearMonthDay getDtVencimento() {
        return this.dtVencimento;
    }

    public void setDtVencimento(YearMonthDay dtVencimento) {
        this.dtVencimento = dtVencimento;
    }

    public String getNrBoleto() {
        return this.nrBoleto;
    }

    public void setNrBoleto(String nrBoleto) {
        this.nrBoleto = nrBoleto;
    }

    public BigDecimal getVlPagamento() {
        return this.vlPagamento;
    }

    public void setVlPagamento(BigDecimal vlPagamento) {
        this.vlPagamento = vlPagamento;
    }

    public DomainValue getTpStatusParcelaIndenizacao() {
        return this.tpStatusParcelaIndenizacao;
    }

	public void setTpStatusParcelaIndenizacao(
			DomainValue tpStatusParcelaIndenizacao) {
        this.tpStatusParcelaIndenizacao = tpStatusParcelaIndenizacao;
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

    public String toString() {
		return new ToStringBuilder(this).append("idParcelaReciboIndenizacao",
				getIdParcelaReciboIndenizacao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ParcelaReciboIndenizacao))
			return false;
        ParcelaReciboIndenizacao castOther = (ParcelaReciboIndenizacao) other;
		return new EqualsBuilder().append(this.getIdParcelaReciboIndenizacao(),
				castOther.getIdParcelaReciboIndenizacao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdParcelaReciboIndenizacao())
            .toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

}
