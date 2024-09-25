package com.mercurio.lms.entrega.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.configuracoes.model.ContaBancaria;

/** @author LMS Custom Hibernate CodeGenerator */
public class ChequeReembolso implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idChequeReembolso;

    /** persistent field */
    private Integer nrCheque;
    
    /** persistent field */
    private Integer nrBanco;
    
    /** persistent field */
    private Integer nrAgencia;

    /** persistent field */
    private YearMonthDay dtCheque;

    /** persistent field */
    private BigDecimal vlCheque;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;
    
    private ContaBancaria contaBancaria;

    /** persistent field */
    private com.mercurio.lms.entrega.model.ReciboReembolso reciboReembolso;
    
    private String dvAgencia;

    public String getDvAgencia() {
		return dvAgencia;
	}

	public void setDvAgencia(String dvAgencia) {
		this.dvAgencia = dvAgencia;
	}

	public Long getIdChequeReembolso() {
        return this.idChequeReembolso;
    }

    public void setIdChequeReembolso(Long idChequeReembolso) {
        this.idChequeReembolso = idChequeReembolso;
    }

    public Integer getNrCheque() {
        return this.nrCheque;
    }

    public void setNrCheque(Integer nrCheque) {
        this.nrCheque = nrCheque;
    }

    public YearMonthDay getDtCheque() {
        return this.dtCheque;
    }

    public void setDtCheque(YearMonthDay dtCheque) {
        this.dtCheque = dtCheque;
    }

    public BigDecimal getVlCheque() {
        return this.vlCheque;
    }

    public void setVlCheque(BigDecimal vlCheque) {
        this.vlCheque = vlCheque;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public Integer getNrAgencia() {
		return nrAgencia;
	}

	public void setNrAgencia(Integer nrAgencia) {
		this.nrAgencia = nrAgencia;
	}

	public Integer getNrBanco() {
		return nrBanco;
	}

	public void setNrBanco(Integer nrBanco) {
		this.nrBanco = nrBanco;
	}

	public com.mercurio.lms.entrega.model.ReciboReembolso getReciboReembolso() {
        return this.reciboReembolso;
    }

	public void setReciboReembolso(
			com.mercurio.lms.entrega.model.ReciboReembolso reciboReembolso) {
        this.reciboReembolso = reciboReembolso;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idChequeReembolso",
				getIdChequeReembolso()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ChequeReembolso))
			return false;
        ChequeReembolso castOther = (ChequeReembolso) other;
		return new EqualsBuilder().append(this.getIdChequeReembolso(),
				castOther.getIdChequeReembolso()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdChequeReembolso())
            .toHashCode();
    }

	public ContaBancaria getContaBancaria() {
		return contaBancaria;
	}

	public void setContaBancaria(ContaBancaria contaBancaria) {
		this.contaBancaria = contaBancaria;
	}

}
