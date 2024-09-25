package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class PagtoPedagioCc implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPagtoPedagioCc;
    
    /** identifier field */
    private Integer versao;

    /** persistent field */
    private BigDecimal vlPedagio;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.ControleCarga controleCarga;

    /** persistent field */
    private com.mercurio.lms.municipios.model.TipoPagamPostoPassagem tipoPagamPostoPassagem;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.CartaoPedagio cartaoPedagio;
    
    /** persistent field */
    private com.mercurio.lms.carregamento.model.OperadoraCartaoPedagio operadoraCartaoPedagio;

    public Long getIdPagtoPedagioCc() {
        return this.idPagtoPedagioCc;
    }

    public void setIdPagtoPedagioCc(Long idPagtoPedagioCc) {
        this.idPagtoPedagioCc = idPagtoPedagioCc;
    }

    public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

    public BigDecimal getVlPedagio() {
        return this.vlPedagio;
    }

    public void setVlPedagio(BigDecimal vlPedagio) {
        this.vlPedagio = vlPedagio;
    }

    public com.mercurio.lms.carregamento.model.ControleCarga getControleCarga() {
        return this.controleCarga;
    }

	public void setControleCarga(
			com.mercurio.lms.carregamento.model.ControleCarga controleCarga) {
        this.controleCarga = controleCarga;
    }

    public com.mercurio.lms.municipios.model.TipoPagamPostoPassagem getTipoPagamPostoPassagem() {
        return this.tipoPagamPostoPassagem;
    }

	public void setTipoPagamPostoPassagem(
			com.mercurio.lms.municipios.model.TipoPagamPostoPassagem tipoPagamPostoPassagem) {
        this.tipoPagamPostoPassagem = tipoPagamPostoPassagem;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public com.mercurio.lms.carregamento.model.CartaoPedagio getCartaoPedagio() {
        return this.cartaoPedagio;
    }

	public void setCartaoPedagio(
			com.mercurio.lms.carregamento.model.CartaoPedagio cartaoPedagio) {
        this.cartaoPedagio = cartaoPedagio;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idPagtoPedagioCc",
				getIdPagtoPedagioCc()).toString();
    }
    
    public com.mercurio.lms.carregamento.model.OperadoraCartaoPedagio getOperadoraCartaoPedagio() {
		return operadoraCartaoPedagio;
	}

	public void setOperadoraCartaoPedagio(
			com.mercurio.lms.carregamento.model.OperadoraCartaoPedagio operadoraCartaoPedagio) {
		this.operadoraCartaoPedagio = operadoraCartaoPedagio;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof PagtoPedagioCc))
			return false;
        PagtoPedagioCc castOther = (PagtoPedagioCc) other;
		return new EqualsBuilder().append(this.getIdPagtoPedagioCc(),
				castOther.getIdPagtoPedagioCc()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPagtoPedagioCc()).toHashCode();
    }

}
