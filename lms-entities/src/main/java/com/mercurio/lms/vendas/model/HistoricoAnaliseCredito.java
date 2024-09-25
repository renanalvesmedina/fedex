package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Usuario;

/** @author LMS Custom Hibernate CodeGenerator */
public class HistoricoAnaliseCredito implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idHistoricoAnaliseCredito;

	/** persistent field */
	private DateTime dhEvento;

	/** persistent field */
	private DomainValue tpEvento;

	/** persistent field */
	private String obEvento;

	/** persistent field */
	private AnaliseCreditoCliente analiseCreditoCliente;

	/** persistent field */
	private Usuario usuario;

	public Long getIdHistoricoAnaliseCredito() {
		return idHistoricoAnaliseCredito;
	}

	public void setIdHistoricoAnaliseCredito(Long idHistoricoAnaliseCredito) {
		this.idHistoricoAnaliseCredito = idHistoricoAnaliseCredito;
	}

	public DateTime getDhEvento() {
		return dhEvento;
	}

	public void setDhEvento(DateTime dhEvento) {
		this.dhEvento = dhEvento;
	}

	public DomainValue getTpEvento() {
		return tpEvento;
	}

	public void setTpEvento(DomainValue tpEvento) {
		this.tpEvento = tpEvento;
	}

	public String getObEvento() {
		return obEvento;
	}

	public void setObEvento(String obEvento) {
		this.obEvento = obEvento;
	}

	public AnaliseCreditoCliente getAnaliseCreditoCliente() {
		return analiseCreditoCliente;
	}

	public void setAnaliseCreditoCliente(
			AnaliseCreditoCliente analiseCreditoCliente) {
		this.analiseCreditoCliente = analiseCreditoCliente;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}