package com.mercurio.lms.fretecarreteiroviagem.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class ValorCombustivel implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idValorCombustivel;

    /** persistent field */
    private BigDecimal vlValorCombustivel;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.fretecarreteiroviagem.model.TipoCombustivel tipoCombustivel;

    /** persistent field */
    private MoedaPais moedaPais;

    public Long getIdValorCombustivel() {
        return this.idValorCombustivel;
    }

    public void setIdValorCombustivel(Long idValorCombustivel) {
        this.idValorCombustivel = idValorCombustivel;
    }

    public BigDecimal getVlValorCombustivel() {
        return this.vlValorCombustivel;
    }

    public void setVlValorCombustivel(BigDecimal vlValorCombustivel) {
        this.vlValorCombustivel = vlValorCombustivel;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public com.mercurio.lms.fretecarreteiroviagem.model.TipoCombustivel getTipoCombustivel() {
        return this.tipoCombustivel;
    }

	public void setTipoCombustivel(
			com.mercurio.lms.fretecarreteiroviagem.model.TipoCombustivel tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idValorCombustivel",
				getIdValorCombustivel()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ValorCombustivel))
			return false;
        ValorCombustivel castOther = (ValorCombustivel) other;
		return new EqualsBuilder().append(this.getIdValorCombustivel(),
				castOther.getIdValorCombustivel()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdValorCombustivel())
            .toHashCode();
    }

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public MoedaPais getMoedaPais() {
		return moedaPais;
	}

	public void setMoedaPais(MoedaPais moedaPais) {
		this.moedaPais = moedaPais;
	}

}
