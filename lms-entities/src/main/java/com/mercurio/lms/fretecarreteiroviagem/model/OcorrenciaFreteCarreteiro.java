package com.mercurio.lms.fretecarreteiroviagem.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class OcorrenciaFreteCarreteiro implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idOcorrenciaFreteCarreteiro;

    /** persistent field */
    private DomainValue tpOcorrencia;

    /** persistent field */
    private YearMonthDay dtOcorrenciaFreteCarreteiro;

    /** persistent field */
    private String obMotivo;

    /** persistent field */
    private YearMonthDay dtInclusao;

    /** persistent field */
    private Boolean blDescontoCancelado;

    /** nullable persistent field */
    private BigDecimal vlDesconto;

    /** persistent field */
    private com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro reciboFreteCarreteiro;

    public Long getIdOcorrenciaFreteCarreteiro() {
        return this.idOcorrenciaFreteCarreteiro;
    }

    public void setIdOcorrenciaFreteCarreteiro(Long idOcorrenciaFreteCarreteiro) {
        this.idOcorrenciaFreteCarreteiro = idOcorrenciaFreteCarreteiro;
    }

    public DomainValue getTpOcorrencia() {
        return this.tpOcorrencia;
    }

    public void setTpOcorrencia(DomainValue tpOcorrencia) {
        this.tpOcorrencia = tpOcorrencia;
    }

    public YearMonthDay getDtOcorrenciaFreteCarreteiro() {
        return this.dtOcorrenciaFreteCarreteiro;
    }

	public void setDtOcorrenciaFreteCarreteiro(
			YearMonthDay dtOcorrenciaFreteCarreteiro) {
        this.dtOcorrenciaFreteCarreteiro = dtOcorrenciaFreteCarreteiro;
    }

    public String getObMotivo() {
        return this.obMotivo;
    }

    public void setObMotivo(String obMotivo) {
        this.obMotivo = obMotivo;
    }

    public YearMonthDay getDtInclusao() {
        return this.dtInclusao;
    }

    public void setDtInclusao(YearMonthDay dtInclusao) {
        this.dtInclusao = dtInclusao;
    }

    public Boolean getBlDescontoCancelado() {
		return blDescontoCancelado;
	}

	public void setBlDescontoCancelado(Boolean blDescontoCancelado) {
		this.blDescontoCancelado = blDescontoCancelado;
	}

	public BigDecimal getVlDesconto() {
        return this.vlDesconto;
    }

    public void setVlDesconto(BigDecimal vlDesconto) {
        this.vlDesconto = vlDesconto;
    }

    public com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro getReciboFreteCarreteiro() {
        return this.reciboFreteCarreteiro;
    }

	public void setReciboFreteCarreteiro(
			com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro reciboFreteCarreteiro) {
        this.reciboFreteCarreteiro = reciboFreteCarreteiro;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idOcorrenciaFreteCarreteiro",
				getIdOcorrenciaFreteCarreteiro()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof OcorrenciaFreteCarreteiro))
			return false;
        OcorrenciaFreteCarreteiro castOther = (OcorrenciaFreteCarreteiro) other;
		return new EqualsBuilder().append(
				this.getIdOcorrenciaFreteCarreteiro(),
				castOther.getIdOcorrenciaFreteCarreteiro()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdOcorrenciaFreteCarreteiro())
            .toHashCode();
    }

}
