package com.mercurio.lms.tributos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.joda.time.YearMonthDay;

import com.mercurio.lms.municipios.model.UnidadeFederativa;

/** @author LMS Custom Hibernate CodeGenerator */
public class ParametroSubstituicaoTrib implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idParametroSubstituicaoTrib;

    /** persistent field */
    private Boolean blEmbuteICMSParcelas;

    /** persistent field */
    private Boolean blImpDadosCalcCTRC;
    
    /** persistent field */
    private Boolean blAplicarClientesEspeciais;

    /** persistent field */
    private BigDecimal pcRetencao;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;
    
    private UnidadeFederativa unidadeFederativa;

    /** persistent field */
    private Boolean blImprimeMemoCalcCte;


	public Boolean getBlEmbuteICMSParcelas() {
		return blEmbuteICMSParcelas;
	}

	public void setBlEmbuteICMSParcelas(Boolean blEmbuteICMSParcelas) {
		this.blEmbuteICMSParcelas = blEmbuteICMSParcelas;
	}

	public Boolean getBlImpDadosCalcCTRC() {
		return blImpDadosCalcCTRC;
	}

	public void setBlImpDadosCalcCTRC(Boolean blImpDadosCalcCTRC) {
		this.blImpDadosCalcCTRC = blImpDadosCalcCTRC;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public Long getIdParametroSubstituicaoTrib() {
		return idParametroSubstituicaoTrib;
	}

	public void setIdParametroSubstituicaoTrib(Long idParametroSubstituicaoTrib) {
		this.idParametroSubstituicaoTrib = idParametroSubstituicaoTrib;
	}

	public BigDecimal getPcRetencao() {
		return pcRetencao;
	}

	public void setPcRetencao(BigDecimal pcRetencao) {
		this.pcRetencao = pcRetencao;
	}

	public UnidadeFederativa getUnidadeFederativa() {
		return unidadeFederativa;
	}

	public void setUnidadeFederativa(UnidadeFederativa unidadeFederativa) {
		this.unidadeFederativa = unidadeFederativa;
	}

	public Boolean getBlAplicarClientesEspeciais() {
		return blAplicarClientesEspeciais;
	}

	public void setBlAplicarClientesEspeciais(Boolean blAplicarClientesEspeciais) {
		this.blAplicarClientesEspeciais = blAplicarClientesEspeciais;
	}

	public Boolean getBlImprimeMemoCalcCte() {
		return blImprimeMemoCalcCte;
}

	public void setBlImprimeMemoCalcCte(Boolean blImprimeMemoCalcCte) {
		this.blImprimeMemoCalcCte = blImprimeMemoCalcCte;
	}

}
