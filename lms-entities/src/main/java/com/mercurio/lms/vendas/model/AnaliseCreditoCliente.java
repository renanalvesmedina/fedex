package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Usuario;

/** @author LMS Custom Hibernate CodeGenerator */
public class AnaliseCreditoCliente implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idAnaliseCreditoCliente;

	/** persistent field */
	private DateTime dhSolicitacao;

	/** persistent field */
	private DomainValue tpSituacao;

	/** persistent field */
	private DateTime dhConclusao;

	/** persistent field */
	private Boolean blCreditoLiberado;

	/** persistent field */
	private DateTime dhUltimaConsultaSerasa;

	/** transient field */
	private transient BigDecimal vlLimiteCredito;

	/** transient field */
	private transient Moeda moedaByIdMoedaLimCred;

	/** persistent field */
	private Cliente cliente;

	/** persistent field */
	private Usuario usuario;

	/** persistent field */
	private Usuario usuarioAnalise;

	public Long getIdAnaliseCreditoCliente() {
		return idAnaliseCreditoCliente;
	}

	public void setIdAnaliseCreditoCliente(Long idAnaliseCreditoCliente) {
		this.idAnaliseCreditoCliente = idAnaliseCreditoCliente;
	}

	public DateTime getDhSolicitacao() {
		return dhSolicitacao;
	}

	public void setDhSolicitacao(DateTime dhSolicitacao) {
		this.dhSolicitacao = dhSolicitacao;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public DateTime getDhConclusao() {
		return dhConclusao;
	}

	public void setDhConclusao(DateTime dhConclusao) {
		this.dhConclusao = dhConclusao;
	}

	public Boolean getBlCreditoLiberado() {
		return blCreditoLiberado;
	}

	public void setBlCreditoLiberado(Boolean blCreditoLiberado) {
		this.blCreditoLiberado = blCreditoLiberado;
	}

	public DateTime getDhUltimaConsultaSerasa() {
		return dhUltimaConsultaSerasa;
	}

	public void setDhUltimaConsultaSerasa(DateTime dhUltimaConsultaSerasa) {
		this.dhUltimaConsultaSerasa = dhUltimaConsultaSerasa;
	}

	public BigDecimal getVlLimiteCredito() {
		return vlLimiteCredito;
	}

	public void setVlLimiteCredito(BigDecimal vlLimiteCredito) {
		this.vlLimiteCredito = vlLimiteCredito;
	}

	public Moeda getMoedaByIdMoedaLimCred() {
		return moedaByIdMoedaLimCred;
	}

	public void setMoedaByIdMoedaLimCred(Moeda moedaByIdMoedaLimCred) {
		this.moedaByIdMoedaLimCred = moedaByIdMoedaLimCred;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuarioAnalise() {
		return usuarioAnalise;
	}

	public void setUsuarioAnalise(Usuario usuarioAnalise) {
		this.usuarioAnalise = usuarioAnalise;
	}
}