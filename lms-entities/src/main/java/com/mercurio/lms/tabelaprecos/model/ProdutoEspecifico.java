package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class ProdutoEspecifico implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Long idProdutoEspecifico;

    /** persistent field */
    private Short nrTarifaEspecifica;

    /** persistent field */
    private VarcharI18n dsProdutoEspecifico;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private BigDecimal psMinimo;

    /** persistent field */
    private List faixaProgressivas;

    public Long getIdProdutoEspecifico() {
        return this.idProdutoEspecifico;
    }

    public void setIdProdutoEspecifico(Long idProdutoEspecifico) {
        this.idProdutoEspecifico = idProdutoEspecifico;
    }

    public Short getNrTarifaEspecifica() {
        return this.nrTarifaEspecifica;
    }

    public void setNrTarifaEspecifica(Short nrTarifaEspecifica) {
        this.nrTarifaEspecifica = nrTarifaEspecifica;
    }

    public VarcharI18n getDsProdutoEspecifico() {
		return dsProdutoEspecifico;
    }

	public void setDsProdutoEspecifico(VarcharI18n dsProdutoEspecifico) {
        this.dsProdutoEspecifico = dsProdutoEspecifico;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public BigDecimal getPsMinimo() {
        return this.psMinimo;
    }

    public void setPsMinimo(BigDecimal psMinimo) {
        this.psMinimo = psMinimo;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.tabelaprecos.model.FaixaProgressiva.class)     
    public List getFaixaProgressivas() {
        return this.faixaProgressivas;
    }

    public void setFaixaProgressivas(List faixaProgressivas) {
        this.faixaProgressivas = faixaProgressivas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idProdutoEspecifico",
				getIdProdutoEspecifico()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ProdutoEspecifico))
			return false;
        ProdutoEspecifico castOther = (ProdutoEspecifico) other;
		return new EqualsBuilder().append(this.getIdProdutoEspecifico(),
				castOther.getIdProdutoEspecifico()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdProdutoEspecifico())
            .toHashCode();
    }

}
