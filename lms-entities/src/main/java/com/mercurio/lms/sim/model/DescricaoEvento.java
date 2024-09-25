package com.mercurio.lms.sim.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class DescricaoEvento implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDescricaoEvento;

    /** persistent field */
    private Short cdDescricaoEvento;

    /** persistent field */
    private VarcharI18n  dsDescricaoEvento;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List eventos;

    public Long getIdDescricaoEvento() {
        return this.idDescricaoEvento;
    }

    public void setIdDescricaoEvento(Long idDescricaoEvento) {
        this.idDescricaoEvento = idDescricaoEvento;
    }

    public Short getCdDescricaoEvento() {
        return this.cdDescricaoEvento;
    }

    public void setCdDescricaoEvento(Short cdDescricaoEvento) {
        this.cdDescricaoEvento = cdDescricaoEvento;
    }

    public VarcharI18n getDsDescricaoEvento() {
		return dsDescricaoEvento;
    }

	public void setDsDescricaoEvento(VarcharI18n dsDescricaoEvento) {
        this.dsDescricaoEvento = dsDescricaoEvento;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.sim.model.Evento.class)     
    public List getEventos() {
        return this.eventos;
    }

    public void setEventos(List eventos) {
        this.eventos = eventos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDescricaoEvento",
				getIdDescricaoEvento()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DescricaoEvento))
			return false;
        DescricaoEvento castOther = (DescricaoEvento) other;
		return new EqualsBuilder().append(this.getIdDescricaoEvento(),
				castOther.getIdDescricaoEvento()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDescricaoEvento())
            .toHashCode();
    }

    public String getDescricaoEventoConcatenado() {
    	if (this.cdDescricaoEvento!= null && dsDescricaoEvento!=null){
			return this.cdDescricaoEvento.toString()
					+ " - "
					+ this.dsDescricaoEvento.getValue(LocaleContextHolder
							.getLocale());
		} else
			return "";
    }
}
