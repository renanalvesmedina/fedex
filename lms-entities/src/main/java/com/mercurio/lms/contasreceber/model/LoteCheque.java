package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class LoteCheque implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idLoteCheque;

    /** persistent field */
    private Long nrLoteCheque;

    /** persistent field */
    private Integer versao;    
    
    /** persistent field */
    private Date dtEmissao;

    /** persistent field */
    private Boolean blFechado;

    /** persistent field */
    private BigDecimal vlTotalLote;

    /** persistent field */
    private DomainValue tpSituacaoLoteCheque;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Redeco redeco;
    
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /** persistent field */
    private List cheques;

    /** full constructor */
	public LoteCheque(Long nrLoteCheque, Date dtEmissao, Boolean blFechado,
			DomainValue tpSituacaoLoteCheque,
			com.mercurio.lms.contasreceber.model.Redeco redeco, List cheques) {
        this.nrLoteCheque = nrLoteCheque;
        this.dtEmissao = dtEmissao;
        this.blFechado = blFechado;
        this.tpSituacaoLoteCheque = tpSituacaoLoteCheque;
        this.redeco = redeco;
        this.cheques = cheques;
    }

    /** default constructor */
    public LoteCheque() {
    }

    public Long getIdLoteCheque() {
        return this.idLoteCheque;
    }

    public void setIdLoteCheque(Long idLoteCheque) {
        this.idLoteCheque = idLoteCheque;
    }

    public Long getNrLoteCheque() {
        return this.nrLoteCheque;
    }

    public void setNrLoteCheque(Long nrLoteCheque) {
        this.nrLoteCheque = nrLoteCheque;
    }

    public Date getDtEmissao() {
        return this.dtEmissao;
    }

    public void setDtEmissao(Date dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public Boolean getBlFechado() {
        return this.blFechado;
    }

    public void setBlFechado(Boolean blFechado) {
        this.blFechado = blFechado;
    }

    public DomainValue getTpSituacaoLoteCheque() {
        return this.tpSituacaoLoteCheque;
    }

    public void setTpSituacaoLoteCheque(DomainValue tpSituacaoLoteCheque) {
        this.tpSituacaoLoteCheque = tpSituacaoLoteCheque;
    }

    public com.mercurio.lms.contasreceber.model.Redeco getRedeco() {
        return this.redeco;
    }

    public void setRedeco(com.mercurio.lms.contasreceber.model.Redeco redeco) {
        this.redeco = redeco;
    }

    public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.Cheque.class)     
    public List getCheques() {
        return this.cheques;
    }

    public void setCheques(List cheques) {
        this.cheques = cheques;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
		return moeda;
	}

	public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
		this.moeda = moeda;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idLoteCheque",
				getIdLoteCheque()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof LoteCheque))
			return false;
        LoteCheque castOther = (LoteCheque) other;
		return new EqualsBuilder().append(this.getIdLoteCheque(),
				castOther.getIdLoteCheque()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdLoteCheque()).toHashCode();
    }

	public BigDecimal getVlTotalLote() {
		return vlTotalLote;
	}

	public void setVlTotalLote(BigDecimal vlTotalLote) {
		this.vlTotalLote = vlTotalLote;
	}

}