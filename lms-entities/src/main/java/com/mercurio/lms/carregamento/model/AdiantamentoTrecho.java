package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.municipios.model.Filial;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contratacaoveiculos.model.PostoConveniado;

/** @author LMS Custom Hibernate CodeGenerator */
public class AdiantamentoTrecho implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idAdiantamentoTrecho;
    
	/** persistent field */
	private BigDecimal pcFrete;
    
    /** persistent field */
    private DomainValue tpStatusRecibo;
    
    /** persistent field */
    private BigDecimal vlAdiantamento;
    
    /** persistent field */
    private BigDecimal vlFrete;
    
    /** persistent field */
    private com.mercurio.lms.carregamento.model.ControleCarga controleCarga;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialOrigem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialDestino;

    /** persistent field */
    private com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro reciboFreteCarreteiro;
    
    private PostoConveniado postoConveniado;

	public AdiantamentoTrecho() {
	}

	public AdiantamentoTrecho(Long idAdiantamentoTrecho, BigDecimal pcFrete, DomainValue tpStatusRecibo, BigDecimal vlAdiantamento, BigDecimal vlFrete, ControleCarga controleCarga, Filial filialByIdFilialOrigem, Filial filialByIdFilialDestino, ReciboFreteCarreteiro reciboFreteCarreteiro, PostoConveniado postoConveniado) {
		this.idAdiantamentoTrecho = idAdiantamentoTrecho;
		this.pcFrete = pcFrete;
		this.tpStatusRecibo = tpStatusRecibo;
		this.vlAdiantamento = vlAdiantamento;
		this.vlFrete = vlFrete;
		this.controleCarga = controleCarga;
		this.filialByIdFilialOrigem = filialByIdFilialOrigem;
		this.filialByIdFilialDestino = filialByIdFilialDestino;
		this.reciboFreteCarreteiro = reciboFreteCarreteiro;
		this.postoConveniado = postoConveniado;
	}

	public Long getIdAdiantamentoTrecho() {
		return idAdiantamentoTrecho;
	}

	public void setIdAdiantamentoTrecho(Long idAdiantamentoTrecho) {
		this.idAdiantamentoTrecho = idAdiantamentoTrecho;
	}

	public BigDecimal getPcFrete() {
		return pcFrete;
	}

	public void setPcFrete(BigDecimal pcFrete) {
		this.pcFrete = pcFrete;
	}

	public DomainValue getTpStatusRecibo() {
		return tpStatusRecibo;
	}

	public void setTpStatusRecibo(DomainValue tpStatusRecibo) {
		this.tpStatusRecibo = tpStatusRecibo;
	}

	public BigDecimal getVlAdiantamento() {
		return vlAdiantamento;
	}

	public void setVlAdiantamento(BigDecimal vlAdiantamento) {
		this.vlAdiantamento = vlAdiantamento;
	}

	public BigDecimal getVlFrete() {
		return vlFrete;
	}

	public void setVlFrete(BigDecimal vlFrete) {
		this.vlFrete = vlFrete;
	}

	public com.mercurio.lms.carregamento.model.ControleCarga getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(
			com.mercurio.lms.carregamento.model.ControleCarga controleCarga) {
		this.controleCarga = controleCarga;
	}

	public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialDestino() {
		return filialByIdFilialDestino;
	}

	public void setFilialByIdFilialDestino(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialDestino) {
		this.filialByIdFilialDestino = filialByIdFilialDestino;
	}

	public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialOrigem() {
		return filialByIdFilialOrigem;
	}

	public void setFilialByIdFilialOrigem(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialOrigem) {
		this.filialByIdFilialOrigem = filialByIdFilialOrigem;
	}

	public com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro getReciboFreteCarreteiro() {
		return reciboFreteCarreteiro;
	}

	public void setReciboFreteCarreteiro(
			com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro reciboFreteCarreteiro) {
		this.reciboFreteCarreteiro = reciboFreteCarreteiro;
	}

    public String toString() {
		return new ToStringBuilder(this).append("idAdiantamentoTrecho",
				getIdAdiantamentoTrecho()).toString();
    }

	public void setPostoConveniado(PostoConveniado postoConveniado) {
		this.postoConveniado = postoConveniado;
	}

	public PostoConveniado getPostoConveniado() {
		return postoConveniado;
	}
    
    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AdiantamentoTrecho))
			return false;
        AdiantamentoTrecho castOther = (AdiantamentoTrecho) other;
		return new EqualsBuilder().append(this.getIdAdiantamentoTrecho(),
				castOther.getIdAdiantamentoTrecho()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdAdiantamentoTrecho())
            .toHashCode();
    }
}