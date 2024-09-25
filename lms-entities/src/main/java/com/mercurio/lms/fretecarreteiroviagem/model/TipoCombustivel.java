package com.mercurio.lms.fretecarreteiroviagem.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class TipoCombustivel implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoCombustivel;

    /** persistent field */
    private VarcharI18n dsTipoCombustivel;
    
    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List valorCombustiveis;

    /** persistent field */
    private List tpCombustTpMeioTransps;

    public Long getIdTipoCombustivel() {
        return this.idTipoCombustivel;
    }

    public void setIdTipoCombustivel(Long idTipoCombustivel) {
        this.idTipoCombustivel = idTipoCombustivel;
    }

    public VarcharI18n getDsTipoCombustivel() {
		return dsTipoCombustivel;
    }

	public void setDsTipoCombustivel(VarcharI18n dsTipoCombustivel) {
        this.dsTipoCombustivel = dsTipoCombustivel;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.fretecarreteiroviagem.model.ValorCombustivel.class)     
    public List getValorCombustiveis() {
        return this.valorCombustiveis;
    }

    public void setValorCombustiveis(List valorCombustiveis) {
        this.valorCombustiveis = valorCombustiveis;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.TpCombustTpMeioTransp.class)     
    public List getTpCombustTpMeioTransps() {
        return this.tpCombustTpMeioTransps;
    }

    public void setTpCombustTpMeioTransps(List tpCombustTpMeioTransps) {
        this.tpCombustTpMeioTransps = tpCombustTpMeioTransps;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTipoCombustivel",
				getIdTipoCombustivel()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoCombustivel))
			return false;
        TipoCombustivel castOther = (TipoCombustivel) other;
		return new EqualsBuilder().append(this.getIdTipoCombustivel(),
				castOther.getIdTipoCombustivel()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoCombustivel())
            .toHashCode();
    }

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

}
