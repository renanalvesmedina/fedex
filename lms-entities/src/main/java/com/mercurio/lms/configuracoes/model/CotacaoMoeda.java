package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class CotacaoMoeda implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idCotacaoMoeda;

    /** persistent field */
    private BigDecimal vlCotacaoMoeda;

    /** persistent field */
    private YearMonthDay dtCotacaoMoeda;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.MoedaPais moedaPais;

    /** persistent field */
    private List recibos;

    /** persistent field */
    private List faturas;

    public Long getIdCotacaoMoeda() {
        return this.idCotacaoMoeda;
    }

    public void setIdCotacaoMoeda(Long idCotacaoMoeda) {
        this.idCotacaoMoeda = idCotacaoMoeda;
    }

    public BigDecimal getVlCotacaoMoeda() {
        return this.vlCotacaoMoeda;
    }

    public void setVlCotacaoMoeda(BigDecimal vlCotacaoMoeda) {
        this.vlCotacaoMoeda = vlCotacaoMoeda;
    }

    public YearMonthDay getDtCotacaoMoeda() {
        return this.dtCotacaoMoeda;
    }

    public void setDtCotacaoMoeda(YearMonthDay dtCotacaoMoeda) {
        this.dtCotacaoMoeda = dtCotacaoMoeda;
    }

    public com.mercurio.lms.configuracoes.model.MoedaPais getMoedaPais() {
        return this.moedaPais;
    }

	public void setMoedaPais(
			com.mercurio.lms.configuracoes.model.MoedaPais moedaPais) {
        this.moedaPais = moedaPais;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.Recibo.class)     
    public List getRecibos() {
        return this.recibos;
    }

    public void setRecibos(List recibos) {
        this.recibos = recibos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.Fatura.class)     
    public List getFaturas() {
        return this.faturas;
    }

    public void setFaturas(List faturas) {
        this.faturas = faturas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idCotacaoMoeda",
				getIdCotacaoMoeda()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof CotacaoMoeda))
			return false;
        CotacaoMoeda castOther = (CotacaoMoeda) other;
		return new EqualsBuilder().append(this.getIdCotacaoMoeda(),
				castOther.getIdCotacaoMoeda()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdCotacaoMoeda()).toHashCode();
    }

}
