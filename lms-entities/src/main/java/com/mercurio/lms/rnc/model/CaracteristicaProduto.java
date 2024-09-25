package com.mercurio.lms.rnc.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class CaracteristicaProduto implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idCaracteristicaProduto;

    /** persistent field */
    private VarcharI18n dsCaracteristicaProduto;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List caractProdutoOcorrencias;

    public Long getIdCaracteristicaProduto() {
        return this.idCaracteristicaProduto;
    }

    public void setIdCaracteristicaProduto(Long idCaracteristicaProduto) {
        this.idCaracteristicaProduto = idCaracteristicaProduto;
    }

    public VarcharI18n getDsCaracteristicaProduto() {
		return dsCaracteristicaProduto;
    }

	public void setDsCaracteristicaProduto(VarcharI18n dsCaracteristicaProduto) {
        this.dsCaracteristicaProduto = dsCaracteristicaProduto;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.rnc.model.CaractProdutoOcorrencia.class)     
    public List getCaractProdutoOcorrencias() {
        return this.caractProdutoOcorrencias;
    }

    public void setCaractProdutoOcorrencias(List caractProdutoOcorrencias) {
        this.caractProdutoOcorrencias = caractProdutoOcorrencias;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idCaracteristicaProduto",
				getIdCaracteristicaProduto()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof CaracteristicaProduto))
			return false;
        CaracteristicaProduto castOther = (CaracteristicaProduto) other;
		return new EqualsBuilder().append(this.getIdCaracteristicaProduto(),
				castOther.getIdCaracteristicaProduto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdCaracteristicaProduto())
            .toHashCode();
    }

}
