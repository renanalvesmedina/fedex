package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class TipoCusto implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoCusto;

    /** persistent field */
    private VarcharI18n dsTipoCusto;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List valorCustos;

    public Long getIdTipoCusto() {
        return this.idTipoCusto;
    }

    public void setIdTipoCusto(Long idTipoCusto) {
        this.idTipoCusto = idTipoCusto;
    }

    public VarcharI18n getDsTipoCusto() {
		return dsTipoCusto;
    }

	public void setDsTipoCusto(VarcharI18n dsTipoCusto) {
        this.dsTipoCusto = dsTipoCusto;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.ValorCusto.class)     
    public List getValorCustos() {
        return this.valorCustos;
    }

    public void setValorCustos(List valorCustos) {
        this.valorCustos = valorCustos;
    }

    public String toString() {
        return new ToStringBuilder(this)
				.append("idTipoCusto", getIdTipoCusto()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoCusto))
			return false;
        TipoCusto castOther = (TipoCusto) other;
		return new EqualsBuilder().append(this.getIdTipoCusto(),
				castOther.getIdTipoCusto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoCusto()).toHashCode();
    }

}
