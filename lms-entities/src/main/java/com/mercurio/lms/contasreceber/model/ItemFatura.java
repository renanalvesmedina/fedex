package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ItemFatura implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idItemFatura;
        
    private Integer versao;    

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Fatura fatura;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.DevedorDocServFat devedorDocServFat;

    /** persistent field - default value false*/
    private Boolean blExcluir = false;

    public Long getIdItemFatura() {
        return this.idItemFatura;
    }

    public void setIdItemFatura(Long idItemFatura) {
        this.idItemFatura = idItemFatura;
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

	public ItemFatura() {
		super();
		setBlExcluir(false);
	}
	
    public String toString() {
		return new ToStringBuilder(this).append("idItemFatura",
				getIdItemFatura()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ItemFatura))
			return false;
        ItemFatura castOther = (ItemFatura) other;
		return new EqualsBuilder().append(this.getIdItemFatura(),
				castOther.getIdItemFatura()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdItemFatura()).toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public Boolean getBlExcluir() {
		return blExcluir;
}

	public void setBlExcluir(Boolean blExcluir) {
		this.blExcluir = blExcluir;
	}

}
