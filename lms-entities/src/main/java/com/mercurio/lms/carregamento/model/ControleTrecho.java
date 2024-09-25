package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.util.List;

import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.TrechoRotaIdaVolta;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class ControleTrecho implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idControleTrecho;

    /** persistent field */
    private DateTime dhPrevisaoSaida;

    /** persistent field */
    private DateTime dhPrevisaoChegada;

    /** nullable persistent field */
    private DateTime dhSaida;

    /** nullable persistent field */
    private DateTime dhChegada;

    /** nullable persistent field */
    private Integer nrTempoViagem;
    
    /** persistent field */
    private Integer nrTempoOperacao;
    
    /** persistent field */
    private Integer nrDistancia;
    
    /** persistent field */
    private Boolean blTrechoDireto;

    /** persistent field */
    private Boolean blInseridoManualmente;
    
    /** identifier field */
    private Integer versao;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.ControleCarga controleCarga;

    /** persistent field */
    private com.mercurio.lms.municipios.model.TrechoRotaIdaVolta trechoRotaIdaVolta;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialOrigem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialDestino;

    /** persistent field */
    private List eventoMeioTransportes;

    /** persistent field */
    private List solicMonitPreventivos;

    /** persistent field */
    private List localTrocas;
    
    /** persistent field */
    private List postoAvancadoCcs;

    /** persistent field */
    private List manifestos;

    public ControleTrecho() {
    }

    public ControleTrecho(Long idControleTrecho, DateTime dhPrevisaoSaida, DateTime dhPrevisaoChegada, DateTime dhSaida, DateTime dhChegada, Integer nrTempoViagem, Integer nrTempoOperacao, Integer nrDistancia, Boolean blTrechoDireto, Boolean blInseridoManualmente, Integer versao, ControleCarga controleCarga, TrechoRotaIdaVolta trechoRotaIdaVolta, Filial filialByIdFilialOrigem, Filial filialByIdFilialDestino, List eventoMeioTransportes, List solicMonitPreventivos, List localTrocas, List postoAvancadoCcs, List manifestos) {
        this.idControleTrecho = idControleTrecho;
        this.dhPrevisaoSaida = dhPrevisaoSaida;
        this.dhPrevisaoChegada = dhPrevisaoChegada;
        this.dhSaida = dhSaida;
        this.dhChegada = dhChegada;
        this.nrTempoViagem = nrTempoViagem;
        this.nrTempoOperacao = nrTempoOperacao;
        this.nrDistancia = nrDistancia;
        this.blTrechoDireto = blTrechoDireto;
        this.blInseridoManualmente = blInseridoManualmente;
        this.versao = versao;
        this.controleCarga = controleCarga;
        this.trechoRotaIdaVolta = trechoRotaIdaVolta;
        this.filialByIdFilialOrigem = filialByIdFilialOrigem;
        this.filialByIdFilialDestino = filialByIdFilialDestino;
        this.eventoMeioTransportes = eventoMeioTransportes;
        this.solicMonitPreventivos = solicMonitPreventivos;
        this.localTrocas = localTrocas;
        this.postoAvancadoCcs = postoAvancadoCcs;
        this.manifestos = manifestos;
    }

    public Long getIdControleTrecho() {
        return this.idControleTrecho;
    }

    public void setIdControleTrecho(Long idControleTrecho) {
        this.idControleTrecho = idControleTrecho;
    }

    public DateTime getDhPrevisaoSaida() {
        return this.dhPrevisaoSaida;
    }

    public void setDhPrevisaoSaida(DateTime dhPrevisaoSaida) {
        this.dhPrevisaoSaida = dhPrevisaoSaida;
    }

    public DateTime getDhPrevisaoChegada() {
        return this.dhPrevisaoChegada;
    }

    public void setDhPrevisaoChegada(DateTime dhPrevisaoChegada) {
        this.dhPrevisaoChegada = dhPrevisaoChegada;
    }

    public DateTime getDhSaida() {
        return this.dhSaida;
    }

    public void setDhSaida(DateTime dhSaida) {
        this.dhSaida = dhSaida;
    }

    public DateTime getDhChegada() {
        return this.dhChegada;
    }

    public void setDhChegada(DateTime dhChegada) {
        this.dhChegada = dhChegada;
    }
    
    public Integer getNrTempoOperacao() {
		return nrTempoOperacao;
	}

	public void setNrTempoOperacao(Integer nrTempoOperacao) {
		this.nrTempoOperacao = nrTempoOperacao;
	}

	public Integer getNrDistancia() {
		return nrDistancia;
	}

	public void setNrDistancia(Integer nrDistancia) {
		this.nrDistancia = nrDistancia;
	}

	public Integer getNrTempoViagem() {
		return nrTempoViagem;
	}

	public void setNrTempoViagem(Integer nrTempoViagem) {
		this.nrTempoViagem = nrTempoViagem;
	}

    public Boolean getBlInseridoManualmente() {
		return blInseridoManualmente;
	}

	public void setBlInseridoManualmente(Boolean blInseridoManualmente) {
		this.blInseridoManualmente = blInseridoManualmente;
	}

	public Boolean getBlTrechoDireto() {
		return blTrechoDireto;
	}

	public void setBlTrechoDireto(Boolean blTrechoDireto) {
		this.blTrechoDireto = blTrechoDireto;
	}

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public com.mercurio.lms.carregamento.model.ControleCarga getControleCarga() {
        return this.controleCarga;
    }

	public void setControleCarga(
			com.mercurio.lms.carregamento.model.ControleCarga controleCarga) {
        this.controleCarga = controleCarga;
    }

    public com.mercurio.lms.municipios.model.TrechoRotaIdaVolta getTrechoRotaIdaVolta() {
        return this.trechoRotaIdaVolta;
    }

	public void setTrechoRotaIdaVolta(
			com.mercurio.lms.municipios.model.TrechoRotaIdaVolta trechoRotaIdaVolta) {
        this.trechoRotaIdaVolta = trechoRotaIdaVolta;
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
	
	@ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.PostoAvancadoCc.class)     
	public List getPostoAvancadoCcs() {
		return postoAvancadoCcs;
	}

	public void setPostoAvancadoCcs(List postoAvancadoCcs) {
		this.postoAvancadoCcs = postoAvancadoCcs;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte.class)     
    public List getEventoMeioTransportes() {
        return this.eventoMeioTransportes;
    }
    
    public void setEventoMeioTransportes(List eventoMeioTransportes) {
        this.eventoMeioTransportes = eventoMeioTransportes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.sgr.model.SolicMonitPreventivo.class)     
    public List getSolicMonitPreventivos() {
        return this.solicMonitPreventivos;
    }

    public void setSolicMonitPreventivos(List solicMonitPreventivos) {
        this.solicMonitPreventivos = solicMonitPreventivos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.LocalTroca.class)     
    public List getLocalTrocas() {
        return this.localTrocas;
    }

    public void setLocalTrocas(List localTrocas) {
        this.localTrocas = localTrocas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.Manifesto.class)     
    public List getManifestos() {
        return this.manifestos;
    }

    public void setManifestos(List manifestos) {
        this.manifestos = manifestos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idControleTrecho",
				getIdControleTrecho()).toString();
    }

	public String toString(ToStringStyle style) {
		return new ToStringBuilder(this, style)
				.append("ID_CONTROLE_TRECHO", idControleTrecho)
				.append("ID_CONTROLE_CARGA", controleCarga != null ? controleCarga.getIdControleCarga() : null)
				.append("ID_FILIAL_ORIGEM", filialByIdFilialOrigem != null ? filialByIdFilialOrigem.getIdFilial() : null)
				.append("ID_FILIAL_DESTINO", filialByIdFilialDestino != null ? filialByIdFilialDestino.getIdFilial() : null)
				.append("ID_TRECHO_ROTA_IDA_VOLTA", trechoRotaIdaVolta != null ? trechoRotaIdaVolta.getIdTrechoRotaIdaVolta() : null)
				.append("DH_PREVISAO_SAIDA", dhPrevisaoSaida)
				.append("DH_PREVISAO_CHEGADA", dhPrevisaoChegada)
				.append("NR_TEMPO_VIAGEM", nrTempoViagem)
				.append("NR_TEMPO_OPERACAO", nrTempoOperacao)
				.append("NR_DISTANCIA", nrDistancia)
				.append("DH_SAIDA", dhSaida)
				.append("DH_CHEGADA", dhChegada)
				.append("NR_VERSAO", versao)
				.append("BL_TRECHO_DIRETO", blTrechoDireto)
				.append("BL_INSERIDO_MANUALMENTE", blInseridoManualmente)
				.toString();
	}

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ControleTrecho))
			return false;
        ControleTrecho castOther = (ControleTrecho) other;
		return new EqualsBuilder().append(this.getIdControleTrecho(),
				castOther.getIdControleTrecho()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdControleTrecho()).toHashCode();
    }

}
