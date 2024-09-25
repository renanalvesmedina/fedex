package com.mercurio.lms.seguros.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

public class ApoliceSeguroParcela implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/** identifier field */
    private Long idApoliceSeguroParcela;
    
    /** persistent field */
    private com.mercurio.lms.seguros.model.ApoliceSeguro apoliceSeguro;
    
    /** persistent field */
    private Long nrParcela;
    
    /** persistent field */
    private BigDecimal vlParcela;
    
    /** persistent field */
    private YearMonthDay dtVencimento;
    
    /** persistent field */
    private DomainValue tpSituacaoPagamento;
    
    /** persistent field */
    private byte[] dcComprovante;

	public Long getIdApoliceSeguroParcela() {
		return idApoliceSeguroParcela;
	}

	public void setIdApoliceSeguroParcela(Long idApoliceSeguroParcela) {
		this.idApoliceSeguroParcela = idApoliceSeguroParcela;
	}

	public com.mercurio.lms.seguros.model.ApoliceSeguro getApoliceSeguro() {
		return apoliceSeguro;
	}

	public void setApoliceSeguro(com.mercurio.lms.seguros.model.ApoliceSeguro apoliceSeguro) {
		this.apoliceSeguro = apoliceSeguro;
	}

	public Long getNrParcela() {
		return nrParcela;
	}

	public void setNrParcela(Long nrParcela) {
		this.nrParcela = nrParcela;
	}

	public BigDecimal getVlParcela() {
		return vlParcela;
	}

	public void setVlParcela(BigDecimal vlParcela) {
		this.vlParcela = vlParcela;
	}

	public YearMonthDay getDtVencimento() {
		return dtVencimento;
	}

	public void setDtVencimento(YearMonthDay dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public DomainValue getTpSituacaoPagamento() {
		return tpSituacaoPagamento;
	}

	public void setTpSituacaoPagamento(DomainValue tpSituacaoPagamento) {
		this.tpSituacaoPagamento = tpSituacaoPagamento;
	}

	public byte[] getDcComprovante() {
		return dcComprovante;
	}

	public void setDcComprovante(byte[] dcComprovante) {
		this.dcComprovante = dcComprovante;
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("idApoliceSeguroParcela",
				getIdApoliceSeguroParcela()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ApoliceSeguroParcela))
			return false;
        ApoliceSeguroParcela castOther = (ApoliceSeguroParcela) other;
		return new EqualsBuilder().append(this.getIdApoliceSeguroParcela(),
				castOther.getIdApoliceSeguroParcela()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdApoliceSeguroParcela()).toHashCode();
    }
    
}
