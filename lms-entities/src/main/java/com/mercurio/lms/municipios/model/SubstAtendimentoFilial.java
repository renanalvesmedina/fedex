package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class SubstAtendimentoFilial implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idSubstAtendimentoFilial;

    /** persistent field */ 
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativa;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipio;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.NaturezaProduto naturezaProduto;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Regional regional;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Servico servico;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialOrigem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialDestino;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialDestinoSubstituta;

    /** persistent field */
    private DomainValue tpDesvioCarga;
    
    public Long getIdSubstAtendimentoFilial() {
        return this.idSubstAtendimentoFilial;
    }

    public void setIdSubstAtendimentoFilial(Long idSubstAtendimentoFilial) {
        this.idSubstAtendimentoFilial = idSubstAtendimentoFilial;
    }

    public com.mercurio.lms.municipios.model.UnidadeFederativa getUnidadeFederativa() {
        return this.unidadeFederativa;
    }

	public void setUnidadeFederativa(
			com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativa) {
        this.unidadeFederativa = unidadeFederativa;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipio() {
        return this.municipio;
    }

	public void setMunicipio(
			com.mercurio.lms.municipios.model.Municipio municipio) {
        this.municipio = municipio;
    }

    public com.mercurio.lms.expedicao.model.NaturezaProduto getNaturezaProduto() {
        return this.naturezaProduto;
    }

	public void setNaturezaProduto(
			com.mercurio.lms.expedicao.model.NaturezaProduto naturezaProduto) {
        this.naturezaProduto = naturezaProduto;
    }

    public com.mercurio.lms.municipios.model.Regional getRegional() {
        return this.regional;
    }

    public void setRegional(com.mercurio.lms.municipios.model.Regional regional) {
        this.regional = regional;
    }

    public com.mercurio.lms.configuracoes.model.Servico getServico() {
        return this.servico;
    }

    public void setServico(com.mercurio.lms.configuracoes.model.Servico servico) {
        this.servico = servico;
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

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialDestinoSubstituta() {
        return this.filialByIdFilialDestinoSubstituta;
    }

	public void setFilialByIdFilialDestinoSubstituta(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialDestinoSubstituta) {
        this.filialByIdFilialDestinoSubstituta = filialByIdFilialDestinoSubstituta;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idSubstAtendimentoFilial",
				getIdSubstAtendimentoFilial()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof SubstAtendimentoFilial))
			return false;
        SubstAtendimentoFilial castOther = (SubstAtendimentoFilial) other;
		return new EqualsBuilder().append(this.getIdSubstAtendimentoFilial(),
				castOther.getIdSubstAtendimentoFilial()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdSubstAtendimentoFilial())
            .toHashCode();
    }

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public DomainValue getTpDesvioCarga() {
		return tpDesvioCarga;
}

	public void setTpDesvioCarga(DomainValue tpDesvioCarga) {
		this.tpDesvioCarga = tpDesvioCarga;
	}

}
