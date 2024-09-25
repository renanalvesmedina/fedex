package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class CampoComplementar implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idCampoComplementar;

    /** persistent field */
    private String nmCampoComplementar;

    /** persistent field */
    private VarcharI18n dsCampoComplementar;

    /** persistent field */
    private DomainValue tpCampoComplementar;

    /** persistent field */
    private Boolean blOpcional;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private Byte nrTamanho;

    /** nullable persistent field */
    private String dsFormatacao;

    /** persistent field */
    private List valorCampoComplementars;

    public Long getIdCampoComplementar() {
        return this.idCampoComplementar;
    }

    public void setIdCampoComplementar(Long idCampoComplementar) {
        this.idCampoComplementar = idCampoComplementar;
    }

    public String getNmCampoComplementar() {
        return this.nmCampoComplementar;
    }

    public void setNmCampoComplementar(String nmCampoComplementar) {
        this.nmCampoComplementar = nmCampoComplementar;
    }

    public VarcharI18n getDsCampoComplementar() {
		return dsCampoComplementar;
    }

	public void setDsCampoComplementar(VarcharI18n dsCampoComplementar) {
        this.dsCampoComplementar = dsCampoComplementar;
    }

    public DomainValue getTpCampoComplementar() {
        return this.tpCampoComplementar;
    }

    public void setTpCampoComplementar(DomainValue tpCampoComplementar) {
        this.tpCampoComplementar = tpCampoComplementar;
    }

    public Boolean getBlOpcional() {
        return this.blOpcional;
    }

    public void setBlOpcional(Boolean blOpcional) {
        this.blOpcional = blOpcional;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public Byte getNrTamanho() {
        return this.nrTamanho;
    }

    public void setNrTamanho(Byte nrTamanho) {
        this.nrTamanho = nrTamanho;
    }

    public String getDsFormatacao() {
        return this.dsFormatacao;
    }

    public void setDsFormatacao(String dsFormatacao) {
        this.dsFormatacao = dsFormatacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ValorCampoComplementar.class)     
    public List getValorCampoComplementars() {
        return this.valorCampoComplementars;
    }

    public void setValorCampoComplementars(List valorCampoComplementars) {
        this.valorCampoComplementars = valorCampoComplementars;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idCampoComplementar",
				getIdCampoComplementar()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof CampoComplementar))
			return false;
        CampoComplementar castOther = (CampoComplementar) other;
		return new EqualsBuilder().append(this.getIdCampoComplementar(),
				castOther.getIdCampoComplementar()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdCampoComplementar())
            .toHashCode();
    }

}
