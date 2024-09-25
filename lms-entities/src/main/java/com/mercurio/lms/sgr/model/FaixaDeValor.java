package com.mercurio.lms.sgr.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

public class FaixaDeValor implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idFaixaDeValor;
	private BigDecimal vlLimiteMinimo;
	private Boolean blRequerLiberacaoCemop;
	private BigDecimal vlLimiteMaximo;
	@JsonIgnore
	private EnquadramentoRegra enquadramentoRegra;
	@JsonIgnore
	private List<ExigenciaFaixaValor> exigenciaFaixaValors;

	// LMS-6846
	private Boolean blExclusivaAeroporto;
	// LMS-6848
	private List<FaixaValorNaturezaImpedida> naturezasImpedidas;
	// LMS-7254
	private Boolean blExclusivaCliente;

	public Long getIdFaixaDeValor() {
		return idFaixaDeValor;
	}

	public void setIdFaixaDeValor(Long idFaixaDeValor) {
		this.idFaixaDeValor = idFaixaDeValor;
	}

	public BigDecimal getVlLimiteMinimo() {
		return vlLimiteMinimo;
	}

	public void setVlLimiteMinimo(BigDecimal vlLimiteMinimo) {
		this.vlLimiteMinimo = vlLimiteMinimo;
	}

	public Boolean getBlRequerLiberacaoCemop() {
		return blRequerLiberacaoCemop;
	}

	public void setBlRequerLiberacaoCemop(Boolean blRequerLiberacaoCemop) {
		this.blRequerLiberacaoCemop = blRequerLiberacaoCemop;
	}

	public BigDecimal getVlLimiteMaximo() {
		return vlLimiteMaximo;
	}

	public void setVlLimiteMaximo(BigDecimal vlLimiteMaximo) {
		this.vlLimiteMaximo = vlLimiteMaximo;
	}

	public EnquadramentoRegra getEnquadramentoRegra() {
		return enquadramentoRegra;
	}

	public void setEnquadramentoRegra(EnquadramentoRegra enquadramentoRegra) {
		this.enquadramentoRegra = enquadramentoRegra;
	}

	@ParametrizedAttribute(type = ExigenciaFaixaValor.class)
	public List<ExigenciaFaixaValor> getExigenciaFaixaValors() {
		return exigenciaFaixaValors;
	}

	public void setExigenciaFaixaValors(List<ExigenciaFaixaValor> exigenciaFaixaValors) {
		this.exigenciaFaixaValors = exigenciaFaixaValors;
	}

	public Boolean getBlExclusivaAeroporto() {
		return blExclusivaAeroporto;
	}

	public void setBlExclusivaAeroporto(Boolean blExclusivaAeroporto) {
		this.blExclusivaAeroporto = blExclusivaAeroporto;
	}

	@ParametrizedAttribute(type = FaixaValorNaturezaImpedida.class)
	public List<FaixaValorNaturezaImpedida> getNaturezasImpedidas() {
		return naturezasImpedidas;
	}

	public void setNaturezasImpedidas(List<FaixaValorNaturezaImpedida> naturezasImpedidas) {
		this.naturezasImpedidas = naturezasImpedidas;
	}

	public Boolean getBlExclusivaCliente() {
		return blExclusivaCliente;
	}

	public void setBlExclusivaCliente(Boolean blExclusivaCliente) {
		this.blExclusivaCliente = blExclusivaCliente;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append(idFaixaDeValor)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof FaixaDeValor)) {
			return false;
		}
		FaixaDeValor cast = (FaixaDeValor) other;
		return new EqualsBuilder()
				.append(idFaixaDeValor, cast.idFaixaDeValor)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idFaixaDeValor)
				.toHashCode();
	}

}
