package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class CargoOperacional implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idCargoOperacional;

    /** persistent field */
    private String dsCargo;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List integrantesEquipe;
    
    /** persistent field */
    private List integrantesEqOperac;

    public CargoOperacional() {
    }

    public CargoOperacional(Long idCargoOperacional, String dsCargo, DomainValue tpSituacao, List integrantesEquipe, List integrantesEqOperac) {
        this.idCargoOperacional = idCargoOperacional;
        this.dsCargo = dsCargo;
        this.tpSituacao = tpSituacao;
        this.integrantesEquipe = integrantesEquipe;
        this.integrantesEqOperac = integrantesEqOperac;
    }

    public String getDsCargo() {
        return dsCargo;
    }

    public void setDsCargo(String dsCargo) {
        this.dsCargo = dsCargo;
    }

    public Long getIdCargoOperacional() {
        return idCargoOperacional;
    }

    public void setIdCargoOperacional(Long idCargoOperacional) {
        this.idCargoOperacional = idCargoOperacional;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.IntegranteEquipe.class)     
    public List getIntegrantesEquipe() {
        return integrantesEquipe;
    }

    public void setIntegrantesEquipe(List integrantesEquipe) {
        this.integrantesEquipe = integrantesEquipe;
    }
    
    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.IntegranteEqOperac.class)     
    public List getIntegrantesEqOperac() {
        return integrantesEqOperac;
    }

    /**
	 * @param integrantesEqOperac
	 *            The integrantesEqOperac to set.
     */
    public void setIntegrantesEqOperac(List integrantesEqOperac) {
        this.integrantesEqOperac = integrantesEqOperac;
    }
    
    public DomainValue getTpSituacao() {
        return tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }
    
    public String toString() {
		return new ToStringBuilder(this).append("idCargoOperacional",
				getIdCargoOperacional()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof CargoOperacional))
			return false;
        CargoOperacional castOther = (CargoOperacional) other;
		return new EqualsBuilder().append(this.getIdCargoOperacional(),
				castOther.getIdCargoOperacional()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdCargoOperacional())
            .toHashCode();
    }
}
