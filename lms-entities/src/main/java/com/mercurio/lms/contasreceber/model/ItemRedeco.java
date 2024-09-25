package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ItemRedeco implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idItemRedeco;
    
    private Integer versao;    

    /** persistent field */
    private BigDecimal vlTarifa;

    /** persistent field */
    private BigDecimal vlJuros;

    /** nullable persistent field */
    private String obItemRedeco;
    
    /** nullable persistent field */
    private BigDecimal vlDiferencaCambialCotacao;    

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Fatura fatura;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Recibo recibo;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Redeco redeco;

    public Long getIdItemRedeco() {
        return this.idItemRedeco;
    }

    public void setIdItemRedeco(Long idItemRedeco) {
        this.idItemRedeco = idItemRedeco;
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

    public String getObItemRedeco() {
        return this.obItemRedeco;
    }

    public void setObItemRedeco(String obItemRedeco) {
        this.obItemRedeco = obItemRedeco;
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

    public com.mercurio.lms.contasreceber.model.Redeco getRedeco() {
        return this.redeco;
    }

    public void setRedeco(com.mercurio.lms.contasreceber.model.Redeco redeco) {
        this.redeco = redeco;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idItemRedeco",
				getIdItemRedeco()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ItemRedeco))
			return false;
        ItemRedeco castOther = (ItemRedeco) other;
		return new EqualsBuilder().append(this.getIdItemRedeco(),
				castOther.getIdItemRedeco()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdItemRedeco()).toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public BigDecimal getVlDiferencaCambialCotacao() {
		return vlDiferencaCambialCotacao;
	}

	public void setVlDiferencaCambialCotacao(
			BigDecimal vlDiferencaCambialCotacao) {
		this.vlDiferencaCambialCotacao = vlDiferencaCambialCotacao;
	}

}
