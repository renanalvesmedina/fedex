package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class PostoPassagemCc implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPostoPassagemCc;

    /** identifier field */
    private Integer versao;

    /** persistent field */
    private BigDecimal vlPagar;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.ControleCarga controleCarga;

    /** persistent field */
    private com.mercurio.lms.municipios.model.TipoPagamPostoPassagem tipoPagamPostoPassagem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.PostoPassagem postoPassagem;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    public Long getIdPostoPassagemCc() {
        return this.idPostoPassagemCc;
    }

    public void setIdPostoPassagemCc(Long idPostoPassagemCc) {
        this.idPostoPassagemCc = idPostoPassagemCc;
    }

    public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

    public BigDecimal getVlPagar() {
        return this.vlPagar;
    }

    public void setVlPagar(BigDecimal vlPagar) {
        this.vlPagar = vlPagar;
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

    public com.mercurio.lms.municipios.model.PostoPassagem getPostoPassagem() {
        return this.postoPassagem;
    }

	public void setPostoPassagem(
			com.mercurio.lms.municipios.model.PostoPassagem postoPassagem) {
        this.postoPassagem = postoPassagem;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idPostoPassagemCc",
				getIdPostoPassagemCc()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof PostoPassagemCc))
			return false;
        PostoPassagemCc castOther = (PostoPassagemCc) other;
		return new EqualsBuilder().append(this.getIdPostoPassagemCc(),
				castOther.getIdPostoPassagemCc()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPostoPassagemCc())
            .toHashCode();
    }

}
