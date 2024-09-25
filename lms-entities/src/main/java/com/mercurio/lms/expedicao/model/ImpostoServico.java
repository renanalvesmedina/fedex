package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ImpostoServico implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Long idImpostoServico;

    /** persistent field */
    private BigDecimal vlImposto;

    /** persistent field */
    private BigDecimal vlBaseCalculo;

    /** persistent field */
    private BigDecimal pcAliquota;

    /** persistent field */
    private DomainValue tpImposto;

    /** persistent field */
    private Boolean blRetencaoTomadorServico;

    /** persistent field */
    private BigDecimal vlBaseEstorno;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.Conhecimento conhecimento;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.NotaFiscalServico notaFiscalServico;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipioByIdMunicipioIncidencia;

    public Long getIdImpostoServico() {
        return this.idImpostoServico;
    }

    public void setIdImpostoServico(Long idImpostoServico) {
        this.idImpostoServico = idImpostoServico;
    }

    public BigDecimal getVlImposto() {
        return this.vlImposto;
    }

    public void setVlImposto(BigDecimal vlImposto) {
        this.vlImposto = vlImposto;
    }

    public BigDecimal getVlBaseCalculo() {
        return this.vlBaseCalculo;
    }

    public void setVlBaseCalculo(BigDecimal vlBaseCalculo) {
        this.vlBaseCalculo = vlBaseCalculo;
    }

    public BigDecimal getPcAliquota() {
        return this.pcAliquota;
    }

    public void setPcAliquota(BigDecimal pcAliquota) {
        this.pcAliquota = pcAliquota;
    }

    public DomainValue getTpImposto() {
        return this.tpImposto;
    }

    public void setTpImposto(DomainValue tpImposto) {
        this.tpImposto = tpImposto;
    }

    public Boolean getBlRetencaoTomadorServico() {
        return this.blRetencaoTomadorServico;
    }

    public void setBlRetencaoTomadorServico(Boolean blRetencaoTomadorServico) {
        this.blRetencaoTomadorServico = blRetencaoTomadorServico;
    }

    public BigDecimal getVlBaseEstorno() {
        return this.vlBaseEstorno;
    }

    public void setVlBaseEstorno(BigDecimal vlBaseEstorno) {
        this.vlBaseEstorno = vlBaseEstorno;
    }

    public com.mercurio.lms.expedicao.model.Conhecimento getConhecimento() {
        return this.conhecimento;
    }

	public void setConhecimento(
			com.mercurio.lms.expedicao.model.Conhecimento conhecimento) {
        this.conhecimento = conhecimento;
    }

    public com.mercurio.lms.expedicao.model.NotaFiscalServico getNotaFiscalServico() {
        return this.notaFiscalServico;
    }

	public void setNotaFiscalServico(
			com.mercurio.lms.expedicao.model.NotaFiscalServico notaFiscalServico) {
        this.notaFiscalServico = notaFiscalServico;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipioByIdMunicipioIncidencia() {
        return this.municipioByIdMunicipioIncidencia;
    }

	public void setMunicipioByIdMunicipioIncidencia(
			com.mercurio.lms.municipios.model.Municipio municipioByIdMunicipioIncidencia) {
        this.municipioByIdMunicipioIncidencia = municipioByIdMunicipioIncidencia;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idImpostoServico",
				getIdImpostoServico()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ImpostoServico))
			return false;
        ImpostoServico castOther = (ImpostoServico) other;
		return new EqualsBuilder().append(this.getIdImpostoServico(),
				castOther.getIdImpostoServico()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdImpostoServico()).toHashCode();
    }

}
