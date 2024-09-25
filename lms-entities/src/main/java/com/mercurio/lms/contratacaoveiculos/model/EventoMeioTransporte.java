package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class EventoMeioTransporte implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEventoMeioTransporte;

    /** persistent field */
    private DomainValue tpSituacaoMeioTransporte;

    /** persistent field */
    private DateTime dhInicioEvento;

    /** nullable persistent field */
    private DateTime dhFimEvento;
    
    /** nullable persistent field */
    private DateTime dhGeracao;

    /** nullable persistent field */
    private String dsLocalManutencao;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.ControleTrecho controleTrecho;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporte;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.municipios.model.PontoParadaTrecho pontoParadaTrecho;
    
    /** persistent field */
    private com.mercurio.lms.carregamento.model.ControleCarga controleCarga;

    /** persistent field */
    private com.mercurio.lms.portaria.model.Box box;
    
    public Long getIdEventoMeioTransporte() {
        return this.idEventoMeioTransporte;
    }

    public void setIdEventoMeioTransporte(Long idEventoMeioTransporte) {
        this.idEventoMeioTransporte = idEventoMeioTransporte;
    }

    public DomainValue getTpSituacaoMeioTransporte() {
        return this.tpSituacaoMeioTransporte;
    }

    public void setTpSituacaoMeioTransporte(DomainValue tpSituacaoMeioTransporte) {
        this.tpSituacaoMeioTransporte = tpSituacaoMeioTransporte;
    }

    public DateTime getDhFimEvento() {
		return dhFimEvento;
	}

	public void setDhFimEvento(DateTime dhFimEvento) {
		this.dhFimEvento = dhFimEvento;
	}

	public DateTime getDhInicioEvento() {
		return dhInicioEvento;
	}

	public void setDhInicioEvento(DateTime dhInicioEvento) {
		this.dhInicioEvento = dhInicioEvento;
	}

	public void setDhGeracao(DateTime dhGeracao) {
		this.dhGeracao = dhGeracao;
	}

	public DateTime getDhGeracao() {
		return dhGeracao;
	}

	public String getDsLocalManutencao() {
        return this.dsLocalManutencao;
    }

    public void setDsLocalManutencao(String dsLocalManutencao) {
        this.dsLocalManutencao = dsLocalManutencao;
    }

    public com.mercurio.lms.carregamento.model.ControleTrecho getControleTrecho() {
        return this.controleTrecho;
    }

	public void setControleTrecho(
			com.mercurio.lms.carregamento.model.ControleTrecho controleTrecho) {
        this.controleTrecho = controleTrecho;
    }

    public com.mercurio.lms.contratacaoveiculos.model.MeioTransporte getMeioTransporte() {
        return this.meioTransporte;
    }

	public void setMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporte) {
        this.meioTransporte = meioTransporte;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public com.mercurio.lms.municipios.model.PontoParadaTrecho getPontoParadaTrecho() {
        return this.pontoParadaTrecho;
    }

	public void setPontoParadaTrecho(
			com.mercurio.lms.municipios.model.PontoParadaTrecho pontoParadaTrecho) {
        this.pontoParadaTrecho = pontoParadaTrecho;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idEventoMeioTransporte",
				getIdEventoMeioTransporte()).toString();
    }

	public String toString(ToStringStyle style) {
		return new ToStringBuilder(this, style)
				.append("ID_EVENTO_MEIO_TRANSPORTE", idEventoMeioTransporte)
				.append("ID_MEIO_TRANSPORTE", meioTransporte != null ? meioTransporte.getIdMeioTransporte() : null)
				.append("TP_SITUACAO_MEIO_TRANSPORTE", tpSituacaoMeioTransporte != null ? tpSituacaoMeioTransporte.getValue() : null)
				.append("DH_GERACAO", dhGeracao)
				.append("DH_EVENTO_INICIAL", dhInicioEvento)
				.append("ID_FILIAL", filial != null ? filial.getIdFilial() : null)
				.append("ID_PONTO_PARADA_TRECHO", pontoParadaTrecho != null ? pontoParadaTrecho.getIdPontoParadaTrecho() : null)
				.append("ID_CONTROLE_TRECHO", controleTrecho != null ? controleTrecho.getIdControleTrecho() : null)
				.append("DH_EVENTO_FINAL", dhFimEvento)
				.append("DS_LOCAL_MANUTENCAO", dsLocalManutencao)
				.append("ID_BOX", box != null ? box.getIdBox() : null)
				.append("ID_CONTROLE_CARGA", controleCarga != null ? controleCarga.getIdControleCarga() : null)
				.append("ID_USUARIO", usuario != null ? usuario.getIdUsuario() : null)
				.toString();
	}

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EventoMeioTransporte))
			return false;
        EventoMeioTransporte castOther = (EventoMeioTransporte) other;
		return new EqualsBuilder().append(this.getIdEventoMeioTransporte(),
				castOther.getIdEventoMeioTransporte()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEventoMeioTransporte())
            .toHashCode();
    }

	/**
	 * @return Returns the controleCarga.
	 */
	public com.mercurio.lms.carregamento.model.ControleCarga getControleCarga() {
		return controleCarga;
	}

	/**
	 * @param controleCarga
	 *            The controleCarga to set.
	 */
	public void setControleCarga(
			com.mercurio.lms.carregamento.model.ControleCarga controleCarga) {
		this.controleCarga = controleCarga;
	}

	/**
	 * @return Returns the box.
	 */
	public com.mercurio.lms.portaria.model.Box getBox() {
		return box;
	}

	/**
	 * @param box
	 *            The box to set.
	 */
	public void setBox(com.mercurio.lms.portaria.model.Box box) {
		this.box = box;
	}

	public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
		return usuario;
}

	public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
		this.usuario = usuario;
	}

}
