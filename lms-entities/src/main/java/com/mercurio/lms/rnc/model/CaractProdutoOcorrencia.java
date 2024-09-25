package com.mercurio.lms.rnc.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class CaractProdutoOcorrencia implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idCaractProdutoOcorrencia;

    /** persistent field */
    private String dsCaractProdutoOcorrencia;

    /** persistent field */
    private com.mercurio.lms.rnc.model.CaracteristicaProduto caracteristicaProduto;

    /** persistent field */
    private com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade ocorrenciaNaoConformidade;

    public Long getIdCaractProdutoOcorrencia() {
        return this.idCaractProdutoOcorrencia;
    }

    public void setIdCaractProdutoOcorrencia(Long idCaractProdutoOcorrencia) {
        this.idCaractProdutoOcorrencia = idCaractProdutoOcorrencia;
    }

    public String getDsCaractProdutoOcorrencia() {
        return this.dsCaractProdutoOcorrencia;
    }

    public void setDsCaractProdutoOcorrencia(String dsCaractProdutoOcorrencia) {
        this.dsCaractProdutoOcorrencia = dsCaractProdutoOcorrencia;
    }

    public com.mercurio.lms.rnc.model.CaracteristicaProduto getCaracteristicaProduto() {
        return this.caracteristicaProduto;
    }

	public void setCaracteristicaProduto(
			com.mercurio.lms.rnc.model.CaracteristicaProduto caracteristicaProduto) {
        this.caracteristicaProduto = caracteristicaProduto;
    }

    public com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade getOcorrenciaNaoConformidade() {
        return this.ocorrenciaNaoConformidade;
    }

	public void setOcorrenciaNaoConformidade(
			com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade ocorrenciaNaoConformidade) {
        this.ocorrenciaNaoConformidade = ocorrenciaNaoConformidade;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idCaractProdutoOcorrencia",
				getIdCaractProdutoOcorrencia()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof CaractProdutoOcorrencia))
			return false;
        CaractProdutoOcorrencia castOther = (CaractProdutoOcorrencia) other;
		return new EqualsBuilder().append(this.getIdCaractProdutoOcorrencia(),
				castOther.getIdCaractProdutoOcorrencia()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdCaractProdutoOcorrencia())
            .toHashCode();
    }

}
