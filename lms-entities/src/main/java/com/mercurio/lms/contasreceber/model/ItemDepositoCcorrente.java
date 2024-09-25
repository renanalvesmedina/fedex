package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ItemDepositoCcorrente implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idItemDepositoCcorrente;
    
    private Integer versao;     

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Fatura fatura;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.DevedorDocServFat devedorDocServFat;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.DepositoCcorrente depositoCcorrente;

    public Long getIdItemDepositoCcorrente() {
        return this.idItemDepositoCcorrente;
    }

    public void setIdItemDepositoCcorrente(Long idItemDepositoCcorrente) {
        this.idItemDepositoCcorrente = idItemDepositoCcorrente;
    }

    public com.mercurio.lms.contasreceber.model.Fatura getFatura() {
        return this.fatura;
    }

    public void setFatura(com.mercurio.lms.contasreceber.model.Fatura fatura) {
        this.fatura = fatura;
    }

    public com.mercurio.lms.contasreceber.model.DevedorDocServFat getDevedorDocServFat() {
        return this.devedorDocServFat;
    }

	public void setDevedorDocServFat(
			com.mercurio.lms.contasreceber.model.DevedorDocServFat devedorDocServFat) {
        this.devedorDocServFat = devedorDocServFat;
    }

    public com.mercurio.lms.contasreceber.model.DepositoCcorrente getDepositoCcorrente() {
        return this.depositoCcorrente;
    }

	public void setDepositoCcorrente(
			com.mercurio.lms.contasreceber.model.DepositoCcorrente depositoCcorrente) {
        this.depositoCcorrente = depositoCcorrente;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idItemDepositoCcorrente",
				getIdItemDepositoCcorrente()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ItemDepositoCcorrente))
			return false;
        ItemDepositoCcorrente castOther = (ItemDepositoCcorrente) other;
		return new EqualsBuilder().append(this.getIdItemDepositoCcorrente(),
				castOther.getIdItemDepositoCcorrente()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdItemDepositoCcorrente())
            .toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

}
