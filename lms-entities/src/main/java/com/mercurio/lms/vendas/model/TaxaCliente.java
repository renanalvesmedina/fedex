package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.Collator;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class TaxaCliente implements Serializable, Comparable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTaxaCliente;

    /** persistent field */
    private DomainValue tpTaxaIndicador;

    /** persistent field */
    private BigDecimal vlTaxa;

    /** nullable persistent field */
    private BigDecimal psMinimo;

    /** nullable persistent field */
    private BigDecimal vlExcedente;

    /** nullable persistent field */
    private BigDecimal pcReajTaxa;

    /** nullable persistent field */
    private BigDecimal pcReajVlExcedente;
    
    private String dsSimbolo;

    /** persistent field */
    private com.mercurio.lms.vendas.model.ParametroCliente parametroCliente;

    /** persistent field */
    private com.mercurio.lms.tabelaprecos.model.ParcelaPreco parcelaPreco;

    private Boolean blAlterou = false;
    
    public Long getIdTaxaCliente() {
        return this.idTaxaCliente;
    }

    public void setIdTaxaCliente(Long idTaxaCliente) {
        this.idTaxaCliente = idTaxaCliente;
    }

    public DomainValue getTpTaxaIndicador() {
        return this.tpTaxaIndicador;
    }

    public void setTpTaxaIndicador(DomainValue tpTaxaIndicador) {
        this.tpTaxaIndicador = tpTaxaIndicador;
    }

    public BigDecimal getVlTaxa() {
        return this.vlTaxa;
    }
    
    public String getValor(){
    	if(getTpTaxaIndicador() != null) {
			NumberFormat form = new DecimalFormat("##,###,###,###,##0.00",
					new DecimalFormatSymbols(LocaleContextHolder.getLocale()));
    		String indicador = getTpTaxaIndicador().getValue(); 
    		if("T".equals(indicador))
    			return form.format(0);
    		else if(!"V".equals(indicador))
    			return form.format(this.vlTaxa) + "%";
    		else
    			return  getDsSimbolo() + " " + form.format(this.vlTaxa);
    	}
        return null;
    }
    
    public void setVlTaxa(BigDecimal vlTaxa) {
        this.vlTaxa = vlTaxa;
    }

    public BigDecimal getPsMinimo() {
        return this.psMinimo;
    }

    public void setPsMinimo(BigDecimal psMinimo) {
        this.psMinimo = psMinimo;
    }

    public BigDecimal getVlExcedente() {
        return this.vlExcedente;
    }

    public void setVlExcedente(BigDecimal vlExcedente) {
        this.vlExcedente = vlExcedente;
    }

    public BigDecimal getPcReajTaxa() {
        return this.pcReajTaxa;
    }

    public void setPcReajTaxa(BigDecimal pcReajTaxa) {
        this.pcReajTaxa = pcReajTaxa;
    }

    public BigDecimal getPcReajVlExcedente() {
        return this.pcReajVlExcedente;
    }

    public void setPcReajVlExcedente(BigDecimal pcReajVlExcedente) {
        this.pcReajVlExcedente = pcReajVlExcedente;
    }

    public com.mercurio.lms.vendas.model.ParametroCliente getParametroCliente() {
        return this.parametroCliente;
    }

	public void setParametroCliente(
			com.mercurio.lms.vendas.model.ParametroCliente parametroCliente) {
        this.parametroCliente = parametroCliente;
    }

    public com.mercurio.lms.tabelaprecos.model.ParcelaPreco getParcelaPreco() {
        return this.parcelaPreco;
    }

	public void setParcelaPreco(
			com.mercurio.lms.tabelaprecos.model.ParcelaPreco parcelaPreco) {
        this.parcelaPreco = parcelaPreco;
    }

    public String getDsSimbolo() {
    	if(getTpTaxaIndicador() != null) {
    		String indicador = getTpTaxaIndicador().getValue(); 
    		if(!"V".equals(indicador))
    			return "";
    	}
    	return dsSimbolo;
	}

	public void setDsSimbolo(String dsSimbolo) {
		this.dsSimbolo = dsSimbolo;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idTaxaCliente",
				getIdTaxaCliente()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TaxaCliente))
			return false;
        TaxaCliente castOther = (TaxaCliente) other;
		return new EqualsBuilder().append(this.getIdTaxaCliente(),
				castOther.getIdTaxaCliente()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTaxaCliente()).toHashCode();
    }

    public int compareTo(Object o) {
		Collator collator = Collator.getInstance(LocaleContextHolder
				.getLocale());
		TaxaCliente t = (TaxaCliente) o;
		return collator
				.compare(this.getParcelaPreco().getNmParcelaPreco().getValue(),
				t.getParcelaPreco().getNmParcelaPreco().getValue());
	}
    
	public Boolean getBlAlterou() {
		return blAlterou;
}

	public void setBlAlterou(Boolean blAlterou) {
		this.blAlterou = blAlterou;
	}
    
}
