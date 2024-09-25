package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class OrdemFilialFluxo implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idOrdemFilialFluxo;

    /** persistent field */
    private Byte nrOrdem;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private FluxoFilial fluxoFilial;

    public Long getIdOrdemFilialFluxo() {
        return this.idOrdemFilialFluxo;
    }

    public void setIdOrdemFilialFluxo(Long idOrdemFilialFluxo) {
        this.idOrdemFilialFluxo = idOrdemFilialFluxo;
    }

    public Byte getNrOrdem() {
        return this.nrOrdem;
    }

    public void setNrOrdem(Byte nrOrdem) {
        this.nrOrdem = nrOrdem;
    }

    public FluxoFilial getFluxoFilial() {
    	return this.fluxoFilial;
    }

    public void setFluxoFilial(FluxoFilial fluxoFilial) {
        this.fluxoFilial = fluxoFilial;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idOrdemFilialFluxo",
				getIdOrdemFilialFluxo()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof OrdemFilialFluxo))
			return false;
        OrdemFilialFluxo castOther = (OrdemFilialFluxo) other;
		return new EqualsBuilder().append(this.getIdOrdemFilialFluxo(),
				castOther.getIdOrdemFilialFluxo()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdOrdemFilialFluxo())
            .toHashCode();
    }

}
