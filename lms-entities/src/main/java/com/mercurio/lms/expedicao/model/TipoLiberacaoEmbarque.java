package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class TipoLiberacaoEmbarque implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Long idTipoLiberacaoEmbarque;

    /** persistent field */
    private VarcharI18n dsTipoLiberacaoEmbarque;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List liberacaoDocServs;

    public Long getIdTipoLiberacaoEmbarque() {
        return this.idTipoLiberacaoEmbarque;
    }

    public void setIdTipoLiberacaoEmbarque(Long idTipoLiberacaoEmbarque) {
        this.idTipoLiberacaoEmbarque = idTipoLiberacaoEmbarque;
    }

    public VarcharI18n getDsTipoLiberacaoEmbarque() {
		return dsTipoLiberacaoEmbarque;
    }

	public void setDsTipoLiberacaoEmbarque(VarcharI18n dsTipoLiberacaoEmbarque) {
        this.dsTipoLiberacaoEmbarque = dsTipoLiberacaoEmbarque;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.LiberacaoDocServ.class)     
    public List getLiberacaoDocServs() {
        return this.liberacaoDocServs;
    }

    public void setLiberacaoDocServs(List liberacaoDocServs) {
        this.liberacaoDocServs = liberacaoDocServs;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTipoLiberacaoEmbarque",
				getIdTipoLiberacaoEmbarque()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoLiberacaoEmbarque))
			return false;
        TipoLiberacaoEmbarque castOther = (TipoLiberacaoEmbarque) other;
		return new EqualsBuilder().append(this.getIdTipoLiberacaoEmbarque(),
				castOther.getIdTipoLiberacaoEmbarque()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoLiberacaoEmbarque())
            .toHashCode();
    }

}
