package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ValorCusto implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idValorCusto;

    /** persistent field */
    private BigDecimal vlCusto;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.DoctoServico doctoServico;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.TipoCusto tipoCusto;

    public Long getIdValorCusto() {
        return this.idValorCusto;
    }

    public void setIdValorCusto(Long idValorCusto) {
        this.idValorCusto = idValorCusto;
    }

    public BigDecimal getVlCusto() {
        return this.vlCusto;
    }

    public void setVlCusto(BigDecimal vlCusto) {
        this.vlCusto = vlCusto;
    }

    public com.mercurio.lms.expedicao.model.DoctoServico getDoctoServico() {
        return this.doctoServico;
    }

	public void setDoctoServico(
			com.mercurio.lms.expedicao.model.DoctoServico doctoServico) {
        this.doctoServico = doctoServico;
    }

    public com.mercurio.lms.expedicao.model.TipoCusto getTipoCusto() {
        return this.tipoCusto;
    }

	public void setTipoCusto(
			com.mercurio.lms.expedicao.model.TipoCusto tipoCusto) {
        this.tipoCusto = tipoCusto;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idValorCusto",
				getIdValorCusto()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ValorCusto))
			return false;
        ValorCusto castOther = (ValorCusto) other;
		return new EqualsBuilder().append(this.getIdValorCusto(),
				castOther.getIdValorCusto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdValorCusto()).toHashCode();
    }

}
