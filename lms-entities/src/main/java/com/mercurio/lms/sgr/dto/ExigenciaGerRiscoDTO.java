package com.mercurio.lms.sgr.dto;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.sgr.model.EnquadramentoRegra;
import com.mercurio.lms.sgr.model.ExigenciaFaixaValor;
import com.mercurio.lms.sgr.model.ExigenciaGerRisco;
import com.mercurio.lms.sgr.model.TipoExigenciaGerRisco;

/**
 * LMS-6850 - Data Transfer Object para resultado da busca e resolução de
 * exigências do Plano de Gerenciamento de Riscos.
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 * 
 */
public class ExigenciaGerRiscoDTO implements Comparable<ExigenciaGerRiscoDTO> {

	private EnquadramentoRegra enquadramentoRegra;
	private ExigenciaGerRisco exigenciaGerRisco;
	private TipoExigenciaGerRisco tipoExigenciaGerRisco;
	private Integer qtExigida;
	private Filial filialInicio;
	private Integer vlKmFranquia;

	public ExigenciaGerRiscoDTO() {
		super();
	}

	public ExigenciaGerRiscoDTO(
			EnquadramentoRegra enquadramentoRegra, ExigenciaGerRisco exigenciaGerRisco, TipoExigenciaGerRisco tipoExigenciaGerRisco,
			Integer qtExigida, Filial filialInicio, Integer vlKmFranquia) {
		this();
		this.enquadramentoRegra = enquadramentoRegra;
		this.exigenciaGerRisco = exigenciaGerRisco;
		this.tipoExigenciaGerRisco = tipoExigenciaGerRisco;
		this.qtExigida = qtExigida;
		this.filialInicio = filialInicio;
		this.vlKmFranquia = vlKmFranquia;
	}

	public ExigenciaGerRiscoDTO(ExigenciaFaixaValor exigenciaFaixaValor) {
		this(
				exigenciaFaixaValor.getFaixaDeValor().getEnquadramentoRegra(),
				exigenciaFaixaValor.getExigenciaGerRisco(),
				exigenciaFaixaValor.getExigenciaGerRisco().getTipoExigenciaGerRisco(),
				exigenciaFaixaValor.getQtExigida(),
				exigenciaFaixaValor.getFilialInicio(),
				exigenciaFaixaValor.getVlKmFranquia());
	}

	public ExigenciaGerRiscoDTO(ExigenciaGerRiscoDTO exigenciaGerRiscoDTO, Integer qtExigida) {
		this(
				null,
				exigenciaGerRiscoDTO.exigenciaGerRisco,
				exigenciaGerRiscoDTO.tipoExigenciaGerRisco,
				qtExigida,
				exigenciaGerRiscoDTO.filialInicio,
				exigenciaGerRiscoDTO.vlKmFranquia);
	}

	public EnquadramentoRegra getEnquadramentoRegra() {
		return enquadramentoRegra;
	}

	public void setEnquadramentoRegra(EnquadramentoRegra enquadramentoRegra) {
		this.enquadramentoRegra = enquadramentoRegra;
	}

	public ExigenciaGerRisco getExigenciaGerRisco() {
		return exigenciaGerRisco;
	}

	public void setExigenciaGerRisco(ExigenciaGerRisco exigenciaGerRisco) {
		this.exigenciaGerRisco = exigenciaGerRisco;
	}

	public TipoExigenciaGerRisco getTipoExigenciaGerRisco() {
		return tipoExigenciaGerRisco;
	}

	public void setTipoExigenciaGerRisco(TipoExigenciaGerRisco tipoExigenciaGerRisco) {
		this.tipoExigenciaGerRisco = tipoExigenciaGerRisco;
	}

	public Integer getQtExigida() {
		return qtExigida != null ? qtExigida : 0;
	}

	public void setQtExigida(Integer qtExigida) {
		this.qtExigida = qtExigida;
	}

	public Filial getFilialInicio() {
		return filialInicio;
	}

	public void setFilialInicio(Filial filialInicio) {
		this.filialInicio = filialInicio;
	}

	public Integer getVlKmFranquia() {
		return vlKmFranquia != null ? vlKmFranquia : 0;
	}

	public void setVlKmFranquia(Integer vlKmFranquia) {
		this.vlKmFranquia = vlKmFranquia;
	}

	@Override
	public String toString() {
		ToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
		return new ToStringBuilder(this)
				.append(enquadramentoRegra)
				.append(exigenciaGerRisco)
				.append(qtExigida)
				.append(filialInicio != null ? filialInicio.getSgFilial() : null)
				.append(vlKmFranquia)
				.toString();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof ExigenciaGerRiscoDTO)) {
			return false;
		}
		ExigenciaGerRiscoDTO cast = (ExigenciaGerRiscoDTO) other;
		return new EqualsBuilder()
				.append(enquadramentoRegra, cast.enquadramentoRegra)
				.append(exigenciaGerRisco, cast.exigenciaGerRisco)
				.append(qtExigida, cast.qtExigida)
				.append(filialInicio, cast.filialInicio)
				.append(vlKmFranquia, cast.vlKmFranquia)
				.isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(enquadramentoRegra)
			.append(exigenciaGerRisco)
			.append(qtExigida)
			.append(filialInicio)
			.append(vlKmFranquia)
			.toHashCode();
	}

	@Override
	public int compareTo(ExigenciaGerRiscoDTO other) {
		return new CompareToBuilder()
				.append(tipoExigenciaGerRisco.getTpExigencia().getOrder(), other.tipoExigenciaGerRisco.getTpExigencia().getOrder())
				.append(exigenciaGerRisco.getDsResumida().getValue(), other.exigenciaGerRisco.getDsResumida().getValue())
				.toComparison();
	}

	public boolean equalsEnquadramentoRegra(ExigenciaGerRiscoDTO other) {
		return new EqualsBuilder()
				.append(enquadramentoRegra, other.enquadramentoRegra)
				.isEquals();
	}

	public boolean equalsTipoExigenciaGerRisco(ExigenciaGerRiscoDTO other) {
		return new EqualsBuilder()
				.append(tipoExigenciaGerRisco, other.tipoExigenciaGerRisco)
				.isEquals();
	}

	public boolean equalsExigenciaGerRisco(ExigenciaGerRiscoDTO other) {
		return new EqualsBuilder()
				.append(exigenciaGerRisco, other.exigenciaGerRisco)
				.isEquals();
	}

	public boolean equalsFilialInicio(ExigenciaGerRiscoDTO other) {
		return new EqualsBuilder()
				.append(filialInicio, other.filialInicio)
				.isEquals();
	}

}
