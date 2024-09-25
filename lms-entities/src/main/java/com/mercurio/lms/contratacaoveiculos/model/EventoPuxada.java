package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

public class EventoPuxada implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Long idEventoPuxada;
    
    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao solicitacaoContratacao;

    private DomainValue tpStatusEvento;
    
    /** persistent field */
    private DateTime dhEvento;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;
    
    /** persistent field */
    private String dhEventoTzr;

	/**
	 * @return the idEventoPuxada
	 */
	public Long getIdEventoPuxada() {
		return idEventoPuxada;
	}

	/**
	 * @param idEventoPuxada the idEventoPuxada to set
	 */
	public void setIdEventoPuxada(Long idEventoPuxada) {
		this.idEventoPuxada = idEventoPuxada;
	}

	/**
	 * @return the solicitacaoContratacao
	 */
	public com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao getSolicitacaoContratacao() {
		return solicitacaoContratacao;
	}

	/**
	 * @param solicitacaoContratacao the solicitacaoContratacao to set
	 */
	public void setSolicitacaoContratacao(
			com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao solicitacaoContratacao) {
		this.solicitacaoContratacao = solicitacaoContratacao;
	}

	/**
	 * @return the tpStatusEvento
	 */
	public DomainValue getTpStatusEvento() {
		return tpStatusEvento;
	}

	/**
	 * @param tpStatusEvento the tpStatusEvento to set
	 */
	public void setTpStatusEvento(DomainValue tpStatusEvento) {
		this.tpStatusEvento = tpStatusEvento;
	}

	/**
	 * @return the dhEvento
	 */
	public DateTime getDhEvento() {
		return dhEvento;
	}

	/**
	 * @param dhEvento the dhEvento to set
	 */
	public void setDhEvento(DateTime dhEvento) {
		this.dhEvento = dhEvento;
	}

	

	

	/**
	 * @return the dhEventoTzr
	 */
	public String getDhEventoTzr() {
		return dhEventoTzr;
	}

	/**
	 * @param dhEventoTzr the dhEventoTzr to set
	 */
	public void setDhEventoTzr(String dhEventoTzr) {
		this.dhEventoTzr = dhEventoTzr;
	}

	public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
		this.usuario = usuario;
	}

}
