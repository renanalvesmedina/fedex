package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class CartaoPedagio implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idCartaoPedagio;

    /** persistent field */
    private Long nrCartao;

    /** persistent field */
    private YearMonthDay dtValidade;
    
    /** persistent field */
    private Boolean blCartaoTerceiro;
    
    /** persistent field */
    private com.mercurio.lms.carregamento.model.OperadoraCartaoPedagio operadoraCartaoPedagio;

    /** persistent field */
    private List pagtoPedagioCcs;
    
    private String dtValidadeMenorDtAtual;

    public Long getIdCartaoPedagio() {
        return this.idCartaoPedagio;
    }

    public void setIdCartaoPedagio(Long idCartaoPedagio) {
        this.idCartaoPedagio = idCartaoPedagio;
    }

    public Long getNrCartao() {
        return this.nrCartao;
    }

    public void setNrCartao(Long nrCartao) {
        this.nrCartao = nrCartao;
    }

    public YearMonthDay getDtValidade() {
        return this.dtValidade;
    }

    public void setDtValidade(YearMonthDay dtValidade) {
        this.dtValidade = dtValidade;
    }

    public Boolean getBlCartaoTerceiro() {
		return blCartaoTerceiro;
	}

	public void setBlCartaoTerceiro(Boolean blCartaoTerceiro) {
		this.blCartaoTerceiro = blCartaoTerceiro;
	}

	public com.mercurio.lms.carregamento.model.OperadoraCartaoPedagio getOperadoraCartaoPedagio() {
        return this.operadoraCartaoPedagio;
    }

	public void setOperadoraCartaoPedagio(
			com.mercurio.lms.carregamento.model.OperadoraCartaoPedagio operadoraCartaoPedagio) {
        this.operadoraCartaoPedagio = operadoraCartaoPedagio;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.PagtoPedagioCc.class)     
    public List getPagtoPedagioCcs() {
        return this.pagtoPedagioCcs;
    }

    public void setPagtoPedagioCcs(List pagtoPedagioCcs) {
        this.pagtoPedagioCcs = pagtoPedagioCcs;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idCartaoPedagio",
				getIdCartaoPedagio()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof CartaoPedagio))
			return false;
        CartaoPedagio castOther = (CartaoPedagio) other;
		return new EqualsBuilder().append(this.getIdCartaoPedagio(),
				castOther.getIdCartaoPedagio()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdCartaoPedagio()).toHashCode();
    }

	public String getDtValidadeMenorDtAtual() {
		return dtValidadeMenorDtAtual;
	}

	public void setDtValidadeMenorDtAtual(String dtValidadeMenorDtAtual) {
		this.dtValidadeMenorDtAtual = dtValidadeMenorDtAtual;
	}
}