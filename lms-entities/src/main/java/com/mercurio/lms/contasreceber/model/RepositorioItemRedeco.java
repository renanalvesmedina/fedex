package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class RepositorioItemRedeco implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRepositorioItemRedeco;

    /** persistent field */
    private BigDecimal vlTarifa;

    /** persistent field */
    private BigDecimal vlJuros;

    /** nullable persistent field */
    private String obRepositorioItemRedeco;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Fatura fatura;
    
    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Recibo recibo;    

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Redeco redeco;

    public Long getIdRepositorioItemRedeco() {
        return this.idRepositorioItemRedeco;
    }

    public void setIdRepositorioItemRedeco(Long idRepositorioItemRedeco) {
        this.idRepositorioItemRedeco = idRepositorioItemRedeco;
    }

    public BigDecimal getVlTarifa() {
        return this.vlTarifa;
    }

    public void setVlTarifa(BigDecimal vlTarifa) {
        this.vlTarifa = vlTarifa;
    }

    public BigDecimal getVlJuros() {
        return this.vlJuros;
    }

    public void setVlJuros(BigDecimal vlJuros) {
        this.vlJuros = vlJuros;
    }

    public String getObRepositorioItemRedeco() {
        return this.obRepositorioItemRedeco;
    }

    public void setObRepositorioItemRedeco(String obRepositorioItemRedeco) {
        this.obRepositorioItemRedeco = obRepositorioItemRedeco;
    }

    public com.mercurio.lms.contasreceber.model.Fatura getFatura() {
        return this.fatura;
    }

    public void setFatura(com.mercurio.lms.contasreceber.model.Fatura fatura) {
        this.fatura = fatura;
    }

    public com.mercurio.lms.contasreceber.model.Redeco getRedeco() {
        return this.redeco;
    }

    public void setRedeco(com.mercurio.lms.contasreceber.model.Redeco redeco) {
        this.redeco = redeco;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idRepositorioItemRedeco",
				getIdRepositorioItemRedeco()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RepositorioItemRedeco))
			return false;
        RepositorioItemRedeco castOther = (RepositorioItemRedeco) other;
		return new EqualsBuilder().append(this.getIdRepositorioItemRedeco(),
				castOther.getIdRepositorioItemRedeco()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRepositorioItemRedeco())
            .toHashCode();
    }

	public com.mercurio.lms.contasreceber.model.Recibo getRecibo() {
		return recibo;
	}

	public void setRecibo(com.mercurio.lms.contasreceber.model.Recibo recibo) {
		this.recibo = recibo;
	}

}
