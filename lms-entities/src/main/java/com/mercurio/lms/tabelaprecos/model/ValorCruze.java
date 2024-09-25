package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class ValorCruze implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idValorCruze;

    /** persistent field */
    private BigDecimal nrFaixaInicialPeso;

    /** persistent field */
    private BigDecimal nrFaixaFinalPeso;

    /** persistent field */
    private BigDecimal vlCruze;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Empresa empresa;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    public Long getIdValorCruze() {
        return this.idValorCruze;
    }

    public void setIdValorCruze(Long idValorCruze) {
        this.idValorCruze = idValorCruze;
    }

    public BigDecimal getNrFaixaInicialPeso() {
        return this.nrFaixaInicialPeso;
    }

    public void setNrFaixaInicialPeso(BigDecimal nrFaixaInicialPeso) {
        this.nrFaixaInicialPeso = nrFaixaInicialPeso;
    }

    public BigDecimal getNrFaixaFinalPeso() {
        return this.nrFaixaFinalPeso;
    }

    public void setNrFaixaFinalPeso(BigDecimal nrFaixaFinalPeso) {
        this.nrFaixaFinalPeso = nrFaixaFinalPeso;
    }

    public BigDecimal getVlCruze() {
        return this.vlCruze;
    }

    public void setVlCruze(BigDecimal vlCruze) {
        this.vlCruze = vlCruze;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public com.mercurio.lms.municipios.model.Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(com.mercurio.lms.municipios.model.Empresa empresa) {
        this.empresa = empresa;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idValorCruze",
				getIdValorCruze()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ValorCruze))
			return false;
        ValorCruze castOther = (ValorCruze) other;
		return new EqualsBuilder().append(this.getIdValorCruze(),
				castOther.getIdValorCruze()).isEquals();
    }

    public boolean equals(ValorCruze rp){
		return rp != null
				&& this.getIdValorCruze().equals(rp.getIdValorCruze());
    }
    
    public int hashCode() {
		return new HashCodeBuilder().append(getIdValorCruze()).toHashCode();
    }

}
