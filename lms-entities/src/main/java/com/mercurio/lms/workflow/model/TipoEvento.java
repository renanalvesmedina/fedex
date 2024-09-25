package com.mercurio.lms.workflow.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class TipoEvento implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoEvento;

    /** persistent field */
    private Short nrTipoEvento;

    /** persistent field */
    private VarcharI18n dsTipoEvento;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private com.mercurio.lms.workflow.model.EventoWorkflow eventoWorkflow;

    public Long getIdTipoEvento() {
        return this.idTipoEvento;
    }

    public void setIdTipoEvento(Long idTipoEvento) {
        this.idTipoEvento = idTipoEvento;
    }

    public Short getNrTipoEvento() {
        return this.nrTipoEvento;
    }

    public void setNrTipoEvento(Short nrTipoEvento) {
        this.nrTipoEvento = nrTipoEvento;
    }

    public VarcharI18n getDsTipoEvento() {
		return dsTipoEvento;
    }

	public void setDsTipoEvento(VarcharI18n dsTipoEvento) {
        this.dsTipoEvento = dsTipoEvento;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.workflow.model.EventoWorkflow getEventoWorkflow() {
        return this.eventoWorkflow;
    }

	public void setEventoWorkflow(
			com.mercurio.lms.workflow.model.EventoWorkflow eventoWorkflow) {
        this.eventoWorkflow = eventoWorkflow;
    }

    public String getCodigoDescricao(){
		StringBuffer codigoDescricao = new StringBuffer()
		.append(this.getNrTipoEvento()).append(" - ")
		.append(this.getDsTipoEvento());
		return codigoDescricao.toString();
    }
    
    public String toString() {
		return new ToStringBuilder(this).append("idTipoEvento",
				getIdTipoEvento()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoEvento))
			return false;
        TipoEvento castOther = (TipoEvento) other;
		return new EqualsBuilder().append(this.getIdTipoEvento(),
				castOther.getIdTipoEvento()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoEvento()).toHashCode();
    }

}
