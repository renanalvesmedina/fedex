package com.mercurio.lms.coleta.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class EventoManifestoColeta implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEventoManifestoColeta;

    /** persistent field */
    private DateTime dhEvento;

    /** persistent field */
    private DomainValue tpEventoManifestoColeta;

    /** persistent field */
    private com.mercurio.lms.coleta.model.ManifestoColeta manifestoColeta;

    public Long getIdEventoManifestoColeta() {
        return this.idEventoManifestoColeta;
    }

    public void setIdEventoManifestoColeta(Long idEventoManifestoColeta) {
        this.idEventoManifestoColeta = idEventoManifestoColeta;
    }

    public DateTime getDhEvento() {
        return this.dhEvento;
    }

    public void setDhEvento(DateTime dhEvento) {
        this.dhEvento = dhEvento;
    }

    public DomainValue getTpEventoManifestoColeta() {
        return this.tpEventoManifestoColeta;
    }

    public void setTpEventoManifestoColeta(DomainValue tpEventoManifestoColeta) {
        this.tpEventoManifestoColeta = tpEventoManifestoColeta;
    }

    public com.mercurio.lms.coleta.model.ManifestoColeta getManifestoColeta() {
        return this.manifestoColeta;
    }

	public void setManifestoColeta(
			com.mercurio.lms.coleta.model.ManifestoColeta manifestoColeta) {
        this.manifestoColeta = manifestoColeta;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idEventoManifestoColeta",
				getIdEventoManifestoColeta()).toString();
    }

	public String toString(ToStringStyle style) {
		return new ToStringBuilder(this, style)
				.append("ID_EVENTO_MANIFESTO_COLETA", idEventoManifestoColeta)
				.append("ID_MANIFESTO_COLETA", manifestoColeta != null ? manifestoColeta.getIdManifestoColeta() : null)
				.append("DH_EVENTO", dhEvento)
				.append("TP_EVENTO_MANIFESTO_COLETA", tpEventoManifestoColeta != null ? tpEventoManifestoColeta.getValue() : null)
				.toString();
	}

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EventoManifestoColeta))
			return false;
        EventoManifestoColeta castOther = (EventoManifestoColeta) other;
		return new EqualsBuilder().append(this.getIdEventoManifestoColeta(),
				castOther.getIdEventoManifestoColeta()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEventoManifestoColeta())
            .toHashCode();
    }

}
