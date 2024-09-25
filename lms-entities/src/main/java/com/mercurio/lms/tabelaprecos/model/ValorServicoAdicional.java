package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ValorServicoAdicional implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idValorServicoAdicional;

    /** persistent field */
    private BigDecimal vlServico;

    /** nullable persistent field */
    private BigDecimal vlMinimo;

    /** nullable persistent field */
    private com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela tabelaPrecoParcela;

    public Long getIdValorServicoAdicional() {
        return this.idValorServicoAdicional;
    }

    public void setIdValorServicoAdicional(Long idValorServicoAdicional) {
        this.idValorServicoAdicional = idValorServicoAdicional;
    }

    public BigDecimal getVlServico() {
        return this.vlServico;
    }

    public void setVlServico(BigDecimal vlServico) {
        this.vlServico = vlServico;
    }

    public BigDecimal getVlMinimo() {
        return this.vlMinimo;
    }

    public void setVlMinimo(BigDecimal vlMinimo) {
        this.vlMinimo = vlMinimo;
    }

    public com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela getTabelaPrecoParcela() {
        return this.tabelaPrecoParcela;
    }

	public void setTabelaPrecoParcela(
			com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela tabelaPrecoParcela) {
        this.tabelaPrecoParcela = tabelaPrecoParcela;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idValorServicoAdicional",
				getIdValorServicoAdicional()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ValorServicoAdicional))
			return false;
        ValorServicoAdicional castOther = (ValorServicoAdicional) other;
		return new EqualsBuilder().append(this.getIdValorServicoAdicional(),
				castOther.getIdValorServicoAdicional()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdValorServicoAdicional())
            .toHashCode();
    }

}
