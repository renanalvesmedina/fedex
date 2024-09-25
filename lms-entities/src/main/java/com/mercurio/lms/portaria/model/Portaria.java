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
public class Portaria implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPortaria;

    /** persistent field */
    private Byte nrPortaria;

    /** persistent field */
    private String dsPortaria;

    /** persistent field */
    private DomainValue tpFuncao;

    /** persistent field */
    private Boolean blPadraoFilial;
    
    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.portaria.model.Terminal terminal;

    /** persistent field */
    private List controleEntSaidaTerceiros;

    public Long getIdPortaria() {
        return this.idPortaria;
    }

    public void setIdPortaria(Long idPortaria) {
        this.idPortaria = idPortaria;
    }

    public Byte getNrPortaria() {
        return this.nrPortaria;
    }

    public void setNrPortaria(Byte nrPortaria) {
        this.nrPortaria = nrPortaria;
    }

    public String getDsPortaria() {
        return this.dsPortaria;
    }

    public void setDsPortaria(String dsPortaria) {
        this.dsPortaria = dsPortaria;
    }

    public DomainValue getTpFuncao() {
        return this.tpFuncao;
    }

    public void setTpFuncao(DomainValue tpFuncao) {
        this.tpFuncao = tpFuncao;
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

    public com.mercurio.lms.portaria.model.Terminal getTerminal() {
        return this.terminal;
    }

    public void setTerminal(com.mercurio.lms.portaria.model.Terminal terminal) {
        this.terminal = terminal;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.portaria.model.ControleEntSaidaTerceiro.class)     
    public List getControleEntSaidaTerceiros() {
        return this.controleEntSaidaTerceiros;
    }

    public void setControleEntSaidaTerceiros(List controleEntSaidaTerceiros) {
        this.controleEntSaidaTerceiros = controleEntSaidaTerceiros;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idPortaria", getIdPortaria())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Portaria))
			return false;
        Portaria castOther = (Portaria) other;
		return new EqualsBuilder().append(this.getIdPortaria(),
				castOther.getIdPortaria()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPortaria()).toHashCode();
    }

	public Boolean getBlPadraoFilial() {
		return blPadraoFilial;
	}

	public void setBlPadraoFilial(Boolean blPadraoFilial) {
		this.blPadraoFilial = blPadraoFilial;
	}

}
