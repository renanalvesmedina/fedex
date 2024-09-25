package com.mercurio.lms.sgr.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mercurio.lms.expedicao.model.NaturezaProduto;

public class FaixaValorNaturezaImpedida implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idFaixaValorNatureza;
	@JsonIgnore
	private NaturezaProduto naturezaProduto;
	private BigDecimal vlLimitePermitido;
	@JsonIgnore
	private FaixaDeValor faixaDeValor;

	public Long getIdFaixaValorNatureza() {
		return idFaixaValorNatureza;
	}

	public void setIdFaixaValorNatureza(Long idFaixaValorNatureza) {
		this.idFaixaValorNatureza = idFaixaValorNatureza;
	}

	public NaturezaProduto getNaturezaProduto() {
		return naturezaProduto;
	}

	public void setNaturezaProduto(NaturezaProduto naturezaProduto) {
		this.naturezaProduto = naturezaProduto;
	}

	public BigDecimal getVlLimitePermitido() {
		return vlLimitePermitido;
	}

	public void setVlLimitePermitido(BigDecimal vlLimitePermitido) {
		this.vlLimitePermitido = vlLimitePermitido;
	}

	public FaixaDeValor getFaixaDeValor() {
		return faixaDeValor;
	}
	
	public void setFaixaDeValor(FaixaDeValor faixaDeValor) {
		this.faixaDeValor = faixaDeValor;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append(idFaixaValorNatureza)
				.toString();
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof FaixaValorNaturezaImpedida)) {
			return false;
		}
		FaixaValorNaturezaImpedida cast = (FaixaValorNaturezaImpedida) other;
		return new EqualsBuilder()
				.append(idFaixaValorNatureza, cast.idFaixaValorNatureza)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(idFaixaValorNatureza)
				.toHashCode();
	}
	
}
