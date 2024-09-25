package com.mercurio.lms.portaria.model.evento;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.portaria.model.service.utils.ConstantesPortaria;
import com.mercurio.lms.portaria.model.service.utils.EventoDoctoServicoHelper;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.EventoDocumentoServico;

import br.com.tntbrasil.integracao.domains.jms.VirtualTopics;

/**
 * LMSA-768 - Evento relacionado ao {@link DoctoServico} gerando um
 * {@link EventoDocumentoServico} e a respectiva mensagem na fila
 * {@code EVENTO_DOCTO_SERVICO_CONSUMER}.
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 *
 */
public class EventoPortariaDoctoServico extends EventoPortaria<EventoDocumentoServico> {

	private static final DomainValue TP_DOCUMENTO_CONTROLE_CARGA =
			new DomainValue(ConstantesPortaria.TP_DOCUMENTO_CONTROLE_CARGA);

	public static EventoPortariaDoctoServico createEventoPortaria(
			Filial filial, Usuario usuario, DateTime dhEvento, DoctoServico doctoServico) {
		Validate.notNull(filial, "A filial não pode ser nula.");
		Validate.notNull(usuario, "O usuário não pode ser nulo.");
		Validate.notNull(doctoServico, "O documento de serviço não pode ser nulo.");
		return new EventoPortariaDoctoServico(filial, usuario, dhEvento, doctoServico);
	}

	private DoctoServico doctoServico;
	private Evento evento;
	private String nrDocumento;
	private String obComplemento;

	private EventoPortariaDoctoServico(Filial filial, Usuario usuario, DateTime dhEvento, DoctoServico doctoServico) {
		super(filial, usuario, dhEvento);
		this.doctoServico = doctoServico;
	}
	
	public DoctoServico getDoctoServico() {
		return doctoServico;
	}
	
	public Evento getEvento() {
		return evento;
	}

	public EventoPortariaDoctoServico setEvento(Evento evento) {
		this.evento = evento;
		return this;
	}

	public EventoPortariaDoctoServico setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
		return this;
	}

	public EventoPortariaDoctoServico setObComplemento(String obComplemento) {
		this.obComplemento = obComplemento;
		return this;
	}

	@Override
	public EventoDocumentoServico gerarEntidade() {
		if (entidade == null) {
			Validate.notNull(evento, "O atributo evento não pode ser nulo.");
			entidade = new EventoDocumentoServico();
			entidade.setFilial(filial);
			entidade.setUsuario(usuario);
			entidade.setDhEvento(dhEvento);
			entidade.setDhInclusao(getDataHoraAtual());
			entidade.setDoctoServico(doctoServico);
			entidade.setEvento(evento);
			entidade.setNrDocumento(nrDocumento);
			entidade.setObComplemento(obComplemento);
			entidade.setTpDocumento(TP_DOCUMENTO_CONTROLE_CARGA);
			entidade.setBlEventoCancelado(false);
		}
		return entidade;
	}

	@Override
	public VirtualTopics verificarMensagem() {
		return VirtualTopics.EVENTO_DOCUMENTO_SERVICO;
	}

	@Override
	public Serializable gerarMensagem() {
		return EventoDoctoServicoHelper.convertEventoDoctoServico((gerarEntidade()));
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.appendSuper(super.toString())
				.append(doctoServico)
				.append(evento)
				.append(nrDocumento)
				.append(obComplemento)
				.toString();
	}

}
