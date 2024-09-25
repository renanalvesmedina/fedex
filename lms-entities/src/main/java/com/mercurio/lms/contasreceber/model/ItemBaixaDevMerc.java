package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ItemBaixaDevMerc implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idItemBaixaDevMerc;

    /** persistent field */
    private Integer versao;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.DevedorDocServFat devedorDocServFat;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.BaixaDevMerc baixaDevMerc;

    public Long getIdItemBaixaDevMerc() {
        return this.idItemBaixaDevMerc;
    }

    public void setIdItemBaixaDevMerc(Long idItemBaixaDevMerc) {
        this.idItemBaixaDevMerc = idItemBaixaDevMerc;
    }

    public Integer getVersao() {
        return this.versao;
    }

    public void setVersao(Integer versao) {
        this.versao = versao;
    }

    public com.mercurio.lms.contasreceber.model.BaixaDevMerc getBaixaDevMerc() {
        return this.baixaDevMerc;
    }

	public void setBaixaDevMerc(
			com.mercurio.lms.contasreceber.model.BaixaDevMerc baixaDevMerc) {
        this.baixaDevMerc = baixaDevMerc;
    }

	public com.mercurio.lms.contasreceber.model.DevedorDocServFat getDevedorDocServFat() {
		return devedorDocServFat;
	}

	public void setDevedorDocServFat(
			com.mercurio.lms.contasreceber.model.DevedorDocServFat devedorDocServFat) {
		this.devedorDocServFat = devedorDocServFat;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idItemBaixaDevMerc",
				getIdItemBaixaDevMerc()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ItemBaixaDevMerc))
			return false;
        ItemBaixaDevMerc castOther = (ItemBaixaDevMerc) other;
		return new EqualsBuilder().append(this.getIdItemBaixaDevMerc(),
				castOther.getIdItemBaixaDevMerc()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdItemBaixaDevMerc())
            .toHashCode();
    }

}
