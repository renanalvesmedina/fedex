package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

/** @author LMS Custom Hibernate CodeGenerator */
public class NotaFiscalCtoCooperada implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idNotaFiscalCtoCooperada;

    /** persistent field */
    private Long nrNotaFiscal;

    /** persistent field */
    private BigDecimal vlTotal;

    /** persistent field */
    private Long qtVolumes;

    /** persistent field */
    private BigDecimal psMercadoria;

    /** persistent field */
    private YearMonthDay dtEmissao;

    /** persistent field */
    private String dsSerie;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.CtoCtoCooperada ctoCtoCooperada;

    public Long getIdNotaFiscalCtoCooperada() {
        return this.idNotaFiscalCtoCooperada;
    }

    public void setIdNotaFiscalCtoCooperada(Long idNotaFiscalCtoCooperada) {
        this.idNotaFiscalCtoCooperada = idNotaFiscalCtoCooperada;
    }

    public Long getNrNotaFiscal() {
        return this.nrNotaFiscal;
    }

    public void setNrNotaFiscal(Long nrNotaFiscal) {
        this.nrNotaFiscal = nrNotaFiscal;
    }

    public BigDecimal getVlTotal() {
        return this.vlTotal;
    }

    public void setVlTotal(BigDecimal vlTotal) {
        this.vlTotal = vlTotal;
    }

    public Long getQtVolumes() {
        return this.qtVolumes;
    }

    public void setQtVolumes(Long qtVolumes) {
        this.qtVolumes = qtVolumes;
    }

    public BigDecimal getPsMercadoria() {
        return this.psMercadoria;
    }

    public void setPsMercadoria(BigDecimal psMercadoria) {
        this.psMercadoria = psMercadoria;
    }

    public YearMonthDay getDtEmissao() {
        return this.dtEmissao;
    }

    public void setDtEmissao(YearMonthDay dtEmissao) {
        this.dtEmissao = dtEmissao;
    }
     
    public String getDsSerie() {
        return this.dsSerie;
    }

    public void setDsSerie(String dsSerie) {
        this.dsSerie = dsSerie;
    }

    public com.mercurio.lms.expedicao.model.CtoCtoCooperada getCtoCtoCooperada() {
        return this.ctoCtoCooperada;
    }

	public void setCtoCtoCooperada(
			com.mercurio.lms.expedicao.model.CtoCtoCooperada ctoCtoCooperada) {
        this.ctoCtoCooperada = ctoCtoCooperada;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idNotaFiscalCtoCooperada",
				getIdNotaFiscalCtoCooperada()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof NotaFiscalCtoCooperada))
			return false;
        NotaFiscalCtoCooperada castOther = (NotaFiscalCtoCooperada) other;
		return new EqualsBuilder().append(this.getIdNotaFiscalCtoCooperada(),
				castOther.getIdNotaFiscalCtoCooperada()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdNotaFiscalCtoCooperada())
            .toHashCode();
    }

}
