package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class OcorrenciaRetornoBanco implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idOcorrenciaRetornoBanco;

    /** persistent field */
    private BigDecimal vlRecebido;

    /** persistent field */
    private BigDecimal vlDesconto;

    /** persistent field */
    private BigDecimal vlJuro;

    /** persistent field */
    private BigDecimal vlTarifa;

    /** persistent field */
    private Date dtOcorrencia;

    /** persistent field */
    private DomainValue tpOcorrenciaRetornoBanco;

    /** nullable persistent field */
    private String obOcorrenciaRetornoBanco;

    /** persistent field */
    private Boleto boleto;

    public Long getIdOcorrenciaRetornoBanco() {
        return this.idOcorrenciaRetornoBanco;
    }

    public void setIdOcorrenciaRetornoBanco(Long idOcorrenciaRetornoBanco) {
        this.idOcorrenciaRetornoBanco = idOcorrenciaRetornoBanco;
    }

    public BigDecimal getVlRecebido() {
        return this.vlRecebido;
    }

    public void setVlRecebido(BigDecimal vlRecebido) {
        this.vlRecebido = vlRecebido;
    }

    public BigDecimal getVlDesconto() {
        return this.vlDesconto;
    }

    public void setVlDesconto(BigDecimal vlDesconto) {
        this.vlDesconto = vlDesconto;
    }

    public BigDecimal getVlJuro() {
        return this.vlJuro;
    }

    public void setVlJuro(BigDecimal vlJuro) {
        this.vlJuro = vlJuro;
    }

    public BigDecimal getVlTarifa() {
        return this.vlTarifa;
    }

    public void setVlTarifa(BigDecimal vlTarifa) {
        this.vlTarifa = vlTarifa;
    }

    public Date getDtOcorrencia() {
        return this.dtOcorrencia;
    }

    public void setDtOcorrencia(Date dtOcorrencia) {
        this.dtOcorrencia = dtOcorrencia;
    }

    public DomainValue getTpOcorrenciaRetornoBanco() {
        return this.tpOcorrenciaRetornoBanco;
    }

    public void setTpOcorrenciaRetornoBanco(DomainValue tpOcorrenciaRetornoBanco) {
        this.tpOcorrenciaRetornoBanco = tpOcorrenciaRetornoBanco;
    }

    public String getObOcorrenciaRetornoBanco() {
        return this.obOcorrenciaRetornoBanco;
    }

    public void setObOcorrenciaRetornoBanco(String obOcorrenciaRetornoBanco) {
        this.obOcorrenciaRetornoBanco = obOcorrenciaRetornoBanco;
    }

    public Boleto getBoleto() {
        return this.boleto;
    }

    public void setBoleto(Boleto boleto) {
        this.boleto = boleto;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idOcorrenciaRetornoBanco",
				getIdOcorrenciaRetornoBanco()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof OcorrenciaRetornoBanco))
			return false;
        OcorrenciaRetornoBanco castOther = (OcorrenciaRetornoBanco) other;
		return new EqualsBuilder().append(this.getIdOcorrenciaRetornoBanco(),
				castOther.getIdOcorrenciaRetornoBanco()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdOcorrenciaRetornoBanco())
            .toHashCode();
    }

}
