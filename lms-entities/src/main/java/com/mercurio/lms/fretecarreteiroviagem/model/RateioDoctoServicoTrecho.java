package com.mercurio.lms.fretecarreteiroviagem.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.expedicao.model.DoctoServico;

/** @author LMS Custom Hibernate CodeGenerator */
public class RateioDoctoServicoTrecho implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRateioDoctoServicoTrecho;

    /** persistent field */
    private BigDecimal vlRateio;

    /** persistent field */
    private ControleTrecho controleTrecho;

    /** persistent field */
    private DoctoServico doctoServico;
    
    public Long getIdRateioDoctoServicoTrecho() {
        return this.idRateioDoctoServicoTrecho;
    }

    public void setIdRateioDoctoServicoTrecho(Long idRateioDoctoServicoTrecho) {
        this.idRateioDoctoServicoTrecho = idRateioDoctoServicoTrecho;
    }

    public BigDecimal getVlRateio() {
        return this.vlRateio;
    }

    public void setVlRateio(BigDecimal vlRateio) {
        this.vlRateio = vlRateio;
    }

    public ControleTrecho getControleTrecho() {
        return this.controleTrecho;
    }

    public void setControleTrecho(ControleTrecho controleTrecho) {
        this.controleTrecho = controleTrecho;
    }

    public DoctoServico getDoctoServico() {
        return this.doctoServico;
    }

    public void setDoctoServico(DoctoServico doctoServico) {
        this.doctoServico = doctoServico;
    }
    
    public String toString() {
		return new ToStringBuilder(this).append("idRateioDoctoServicoTrecho",
				getIdRateioDoctoServicoTrecho()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RateioDoctoServicoTrecho))
			return false;
        RateioDoctoServicoTrecho castOther = (RateioDoctoServicoTrecho) other;
		return new EqualsBuilder().append(this.getIdRateioDoctoServicoTrecho(),
				castOther.getIdRateioDoctoServicoTrecho()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRateioDoctoServicoTrecho())
            .toHashCode();
    }

}
