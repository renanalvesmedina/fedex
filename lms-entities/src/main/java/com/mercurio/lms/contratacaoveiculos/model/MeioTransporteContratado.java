package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class MeioTransporteContratado implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMeioTransporteContratado;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporte;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao solicitacaoContratacao;

    public Long getIdMeioTransporteContratado() {
        return this.idMeioTransporteContratado;
    }

    public void setIdMeioTransporteContratado(Long idMeioTransporteContratado) {
        this.idMeioTransporteContratado = idMeioTransporteContratado;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.contratacaoveiculos.model.MeioTransporte getMeioTransporte() {
        return this.meioTransporte;
    }

	public void setMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporte) {
        this.meioTransporte = meioTransporte;
    }

    public com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao getSolicitacaoContratacao() {
        return this.solicitacaoContratacao;
    }

	public void setSolicitacaoContratacao(
			com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao solicitacaoContratacao) {
        this.solicitacaoContratacao = solicitacaoContratacao;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMeioTransporteContratado",
				getIdMeioTransporteContratado()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MeioTransporteContratado))
			return false;
        MeioTransporteContratado castOther = (MeioTransporteContratado) other;
		return new EqualsBuilder().append(this.getIdMeioTransporteContratado(),
				castOther.getIdMeioTransporteContratado()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMeioTransporteContratado())
            .toHashCode();
    }

}
