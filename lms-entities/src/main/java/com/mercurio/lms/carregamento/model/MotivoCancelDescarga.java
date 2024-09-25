package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class MotivoCancelDescarga implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMotivoCancelDescarga;

    /** persistent field */
    private VarcharI18n dsMotivo;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List carregamentoDescargas;

    public Long getIdMotivoCancelDescarga() {
        return this.idMotivoCancelDescarga;
    }

    public void setIdMotivoCancelDescarga(Long idMotivoCancelDescarga) {
        this.idMotivoCancelDescarga = idMotivoCancelDescarga;
    }

    public VarcharI18n getDsMotivo() {
		return dsMotivo;
    }

	public void setDsMotivo(VarcharI18n dsMotivo) {
        this.dsMotivo = dsMotivo;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.CarregamentoDescarga.class)     
    public List getCarregamentoDescargas() {
        return this.carregamentoDescargas;
    }

    public void setCarregamentoDescargas(List carregamentoDescargas) {
        this.carregamentoDescargas = carregamentoDescargas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMotivoCancelDescarga",
				getIdMotivoCancelDescarga()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MotivoCancelDescarga))
			return false;
        MotivoCancelDescarga castOther = (MotivoCancelDescarga) other;
		return new EqualsBuilder().append(this.getIdMotivoCancelDescarga(),
				castOther.getIdMotivoCancelDescarga()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMotivoCancelDescarga())
            .toHashCode();
    }

}
