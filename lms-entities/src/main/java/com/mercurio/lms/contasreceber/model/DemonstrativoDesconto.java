package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class DemonstrativoDesconto implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDemonstrativoDesconto;

    /** persistent field */
    private Long nrDemonstrativoDesconto;

    /** persistent field */
    private Boolean dvDemonstrativoDesconto;

    /** persistent field */
    private BigDecimal vlDemonstrativoDesconto;

    /** persistent field */
    private YearMonthDay dtEmissao;

    /** persistent field */
    private DomainValue tpSituacaoDemonstrativoDesc;

    /** persistent field */
    private String obDemonstrativoDesconto;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    public Long getIdDemonstrativoDesconto() {
        return this.idDemonstrativoDesconto;
    }

    public void setIdDemonstrativoDesconto(Long idDemonstrativoDesconto) {
        this.idDemonstrativoDesconto = idDemonstrativoDesconto;
    }

    public Long getNrDemonstrativoDesconto() {
        return this.nrDemonstrativoDesconto;
    }

    public void setNrDemonstrativoDesconto(Long nrDemonstrativoDesconto) {
        this.nrDemonstrativoDesconto = nrDemonstrativoDesconto;
    }

    public Boolean getDvDemonstrativoDesconto() {
        return this.dvDemonstrativoDesconto;
    }

    public void setDvDemonstrativoDesconto(Boolean dvDemonstrativoDesconto) {
        this.dvDemonstrativoDesconto = dvDemonstrativoDesconto;
    }

    public BigDecimal getVlDemonstrativoDesconto() {
        return this.vlDemonstrativoDesconto;
    }

    public void setVlDemonstrativoDesconto(BigDecimal vlDemonstrativoDesconto) {
        this.vlDemonstrativoDesconto = vlDemonstrativoDesconto;
    }

    public YearMonthDay getDtEmissao() {
        return this.dtEmissao;
    }

    public void setDtEmissao(YearMonthDay dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public DomainValue getTpSituacaoDemonstrativoDesc() {
        return this.tpSituacaoDemonstrativoDesc;
    }

	public void setTpSituacaoDemonstrativoDesc(
			DomainValue tpSituacaoDemonstrativoDesc) {
        this.tpSituacaoDemonstrativoDesc = tpSituacaoDemonstrativoDesc;
    }

    public String getObDemonstrativoDesconto() {
        return this.obDemonstrativoDesconto;
    }

    public void setObDemonstrativoDesconto(String obDemonstrativoDesconto) {
        this.obDemonstrativoDesconto = obDemonstrativoDesconto;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDemonstrativoDesconto",
				getIdDemonstrativoDesconto()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DemonstrativoDesconto))
			return false;
        DemonstrativoDesconto castOther = (DemonstrativoDesconto) other;
		return new EqualsBuilder().append(this.getIdDemonstrativoDesconto(),
				castOther.getIdDemonstrativoDesconto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDemonstrativoDesconto())
            .toHashCode();
    }

}
