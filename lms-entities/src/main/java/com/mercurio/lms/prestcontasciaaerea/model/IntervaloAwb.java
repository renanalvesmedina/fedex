package com.mercurio.lms.prestcontasciaaerea.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class IntervaloAwb implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idIntervaloAwb;

    /** persistent field */
    private Long nrIntervaloInicial;

    /** persistent field */
    private Long dvIntervaloInicial;

    /** persistent field */
    private Long nrIntervaloFinal;

    /** persistent field */
    private Long dvIntervaloFinal;
    
    private Long qtIntervalo;

    /** persistent field */
    private com.mercurio.lms.prestcontasciaaerea.model.PrestacaoConta prestacaoConta;

    /** full constructor */
	public IntervaloAwb(
			Long nrIntervaloInicial,
			Long dvIntervaloInicial,
			Long nrIntervaloFinal,
			Long dvIntervaloFinal,
			com.mercurio.lms.prestcontasciaaerea.model.PrestacaoConta prestacaoConta) {
        this.nrIntervaloInicial = nrIntervaloInicial;
        this.dvIntervaloInicial = dvIntervaloInicial;
        this.nrIntervaloFinal = nrIntervaloFinal;
        this.dvIntervaloFinal = dvIntervaloFinal;
        this.prestacaoConta = prestacaoConta;
    }

    /** default constructor */
    public IntervaloAwb() {
    }

    public Long getIdIntervaloAwb() {
        return this.idIntervaloAwb;
    }

    public void setIdIntervaloAwb(Long idIntervaloAwb) {
        this.idIntervaloAwb = idIntervaloAwb;
    }

    public Long getNrIntervaloInicial() {
        return this.nrIntervaloInicial;
    }

    public void setNrIntervaloInicial(Long nrIntervaloInicial) {
        this.nrIntervaloInicial = nrIntervaloInicial;
    }

    public Long getDvIntervaloInicial() {
        return this.dvIntervaloInicial;
    }

    public void setDvIntervaloInicial(Long dvIntervaloInicial) {
        this.dvIntervaloInicial = dvIntervaloInicial;
    }

    public Long getNrIntervaloFinal() {
        return this.nrIntervaloFinal;
    }

    public void setNrIntervaloFinal(Long nrIntervaloFinal) {
        this.nrIntervaloFinal = nrIntervaloFinal;
    }

    public Long getDvIntervaloFinal() {
        return this.dvIntervaloFinal;
    }

    public void setDvIntervaloFinal(Long dvIntervaloFinal) {
        this.dvIntervaloFinal = dvIntervaloFinal;
    }
    
    /**
     * Quantidade de intervalos.<BR> 
	 * 
     * @return ((nr final - nr inicial) + 1 )
     */
    public Long getQtIntervalo() {
    	if (getNrIntervaloFinal() != null && getNrIntervaloInicial() != null){
			this.qtIntervalo = Long.valueOf(
					(getNrIntervaloFinal().longValue() - getNrIntervaloInicial()
							.longValue()) + 1);
    	}
		return qtIntervalo;
	}

	public void setQtIntervalo(Long qtIntervalo) {
		this.qtIntervalo = qtIntervalo;
	}

	public com.mercurio.lms.prestcontasciaaerea.model.PrestacaoConta getPrestacaoConta() {
        return this.prestacaoConta;
    }

	public void setPrestacaoConta(
			com.mercurio.lms.prestcontasciaaerea.model.PrestacaoConta prestacaoConta) {
        this.prestacaoConta = prestacaoConta;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idIntervaloAwb",
				getIdIntervaloAwb()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof IntervaloAwb))
			return false;
        IntervaloAwb castOther = (IntervaloAwb) other;
		return new EqualsBuilder().append(this.getIdIntervaloAwb(),
				castOther.getIdIntervaloAwb()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdIntervaloAwb()).toHashCode();
    }

}
