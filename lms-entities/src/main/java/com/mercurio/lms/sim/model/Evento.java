package com.mercurio.lms.sim.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.Hibernate;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class Evento implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idEvento;

	/** persistent field */
	private Short cdEvento;

	/** persistent field */
	private DomainValue tpEvento;

	/** persistent field */
	private Boolean blExibeCliente;

	/** persistent field */
	private Boolean blGeraParceiro;

	/** persistent field */
	private Evento cancelaEvento;

	/** persistent field */
	private DomainValue tpSituacao;

	/** persistent field */
	private LocalEvento localEvento;

	/** persistent field */
	private DescricaoEvento descricaoEvento;

	/** persistent field */
	private LocalizacaoMercadoria localizacaoMercadoria;

	/** persistent field */
	private List ocorrenciaEntregas;

	/** persistent field */
	private List eventoClienteRecebes;

	/** persistent field */
	private List eventoDocumentoServicos;

	/** persistent field */
	private List eventoClientes;
	
	/** persistent field */
	private List ocorrenciaPendencias;

	public Evento() {
		
	}

	public Evento(Long idEvento, Short cdEvento, DomainValue tpEvento, Boolean blExibeCliente, Boolean blGeraParceiro, Evento cancelaEvento, DomainValue tpSituacao, LocalEvento localEvento, DescricaoEvento descricaoEvento, LocalizacaoMercadoria localizacaoMercadoria, List ocorrenciaEntregas, List eventoClienteRecebes, List eventoDocumentoServicos, List eventoClientes, List ocorrenciaPendencias) {
		this.idEvento = idEvento;
		this.cdEvento = cdEvento;
		this.tpEvento = tpEvento;
		this.blExibeCliente = blExibeCliente;
		this.blGeraParceiro = blGeraParceiro;
		this.cancelaEvento = cancelaEvento;
		this.tpSituacao = tpSituacao;
		this.localEvento = localEvento;
		this.descricaoEvento = descricaoEvento;
		this.localizacaoMercadoria = localizacaoMercadoria;
		this.ocorrenciaEntregas = ocorrenciaEntregas;
		this.eventoClienteRecebes = eventoClienteRecebes;
		this.eventoDocumentoServicos = eventoDocumentoServicos;
		this.eventoClientes = eventoClientes;
		this.ocorrenciaPendencias = ocorrenciaPendencias;
	}

	public Evento(Long idEvento) {
		this.idEvento = idEvento;
	}
	
	public Long getIdEvento() {
		return this.idEvento;
	}

	public void setIdEvento(Long idEvento) {
		this.idEvento = idEvento;
	}

	public Short getCdEvento() {
		return this.cdEvento;
	}

	public void setCdEvento(Short cdEvento) {
		this.cdEvento = cdEvento;
	}

	public DomainValue getTpEvento() {
		return this.tpEvento;
	}

	public void setTpEvento(DomainValue tpEvento) {
		this.tpEvento = tpEvento;
	}

	public Boolean getBlExibeCliente() {
		return this.blExibeCliente;
	}

	public void setBlExibeCliente(Boolean blExibeCliente) { 
		this.blExibeCliente = blExibeCliente;
	}

	public Boolean getBlGeraParceiro() {
		return this.blGeraParceiro;
	}

	public void setBlGeraParceiro(Boolean blGeraParceiro) {
		this.blGeraParceiro = blGeraParceiro;
	}

	public DomainValue getTpSituacao() {
		return this.tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public LocalEvento getLocalEvento() {
		return this.localEvento;
	}

	public void setLocalEvento(LocalEvento localEvento) {
		this.localEvento = localEvento;
	}

	public DescricaoEvento getDescricaoEvento() {
		return this.descricaoEvento;
	}

	public void setDescricaoEvento(DescricaoEvento descricaoEvento) {
		this.descricaoEvento = descricaoEvento;
	}

	public LocalizacaoMercadoria getLocalizacaoMercadoria() {
		return this.localizacaoMercadoria;
	}

	public void setLocalizacaoMercadoria(
			LocalizacaoMercadoria localizacaoMercadoria) {
		this.localizacaoMercadoria = localizacaoMercadoria;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.entrega.model.OcorrenciaEntrega.class) 
	public List getOcorrenciaEntregas() {
		return this.ocorrenciaEntregas;
	}

	public void setOcorrenciaEntregas(List ocorrenciaEntregas) {
		this.ocorrenciaEntregas = ocorrenciaEntregas;
	}
	
	@ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.OcorrenciaPendencia.class) 
	public List getOcorrenciaPendencias() {
		return this.ocorrenciaPendencias;
	}

	public void setOcorrenciaPendencias(List ocorrenciaPendencias) {
		this.ocorrenciaPendencias = ocorrenciaPendencias;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.sim.model.EventoClienteRecebe.class) 
	public List getEventoClienteRecebes() {
		return this.eventoClienteRecebes;
	}

	public void setEventoClienteRecebes(List eventoClienteRecebes) {
		this.eventoClienteRecebes = eventoClienteRecebes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.sim.model.EventoDocumentoServico.class) 
	public List getEventoDocumentoServicos() {
		return this.eventoDocumentoServicos;
	}

	public void setEventoDocumentoServicos(List eventoDocumentoServicos) {
		this.eventoDocumentoServicos = eventoDocumentoServicos;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.EventoCliente.class) 
	public List getEventoClientes() {
		return this.eventoClientes;
	}

	public void setEventoClientes(List eventoClientes) {
		this.eventoClientes = eventoClientes;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idEvento", getIdEvento())
			.toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Evento))
			return false;
		Evento castOther = (Evento) other;
		return new EqualsBuilder().append(this.getIdEvento(),
				castOther.getIdEvento()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdEvento()).toHashCode();
	}

	public Evento getCancelaEvento() {
		return cancelaEvento;
	}

	public void setCancelaEvento(Evento cancelaEvento) {
		this.cancelaEvento = cancelaEvento;
	}

	public String getDsEvento() {
		StringBuffer sb = new StringBuffer();
		if (this.descricaoEvento != null
				&& Hibernate.isInitialized(this.descricaoEvento)
				&& this.descricaoEvento.getDsDescricaoEvento() != null)
			sb.append(this.descricaoEvento.getDsDescricaoEvento().getValue(
					LocaleContextHolder.getLocale()));
		if (this.localizacaoMercadoria != null
				&& Hibernate.isInitialized(this.localizacaoMercadoria)
				&& this.localizacaoMercadoria.getDsLocalizacaoMercadoria() != null)
			sb.append(" - ").append(
					this.localizacaoMercadoria.getDsLocalizacaoMercadoria()
							.getValue(LocaleContextHolder.getLocale()));

		return sb.toString();
	}
	
	public String getDsEventoMercadoria() {
		StringBuffer sb = new StringBuffer();
		if (this.descricaoEvento != null
				&& Hibernate.isInitialized(this.descricaoEvento)
				&& this.descricaoEvento.getDsDescricaoEvento() != null)
			sb.append(this.descricaoEvento.getDsDescricaoEvento().getValue(
					LocaleContextHolder.getLocale()));
		if (this.localizacaoMercadoria != null
				&& Hibernate.isInitialized(this.localizacaoMercadoria)
				&& this.localizacaoMercadoria.getDsLocalizacaoMercadoria() != null)
			sb.append(" - ").append(
					this.localizacaoMercadoria.getDsLocalizacaoMercadoria()
							.getValue(LocaleContextHolder.getLocale()));

		return sb.toString();
	}
 
}
