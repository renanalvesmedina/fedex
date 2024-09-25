package com.mercurio.lms.fretecarreteiroviagem.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ReferenciaTipoVeiculo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    /** identifier field */
    private Long idReferenciaTipoVeiculo;
    
    private Integer versao;

    /** persistent field */
    private Integer qtKmInicial;

    /** persistent field */
    private Integer qtKmFinal;

    /** persistent field */
    private BigDecimal vlFreteReferencia;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte tipoMeioTransporte;

    /** persistent field */
    private com.mercurio.lms.fretecarreteiroviagem.model.ReferenciaFreteCarreteiro referenciaFreteCarreteiro;

    public Long getIdReferenciaTipoVeiculo() {
        return this.idReferenciaTipoVeiculo;
    }

    public void setIdReferenciaTipoVeiculo(Long idReferenciaTipoVeiculo) {
        this.idReferenciaTipoVeiculo = idReferenciaTipoVeiculo;
    }

    public Integer getQtKmInicial() {
        return this.qtKmInicial;
    }

    public void setQtKmInicial(Integer qtKmInicial) {
        this.qtKmInicial = qtKmInicial;
    }

    public Integer getQtKmFinal() {
        return this.qtKmFinal;
    }

    public void setQtKmFinal(Integer qtKmFinal) {
        this.qtKmFinal = qtKmFinal;
    }

    public BigDecimal getVlFreteReferencia() {
        return this.vlFreteReferencia;
    }

    public void setVlFreteReferencia(BigDecimal vlFreteReferencia) {
        this.vlFreteReferencia = vlFreteReferencia;
    }

    public com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte getTipoMeioTransporte() {
        return this.tipoMeioTransporte;
    }

	public void setTipoMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte tipoMeioTransporte) {
        this.tipoMeioTransporte = tipoMeioTransporte;
    }

    public com.mercurio.lms.fretecarreteiroviagem.model.ReferenciaFreteCarreteiro getReferenciaFreteCarreteiro() {
        return this.referenciaFreteCarreteiro;
    }

	public void setReferenciaFreteCarreteiro(
			com.mercurio.lms.fretecarreteiroviagem.model.ReferenciaFreteCarreteiro referenciaFreteCarreteiro) {
        this.referenciaFreteCarreteiro = referenciaFreteCarreteiro;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idReferenciaTipoVeiculo",
				getIdReferenciaTipoVeiculo()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ReferenciaTipoVeiculo))
			return false;
        ReferenciaTipoVeiculo castOther = (ReferenciaTipoVeiculo) other;
		return new EqualsBuilder().append(this.getIdReferenciaTipoVeiculo(),
				castOther.getIdReferenciaTipoVeiculo()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdReferenciaTipoVeiculo())
            .toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

}
