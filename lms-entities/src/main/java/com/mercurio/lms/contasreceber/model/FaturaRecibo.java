package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class FaturaRecibo implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idFaturaRecibo;
    
    /** persistent field */
    private Integer versao; 
    
    /** persistent field */
    private BigDecimal vlCobrado;
    
    /** persistent field */
    private BigDecimal vlJuroRecebido;    

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Fatura fatura;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Recibo recibo;

    public Long getIdFaturaRecibo() {
        return this.idFaturaRecibo;
    }

    public void setIdFaturaRecibo(Long idFaturaRecibo) {
        this.idFaturaRecibo = idFaturaRecibo;
    }

    public com.mercurio.lms.contasreceber.model.Fatura getFatura() {
        return this.fatura;
    }

    public void setFatura(com.mercurio.lms.contasreceber.model.Fatura fatura) {
        this.fatura = fatura;
    }

    public com.mercurio.lms.contasreceber.model.Recibo getRecibo() {
        return this.recibo;
    }

    public void setRecibo(com.mercurio.lms.contasreceber.model.Recibo recibo) {
        this.recibo = recibo;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idFaturaRecibo",
				getIdFaturaRecibo()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FaturaRecibo))
			return false;
        FaturaRecibo castOther = (FaturaRecibo) other;
		return new EqualsBuilder().append(this.getIdFaturaRecibo(),
				castOther.getIdFaturaRecibo()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFaturaRecibo()).toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public BigDecimal getVlCobrado() {
		return vlCobrado;
	}

	public void setVlCobrado(BigDecimal vlCobrado) {
		this.vlCobrado = vlCobrado;
	}

	public BigDecimal getVlJuroRecebido() {
		return vlJuroRecebido;
	}

	public void setVlJuroRecebido(BigDecimal vlJuroRecebido) {
		this.vlJuroRecebido = vlJuroRecebido;
	}

}
