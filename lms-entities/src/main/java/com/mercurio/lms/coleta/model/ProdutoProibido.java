package com.mercurio.lms.coleta.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class ProdutoProibido implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idProdutoProibido;

    /** persistent field */
    private VarcharI18n dsProduto;

    /** persistent field */
    private List restricaoColetas;

    public Long getIdProdutoProibido() {
        return this.idProdutoProibido;
    }

    public void setIdProdutoProibido(Long idProdutoProibido) {
        this.idProdutoProibido = idProdutoProibido;
    }

    public VarcharI18n getDsProduto() {
		return dsProduto;
    }

	public void setDsProduto(VarcharI18n dsProduto) {
        this.dsProduto = dsProduto;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.coleta.model.RestricaoColeta.class)     
    public List getRestricaoColetas() {
        return this.restricaoColetas;
    }

    public void setRestricaoColetas(List restricaoColetas) {
        this.restricaoColetas = restricaoColetas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idProdutoProibido",
				getIdProdutoProibido()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ProdutoProibido))
			return false;
        ProdutoProibido castOther = (ProdutoProibido) other;
		return new EqualsBuilder().append(this.getIdProdutoProibido(),
				castOther.getIdProdutoProibido()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdProdutoProibido())
            .toHashCode();
    }

}
