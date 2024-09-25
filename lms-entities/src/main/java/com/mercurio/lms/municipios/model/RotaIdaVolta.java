package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class RotaIdaVolta implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRotaIdaVolta;

    /** field */
    private Integer versao;
    
    /** persistent field */
    private DomainValue tpRotaIdaVolta;

    /** persistent field */
    private Integer nrDistancia;

    /** nullable persistent field */
    private BigDecimal vlFreteKm;

    /** nullable persistent field */
    private BigDecimal vlPremio;
    
    /** nullable persistent field */
    private BigDecimal vlFreteCarreteiro;
    
    /** nullable persistent field */
    private String obItinerario;

    /** nullable persistent field */
    private String obRotaIdaVolta;
    
    /** nullable persistent field */
    private Integer nrRota;    

    /** persistent field */
    private com.mercurio.lms.municipios.model.Rota rota;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.MoedaPais moedaPais;

    /** persistent field */
    private com.mercurio.lms.municipios.model.RotaViagem rotaViagem;

    /** persistent field */
    private List criterioAplicSimulacoes;

    /** persistent field */
    private List tipoMeioTranspRotaEvents;

    /** persistent field */
    private List trechoRotaIdaVoltas;

    /** persistent field */
    private List controleCargas;

    public Long getIdRotaIdaVolta() {
        return this.idRotaIdaVolta;
    }

    public void setIdRotaIdaVolta(Long idRotaIdaVolta) {
        this.idRotaIdaVolta = idRotaIdaVolta;
    }

    public DomainValue getTpRotaIdaVolta() {
        return this.tpRotaIdaVolta;
    }

    public void setTpRotaIdaVolta(DomainValue tpRotaIdaVolta) {
        this.tpRotaIdaVolta = tpRotaIdaVolta;
    }

    public Integer getNrDistancia() {
        return this.nrDistancia;
    }

    public void setNrDistancia(Integer nrDistancia) {
        this.nrDistancia = nrDistancia;
    }

    public BigDecimal getVlFreteKm() {
        return this.vlFreteKm;
    }

    public void setVlFreteKm(BigDecimal vlFreteKm) {
        this.vlFreteKm = vlFreteKm;
    }

    public String getObItinerario() {
        return this.obItinerario;
    }

    public void setObItinerario(String obItinerario) {
        this.obItinerario = obItinerario;
    }

    public String getObRotaIdaVolta() {
        return this.obRotaIdaVolta;
    }

    public void setObRotaIdaVolta(String obRotaIdaVolta) {
        this.obRotaIdaVolta = obRotaIdaVolta;
    }

	public BigDecimal getVlPremio() {
		return vlPremio;
	}

	public void setVlPremio(BigDecimal vlPremio) {
		this.vlPremio = vlPremio;
	}

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public Integer getNrRota() {
		return nrRota;
	}

	public void setNrRota(Integer nrRota) {
		this.nrRota = nrRota;
	}

    public com.mercurio.lms.municipios.model.Rota getRota() {
        return this.rota;
    }

    public void setRota(com.mercurio.lms.municipios.model.Rota rota) {
        this.rota = rota;
    }

    public com.mercurio.lms.configuracoes.model.MoedaPais getMoedaPais() {
        return this.moedaPais;
    }

	public void setMoedaPais(
			com.mercurio.lms.configuracoes.model.MoedaPais moedaPais) {
        this.moedaPais = moedaPais;
    }

    public com.mercurio.lms.municipios.model.RotaViagem getRotaViagem() {
        return this.rotaViagem;
    }

	public void setRotaViagem(
			com.mercurio.lms.municipios.model.RotaViagem rotaViagem) {
        this.rotaViagem = rotaViagem;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.fretecarreteiroviagem.model.CriterioAplicSimulacao.class)     
    public List getCriterioAplicSimulacoes() {
        return this.criterioAplicSimulacoes;
    }

    public void setCriterioAplicSimulacoes(List criterioAplicSimulacoes) {
        this.criterioAplicSimulacoes = criterioAplicSimulacoes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.TipoMeioTranspRotaEvent.class)     
    public List getTipoMeioTranspRotaEvents() {
        return this.tipoMeioTranspRotaEvents;
    }

    public void setTipoMeioTranspRotaEvents(List tipoMeioTranspRotaEvents) {
        this.tipoMeioTranspRotaEvents = tipoMeioTranspRotaEvents;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.TrechoRotaIdaVolta.class)     
    public List getTrechoRotaIdaVoltas() {
        return this.trechoRotaIdaVoltas;
    }

    public void setTrechoRotaIdaVoltas(List trechoRotaIdaVoltas) {
        this.trechoRotaIdaVoltas = trechoRotaIdaVoltas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.ControleCarga.class)     
    public List getControleCargas() {
        return this.controleCargas;
    }

    public void setControleCargas(List controleCargas) {
        this.controleCargas = controleCargas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idRotaIdaVolta",
				getIdRotaIdaVolta()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RotaIdaVolta))
			return false;
        RotaIdaVolta castOther = (RotaIdaVolta) other;
		return new EqualsBuilder().append(this.getIdRotaIdaVolta(),
				castOther.getIdRotaIdaVolta()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRotaIdaVolta()).toHashCode();
    }

	public BigDecimal getVlFreteCarreteiro() {
		return vlFreteCarreteiro;
	}

	public void setVlFreteCarreteiro(BigDecimal vlFreteCarreteiro) {
		this.vlFreteCarreteiro = vlFreteCarreteiro;
	}
}
