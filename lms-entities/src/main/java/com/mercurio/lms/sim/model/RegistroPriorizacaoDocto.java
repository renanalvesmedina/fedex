package com.mercurio.lms.sim.model;

import java.io.Serializable;
 
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.expedicao.model.DoctoServico;

/** @author LMS Custom Hibernate CodeGenerator */
public class RegistroPriorizacaoDocto implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRegistroPriorizacaoDocto;
    
    private DoctoServico doctoServico;
    
    private RegistroPriorizacaoEmbarq registroPriorizacaoEmbarq;

	public DoctoServico getDoctoServico() {
		return doctoServico;
	}

	public void setDoctoServico(DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}

	public Long getIdRegistroPriorizacaoDocto() {
		return idRegistroPriorizacaoDocto;
	}

	public void setIdRegistroPriorizacaoDocto(Long idRegistroPriorizacaoDocto) {
		this.idRegistroPriorizacaoDocto = idRegistroPriorizacaoDocto;
	}

	public RegistroPriorizacaoEmbarq getRegistroPriorizacaoEmbarq() {
		return registroPriorizacaoEmbarq;
	}

	public void setRegistroPriorizacaoEmbarq(
			RegistroPriorizacaoEmbarq registroPriorizacaoEmbarq) {
		this.registroPriorizacaoEmbarq = registroPriorizacaoEmbarq;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idRegistroPriorizacaoDocto",
				getIdRegistroPriorizacaoDocto()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RegistroPriorizacaoDocto))
			return false;
        RegistroPriorizacaoDocto castOther = (RegistroPriorizacaoDocto) other;
		return new EqualsBuilder().append(this.getIdRegistroPriorizacaoDocto(),
				castOther.getIdRegistroPriorizacaoDocto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRegistroPriorizacaoDocto())
            .toHashCode();
    }

}
