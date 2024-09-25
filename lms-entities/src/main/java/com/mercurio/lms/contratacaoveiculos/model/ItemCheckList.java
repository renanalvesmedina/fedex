package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class ItemCheckList implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idItemCheckList;

    /** persistent field */
    private VarcharI18n dsItemCheckList;

    /** persistent field */
    private DomainValue tpMeioTransporte;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List itChecklistTpMeioTransps;

    public Long getIdItemCheckList() {
        return this.idItemCheckList;
    }

    public void setIdItemCheckList(Long idItemCheckList) {
        this.idItemCheckList = idItemCheckList;
    }

    public VarcharI18n getDsItemCheckList() {
		return dsItemCheckList;
    }

	public void setDsItemCheckList(VarcharI18n dsItemCheckList) {
        this.dsItemCheckList = dsItemCheckList;
    }

    public DomainValue getTpMeioTransporte() {
        return this.tpMeioTransporte;
    }

    public void setTpMeioTransporte(DomainValue tpMeioTransporte) {
        this.tpMeioTransporte = tpMeioTransporte;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.ItChecklistTpMeioTransp.class)     
    public List getItChecklistTpMeioTransps() {
        return this.itChecklistTpMeioTransps;
    }

    public void setItChecklistTpMeioTransps(List itChecklistTpMeioTransps) {
        this.itChecklistTpMeioTransps = itChecklistTpMeioTransps;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idItemCheckList",
				getIdItemCheckList()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ItemCheckList))
			return false;
        ItemCheckList castOther = (ItemCheckList) other;
		return new EqualsBuilder().append(this.getIdItemCheckList(),
				castOther.getIdItemCheckList()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdItemCheckList()).toHashCode();
    }

}
