package com.mercurio.lms.portaria.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class Doca implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDoca;

    /** persistent field */
    private Short nrDoca;
 
    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** persistent field */
    private DomainValue tpSituacaoDoca;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** nullable persistent field */
    private String dsDoca;

    /** nullable persistent field */
    private String obDoca;

    /** persistent field */
    private com.mercurio.lms.portaria.model.Terminal terminal;

    /** persistent field */
    private List boxs;

    public Long getIdDoca() {
        return this.idDoca;
    }

    public void setIdDoca(Long idDoca) {
        this.idDoca = idDoca;
    }

    public Short getNrDoca() {
        return this.nrDoca;
    }
    
    public String getNumeroDescricaoDoca(){
    	return this.nrDoca + ((this.dsDoca != null) ? " - " + this.dsDoca : "");
    }

    public void setNrDoca(Short nrDoca) {
        this.nrDoca = nrDoca;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public DomainValue getTpSituacaoDoca() {
        return this.tpSituacaoDoca;
    }

    public void setTpSituacaoDoca(DomainValue tpSituacaoDoca) {
        this.tpSituacaoDoca = tpSituacaoDoca;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public String getDsDoca() {
        return this.dsDoca;
    }

    public void setDsDoca(String dsDoca) {
        this.dsDoca = dsDoca;
    }

    public String getObDoca() {
        return this.obDoca;
    }

    public void setObDoca(String obDoca) {
        this.obDoca = obDoca;
    }

    public com.mercurio.lms.portaria.model.Terminal getTerminal() {
        return this.terminal;
    }

    public void setTerminal(com.mercurio.lms.portaria.model.Terminal terminal) {
        this.terminal = terminal;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.portaria.model.Box.class)     
    public List getBoxs() {
        return this.boxs;
    }

    public void setBoxs(List boxs) {
        this.boxs = boxs;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDoca", getIdDoca())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Doca))
			return false;
        Doca castOther = (Doca) other;
		return new EqualsBuilder().append(this.getIdDoca(),
				castOther.getIdDoca()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDoca()).toHashCode();
    }

}
