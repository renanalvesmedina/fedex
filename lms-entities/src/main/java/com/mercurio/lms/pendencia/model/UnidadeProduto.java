package com.mercurio.lms.pendencia.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class UnidadeProduto implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idUnidadeProduto;

    /** persistent field */
    private VarcharI18n dsUnidadeProduto;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List mercadoriaPendenciaMzsByIdUnidadesPorVolume;

    /** persistent field */
    private List mercadoriaPendenciaMzsByIdUnidadeProduto;

    /** persistent field */
	private List entradaPendenciaMatrizs;

    public Long getIdUnidadeProduto() {
        return this.idUnidadeProduto;
    }

    public void setIdUnidadeProduto(Long idUnidadeProduto) {
        this.idUnidadeProduto = idUnidadeProduto;
    }

    public VarcharI18n getDsUnidadeProduto() {
		return dsUnidadeProduto;
    }

	public void setDsUnidadeProduto(VarcharI18n dsUnidadeProduto) {
        this.dsUnidadeProduto = dsUnidadeProduto;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.MercadoriaPendenciaMz.class)     
    public List getMercadoriaPendenciaMzsByIdUnidadesPorVolume() {
        return this.mercadoriaPendenciaMzsByIdUnidadesPorVolume;
    }

	public void setMercadoriaPendenciaMzsByIdUnidadesPorVolume(
			List mercadoriaPendenciaMzsByIdUnidadesPorVolume) {
        this.mercadoriaPendenciaMzsByIdUnidadesPorVolume = mercadoriaPendenciaMzsByIdUnidadesPorVolume;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.MercadoriaPendenciaMz.class)     
    public List getMercadoriaPendenciaMzsByIdUnidadeProduto() {
        return this.mercadoriaPendenciaMzsByIdUnidadeProduto;
    }

	public void setMercadoriaPendenciaMzsByIdUnidadeProduto(
			List mercadoriaPendenciaMzsByIdUnidadeProduto) {
        this.mercadoriaPendenciaMzsByIdUnidadeProduto = mercadoriaPendenciaMzsByIdUnidadeProduto;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.EntradaPendenciaMatriz.class)     
    public List getEntradaPendenciaMatrizs() {
        return this.entradaPendenciaMatrizs;
    }

    public void setEntradaPendenciaMatrizs(List entradaPendenciaMatrizs) {
        this.entradaPendenciaMatrizs = entradaPendenciaMatrizs;
    }    
    
    public String toString() {
		return new ToStringBuilder(this).append("idUnidadeProduto",
				getIdUnidadeProduto()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof UnidadeProduto))
			return false;
        UnidadeProduto castOther = (UnidadeProduto) other;
		return new EqualsBuilder().append(this.getIdUnidadeProduto(),
				castOther.getIdUnidadeProduto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdUnidadeProduto()).toHashCode();
    }

}
