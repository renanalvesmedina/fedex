package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

/** @author LMS Custom Hibernate CodeGenerator */
public class TarifaBoleto implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTarifaBoleto;

    /** persistent field */
    private BigDecimal vlTarifaBoleto;

    /** persistent field */
    private DateTime dhInclusao;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.OcorrenciaBanco ocorrenciaBanco;

    /** persistent field */
    private Boleto boleto;

    public Long getIdTarifaBoleto() {
        return this.idTarifaBoleto;
    }

    public void setIdTarifaBoleto(Long idTarifaBoleto) {
        this.idTarifaBoleto = idTarifaBoleto;
    }

    public BigDecimal getVlTarifaBoleto() {
        return this.vlTarifaBoleto;
    }

    public void setVlTarifaBoleto(BigDecimal vlTarifaBoleto) {
        this.vlTarifaBoleto = vlTarifaBoleto;
    }

    public DateTime getDhInclusao() {
        return this.dhInclusao;
    }

    public void setDhInclusao(DateTime dhInclusao) {
        this.dhInclusao = dhInclusao;
    }

    public com.mercurio.lms.contasreceber.model.OcorrenciaBanco getOcorrenciaBanco() {
        return this.ocorrenciaBanco;
    }

	public void setOcorrenciaBanco(
			com.mercurio.lms.contasreceber.model.OcorrenciaBanco ocorrenciaBanco) {
        this.ocorrenciaBanco = ocorrenciaBanco;
    }

    public Boleto getBoleto() {
        return this.boleto;
    }

    public void setBoleto(Boleto boleto) {
        this.boleto = boleto;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTarifaBoleto",
				getIdTarifaBoleto()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TarifaBoleto))
			return false;
        TarifaBoleto castOther = (TarifaBoleto) other;
		return new EqualsBuilder().append(this.getIdTarifaBoleto(),
				castOther.getIdTarifaBoleto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTarifaBoleto()).toHashCode();
    }

}
