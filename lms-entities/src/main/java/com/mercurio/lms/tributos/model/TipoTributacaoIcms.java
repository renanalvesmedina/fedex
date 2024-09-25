package com.mercurio.lms.tributos.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class TipoTributacaoIcms implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoTributacaoIcms;

    /** persistent field */
    private String dsTipoTributacaoIcms;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List descricaoTributacaoIcms;

    /** persistent field */
    private List conhecimentos;

    /** persistent field */
    private List aliquotaIcms;

    public Long getIdTipoTributacaoIcms() {
        return this.idTipoTributacaoIcms;
    }

    public void setIdTipoTributacaoIcms(Long idTipoTributacaoIcms) {
        this.idTipoTributacaoIcms = idTipoTributacaoIcms;
    }

    public String getDsTipoTributacaoIcms() {
        return this.dsTipoTributacaoIcms;
    }

    public void setDsTipoTributacaoIcms(String dsTipoTributacaoIcms) {
        this.dsTipoTributacaoIcms = dsTipoTributacaoIcms;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.tributos.model.DescricaoTributacaoIcms.class)     
    public List getDescricaoTributacaoIcms() {
        return this.descricaoTributacaoIcms;
    }

    public void setDescricaoTributacaoIcms(List descricaoTributacaoIcms) {
        this.descricaoTributacaoIcms = descricaoTributacaoIcms;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.Conhecimento.class)     
    public List getConhecimentos() {
        return this.conhecimentos;
    }

    public void setConhecimentos(List conhecimentos) {
        this.conhecimentos = conhecimentos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.tributos.model.AliquotaIcms.class)     
    public List getAliquotaIcms() {
        return this.aliquotaIcms;
    }

    public void setAliquotaIcms(List aliquotaIcms) {
        this.aliquotaIcms = aliquotaIcms;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTipoTributacaoIcms",
				getIdTipoTributacaoIcms()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoTributacaoIcms))
			return false;
        TipoTributacaoIcms castOther = (TipoTributacaoIcms) other;
		return new EqualsBuilder().append(this.getIdTipoTributacaoIcms(),
				castOther.getIdTipoTributacaoIcms()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoTributacaoIcms())
            .toHashCode();
    }

}
