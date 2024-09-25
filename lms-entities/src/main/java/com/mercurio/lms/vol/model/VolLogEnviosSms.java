package com.mercurio.lms.vol.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class VolLogEnviosSms implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idLogEnvioSms;

    /** persistent field */
    private DateTime dhEnvio;

    /** persistent field */
    private DomainValue tpEvento;

    /** nullable persistent field */
    private Boolean blTimeout;

    /** nullable persistent field */
    private DateTime dhRetorno;

    /** nullable persistent field */
    private String obMensagem;

    /** persistent field */
    private VolEquipamentos volEquipamento;
    
    public Long getIdLogEnvioSms() {
        return this.idLogEnvioSms;
    }

    public void setIdLogEnvioSms(Long idLogEnvioSms) {
        this.idLogEnvioSms = idLogEnvioSms;
    }

    public DateTime getDhEnvio() {
        return this.dhEnvio;
    }

    public void setDhEnvio(DateTime dhEnvio) {
        this.dhEnvio = dhEnvio;
    }

    public DomainValue getTpEvento() {
        return this.tpEvento;
    }

    public void setTpEvento(DomainValue tpEvento) {
        this.tpEvento = tpEvento;
    }

    public Boolean getBlTimeout() {
        return this.blTimeout;
    }

    public void setBlTimeout(Boolean blTimeout) {
        this.blTimeout = blTimeout;
    }

    public DateTime getDhRetorno() {
        return this.dhRetorno;
    }

    public void setDhRetorno(DateTime dhRetorno) {
        this.dhRetorno = dhRetorno;
    }

    public String getObMensagem() {
        return this.obMensagem;
    }

    public void setObMensagem(String obMensagem) {
        this.obMensagem = obMensagem;
    }

    public VolEquipamentos getVolEquipamento() {
        return this.volEquipamento;
    }

    public void setVolEquipamento(VolEquipamentos volEquipamento) {
        this.volEquipamento = volEquipamento;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idLogEnvioSms",
				getIdLogEnvioSms()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof VolLogEnviosSms))
			return false;
        VolLogEnviosSms castOther = (VolLogEnviosSms) other;
		return new EqualsBuilder().append(this.getIdLogEnvioSms(),
				castOther.getIdLogEnvioSms()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdLogEnvioSms()).toHashCode();
    }

}
