package com.mercurio.lms.prestcontasciaaerea.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class IcmsPrestacao implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idIcmsPrestacao;

    /** persistent field */
    private BigDecimal pcAliquota;

    /** persistent field */
    private BigDecimal vlFrete;

    /** persistent field */
    private BigDecimal vlAdvalorem;

    /** persistent field */
    private BigDecimal vlTaxa;

    /** persistent field */
    private BigDecimal vlIcms;
    
    private BigDecimal vlTotal;

    /** persistent field */
    private com.mercurio.lms.prestcontasciaaerea.model.PrestacaoConta prestacaoConta;

    /** full constructor */
	public IcmsPrestacao(
			BigDecimal pcAliquota,
			BigDecimal vlFrete,
			BigDecimal vlAdvalorem,
			BigDecimal vlTaxa,
			BigDecimal vlIcms,
			com.mercurio.lms.prestcontasciaaerea.model.PrestacaoConta prestacaoConta) {
        this.pcAliquota = pcAliquota;
        this.vlFrete = vlFrete;
        this.vlAdvalorem = vlAdvalorem;
        this.vlTaxa = vlTaxa;
        this.vlIcms = vlIcms;
        this.prestacaoConta = prestacaoConta;
    }

    /** default constructor */
    public IcmsPrestacao() {
    }

    public Long getIdIcmsPrestacao() {
        return this.idIcmsPrestacao;
    }

    public void setIdIcmsPrestacao(Long idIcmsPrestacao) {
        this.idIcmsPrestacao = idIcmsPrestacao;
    }

    public BigDecimal getPcAliquota() {
        return this.pcAliquota;
    }

    public void setPcAliquota(BigDecimal pcAliquota) {
        this.pcAliquota = pcAliquota;
    }

    public BigDecimal getVlFrete() {
        return this.vlFrete;
    }

    public void setVlFrete(BigDecimal vlFrete) {
        this.vlFrete = vlFrete;
    }

    public BigDecimal getVlAdvalorem() {
        return this.vlAdvalorem;
    }

    public void setVlAdvalorem(BigDecimal vlAdvalorem) {
        this.vlAdvalorem = vlAdvalorem;
    }

    public BigDecimal getVlTaxa() {
        return this.vlTaxa;
    }

    public void setVlTaxa(BigDecimal vlTaxa) {
        this.vlTaxa = vlTaxa;
    }

    public BigDecimal getVlIcms() {
        return this.vlIcms;
    }

    public void setVlIcms(BigDecimal vlIcms) {
        this.vlIcms = vlIcms;
    }

    /**
     * Getter para somatório do valor toral e valor do frete.<BR>
	 * 
     *@author Robson Edemar Gehl
     * @return (valor da taxa + valor do frete)
     */
    public BigDecimal getVlTotal() {
    	if (getVlTaxa() != null && getVlFrete() != null){
    		vlTotal = getVlTaxa().add(getVlFrete());
    	}else if (getVlTaxa() == null && getVlFrete() != null){
    		vlTotal = getVlFrete();
    	}else if (getVlTaxa() != null && getVlFrete() == null){
    		vlTotal = getVlTaxa();
    	}
		return vlTotal;
	}

	public void setVlTotal(BigDecimal vlTotal) {
		this.vlTotal = vlTotal;
	}

	public com.mercurio.lms.prestcontasciaaerea.model.PrestacaoConta getPrestacaoConta() {
        return this.prestacaoConta;
    }

	public void setPrestacaoConta(
			com.mercurio.lms.prestcontasciaaerea.model.PrestacaoConta prestacaoConta) {
        this.prestacaoConta = prestacaoConta;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idIcmsPrestacao",
				getIdIcmsPrestacao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof IcmsPrestacao))
			return false;
        IcmsPrestacao castOther = (IcmsPrestacao) other;
		return new EqualsBuilder().append(this.getIdIcmsPrestacao(),
				castOther.getIdIcmsPrestacao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdIcmsPrestacao()).toHashCode();
    }

}
