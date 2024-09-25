package com.mercurio.lms.fretecarreteiroviagem.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class ReferenciaFreteCarreteiro implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idReferenciaFreteCarreteiro;
    
    private Integer versao;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativaByIdUnidadeDestino;

    /** persistent field */
    private com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativaByIdUnidadeFederativaOrigem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialOrigem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialDestino;

    /** persistent field */
    private List referenciaTipoVeiculos;
    
    private MoedaPais moedaPais;

    public MoedaPais getMoedaPais() {
		return moedaPais;
	}

	public void setMoedaPais(MoedaPais moedaPais) {
		this.moedaPais = moedaPais;
	}

    public Long getIdReferenciaFreteCarreteiro() {
        return this.idReferenciaFreteCarreteiro;
    }

    public void setIdReferenciaFreteCarreteiro(Long idReferenciaFreteCarreteiro) {
        this.idReferenciaFreteCarreteiro = idReferenciaFreteCarreteiro;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public com.mercurio.lms.municipios.model.UnidadeFederativa getUnidadeFederativaByIdUnidadeDestino() {
        return this.unidadeFederativaByIdUnidadeDestino;
    }

	public void setUnidadeFederativaByIdUnidadeDestino(
			com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativaByIdUnidadeDestino) {
        this.unidadeFederativaByIdUnidadeDestino = unidadeFederativaByIdUnidadeDestino;
    }

    public com.mercurio.lms.municipios.model.UnidadeFederativa getUnidadeFederativaByIdUnidadeFederativaOrigem() {
        return this.unidadeFederativaByIdUnidadeFederativaOrigem;
    }

	public void setUnidadeFederativaByIdUnidadeFederativaOrigem(
			com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativaByIdUnidadeFederativaOrigem) {
        this.unidadeFederativaByIdUnidadeFederativaOrigem = unidadeFederativaByIdUnidadeFederativaOrigem;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialOrigem() {
        return this.filialByIdFilialOrigem;
    }

	public void setFilialByIdFilialOrigem(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialOrigem) {
        this.filialByIdFilialOrigem = filialByIdFilialOrigem;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialDestino() {
        return this.filialByIdFilialDestino;
    }

	public void setFilialByIdFilialDestino(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialDestino) {
        this.filialByIdFilialDestino = filialByIdFilialDestino;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.fretecarreteiroviagem.model.ReferenciaTipoVeiculo.class)     
    public List getReferenciaTipoVeiculos() {
        return this.referenciaTipoVeiculos;
    }

    public void setReferenciaTipoVeiculos(List referenciaTipoVeiculos) {
        this.referenciaTipoVeiculos = referenciaTipoVeiculos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idReferenciaFreteCarreteiro",
				getIdReferenciaFreteCarreteiro()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ReferenciaFreteCarreteiro))
			return false;
        ReferenciaFreteCarreteiro castOther = (ReferenciaFreteCarreteiro) other;
		return new EqualsBuilder().append(
				this.getIdReferenciaFreteCarreteiro(),
				castOther.getIdReferenciaFreteCarreteiro()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdReferenciaFreteCarreteiro())
            .toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

}
