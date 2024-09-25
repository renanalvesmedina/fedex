package com.mercurio.lms.pendencia.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class EmissaoLote implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEmissaoLote;

    /** persistent field */
    private YearMonthDay dtEmissao;

    /** persistent field */
    private com.mercurio.lms.pendencia.model.LotePendencia lotePendencia;

    /** persistent field */
    private List destinatarioEmissaoLotes;

    public Long getIdEmissaoLote() {
        return this.idEmissaoLote;
    }

    public void setIdEmissaoLote(Long idEmissaoLote) {
        this.idEmissaoLote = idEmissaoLote;
    }

    public YearMonthDay getDtEmissao() {
        return this.dtEmissao;
    }

    public void setDtEmissao(YearMonthDay dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public com.mercurio.lms.pendencia.model.LotePendencia getLotePendencia() {
        return this.lotePendencia;
    }

	public void setLotePendencia(
			com.mercurio.lms.pendencia.model.LotePendencia lotePendencia) {
        this.lotePendencia = lotePendencia;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.DestinatarioEmissaoLote.class)     
    public List getDestinatarioEmissaoLotes() {
        return this.destinatarioEmissaoLotes;
    }

    public void setDestinatarioEmissaoLotes(List destinatarioEmissaoLotes) {
        this.destinatarioEmissaoLotes = destinatarioEmissaoLotes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idEmissaoLote",
				getIdEmissaoLote()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EmissaoLote))
			return false;
        EmissaoLote castOther = (EmissaoLote) other;
		return new EqualsBuilder().append(this.getIdEmissaoLote(),
				castOther.getIdEmissaoLote()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEmissaoLote()).toHashCode();
    }

}
