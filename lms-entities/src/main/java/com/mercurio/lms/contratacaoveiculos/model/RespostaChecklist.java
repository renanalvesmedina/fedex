package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Pessoa;

/** @author LMS Custom Hibernate CodeGenerator */
public class RespostaChecklist implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRespostaChecklist;

    /** persistent field */
    private Boolean blAprovado;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.ChecklistMeioTransporte checklistMeioTransporte;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.ItChecklistTpMeioTransp itChecklistTpMeioTransp;
    
    private Pessoa pessoa;

    public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

    public Long getIdRespostaChecklist() {
        return this.idRespostaChecklist;
    }

    public void setIdRespostaChecklist(Long idRespostaChecklist) {
        this.idRespostaChecklist = idRespostaChecklist;
    }

    public Boolean getBlAprovado() {
        return this.blAprovado;
    }

    public void setBlAprovado(Boolean blAprovado) {
        this.blAprovado = blAprovado;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.contratacaoveiculos.model.ChecklistMeioTransporte getChecklistMeioTransporte() {
        return this.checklistMeioTransporte;
    }

	public void setChecklistMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.ChecklistMeioTransporte checklistMeioTransporte) {
        this.checklistMeioTransporte = checklistMeioTransporte;
    }

    public com.mercurio.lms.contratacaoveiculos.model.ItChecklistTpMeioTransp getItChecklistTpMeioTransp() {
        return this.itChecklistTpMeioTransp;
    }

	public void setItChecklistTpMeioTransp(
			com.mercurio.lms.contratacaoveiculos.model.ItChecklistTpMeioTransp itChecklistTpMeioTransp) {
        this.itChecklistTpMeioTransp = itChecklistTpMeioTransp;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idRespostaChecklist",
				getIdRespostaChecklist()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RespostaChecklist))
			return false;
        RespostaChecklist castOther = (RespostaChecklist) other;
		return new EqualsBuilder().append(this.getIdRespostaChecklist(),
				castOther.getIdRespostaChecklist()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRespostaChecklist())
            .toHashCode();
    }

}
